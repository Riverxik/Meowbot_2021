package ru.riverx.bot.extensions;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.riverx.bot.Meowbot;
import ru.riverx.bot.services.*;
import ru.riverx.utils.DBUsers;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * Created by RiVeRx on 28.04.2021.
 */
public class CommandsExtension extends BaseTextExtension {
    private String name;
    private String username;

    public CommandsExtension(Meowbot bot) {
        super(bot);
    }

    @Override
    public boolean executeIfValid(Update update) {
        boolean res = false;
        try {
            if (update.hasCallbackQuery()) { return false; }
            username = update.getMessage().getChat().getUserName();
            name = update.getMessage().getChat().getFirstName();
            if (username == null) username = name;
            res = super.executeIfValid(update);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    protected boolean execute(Long chatId, String text) {
        if (text.startsWith(Meowbot.COMMAND_SIGN)) {
            String chat = chatId.toString();
            String cmd = text.toLowerCase().substring(1);
            switch (cmd) {
                case "start": {
                    addNewUser(username, name, chatId);
                    meowbot.sendMessage(chat, String.format("Привет, %s!", username));
                    meowbot.sendMessage(chat, String.format("Теперь я Вас знаю как: %s.\r/info - список моих возможностей", name));
                    meowbot.sendSticker(chat, EStickers.CAT_SIT.getFileId());
                } break;
                case "info": {
                    String separator = EEmoji.SMALL_ORANGE_DIAMOND.getCode();
                    String helpInfo =
                            "+ /menu - Главное меню\n" +
                            "+ /info - Мои команды\n" +
                            "+ /space - Картинка дня\n" +
                            "+ /quote - Случайная цитата\n" +
                            "+ /settings - Настройки";
                    helpInfo = helpInfo.replaceAll("[+]+", separator);
                    meowbot.sendMessage(chat, helpInfo);
                    meowbot.sendSticker(chat, EStickers.CAT_SIT.getFileId());
                } break;
                case "space": {
                    meowbot.sendChatAction(chat, "upload_photo");
                    String[] results = SpaceImageService.getImageOfTheDay();
                    meowbot.sendPhoto(chat, results[0], results[1]);
                } break;
                case "quote": {
                    meowbot.sendChatAction(chat, "typing");
                    String result = QuoteService.getQuoteOfTheDay();
                    meowbot.sendMessage(chat, result);
                    meowbot.sendSticker(chat, EStickers.THINKING2.getFileId());
                } break;
                case "settings": {
                    meowbot.sendMessage(chat, SettingsService.showUserSettings(chatId));
                } break;
                case "menu":
                case "stop":
                case "send": {
                    meowbot.sendMessage(chat, "Команда временно не доступна :>");
                    meowbot.sendSticker(chat, EStickers.THINKING.getFileId());
                } break;
                case "flag1_off": {
                    DBUsers.setUserTimeFlag(chatId, true);
                    meowbot.sendMessage(chat, "Хорошо, я больше не буду об этом упоминать :3");
                    meowbot.sendSticker(chat, EStickers.ALL_GOOD.getFileId());
                } break;
                case "status": {
                    RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
                    long uptime = rb.getUptime();
                    String prettyUptime = getPrettyStringForUptime(uptime);
                    meowbot.sendMessage(chat, "Я онлайн уже: " + prettyUptime);
                } break;
                default: {
                    if (chatId != Meowbot.CREATOR_CHAT_ID) {
                        meowbot.sendMessage(chat, String.format("Хмм, я пока не знаю команды '%s'", cmd));
                        meowbot.sendSticker(chat, EStickers.THINKING2.getFileId());
                    }
                } break;
            }
        }
        return false;
    }

    private void addNewUser(String username, String name, long charId) {
        DBUsers.addNewUser(username, name, charId);
    }

    private String getPrettyStringForUptime(long uptime) {
        long seconds = uptime / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        StringBuilder sb = new StringBuilder();
        if (days > 0) sb.append(days).append(" дней, ");
        if (hours > 0) sb.append(hours % 24).append(" часов, ");
        if (minutes > 0) sb.append(minutes % 60).append(" минут, ");
        sb.append(seconds % 60).append(" секунд.");
        return sb.toString();
    }
}
