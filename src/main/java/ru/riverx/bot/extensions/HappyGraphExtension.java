package ru.riverx.bot.extensions;

import ru.riverx.bot.Meowbot;
import ru.riverx.utils.Database;

import java.io.*;

public class HappyGraphExtension extends BaseTextExtension {
    private final Meowbot meowbot;

    public HappyGraphExtension(Meowbot bot) {
        super(bot);
        this.meowbot = bot;
    }

    /**
     * Добавляет новый номер в файл
     * @param number - номер, который должен быть добавлен в файл
     */
    private boolean addNewNumber(int number, Long chatId) {
        int index = 0;
        int lastNumber = 0;
        String lastLine = null;
        StringBuilder sb = new StringBuilder();
        String filePath = Database.filePath;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
                lastLine = line;
            }
        } catch (IOException e) {
            e.printStackTrace();
            meowbot.sendMessage(chatId, "Ошибка при чтении файла очков");
            return false;
        }

        if (lastLine != null) {
            index = Integer.parseInt(lastLine.split(",")[0]) + 1;
            lastNumber = Integer.parseInt(lastLine.split(",")[1]);
        }
        sb.append(index).append(",").append(lastNumber + number);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(sb.toString());
        } catch (IOException ignored) {/* Never should get here... */}
        return true;
    }

    @Override
    protected boolean execute(Long chatId, String text) {
        if (text.startsWith("/newValue")) {
            int number = Integer.parseInt(text.split(" ")[1]);
            if (addNewNumber(number, chatId)) {
                meowbot.sendMessage(chatId, "Новое число " + number + " успешно добавлено!");
                return true;
            }
        }
        return false;
    }
}
