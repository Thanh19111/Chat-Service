package com.thanhpham.chat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.VirtualThreadTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@Configuration
public class AsyncPoolConfig {

    @Bean(name = "virtualTaskExecutor")
    public TaskExecutor virtualTaskExecutor() {
        return new VirtualThreadTaskExecutor("vt-async-");
    }
}
