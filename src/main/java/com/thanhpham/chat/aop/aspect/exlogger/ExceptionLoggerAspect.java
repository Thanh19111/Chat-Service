package com.thanhpham.chat.aop.aspect.exlogger;

import com.thanhpham.chat.aop.annotation.exlogger.ExceptionLogger;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class ExceptionLoggerAspect {
    @AfterThrowing(value = "@annotation(exceptionLogger) || @within(exceptionLogger)", throwing = "exception")
    public void logAllExceptions(JoinPoint joinPoint, Throwable exception, ExceptionLogger exceptionLogger) {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        log.error("[Application Error] Method [{}] Gets Errors >> With Params: {}", methodName, Arrays.toString(args));
        log.error("[Application Error] Exception Error: ", exception);
    }
}
