package ru.riverx.bot.extensions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Created by RiVeRx on 28.04.2021.
 */
public class LogExtension implements BotExtension {
    private static final Logger log = LoggerFactory.getLogger(LogExtension.class);
    @Override
    public boolean executeIfValid(Update update) {
        // For text.
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String msg = update.getMessage().getText();
            log.info(String.format("[Meow]<--[%d][%s]", chatId, msg));
        }
        return false; // Always false cause it don't wanna interrupt another extensions.
    }
}
