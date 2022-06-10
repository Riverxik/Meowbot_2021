package ru.riverx.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by RiVeRx on 28.04.2021.
 */
public class DBUsers {
    public static void addNewUser(String username, String name, long chatId) {
        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();

            String insert = "insert into users (username, name, chat_id)\n" +
                    "values ('"+ username+"', '"+ name +"', '"+ chatId +"');";
            statement.executeUpdate(insert);
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getNameByChatId(long chatId) {
        String username = null;
        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();

            String select = "select name from users where chat_id = '"+ chatId +"'";

            ResultSet resultSet = statement.executeQuery(select);

            while (resultSet.next()) {
                username = resultSet.getString("name");
            }

            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return username;
    }

    public static String getCityByChatId(long chatId) {
        String city = null;
        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();

            String select = "select city from users where chat_id = '"+ chatId +"'";

            ResultSet resultSet = statement.executeQuery(select);

            while (resultSet.next()) {
                city = resultSet.getString("city");
            }

            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return city;
    }

    public static void addUserCity(long chatId, String city) {
        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();

            String insert = "update users " +
                    "set city = '"+city+"' " +
                    "where chat_id = "+chatId+";";
            statement.executeUpdate(insert);
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addUserTime(long chatId, String time) {
        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();

            int iTime = Integer.parseInt(time);
            if (iTime < -23 || iTime > 23) return;

            String insert = "update users " +
                    "set time = '"+iTime+"' " +
                    "where chat_id = "+chatId+";";
            statement.executeUpdate(insert);
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getUserTime(long chatId) {
        int time = 0;
        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();

            String select = "select time from users where chat_id = '"+ chatId +"'";

            ResultSet resultSet = statement.executeQuery(select);

            while (resultSet.next()) {
                time = resultSet.getInt("time");
            }

            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static boolean getUserTimeFlag(long chatId) {
        boolean res = false;
        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();

            String select = "select timeFlag from users where chat_id = '" + chatId + "'";

            ResultSet resultSet = statement.executeQuery(select);

            if (resultSet.next()) {
                res = resultSet.getBoolean("timeFlag");
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static boolean setUserTimeFlag(long chatId, boolean value) {
        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();

            String update = "update users " +
                    "set timeFlag = '"+value+"' " +
                    "where chat_id = "+chatId+";";
            statement.executeUpdate(update);
            statement.close();
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getUserState(long chatId) {
        String state = "NULL";
        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();

            String select = "select state from users where chat_id = '" + chatId +"'";
            ResultSet resultSet = statement.executeQuery(select);
            if (resultSet.next()) {
                state = resultSet.getString("state");
            }
            resultSet.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return state;
    }

    public static boolean updateUserState(long chatId, String newState) {
        try {
            Connection connection = Database.getConnection();
            Statement statement = connection.createStatement();

            String update = "update users " +
                    "set state = '"+newState+"' " +
                    "where chat_id = "+chatId+";";
            statement.executeUpdate(update);
            statement.close();
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
