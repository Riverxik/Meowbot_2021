package ru.riverx.utils;

import ru.riverx.bot.Meowbot;

import java.sql.*;

/**
 * Created by RiVeRx on 28.04.2021.
 */
public class Database {
    private static Connection connection = null;
    private static final String path = "database.db";
    public static String botName;
    public static String botToken;
    public static String filePath;
    public static String weatherApi;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connect();
        }
        return connection;
    }

    public void initiateBot(boolean isTest) {
        String query;
        if (isTest) {
            query = "'telegramTokenTest'";
            botName = "MuraMeowTestBot";
        } else {
            query = "'telegramToken'";
            botName = "MuraMeowBot";
        }
        try {
            botToken = getValue(query);
            filePath = getValue("'filePath'");
            weatherApi = getValue("'weatherApi'");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Meowbot.setUsernameAndToken(botName, botToken);
    }

    public String getValue(String key) throws SQLException {
        String defaultQuery = "SELECT value FROM settings WHERE name = ";
        connection = getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(defaultQuery+key);
        String value = "null";
        if (resultSet.next()) {
            value = resultSet.getString("value");
        }

        resultSet.close();
        statement.close();
        connection.close();
        return value;
    }

    private static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
