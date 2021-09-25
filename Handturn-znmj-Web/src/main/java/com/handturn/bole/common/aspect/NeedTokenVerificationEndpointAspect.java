package com.handturn.bole.common.aspect;

import com.handturn.bole.common.annotation.NeedTokenVerificationEndpoint;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.utils.FebsUtil;
import com.handturn.bole.common.utils.HttpContextUtil;
import com.handturn.bole.monitor.service.IRedisService;
import com.handturn.bole.monitor.service.ISysLogService;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author Eric
 */
@Aspect
@Component
public class NeedTokenVerificationEndpointAspect extends AspectSupport {
    @Autowired
    private IRedisService redisService;

    @Autowired
    private ISysLogService logService;

    @Pointcut("@annotation(com.handturn.bole.common.annotation.NeedTokenVerificationEndpoint)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws FebsException {
        Object result;
        Method targetMethod = resolveMethod(point);
        NeedTokenVerificationEndpoint annotation = targetMethod.getAnnotation(NeedTokenVerificationEndpoint.class);

        try {
            result = point.proceed();
            HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
            String token = request.getHeader("token");
            String error = "";
            if(StringUtils.isEmpty(token)){
                error = "还未登陆,不能执行该操作!";
                throw new FebsException(error);
            }else{
                String accountCode = redisService.get(token);
                if(StringUtils.isEmpty(accountCode)){
                    error = "你的登陆已超时,请重新登陆!";
                    throw new FebsException(error);
                }else{
                    String accountCodeRequest =  request.getHeader("accountCode");
                    if(!accountCode.equals(accountCodeRequest)){
                        error = "账号未登陆!";
                        throw new FebsException(error);
                    }
                }
            }
            //logService.saveLog(point, targetMethod, request, operation, start);
            return result;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            String message = throwable.getMessage() == null?throwable.getLocalizedMessage() == null? "" : throwable.getLocalizedMessage():throwable.getMessage();
            String error = "";
            if(message.contains("Duplicate entry")){
                error = "存在重复的记录!";
            }else {
                error = FebsUtil.containChinese(message) ?message : "系统错误";
            }
            throw new FebsException(error);
        }
    }
}



