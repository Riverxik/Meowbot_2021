package ru.riverx.bot.services;

import org.json.JSONObject;
import ru.riverx.utils.DBUsers;
import ru.riverx.utils.Database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by RiVeRx on 29.04.2021.
 */
public class WeatherService {

    public static boolean isWeatherCommand(String userString) {
        return userString.startsWith("погода");
    }

    public static String getWeather(Long chatId, String userText) {
        String api = Database.weatherApi;
        String city = userText.substring(7);
        String response = RequestService.sendGet(
            "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + api + "&units=metric&lang=ru");
        if (response != null) {
            return getStringAnswer(chatId, new Weather(new JSONObject(response)));
        }
        return String.format("%sМне не удалось найти %s. Пожалуйста проверьте правильность написания.%s",
                EEmoji.THINKING.getCode(),
                city,
                EEmoji.THINKING.getCode());
    }

    private static String getStringAnswer(Long chatId, Weather weather) {
        double rain = weather.getRainCount();
        double snow = weather.getSnowCount();
        int minTemp = weather.getMinTemperature();
        int maxTemp = weather.getMaxTemperature();
        int userTimeDelay = DBUsers.getUserTime(chatId);
        String currentTime = getCurrentTimeWithDelay(weather.getCurrentTime(), userTimeDelay);
        String sunRiseTime = getCurrentTimeWithDelay(weather.getSunrise(), userTimeDelay);
        String sunSetTime = getCurrentTimeWithDelay(weather.getSunset(), userTimeDelay);

        StringBuilder builder = new StringBuilder();

        builder.append(String.format("%sПогода на %s %s %s\n%s %s\n",
                EEmoji.RED_TRIANGLE_POINTED_UP.getCode(),
                EEmoji.CLOCK.getCode(), currentTime,
                EEmoji.RED_TRIANGLE_POINTED_UP.getCode(),
                EEmoji.TOWN.getCode(), weather.getCity()));
        builder.append(String.format("%s: %s\n", weather.getMainWeather(), weather.getDescription()));
        builder.append(String.format("%sОблачность: %d%%\n",
                EEmoji.CLOUD.getCode(), weather.getCloudiness()));
        builder.append(String.format("%sТемпература: %d°C\n",
                EEmoji.THERMOMETER.getCode(), weather.getCurrentTemperature()));
        if (minTemp != maxTemp)
            builder.append(String.format("%sМинимальная: %d°C, %sМаксимальная: %d°C\n",
                    EEmoji.DOWN_ARROW.getCode(), minTemp,
                    EEmoji.UP_ARROW.getCode(), maxTemp));
        if (rain != 0.0)
            builder.append(String.format("%sДождь за посленее время: %.2f мм.\n",
                    EEmoji.RAIN.getCode(), rain));
        if (snow != 0.0)
            builder.append(String.format("%sСнег за посленее время: %.2f мм.\n",
                    EEmoji.SNOW.getCode(), snow));
        builder.append(String.format("%sВидимость: %d метров\n", EEmoji.GLASSES.getCode(), weather.getVisibility()));
        builder.append(String.format("%sДавление: %.2f мм рт. ст.\n%sВлажность: %d%%\n",
                EEmoji.DIAMOND_WITH_DOT.getCode(), weather.getCurrentPressure(),
                EEmoji.DROPLET.getCode(), weather.getCurrentHumidity()));
        builder.append(String.format("%sВетер: %s, %d м/c\n",
                EEmoji.COMPASS.getCode(), weather.getWindDirection(), weather.getWindSpeed()));
        builder.append(String.format("%sВосход: %s %s,\n%sЗакат: %s %s",
                EEmoji.SUNRISE.getCode(), EEmoji.CLOCK.getCode(), sunRiseTime,
                EEmoji.SUNSET.getCode(), EEmoji.CLOCK.getCode(), sunSetTime));

        return builder.toString();
    }

    private static String getCurrentTimeWithDelay(String time, int userTimeDelay) {
        String timeString = null;
        try {
            Calendar calendar = Calendar.getInstance();
            String pattern = "dd.MM.yyyy HH:mm:ss";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            Date date = simpleDateFormat.parse(time);
            calendar.setTime(date);
            calendar.add(Calendar.HOUR_OF_DAY, userTimeDelay);
            timeString = simpleDateFormat.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeString;
    }
}
