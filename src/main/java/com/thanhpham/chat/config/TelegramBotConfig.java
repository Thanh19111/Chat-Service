package com.thanhpham.chat.config;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramBotConfig {
    @Value("${client.telegram.token}")
    private String apiToken;

    @Bean
    TelegramBot initTelegramBot(){
        return new TelegramBot(apiToken);
    }
}
