package com.thanhpham.chat.component;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
public class AsyncExecutor {
    /**
     * Chạy một Supplier bất đồng bộ và trả về kết quả dưới dạng CompletableFuture.
     * Thích hợp cho tác vụ tính toán/I-O có kết quả trả về.
     */
    @Async("virtualTaskExecutor")
    public <T> CompletableFuture<T> executeAsync(Supplier<T> supplier) {
        return CompletableFuture.completedFuture(supplier.get());
    }

    /**
     * Chạy một Function nhận đầu vào input và trả về kết quả async.
     */
    @Async("virtualTaskExecutor")
    public <T, R> CompletableFuture<R> executeAsync(Function<T, R> function, T input) {
        return CompletableFuture.completedFuture(function.apply(input));
    }

    /**
     * Chạy một tác vụ Runnable không trả về kết quả (Fire-and-forget).
     */
    @Async("virtualTaskExecutor")
    public CompletableFuture<Void> runAsync(Runnable runnable) {
        runnable.run();
        return CompletableFuture.completedFuture(null);
    }
}
