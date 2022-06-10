package ru.riverx.bot.extensions;

import ru.riverx.bot.Meowbot;
import ru.riverx.bot.services.EStickers;
import ru.riverx.utils.DBUsers;

/**
 * Created by RiVeRx on 29.04.2021.
 */
public class TextSettingsExtension extends BaseTextExtension{
    public TextSettingsExtension(Meowbot bot) {
        super(bot);
    }

    @Override
    protected boolean execute(Long chatId, String text) {
        String txt = text.toLowerCase();
        if (txt.startsWith("мой город") && txt.length() > 10) {
            String chat = chatId.toString();
            DBUsers.addUserCity(chatId, text.substring(10));
            meowbot.sendMessage(chat, "Я запомнил твой город! Теперь можешь узнавать погоду в своём городе просто " +
                    "написав 'погода'");
            meowbot.sendSticker(chat, EStickers.LIKE_IT.getFileId());
            return true;
        }
        if (txt.startsWith("мой пояс") && txt.length() > 9) {
            String chat = chatId.toString();
            DBUsers.addUserTime(chatId, txt.substring(9));
            meowbot.sendMessage(chat, "Я запомнил твой часовой пояс! Теперь я буду отображать твоё локальное время");
            meowbot.sendSticker(chat, EStickers.LIKE_IT.getFileId());
            return true;
        }
        return false;
    }
}
