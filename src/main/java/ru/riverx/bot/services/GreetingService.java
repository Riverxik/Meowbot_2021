package ru.riverx.bot.services;

/**
 * Created by RiVeRx on 28.04.2021.
 */
public class GreetingService {

    public static String getRandomGreeting() {
        int random = (int) (Math.random() * 10); // 0 - 10
        switch (random) {
            case 0: return "Салют";
            case 1: return "Приветствую";
            case 2: return "Хеллоу";
            case 3: return "Авэ";
            case 4: return "Ола";
            case 5: return "Намасте";
            case 6: return "Хола";
            case 7: return "Аллоха";
            case 8: return "Гамарджоба";
            case 9: return "Бонжур";
            default: return "Привет";
        }
    }

    public static boolean isThatGreeting(String updateMessage) {
        return updateMessage.matches("(прив.*|хай|салют|добр.*|утр.*|вечер.*|ден.*|дня|здра.*)");
    }

    public static boolean isThatAppeal(String updateMessage) {
        return updateMessage.matches("мяу.*|бот.*|слушай|мур");
    }

    public static String getRandomResponse() {
        int random = (int) (Math.random() * 10); // 0 - 10
        switch (random) {
            case 0: return "Да?";
            case 1: return "Мур";
            case 2: return "Я тут";
            case 3: return "Слушаю";
            case 4: return "На месте";
            case 5: return "Готов помочь";
            case 6: return "Угу";
            case 7: return "Всегда на связи";
            case 8: return "На связи";
            case 9: return "Я ждал";
            default: return "хммм";
        }
    }
}
