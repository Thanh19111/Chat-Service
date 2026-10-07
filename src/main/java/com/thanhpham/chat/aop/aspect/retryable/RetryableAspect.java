package com.thanhpham.chat.aop.aspect.retryable;

import com.thanhpham.chat.aop.annotation.retryable.Retryable;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class RetryableAspect {
    @Around("@annotation(retryable)")
    public Object handleRetry(ProceedingJoinPoint joinPoint, Retryable retryable) throws Throwable {
        int maxAttempts = retryable.limit();
        long delay = retryable.delay();
        Class<? extends Throwable>[] retryFor = retryable.retryFor();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();

        Throwable lastException = null;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                if (attempt > 1) log.info("{}/{} attempt for method [{}]", attempt, maxAttempts, methodName);
                return joinPoint.proceed();
            } catch (Throwable ex) {
                lastException = ex;
                if (!shouldRetryForException(ex, retryFor)) {
                    log.warn("Error [{}] not in retry error list", ex.getClass().getSimpleName());
                    throw ex;
                }

                log.warn("Method [{}] got error [{}]: {}. {}/{} attempts was failed.", methodName, ex.getClass().getSimpleName(), ex.getMessage(), attempt, maxAttempts);

                if (attempt == maxAttempts) {
                    log.error("Exceed ({}) max attempts for method [{}]", maxAttempts, methodName);
                    throw lastException;
                }

                if (delay > 0) {
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException("Retry thread was interrupted", ie);
                    }
                }
            }
        }

        assert lastException != null;
        throw lastException;
    }

    private boolean shouldRetryForException(Throwable ex, Class<? extends Throwable>[] retryFor) {
        for (Class<? extends Throwable> clazz : retryFor) {
            if (clazz.isAssignableFrom(ex.getClass())) {
                return true;
            }
        }
        return false;
    }
}
