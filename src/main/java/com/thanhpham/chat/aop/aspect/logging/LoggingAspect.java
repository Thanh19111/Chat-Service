package com.thanhpham.chat.aop.aspect.logging;

import com.thanhpham.chat.aop.annotation.logging.Logging;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @AfterReturning(pointcut = "@annotation(logging)", returning = "result")
    public void logAfterReturning(Logging logging, Object result) {
        log.info("Method returned:");
        log.info("Results: {}", result);
    }
}
