package ru.riverx.bot.extensions;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Created by RiVeRx on 28.04.2021.
 */
public interface BotExtension {
    boolean executeIfValid(Update update);
}
