package ru.riverx.bot.extensions;

import org.junit.BeforeClass;
import org.junit.Test;
import org.telegram.telegrambots.meta.api.objects.Audio;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.riverx.Application;
import ru.riverx.bot.Meowbot;

import static org.junit.Assert.assertFalse;

public class BaseTextExtensionTest {
    private static BaseTextExtension ext;

    @BeforeClass
    public static void setUp() {
        if (Application.meowbot == null) {
            Application.main(new String[]{"true"});
        }
        for (BotExtension extension : Application.meowbot.getExtensionList()) {
            if (extension instanceof BaseTextExtension) {
                ext = (BaseTextExtension) extension;
            }
        }
        assert ext != null;
    }


    @Test
    public void executeIfValidHasMessage() {
        Update update = new Update();
        Message message = new Message();
        Chat chat = new Chat();
        chat.setId(Meowbot.CREATOR_CHAT_ID);
        message.setText("test");
        message.setChat(chat);
        update.setMessage(message);
        boolean result = ext.executeIfValid(update);
        assertFalse(result);
    }

    @Test
    public void executeIfValidHasNoMessage() {
        Update update = new Update();
        boolean result = ext.executeIfValid(update);
        assertFalse(result);
    }

    @Test
    public void executeIfValidHasMessageButHasNoText() {
        Update update = new Update();
        Message message = new Message();
        Chat chat = new Chat();
        Audio audio = new Audio();
        chat.setId(Meowbot.CREATOR_CHAT_ID);
        message.setAudio(audio);
        message.setChat(chat);
        update.setMessage(message);
        boolean result = ext.executeIfValid(update);
        assertFalse(result);
    }
}