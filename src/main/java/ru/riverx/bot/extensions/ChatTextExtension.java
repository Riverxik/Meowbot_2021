package ru.riverx.bot.extensions;

import ru.riverx.bot.Meowbot;
import ru.riverx.bot.services.EStickers;
import ru.riverx.bot.services.GreetingService;
import ru.riverx.utils.DBUsers;

/**
 * Created by RiVeRx on 28.04.2021.
 */
public class ChatTextExtension extends BaseTextExtension {

    public ChatTextExtension(Meowbot bot) {
        super(bot);
    }

    @Override
    protected boolean execute(Long chatId, String text) {
        if (GreetingService.isThatGreeting(text.toLowerCase())) {
            executeGreeting(chatId);
            return true;
        }
        if (GreetingService.isThatAppeal(text.toLowerCase())) {
            executeAppeal(chatId);
            return true;
        }
        return false;
    }

    private void executeGreeting(Long chatId) {
        String msg = String.format("%s, %s!", GreetingService.getRandomGreeting(), DBUsers.getNameByChatId(chatId));
        String chat = chatId.toString();
        meowbot.sendMessage(chat, msg);
        meowbot.sendSticker(chat, EStickers.HI.getFile_id());
    }

    private void executeAppeal(Long chatId) {
        String msg = String.format("%s", GreetingService.getRandomResponse());
        String chat = chatId.toString();
        meowbot.sendMessage(chat, msg);
        meowbot.sendSticker(chat, EStickers.READY_TO_WORK.getFile_id());
    }
}
