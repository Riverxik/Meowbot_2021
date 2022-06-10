package ru.riverx.utils;

/**
 * Created by RiVeRx on 28.04.2021.
 */
public class Utils {
    public static String encode(String line) {
        char[] symbols = line.toCharArray();
        StringBuilder result = new StringBuilder();
        for (char sym :
                symbols) {
            if (Character.isUpperCase(sym))
                result.append((char) (Character.toLowerCase(sym) + (Math.random() * 10) - 5));
            else result.append(sym);
        }
        return result.toString();
    }
}
