package com.handturn.bole.monitor.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.handturn.bole.common.customPage.ICustomPage;
import com.handturn.bole.common.customPage.CustomPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.handturn.bole.common.entity.FebsConstant;
import com.handturn.bole.common.entity.QueryRequest;
import com.handturn.bole.common.handler.UserInfoHolder;
import com.handturn.bole.common.utils.AddressUtil;
import com.handturn.bole.common.utils.IPUtil;
import com.handturn.bole.common.utils.SortUtil;
import com.handturn.bole.monitor.entity.SysLog;
import com.handturn.bole.monitor.mapper.SysLogMapper;
import com.handturn.bole.monitor.service.ISysLogService;
import com.handturn.bole.system.entity.SysUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Eric
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public ICustomPage<SysLog> findLogs(SysLog systemLog, QueryRequest request) {
        QueryWrapper<SysLog> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(systemLog.getUserCode())) {
            queryWrapper.lambda().eq(SysLog::getUserCode, systemLog.getUserCode().toLowerCase());
        }
        if (StringUtils.isNotBlank(systemLog.getUserName())) {
            queryWrapper.lambda().eq(SysLog::getUserName, systemLog.getUserName().toLowerCase());
        }
        if (StringUtils.isNotBlank(systemLog.getOperation())) {
            queryWrapper.lambda().like(SysLog::getOperation, systemLog.getOperation());
        }
        if (StringUtils.isNotBlank(systemLog.getLocation())) {
            queryWrapper.lambda().like(SysLog::getLocation, systemLog.getLocation());
        }
        if (StringUtils.isNotBlank(systemLog.getCreateTimeFrom()) && StringUtils.isNotBlank(systemLog.getCreateTimeTo())) {
            queryWrapper.lambda()
                    .ge(SysLog::getCreateTime, systemLog.getCreateTimeFrom())
                    .le(SysLog::getCreateTime, systemLog.getCreateTimeTo());
        }

        CustomPage<SysLog> page = new CustomPage<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "createTime", FebsConstant.ORDER_DESC, true);

        return (ICustomPage<SysLog>) this.page(page, queryWrapper);
    }

    @Override
    @Transactional
    public void deleteLogs(String[] logIds) {
        List<String> list = Arrays.asList(logIds);
        baseMapper.deleteBatchIds(list);
    }

    @Override
    public void saveLog(ProceedingJoinPoint point, Method method,HttpServletRequest request, String operation, long start) {
        SysLog systemLog = new SysLog();
        // 设置 IP地址
        String ip = IPUtil.getIpAddr(request);
        systemLog.setIp(ip);
        // 设置操作用户
        SysUser sysUser = (SysUser) UserInfoHolder.getUserInfo();
        if (sysUser != null) {
            systemLog.setUserCode(sysUser.getUserCode());
            systemLog.setUserName(sysUser.getUserName());
        }
        // 设置耗时
        systemLog.setTime(System.currentTimeMillis() - start);
        // 设置操作描述
        systemLog.setOperation(operation);
        // 请求的类名
        String className = point.getTarget().getClass().getName();
        // 请求的方法名
        String methodName = method.getName();
        systemLog.setMethod(className + "." + methodName + "()");
        // 请求的方法参数值
        Object[] args = point.getArgs();
        // 请求的方法参数名称
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = u.getParameterNames(method);
        if (args != null && paramNames != null) {
            StringBuilder params = new StringBuilder();
            params = handleParams(params, args, Arrays.asList(paramNames));
            systemLog.setParams(params.toString());
        }
        systemLog.setCreateTime(new Date());
        systemLog.setLocation(AddressUtil.getCityInfo(ip));
        // 保存系统日志
        save(systemLog);
    }

    private StringBuilder handleParams(StringBuilder params, Object[] args, List paramNames) {
        try {
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Map) {
                    Set set = ((Map) args[i]).keySet();
                    List<Object> list = new ArrayList<>();
                    List<Object> paramList = new ArrayList<>();
                    for (Object key : set) {
                        list.add(((Map) args[i]).get(key));
                        paramList.add(key);
                    }
                    return handleParams(params, list.toArray(), paramList);
                } else {
                    if (args[i] instanceof Serializable) {
                        Class<?> aClass = args[i].getClass();
                        try {
                            aClass.getDeclaredMethod("toString", new Class[]{null});
                            // 如果不抛出 NoSuchMethodException 异常则存在 toString 方法 ，安全的 writeValueAsString ，否则 走 Object的 toString方法
                            params.append(" ").append(paramNames.get(i)).append(": ").append(objectMapper.writeValueAsString(args[i]));
                        } catch (NoSuchMethodException e) {
                            params.append(" ").append(paramNames.get(i)).append(": ").append(objectMapper.writeValueAsString(args[i].toString()));
                        }
                    } else if (args[i] instanceof MultipartFile) {
                        MultipartFile file = (MultipartFile) args[i];
                        params.append(" ").append(paramNames.get(i)).append(": ").append(file.getName());
                    } else {
                        params.append(" ").append(paramNames.get(i)).append(": ").append(args[i]);
                    }
                }
            }
        } catch (Exception ignore) {
            params.append("参数解析失败");
        }
        return params;
    }
}
