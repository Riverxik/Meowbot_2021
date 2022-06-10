package ru.riverx.bot.services;

import ru.riverx.utils.DBUsers;

/**
 * Created by RiVeRx on 29.04.2021.
 */
public class SettingsService {
    public static String showUserSettings(long chatId) {
        String city = DBUsers.getCityByChatId(chatId);
        String name = DBUsers.getNameByChatId(chatId);
        int time = DBUsers.getUserTime(chatId);
        return String.format(
                "Твои настройки:\n" +
                "%s Имя: %s\n" +
                "%s Город: %s\n" +
                "%s Время: %d",
                EEmoji.DIAMOND_WITH_DOT.getCode(),
                name,
                EEmoji.DIAMOND_WITH_DOT.getCode(),
                city,
                EEmoji.DIAMOND_WITH_DOT.getCode(),
                time);
    }
}
