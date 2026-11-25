package com.thanhpham.chat.client;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import com.thanhpham.chat.service.IChatService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramClient {
    private final TelegramBot telegramBot;
    private final IChatService iChatService;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);
    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    @Value("#{'@' + '${client.telegram.name}'}")
    private String BOT_NAME;

    @PostConstruct
    void setup() {
        telegramBot.setUpdatesListener(updates -> {
            updates.stream()
                    .filter(update -> ObjectUtils.isNotEmpty(update.message()) && ObjectUtils.isNotEmpty(update.message().text()))
                    .forEach(this::handleMessage);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    @PreDestroy
    public void cleanup() {
        log.info("Shutdown Telegram service");
        telegramBot.removeGetUpdatesListener();
        log.info("Cleanup scheduler queue");
        scheduler.shutdown();
        log.info("Cleanup virtual thread pool");
        executor.shutdown();
    }

    private void handleMessage(Update update) {
        long chatId = update.message().chat().id();
        Long userId = update.message().from().id();
        String userName = update.message().from().firstName() + " " + update.message().from().lastName();
        String userText = update.message().text().replace(BOT_NAME, "").trim();

        AtomicBoolean isProcessing = new AtomicBoolean(true);
        ScheduledFuture<?> typingTask = scheduler.scheduleAtFixedRate(() -> {
            if (isProcessing.get()) telegramBot.execute(new SendChatAction(chatId, ChatAction.typing));
        }, 0, 4000, TimeUnit.MILLISECONDS);

        CompletableFuture.supplyAsync(() -> iChatService.chat(userId.toString(), userText), executor)
                .whenCompleteAsync((result, throwable) -> {
                    isProcessing.set(false);
                    if (!typingTask.isCancelled()) typingTask.cancel(false);

                    SendResponse response = telegramBot.execute(new SendMessage(chatId, result));
                    if (response.isOk()) {
                        log.info("SUCCESS RESPONSE");
                        log.info("USER ID: {}", userId);
                        log.info("USER NAME: {}", userName);
                        log.info("USER TEXT: {}", userText);
                        log.info("RESPONSE LLM: {}", result);
                    }
                }, executor);
    }
}
