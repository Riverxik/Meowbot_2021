package ru.riverx.bot.extensions;

import ru.riverx.bot.Meowbot;
import ru.riverx.bot.services.WeatherService;
import ru.riverx.utils.DBUsers;

/**
 * Created by RiVeRx on 29.04.2021.
 */
public class WeatherExtension extends BaseTextExtension {
    public WeatherExtension(Meowbot bot) {
        super(bot);
    }

    @Override
    protected boolean execute(Long chatId, String text) {
        if (WeatherService.isWeatherCommand(text.toLowerCase())) {
            String chat = chatId.toString();
            boolean isDone = true;
            boolean timeFlag = DBUsers.getUserTimeFlag(chatId);
            if ("погода".equalsIgnoreCase(text)) {
                String city = DBUsers.getCityByChatId(chatId);
                if (city != null) {
                    meowbot.sendMessage(chat, WeatherService.getWeather(chatId, "погода " + city));
                } else {
                    meowbot.sendMessage(chat, "К сожалению, я не знаю твоего города.\n" +
                            "Используй погода <город> чтобы узнать погоду для конкретного города, " +
                            "либо я могу запомнить твой город командой 'Мой город <город>'");
                    isDone = false;
                }
            } else {
                meowbot.sendMessage(chat, WeatherService.getWeather(chatId, text));
            }
            if (!timeFlag && isDone) {
                meowbot.sendMessage(chat, "Псс, если тебе кажется, что текущее время в погоде отображается не правильно," +
                        "то ты можешь исправить его в 'нужную сторону' с помощью команды 'Мой пояс <число>'.\n" +
                        "Например, время отстаёт на 2 часа, тогда используй команду 'Мой пояс 2'\n" +
                        "В качестве параметра ты можешь указать числа от -23 до 23\n" +
                        "Ах да, если всё нормально, но ты не хочешь видеть это сообщение, используй /flag1_off");
            }
            return true;
        }
        return false;
    }
}
