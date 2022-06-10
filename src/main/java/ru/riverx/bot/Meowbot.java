package ru.riverx.bot;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.*;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.riverx.bot.extensions.*;
import ru.riverx.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by RiVeRx on 20.01.2021.
 */
public class Meowbot extends TelegramLongPollingBot {

    public static final long CREATOR_CHAT_ID = 356425250;
    public static final String COMMAND_SIGN = "/";
    private static final Logger log = LoggerFactory.getLogger(Meowbot.class);

    private static String botUsername;
    private static String botToken;
    private final List<BotExtension> extensionList;

    public Meowbot(DefaultBotOptions botOptions) {
        super(botOptions);
        this.extensionList = new ArrayList<>();
        this.extensionList.add(new LogExtension());
        this.extensionList.add(new CommandsExtension(this));
        this.extensionList.add(new KoreanWordExtension(this));
        this.extensionList.add(new ChatTextExtension(this));
        this.extensionList.add(new WeatherExtension(this));
        this.extensionList.add(new HappyGraphExtension(this));
        this.extensionList.add(new TextSettingsExtension(this));
        //this.extensionList.add(new LayshaExtension(this)); // Not ready yet.
    }

    public static void setUsernameAndToken(String name, String token) {
        botUsername = name;
        botToken = token;
    }

    public String getBotUsername() {
        return botUsername;
    }

    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasInlineQuery()) {
            processInlineQuery(update);
        } else {
            for (BotExtension ext : extensionList) {
                if (ext.executeIfValid(update)) {
                    break; // If we execute one of the ext we don't need to continue.
                }
            }
        }
    }

    public void sendMessage(String chatId, String text) {
        sendMessage(chatId, text, null);
    }

    public void sendMessage(Long chatId, String text) {
        sendMessage(String.valueOf(chatId), text, null);
    }

    public void sendMessage(String chatId, String text, InlineKeyboardMarkup keyboardMarkup) {
        SendMessage message = new SendMessage(chatId, text);
        if (keyboardMarkup != null) { message.setReplyMarkup(keyboardMarkup); }
        try {
            execute(message);
            log.info(String.format("[Meow]-->[%s][%s]", message.getChatId(), message.getText()));
        } catch (TelegramApiException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    public void sendMediaGroup(String chatId, String text, List<InputMedia> medias, InlineKeyboardMarkup keyboardMarkup) {
        SendMediaGroup sendMediaGroup = new SendMediaGroup();
        sendMediaGroup.setChatId(chatId);
        sendMediaGroup.setMedias(medias);
        try {
            List<Message> list = execute(sendMediaGroup);
            for (Message m : list) {
                editMessageCaption(chatId, m.getMessageId(), null, null);
            }
            editMessageCaption(chatId, list.get(0).getMessageId(), text, keyboardMarkup);
            log.info(String.format("[Meow]-->[%s][%s]", chatId, "medias"));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void editReplyKeyboard(String chatId, Integer messageId, InlineKeyboardMarkup keyboardMarkup) {
        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup();
        editMessageReplyMarkup.setChatId(chatId);
        editMessageReplyMarkup.setMessageId(messageId);
        editMessageReplyMarkup.setReplyMarkup(keyboardMarkup);
        try {
            execute(editMessageReplyMarkup);
            log.info(String.format("[Meow]-->[%s][%s]", chatId, "replyKeyboard"));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void editMessageText(String chatId, Integer messageId, String newText) {
        editMessageText(chatId, messageId, newText, null);
    }

    public void editMessageText(String chatId, Integer messageId, String newText, InlineKeyboardMarkup keyboardMarkup) {
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(chatId);
        editMessage.setMessageId(messageId);
        if (newText != null) {
            editMessage.setText(newText);
        }
        if (keyboardMarkup != null) { editMessage.setReplyMarkup(keyboardMarkup); }
        try {
            execute(editMessage);
            log.info(String.format("[Meow]-~>[%s]", newText));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void editMessageCaption(String chatId, Integer messageId, String newCaption, InlineKeyboardMarkup keyboardMarkup) {
        EditMessageCaption editMessageCaption = new EditMessageCaption();
        editMessageCaption.setChatId(chatId);
        editMessageCaption.setMessageId(messageId);
        editMessageCaption.setCaption(newCaption);
        editMessageCaption.setParseMode("HTML");
        editMessageCaption.setReplyMarkup(keyboardMarkup);
        try {
            execute(editMessageCaption);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendSticker(String chatId, String sticker) {
        SendSticker sendSticker = new SendSticker(chatId, new InputFile(sticker));
        try {
            execute(sendSticker);
            log.info(String.format("[Meow]-->[%s][sticker]", chatId));
        } catch (TelegramApiException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    public String getFileUrl(String fileId) {
        String url = null;
        GetFile getFile = new GetFile(fileId);
        try {
            File file = execute(getFile);
            url = file.getFileUrl(Meowbot.botToken);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return url;
    }

    public java.io.File getFile(String fileUrl) {
        try {
            java.io.File tmpTile = java.io.File.createTempFile("photo", ".jpg");
            InputStream is = new URL(fileUrl).openStream();
            FileUtils.copyInputStreamToFile(is, tmpTile);
            return tmpTile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendPhoto(String chatId, String caption, String url) {
        sendPhoto(chatId, caption, url, null);
    }

    public void sendPhoto(String chatId, String caption, String url, InlineKeyboardMarkup keyboardMarkup) {
        SendPhoto sendPhoto = new SendPhoto(chatId, new InputFile(url));
        sendPhoto.setCaption(caption);
        sendPhoto.setParseMode("HTML");
        if (keyboardMarkup != null) {
            sendPhoto.setReplyMarkup(keyboardMarkup);
        }
        try {
            Message m = execute(sendPhoto);
            log.info(String.format("[Meow]-->[%s][photo][%s]", chatId, m.getMessageId()));
        } catch (TelegramApiException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    public void sendVideo(String chatId, String caption, String url) {
        sendVideo(chatId, caption, url, null);
    }

    public void sendVideo(String chatId, String caption, String url, InlineKeyboardMarkup keyboardMarkup) {
        SendVideo sendVideo = new SendVideo();
        sendVideo.setChatId(chatId);
        sendVideo.setCaption(caption);
        sendVideo.setVideo(new InputFile(url));
        sendVideo.setParseMode("HTML");
        sendVideo.setReplyMarkup(keyboardMarkup);
        try {
            execute(sendVideo);
            log.info(String.format("[Meow]-->[%s][video]", chatId));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendAnimation(String chatId, String caption, String url) {
        sendAnimation(chatId, caption, url, null);
    }

    public void sendAnimation(String chatId, String caption, String url, InlineKeyboardMarkup keyboardMarkup) {
        SendAnimation sendAnimation = new SendAnimation();
        sendAnimation.setChatId(chatId);
        sendAnimation.setCaption(caption);
        sendAnimation.setAnimation(new InputFile(url));
        sendAnimation.setParseMode("HTML");
        sendAnimation.setReplyMarkup(keyboardMarkup);
        try {
            execute(sendAnimation);
            log.info(String.format("[Meow]-->[%s][gif]", chatId));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendChatAction(String chatId, String action) {
        try {
            execute(new SendChatAction(chatId, action));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void deleteMessage(String chatId, int messageId) {
        DeleteMessage msg = new DeleteMessage(chatId, messageId);
        try {
            execute(msg);
            log.info(String.format("[Meow]-/>[%s][%s]", chatId, messageId));
        } catch (TelegramApiException e) {
            e.printStackTrace();
            log.error("Error deleting message: " + e.getMessage());
        }
    }

    private void processInlineQuery(Update update) {
        InlineQuery iq = update.getInlineQuery();
        final String query = iq.getQuery();
        AnswerInlineQuery answerInlineQuery = new AnswerInlineQuery();
        answerInlineQuery.setInlineQueryId(iq.getId());
        List<InlineQueryResult> resultList = new ArrayList<>();
        InlineQueryResultArticle resultArticle = new InlineQueryResultArticle();

        resultArticle.setTitle("Матозаменитель");
        resultArticle.setDescription("Зашифрует всё что написано капсом ;)");

        InputTextMessageContent text = new InputTextMessageContent();
        text.setMessageText(Utils.encode(query));

        resultArticle.setInputMessageContent(text);
        resultArticle.setHideUrl(true);
        resultArticle.setId(iq.getId());
        resultList.add(resultArticle);
        answerInlineQuery.setResults(resultList);
        try {
            sendApiMethod(answerInlineQuery);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void pingPong(Update update) {
        String text = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();

        try {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(String.valueOf(chatId));
            sendMessage.setText(text);

            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
