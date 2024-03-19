package ru.riverx.bot.extensions;

import org.junit.BeforeClass;
import org.junit.Test;
import ru.riverx.Application;
import ru.riverx.bot.Meowbot;
import ru.riverx.utils.DBUsers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WeatherExtensionTest {
    private static WeatherExtension ext;

    @BeforeClass
    public static void setUp() {
        if (Application.meowbot == null) {
            Application.main(new String[]{"true"});
        }
        for (BotExtension extension : Application.meowbot.getExtensionList()) {
            if (extension instanceof WeatherExtension) {
                ext = (WeatherExtension) extension;
            }
        }
        assert ext != null;
    }

    @Test
    public void executeFalse() {
        boolean result = ext.execute(Meowbot.CREATOR_CHAT_ID, "Жопа суслика");
        assertFalse(result);
    }

    @Test
    public void executeWeatherUserWithCity() {
        boolean result = ext.execute(Meowbot.CREATOR_CHAT_ID, "Погода");
        assertTrue(result);
    }

    @Test
    public void executeWeatherUserWithoutCity() {
        boolean result = ext.execute(123123L, "Погода");
        assertTrue(result);
    }

    @Test
    public void executeWeatherWithCityName() {
        DBUsers.addUserCity(Meowbot.CREATOR_CHAT_ID, "Саратов");
        DBUsers.setUserTimeFlag(Meowbot.CREATOR_CHAT_ID, false);
        boolean result = ext.execute(Meowbot.CREATOR_CHAT_ID, "Погода Саратов");
        DBUsers.setUserTimeFlag(Meowbot.CREATOR_CHAT_ID, true);
        assertTrue(result);
    }
}