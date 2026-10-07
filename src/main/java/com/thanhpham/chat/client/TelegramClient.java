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
    private final String BOT_NAME = "@" + "thanhpv_bot";

    @PostConstruct
    void setup() {
        telegramBot.setUpdatesListener(updates -> {
            updates.stream()
                    .filter(update -> update.message() != null && update.message().text() != null)
                    .forEach(this::handleMessage);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    @PreDestroy
    public void cleanup() {
        telegramBot.removeGetUpdatesListener();
        scheduler.shutdown();
        executor.shutdown();
    }

    private void handleMessage(Update update) {
        long chatId = update.message().chat().id();
        Long userId = update.message().from().id();
        String userName = update.message().from().firstName() + " " + update.message().from().lastName();
        String userText = update.message().text().replace(BOT_NAME, "").trim();

        AtomicBoolean isProcessing = new AtomicBoolean(true);
        ScheduledFuture<?> typingTask = startTypingEffect(chatId, isProcessing);
        CompletableFuture<String> task = generateText(String.valueOf(userId), userText)
                .whenCompleteAsync((result, throwable) -> {
                    isProcessing.set(false);
                    if (!typingTask.isCancelled()) {
                        typingTask.cancel(false);
                    }

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

    private ScheduledFuture<?> startTypingEffect(long chatId, AtomicBoolean isProcessing) {
        telegramBot.execute(new SendChatAction(chatId, ChatAction.typing));
        return scheduler.scheduleAtFixedRate(() -> {
            if (isProcessing.get()) {
                telegramBot.execute(new SendChatAction(chatId, ChatAction.typing));
            }
        }, 2000, 4000, TimeUnit.MILLISECONDS);
    }

    public CompletableFuture<String> generateText(String userId, String message) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return iChatService.chat(userId, message);
            } catch (Exception e) {
                log.error(e.getMessage());
                return "Something went wrong, please try again :(";
            }
        }, executor);
    }
}
