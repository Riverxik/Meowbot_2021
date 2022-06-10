package ru.riverx.bot.services;

/**
 * Created by RiVeRx on 28.04.2021.
 */
public enum EEmoji {
    UMBRELLA("☔"),
    SUNNY("☀"),
    CLOUD("☁"),
    SUNCLOUD("⛅"),
    CLOCK("⌚"),
    NIGHT("\uD83C\uDF03"),
    SUNRISE("\uD83C\uDF05"),
    SUNSET("\uD83C\uDF07"),
    ELECTRICY("⚡"),
    FOG("\uD83C\uDF2B"),
    ZZZ("\uD83D\uDCA4"),
    THERMOMETER("\uD83C\uDF21"),
    RAIN("\uD83C\uDF27"),
    SNOW("\uD83C\uDF28"),
    LIGHTING("\uD83C\uDF29"),
    TORNADO("\uD83C\uDF2A"),
    TOWN("\uD83C\uDFD9"),
    COMPASS("\uD83E\uDDED"),
    WINDFACE("\uD83C\uDF2C"),
    DROPLET("\uD83D\uDCA7"),
    GLASSES("\uD83D\uDC53"),
    THINKING("\uD83E\uDD14"),
    MATE_HEART("♥️"),
    BLUE_HEART("\uD83D\uDC99"),
    ORANGE_HEART("\uD83E\uDDE1"),
    VIOLET_HEART("\uD83D\uDC9C"),

    LARGE_ORANGE_DIAMOND("\uD83D\uDD36"),
    LARGE_BLUE_DIAMOND("\uD83D\uDD37"),
    SMALL_ORANGE_DIAMOND("\uD83D\uDD38"),
    SMALL_BLUE_DIAMOND("\uD83D\uDD39"),
    RED_TRIANGLE_POINTED_UP("\uD83D\uDD3A"),
    RED_TRIANGLE_POINTED_DOWN("\uD83D\uDD3B"),
    DIAMOND_WITH_DOT("\uD83D\uDCA0"),
    GREEN_ARROW("✅"),
    RED_X("❌"),
    UP_ARROW("⬆"),
    DOWN_ARROW("⬇"),

    BOOK_DIARY("\uD83D\uDCD6");

    private String code;
    public String getCode() { return this.code; }

    EEmoji(String code) { this.code = code; }
}
