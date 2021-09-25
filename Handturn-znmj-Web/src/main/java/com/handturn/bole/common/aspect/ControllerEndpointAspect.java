package com.handturn.bole.common.aspect;

import com.handturn.bole.common.annotation.ControllerEndpoint;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.utils.FebsUtil;
import com.handturn.bole.common.utils.HttpContextUtil;
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
public class ControllerEndpointAspect extends AspectSupport {

    @Autowired
    private ISysLogService logService;

    @Pointcut("@annotation(com.handturn.bole.common.annotation.ControllerEndpoint)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws FebsException {
        Object result;
        Method targetMethod = resolveMethod(point);
        ControllerEndpoint annotation = targetMethod.getAnnotation(ControllerEndpoint.class);
        String operation = annotation.operation();
        long start = System.currentTimeMillis();
        try {
            result = point.proceed();
            if (StringUtils.isNotBlank(operation)) {
                HttpServletRequest request = HttpContextUtil.getHttpServletRequest();
                logService.saveLog(point, targetMethod, request, operation, start);
            }
            return result;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            String exceptionMessage = annotation.exceptionMessage();
            String message = throwable.getMessage() == null?throwable.getLocalizedMessage() == null? "" : throwable.getLocalizedMessage():throwable.getMessage();
            String error = "";
            if(message.contains("Duplicate entry")){
                error = "存在重复的记录!";
            }else {
                error = FebsUtil.containChinese(message) ? exceptionMessage + "，" + message : exceptionMessage;
            }
            throw new FebsException(error);
        }
    }
}



