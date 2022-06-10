package ru.riverx.bot.services;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by RiVeRx on 29.04.2021.
 */
public class Weather {
    private String mainWeather = "unknown"; // Состояние.
    private String description = "unknown"; // Описание погоды
    private String sunrise = "not set";     // Время восхода.
    private String sunset = "not set";      // Время заката.
    private String currentTime = "not set"; // Текущее время.
    private String windDirection = "";      // Направление ветра.
    private String city = "Unknown city";   // Город.
    private int iconId = 0;                 // Идентификатор иконки.
    private int currentTemperature = 0;     // Текущая температура.
    private int maxTemperature = 0;         // Максимальная температура
    private int minTemperature = 0;         // Минимальная температура.
    private int cloudiness = 0;             // Облачность.
    private int currentHumidity = 0;        // Влажность
    private int windSpeed = 0;              // Скорость ветра.
    private int visibility = 0;             // Видимость в метрах.
    private double currentPressure = 0.0;   // Давление.
    private double rainCount = 0.0;         // Количество дождя в мм.
    private double snowCount = 0.0;         // Количество снега в мм.

    public Weather(JSONObject response) {
        setMainWeather(response.getJSONArray("weather"));
        this.currentTime = getHumanTimeFromUTC(response.getInt("dt"));
        this.city = response.getString("name");
        setTemperature(response.getJSONObject("main"));
        setCloudiness(response.getJSONObject("clouds"));
        if (response.has("visibility"))
            this.visibility = response.getInt("visibility");
        if (response.has("rain"))
            setRainIfExists(response.getJSONObject("rain"));
        if(response.has("snow"))
            setSnowIfExists(response.getJSONObject("snow"));
        setTime(response.getJSONObject("sys"));
        setWind(response.getJSONObject("wind"));
    }

    private void setMainWeather(JSONArray weather) {
        JSONObject w = weather.getJSONObject(0);
        this.mainWeather = translateWeather(w.getString("main"));
        this.description = w.getString("description");
        this.iconId = w.getInt("id");
    }

    private void setTemperature(JSONObject w) {
        this.currentTemperature = w.getInt("temp");
        this.minTemperature = w.getInt("temp_min");
        this.maxTemperature = w.getInt("temp_max");
        this.currentHumidity = w.getInt("humidity");
        this.currentPressure = w.getInt("pressure")*0.75;
    }

    private void setCloudiness(JSONObject cloud) {
        this.cloudiness = cloud.getInt("all");
    }

    private void setRainIfExists(JSONObject rain) {
        if(rain.has("3h"))
            this.rainCount = rain.getDouble("3h");
        if(rain.has("1h"))
            this.rainCount = rain.getDouble("1h");
    }

    private void setSnowIfExists(JSONObject snow) {
        if(snow.has("3h"))
            this.snowCount = snow.getDouble("3h");
        if(snow.has("1h"))
            this.snowCount = snow.getDouble("1h");
    }

    private void setTime(JSONObject w) {
        this.sunrise = getHumanTimeFromUTC(w.getInt("sunrise"));
        this.sunset = getHumanTimeFromUTC(w.getInt("sunset"));
    }

    private void setWind(JSONObject w) {
        this.windSpeed = w.getInt("speed");
        calculateWindDirection(w.getInt("deg"));
    }

    private String getHumanTimeFromUTC(long time) {
        return new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new java.util.Date (time*1000));
    }

    private String translateWeather(String main) {
        switch (main.toLowerCase()) {
            case "thunderstorm": return EEmoji.LIGHTING.getCode() + "Гроза";
            case "drizzle": return EEmoji.RAIN.getCode() + "Мелкий дождь";
            case "rain": return EEmoji.RAIN.getCode() + "Дождь";
            case "snow": return EEmoji.SNOW.getCode() +"Снег";
            case "clear": return EEmoji.SUNNY.getCode() + "Чистое небо";
            case "clouds": return EEmoji.CLOUD.getCode() + "Облачно";
            case "mist":
            case "fog":
            case "smoke": return EEmoji.FOG.getCode() + "Туман";
            case "haze": return EEmoji.FOG.getCode() + "Мгла";
            case "dust": return "Пыль";
            case "ash": return "Вулканический пепел";
            case "squall": return EEmoji.TORNADO.getCode() + "Вихрь";
            case "tornado": return EEmoji.TORNADO.getCode() + "Торнадо";
            default:
                return "Не понятно";
        }
    }

    private void calculateWindDirection(int degrees) {
        if (degrees >= 0 && degrees < 45)
            this.windDirection = "Северный";
        if (degrees >= 45 && degrees < 90)
            this.windDirection = "Северо-восточный";
        if (degrees >= 90 && degrees < 135)
            this.windDirection = "Восточный";
        if (degrees >= 135 && degrees < 180)
            this.windDirection = "Юго-восточный";
        if (degrees >= 180 && degrees < 225)
            this.windDirection = "Южный";
        if (degrees >= 225 && degrees < 270)
            this.windDirection = "Юго-западный";
        if (degrees >= 270 && degrees < 315)
            this.windDirection = "Западный";
        if (degrees >= 315 && degrees < 360)
            this.windDirection = "Северо-западный";
    }

    public String getMainWeather() {
        return mainWeather;
    }

    public String getDescription() {
        return description;
    }

    public int getIconId() {
        return iconId;
    }

    public String getCity() {
        return city;
    }

    public int getCurrentTemperature() {
        return currentTemperature;
    }

    public int getMaxTemperature() {
        return maxTemperature;
    }

    public int getMinTemperature() {
        return minTemperature;
    }

    public double getCurrentPressure() {
        return currentPressure;
    }

    public int getCurrentHumidity() {
        return currentHumidity;
    }

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public int getCloudiness() {
        return cloudiness;
    }

    public double getRainCount() {
        return rainCount;
    }

    public double getSnowCount() {
        return snowCount;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public int getVisibility() {
        return visibility;
    }
}
