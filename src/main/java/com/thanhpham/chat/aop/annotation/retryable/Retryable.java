package com.thanhpham.chat.aop.annotation.retryable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Retryable {
    // Số lượng retry tối đa
    int limit() default 3;
    // Thời gian chờ thử lại sau khi lỗi, tính bằng ms
    long delay() default 1000L;
    // danh sách exception sẽ được retry
    Class<? extends Throwable>[] retryFor() default { Throwable.class };
}
