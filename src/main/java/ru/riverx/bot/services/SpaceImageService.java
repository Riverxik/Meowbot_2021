package ru.riverx.bot.services;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by RiVeRx on 29.04.2021.
 */
public class SpaceImageService {
    public static String[] getImageOfTheDay() {
        String result = "К сожалению, у меня не получилось получить картинку дня :с";
        ImageOfTheDay img;
        String response = RequestService.sendGet("https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY");
        if (response != null) {
            img = parseResponse(response);
            assert img != null;
            String pretty = getPrettyImageOfTheDayString(img);
            return new String[] { pretty, img.url };
        }
        return new String[] { result, "Попробуйте позже..." };
    }

    public static ImageOfTheDay parseResponse(String response) {
        JSONParser parser = new JSONParser();
        JSONObject jso = null;
        try {
            jso = (JSONObject) parser.parse(response);
            String date = (String) jso.get("date");
            String explanation = (String) jso.get("explanation");
            String hdUrl = (String) jso.get("hdurl");
            String mediaType = (String) jso.get("media_type");
            boolean isVideo = !mediaType.equals("image");
            String serviceVersion = (String) jso.get("service_version");
            String title = (String) jso.get("title");
            String url = (String) jso.get("url");
            return new ImageOfTheDay(date, explanation, hdUrl, isVideo, serviceVersion, title, url);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getPrettyImageOfTheDayString(ImageOfTheDay img) {
        StringBuilder b = new StringBuilder();
        b.append(String.format("%s Картинка дня %s\n",
                EEmoji.SMALL_ORANGE_DIAMOND.getCode(),
                EEmoji.SMALL_ORANGE_DIAMOND.getCode()));
        b.append(String.format("%s Дата: %s\n",
                EEmoji.CLOCK.getCode(),
                img.date));
        b.append(String.format("%s %s\n",
                EEmoji.BOOK_DIARY.getCode(),
                img.title));
        b.append(img.explanation.substring(0, 600)).append("...\n");
        if (img.isVideo) { b.append(String.format("%s Url:%s", EEmoji.SMALL_BLUE_DIAMOND.getCode(), img.url)); }
        else {
            b.append(String.format("%s HD:%s",
                    EEmoji.SMALL_BLUE_DIAMOND.getCode(),
                    img.hdUrl));
        }
        return b.toString();
    }
}

class ImageOfTheDay {
    String date;
    String explanation;
    String hdUrl;
    boolean isVideo = false; // false mean image.
    String serviceVersion;
    String title;
    String url;

    public ImageOfTheDay(String date, String explanation, String hdUrl, boolean isVideo, String serviceVersion, String title, String url) {
        this.date = date;
        this.explanation = explanation;
        this.hdUrl = hdUrl;
        this.isVideo = isVideo;
        this.serviceVersion = serviceVersion;
        this.title = title;
        this.url = url;
    }
}
