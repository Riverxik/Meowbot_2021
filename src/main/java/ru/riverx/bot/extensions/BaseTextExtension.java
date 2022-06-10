package ru.riverx.bot.extensions;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.riverx.bot.Meowbot;

/**
 * Created by RiVeRx on 28.04.2021.
 */
public abstract class BaseTextExtension implements BotExtension {
    protected final Meowbot meowbot;
    public BaseTextExtension(Meowbot bot) {
        this.meowbot = bot;
    }

    @Override
    public boolean executeIfValid(Update update) {
        if (update.hasMessage()) {
            Message msg = update.getMessage();
            if (msg.hasText()) {
                String text = msg.getText();
                Long chatId = msg.getChatId();
                return execute(chatId, text);
            }
        }
        return false;
    }

    protected abstract boolean execute(Long chatId, String text);
}
