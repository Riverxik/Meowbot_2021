package ru.riverx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.riverx.bot.Meowbot;
import ru.riverx.utils.Database;

/**
 * Created by RiVeRx on 20.01.2021.
 */
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);
    private static boolean isTest = false;

    private final static String PROXY_HOST = "159.8.114.37";
    private final static int PROXY_PORT = 80;

    public static void main(String[] args) {
        log.info("Application has been started!");

        if (args.length > 0 && args[0].equalsIgnoreCase("true")) {
            isTest = true;
        }

        log.info("Configuring the database...");
        Database db = new Database();
        db.initiateBot(isTest);
        telegramInit();
    }

    private static void telegramInit() {
        log.info("Trying to register bot.");
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            registerAllBots(telegramBotsApi, false);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            log.warn("Can't initiate bot instance without proxy.");
            telegramInitProxy();
        }
        log.info("Telegram instance started!");
    }

    private static void telegramInitProxy() {
        log.info("Trying to register bot. Proxy ON");
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            registerAllBots(telegramBotsApi, true);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            log.error("Can't initiate bot instance.");
        }
        log.info("Telegram instance started!");
    }

    private static void registerAllBots(TelegramBotsApi telegramBotsApi, boolean isProxy) throws TelegramApiException {
        DefaultBotOptions botOptions = new DefaultBotOptions();
        if (isProxy) {
            // Setting up proxy.
            botOptions.setProxyHost(Application.PROXY_HOST);
            botOptions.setProxyPort(Application.PROXY_PORT);
            botOptions.setProxyType(DefaultBotOptions.ProxyType.HTTP);
        }
        telegramBotsApi.registerBot(new Meowbot(botOptions));
    }
}
