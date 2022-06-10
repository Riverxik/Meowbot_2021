package ru.riverx.bot.extensions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.riverx.bot.Meowbot;

import java.io.*;
import java.util.*;

public class KoreanWordExtension extends BaseTextExtension {
    private static final Logger log = LoggerFactory.getLogger(KoreanWordExtension.class);
    private static final String START_COMMAND = "/korean";
    private static final String ADD_COMMAND = "/addWords";
    private static final String FILE_PATH = "words.mw";
    private static final String CONTINUE = "Продолжить";
    private static final String END = "Закончить";
    private boolean isAddingNewWords = false;
    private List<String> wordKeys;
    private List<String> wordValues;
    private Question currentQuestion;

    public KoreanWordExtension(Meowbot bot) {
        super(bot);
        readWordCollection();
    }

    /**
     * Генерирует вопрос для пользователя.
     * @param chatId - чат пользователя.
     * @param messageId - id сообщения для изменения (для повторных генераций)
     */
    private void generateQuestion(String chatId, Integer messageId) {
        // Выбираем 4 случайных слова.
        String[] words = new String[4];
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            int ind = (int)(Math.random() * wordKeys.size());
            while (indices.contains(ind)) {
               ind = (int)(Math.random() * wordKeys.size());
            }
            indices.add(ind);
            words[i] = wordValues.get(ind);
        }
        // Выбираем из них правильный ответ случайным образом.
        int index = (int)(Math.random() * 4);
        String answer = wordKeys.get(indices.get(index));
        // Генерируем вопрос.
        currentQuestion = new Question(answer, wordValues.get(indices.get(index)), words);
        // Получаем клавиатуру.
        InlineKeyboardMarkup keyboard = getKeyboard(words);
        if (messageId == null) {
            meowbot.sendMessage(chatId, currentQuestion.word, keyboard);
        } else {
            meowbot.editMessageText(chatId, messageId, currentQuestion.word, keyboard);
        }
    }

    /**
     * Возвращает клавиатуру с 4 вариантами ответов.
     * @param words - Массив с 4 вариантами
     * @return - Клавиатуру с 4 вариантами ответов.
     */
    private InlineKeyboardMarkup getKeyboard(String... words) {
        List<InlineKeyboardButton> buttons = new ArrayList<>(words.length);
        ArrayList<List<InlineKeyboardButton>> buttonRows = new ArrayList<>();
        int ind = 0;
        for (String text : words) {
            buttons.add(InlineKeyboardButton.builder().callbackData(text).text(text).build());
            ind++;
            if (ind > 1) {
                ind = 0;
                buttonRows.add(buttons);
                buttons = new ArrayList<>();
            }
        }
        return InlineKeyboardMarkup.builder().keyboard(buttonRows).build();
    }

    /**
     * Считывает текущую коллекцию слов из файла в память.
     */
    private void readWordCollection() {
        wordKeys = new ArrayList<>();
        wordValues = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            while (reader.ready()) {
                String line = reader.readLine();
                wordKeys.add(line.split("-")[0].trim());
                wordValues.add(line.split("-")[1].trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Ошибка при чтении слов из файла: {}", e.getMessage());
        }
    }

    /**
     * Записывает текущую коллекцию слов в файл.
     */
    private void writeWordCollection() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String key : wordKeys) {
                writer.write(key + " - " + wordValues.get(wordKeys.indexOf(key)) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Ошибка при записи слов в файл: {}", e.getMessage());
        }
    }

    /**
     * Получает сырую строку от пользователя со списком слов для добавления в словарь
     * @param rawText - сырая строка от пользователя
     */
    private boolean addNewWords(String rawText) {
        if (rawText.contains("-")) {
            String[] lines = rawText.split("\n");
            for (String line : lines) {
                String key = line.split("-")[0].trim();
                String value = line.split("-")[1].trim();
                if (!wordKeys.contains(key)) {
                    wordKeys.add(key);
                    wordValues.add(value);
                }
            }
            writeWordCollection();
            return true;
        }
        return false;
    }

    private void executeCallback(Long chatId, Integer messageId, CallbackQuery callbackQuery) {
        String userChoice = callbackQuery.getData();
        if (userChoice.equalsIgnoreCase(CONTINUE)) {
            generateQuestion(chatId.toString(), messageId);
        } else if (userChoice.equalsIgnoreCase(END)) {
            meowbot.deleteMessage(chatId.toString(), messageId);
            meowbot.sendMessage(chatId.toString(), "Хорошо поработали! ;)");
        } else if (currentQuestion.answer.equalsIgnoreCase(userChoice)) {
            meowbot.editMessageText(chatId.toString(), messageId, "Правильно!", getKeyboard(CONTINUE, END));
        } else {
            meowbot.editMessageText(chatId.toString(), messageId, "Не правильно! Правильно: " + currentQuestion.answer,
                    getKeyboard(CONTINUE, END));
        }
    }

    @Override
    public boolean executeIfValid(Update update) {
        if (update.hasCallbackQuery()) {
            Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            executeCallback(chatId, messageId, update.getCallbackQuery());
            return true;
        } else {
            Long chatId = update.getMessage().getChatId();
            if (chatId == Meowbot.CREATOR_CHAT_ID) {
                return super.executeIfValid(update);
            }
        }
        return false;
    }

    @Override
    protected boolean execute(Long chatId, String text) {
        if (text.equalsIgnoreCase(START_COMMAND) && !isAddingNewWords) {
            generateQuestion(chatId.toString(), null);
            return true;
        } else if (text.equalsIgnoreCase(ADD_COMMAND) && !isAddingNewWords) {
            meowbot.sendMessage(chatId.toString(), "Пожалуйста напиши список слов для добавления парами. Каждую пару с новой строки: \nпривет - 안녕\nсобака - 개");
            isAddingNewWords = true;
        } else if (isAddingNewWords) {
            boolean result = addNewWords(text);
            isAddingNewWords = false;
            if (result) {
                meowbot.sendMessage(chatId.toString(), "Слова успешно добавлены в словарь!");
            } else {
                meowbot.sendMessage(chatId.toString(), "Мне не удалось добавить новые слова :с");
            }
        }
        return false;
    }
}

class Question {
    String word;
    String answer;
    String[] falseVariants;

    public Question(String word, String answer, String[] variants) {
        this.word = word;
        this.answer = answer;
        this.falseVariants = variants;
    }
}
