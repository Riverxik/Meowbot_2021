package ru.riverx.bot.extensions;

import org.junit.BeforeClass;
import org.junit.Test;
import ru.riverx.Application;
import ru.riverx.bot.Meowbot;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ChatTextExtensionTest {
    private static ChatTextExtension ext;

    @BeforeClass
    public static void setUp() {
        if (Application.meowbot == null) {
            Application.main(new String[]{"true"});
        }
        for (BotExtension extension : Application.meowbot.getExtensionList()) {
            if (extension instanceof ChatTextExtension) {
                ext = (ChatTextExtension) extension;
            }
        }
        assert ext != null;
    }

    @Test
    public void testExecuteGreeting() {
        boolean result = ext.execute(Meowbot.CREATOR_CHAT_ID, "Привет");
        assertTrue(result);
    }

    @Test
    public void testExecuteAppeal() {
        boolean result = ext.execute(Meowbot.CREATOR_CHAT_ID, "мяу");
        assertTrue(result);
    }

    @Test
    public void testExecuteNotGreetingAndNotAppeal() {
        boolean result = ext.execute(Meowbot.CREATOR_CHAT_ID, "Страшное сообщение");
        assertFalse(result);
    }
}