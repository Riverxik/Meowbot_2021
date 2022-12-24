package ru.riverx.bot.extensions;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import ru.riverx.Application;
import ru.riverx.bot.Meowbot;
import ru.riverx.utils.Database;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HappyGraphExtensionTest {
    private static HappyGraphExtension ext;

    @BeforeClass
    public static void setUp() throws InterruptedException {
        Thread.sleep(1000);
        Application.main(new String[]{"true"});
        for (BotExtension extension : Application.meowbot.getExtensionList()) {
            if (extension instanceof HappyGraphExtension) {
                ext = (HappyGraphExtension) extension;
            }
        }
        assert ext != null;
    }

    @After
    public void tearDown() {
        Database.filePath = "values.csv";
    }

    @Test
    public void executeNewValueAdded() {
        boolean result = ext.execute(Meowbot.CREATOR_CHAT_ID, "/newValue 2");
        assertTrue(result);
    }

    @Test
    public void executeNewValueWrongCommand() {
        boolean result = ext.execute(Meowbot.CREATOR_CHAT_ID, "/newVal 3");
        assertFalse(result);
    }

    @Test (expected = NumberFormatException.class)
    public void executeWrongArgument() {
        boolean result = ext.execute(Meowbot.CREATOR_CHAT_ID, "/newValue string");
        assertFalse(result);
    }

    @Test
    public void executeErrorWhileReadingFile() {
        Database.filePath = "values"; // file does not exists.
        boolean result = ext.execute(Meowbot.CREATOR_CHAT_ID, "/newValue 3");
        assertFalse(result);
    }

    @Test
    public void executeTestEmptyFile() throws IOException {
        boolean result = false;
        Database.filePath = "test.txt";
        // Создать файл
        File file = new File(Database.filePath);
        if (file.createNewFile()) {
            // Попробовать записать в новый файл.
            result = ext.execute(Meowbot.CREATOR_CHAT_ID, "/newValue 1");
        }
        // Удалить файл.
        result &= file.delete();
        assertTrue(result);
    }
}