package com.handturn.bole.common.frontfilter;

import com.alibaba.fastjson.JSON;
import com.handturn.bole.common.exception.RedisConnectException;
import com.handturn.bole.master.member.entity.MemberNotifyType;
import com.handturn.bole.master.member.service.IMemberNotifyService;
import com.handturn.bole.monitor.service.IRedisService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Xss攻击拦截器
 *
 * @author Eric
 */
public class FrontFilter implements Filter {

    @Autowired
    private IRedisService redisService;

    private IMemberNotifyService memberNotifyService;

    private static Logger logger = LoggerFactory.getLogger(FrontFilter.class);

    private List<String> includes = new ArrayList<>();
    private List<String> excludes = new ArrayList<>();

    @Override
    public void init(FilterConfig filterConfig) {
        //redis上下文注入
        ServletContext servletContext = filterConfig.getServletContext();
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        redisService = ctx.getBean("redisService",IRedisService.class);
        memberNotifyService = ctx.getBean("memberNotifyService",IMemberNotifyService.class);

        logger.info("------------ front filter init ------------");
        String includeUrls = filterConfig.getInitParameter("includes");
        if (includeUrls != null) {
            String[] url = includeUrls.split(",");
            includes.addAll(Arrays.asList(url));
        }

        String temp = filterConfig.getInitParameter("excludes");
        if (temp != null) {
            String[] url = temp.split(",");
            excludes.addAll(Arrays.asList(url));
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        if (!handleIncludeURL(req)) {
            chain.doFilter(request, response);
            return;
        }

        if (handleExcludeURL(req)) {
            chain.doFilter(request, response);
            return;
        }

        /*logger.info("------------ front filter entry ------------");*/

        String token = req.getHeader("token");
        if(!StringUtils.isEmpty(token)){
            try {
                String accountCode = redisService.get(token);
                if(!StringUtils.isEmpty(accountCode)){

                    //存储token获取其他权限 暂时先用随机数
                    redisService.set(token,accountCode,1000*60*60*24*3L); //token保留3天
                    HttpServletResponse respon = (HttpServletResponse) response;
                    respon.setHeader("token",token);

                    //-----------------------通知消息返回前端--------------------------begin
                    //每次请求触发缓存
                    memberNotifyService.cacheNotifyMessage(accountCode);

                    //获取用户通知
                    String innerNotifyTypesJsonKey = MemberNotifyType.getRedisInnerNotifyTypesKey();
                    String innerNotifyTypesJsonStr = redisService.get(innerNotifyTypesJsonKey);
                    if(!StringUtils.isEmpty(innerNotifyTypesJsonStr)){
                        Set<String> innerNotifyTypesSet = JSON.parseObject(innerNotifyTypesJsonStr,Set.class);

                        //告诉请求头,有那些通知需要处理
                        respon.setHeader(MemberNotifyType.getRedisInnerNotifyTypesKey(),innerNotifyTypesJsonStr);
                        Set<String> redisKeySet = new HashSet<String>();
                        for (String innerNotifyType : innerNotifyTypesSet) {
                            String notifyKey = MemberNotifyType.getRedisKey(innerNotifyType, accountCode);
                            String notifyValue = redisService.get(notifyKey);
                            if(!StringUtils.isEmpty(notifyValue)){
                                respon.setHeader(innerNotifyType,notifyValue);
                                redisKeySet.add(notifyKey);
                            }else{
                                respon.setHeader(innerNotifyType,""); //不存在的提供空值
                            }
                        }

                        redisKeySet.forEach(redisKey ->{
                            try {
                                redisService.del(redisKey);
                            } catch (RedisConnectException e) {
                                logger.debug("内部消息通知(清除消息通知类型-REDIS):异常错误!");
                                e.printStackTrace();
                            }
                        });
                    }
                    //-----------------------通知消息返回前端--------------------------end

                    chain.doFilter(req, respon);
                    return;
                }
            } catch (RedisConnectException e) {
                e.printStackTrace();
            }
        }
        chain.doFilter(req, response);
    }

    @Override
    public void destroy() {
        // do nothing
    }

    private boolean handleExcludeURL(HttpServletRequest request) {
        if (excludes == null || excludes.isEmpty()) {
            return false;
        }
        String url = request.getServletPath();
        return excludes.stream().map(pattern -> Pattern.compile("^" + pattern)).map(p -> p.matcher(url)).anyMatch(Matcher::find);
    }

    private boolean handleIncludeURL(HttpServletRequest request) {
        if (includes == null || includes.isEmpty()) {
            return false;
        }
        String url = request.getServletPath();
        return includes.stream().map(pattern -> Pattern.compile("^" + pattern)).map(p -> p.matcher(url)).anyMatch(Matcher::find);
    }

}
