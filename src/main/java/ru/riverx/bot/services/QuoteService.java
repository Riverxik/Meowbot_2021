package ru.riverx.bot.services;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class QuoteService {
    public static String getQuoteOfTheDay() {
        String result = "К сожалению, у меня не получилось получить цитату дня :с";
        int key = generateTodayKey();
        QuoteOfTheDay quote;
        String response = RequestService.sendGet("https://api.forismatic.com/api/1.0/?method=getQuote&format=json&lang=ru&key="+key);
        if (response != null) {
            quote = parseResponse(response);
            assert quote != null;
            return getPrettyQuoteOfTheDayString(quote);
        }
        return result;
    }

    private static int generateTodayKey() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));
        return now.getDayOfYear();
    }

    private static QuoteOfTheDay parseResponse(String response) {
        JSONParser parser = new JSONParser();
        JSONObject jso = null;
        try {
            jso = (JSONObject) parser.parse(response);
            String text = (String) jso.get("quoteText");
            String author = (String) jso.get("quoteAuthor");
            String link = (String) jso.get("quoteLink");
            return new QuoteOfTheDay(text, author, link);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getPrettyQuoteOfTheDayString(QuoteOfTheDay quote) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s Случайная цитата %s\n",
                EEmoji.SMALL_BLUE_DIAMOND.getCode(),
                EEmoji.SMALL_BLUE_DIAMOND.getCode()));
        sb.append(String.format("%s Автор: %s\n",
                EEmoji.UMBRELLA.getCode(),
                quote.author));
        sb.append(String.format("%s %s\n",
                EEmoji.BOOK_DIARY.getCode(),
                quote.text));
        return sb.toString();
    }
}

class QuoteOfTheDay {
    String text;
    String author;
    String link;

    public QuoteOfTheDay(String text, String author, String link) {
        this.text = text;
        this.author = author;
        this.link = link;
    }
}
