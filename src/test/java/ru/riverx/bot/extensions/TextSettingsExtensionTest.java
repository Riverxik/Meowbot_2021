package ru.riverx.bot.extensions;

import org.junit.BeforeClass;
import org.junit.Test;
import ru.riverx.Application;
import ru.riverx.bot.Meowbot;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TextSettingsExtensionTest {
    private static TextSettingsExtension ext;

    @BeforeClass
    public static void setUp() {
        if (Application.meowbot == null) {
            Application.main(new String[]{"true"});
        }
        for (BotExtension extension : Application.meowbot.getExtensionList()) {
            if (extension instanceof TextSettingsExtension) {
                ext = (TextSettingsExtension) extension;
            }
        }
        assert ext != null;
    }

    @Test
    public void executeMyTown() {
        boolean result = ext.execute(Meowbot.CREATOR_CHAT_ID, "Мой город Саратов");
        assertTrue(result);
    }

    @Test
    public void executeMyTZ() {
        boolean result = ext.execute(Meowbot.CREATOR_CHAT_ID, "Мой пояс 5");
        assertTrue(result);
    }

    @Test
    public void executeFalse() {
        boolean result = ext.execute(Meowbot.CREATOR_CHAT_ID, "Жопа суслика");
        assertFalse(result);
    }
}