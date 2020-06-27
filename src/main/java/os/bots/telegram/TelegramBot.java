package os.bots.telegram;

import net.dv8tion.jda.api.entities.Member;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import os.bots.discord.DiscordBot;
import os.exceptions.NoConnectionWithDiscordException;
import os.utils.Console;
import os.utils.database.DBManager;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class TelegramBot extends TelegramLongPollingBot {
    private static final String TOKEN = "1216336612:AAGcae_ck98BvTPekF9mzg_IE9gRpSQt3q8";
    public static final String USERNAME = "@SquadOS_bot";
    public static final String NICKNAME = "SquadOS";
    public static final String VERSION = "0.2.0_beta";
    public static final String CREATOR = "@s3r3zka";

    public static DBManager dbManager;
    public static final String DB_REGCODES_TABLE = "telegram_regCodes";
    private static final Map<Long, Integer> loginTasks = new HashMap<>();
    private static final Map<Long, RegisterForm> registerTasks = new HashMap<>();

    static {
        // create database connection
        try {
            dbManager = new DBManager("localhost", 3306, "squados");
        } catch (SQLException throwable) {
            Console.errln("Ошибка! " + throwable.getMessage());
        }
    }

    public void onUpdateReceived(Update update) {
        // update data
        Message receivedMessage = update.getMessage();
        String receivedText = receivedMessage.getText();
        long chatID = receivedMessage.getChatId();

        // process data
        try {
            // if message has text
            if (update.hasMessage() && receivedMessage.getText() != null) {
                if (receivedMessage.isCommand()) {

                    // start command
                    if (receivedText.startsWith("/start"))
                        command_start(chatID);

                    // help command
                    if (receivedText.startsWith("/help"))
                        command_help(chatID);

                    // info command
                    if (receivedText.startsWith("/inf"))
                        command_info(chatID);

                    // onlineUsers command
                    if (receivedText.startsWith("/onl"))
                        command_onlineUsers(chatID);

                    // registration command
                    if (receivedText.startsWith("/reg"))
                        command_register(chatID);

                    // login command
                    if (receivedText.startsWith("/log"))
                        command_login(chatID);

                    // exit command
                    if (receivedText.startsWith("/ex"))
                        command_exit(chatID);

                    // discord link command
                    if (receivedText.startsWith("/dsl"))
                        command_discordLink(chatID);

                }

                // get pass to login
                else if (loginTasks.containsKey(chatID)) {
                    authorize(chatID, receivedText);
                }

                // get login to register
                else if (registerTasks.containsKey(chatID)) {
                    register(chatID, receivedText);
                }

                // unknown command
                else {
                    execute(sendMessage(chatID).setText("Я вас не понимаю!\n/help - помощь по командам."));
                }
            }

            // if message has CallbackQuery
            else if (update.hasCallbackQuery()) {
                // todo: make callback query support
            }

            // if message has photo
            else {
                execute(sendMessage(chatID).setText("Сэр, вы ошиблись ботом!"));
            }


        } catch (TelegramApiException | SQLException e) {
            Console.errln("Ошибка при отправке сообщения! " + e.getMessage() + ".");
        }
    }

    // >> BOT UTILS <<
    public String getBotUsername() {
        return USERNAME;
    }

    public String getBotToken() {
        return TOKEN;
    }

    private SendMessage sendMessage(long chatID) {
        return new SendMessage().setChatId(chatID);
    }

    private String getStringDate(Date date) {
        return new SimpleDateFormat("dd.MM.yyyy hh:mm").format(date);
    }

    // >> DEFAULT COMMANDS << //////////////////////////////////////////////////////////////////// >> DEFAULT COMMANDS <<
    // >> DEFAULT COMMANDS << //////////////////////////////////////////////////////////////////// >> DEFAULT COMMANDS <<
    // >> DEFAULT COMMANDS << //////////////////////////////////////////////////////////////////// >> DEFAULT COMMANDS <<

    /**
     * @param chatID
     * @command /start
     * @action send start message
     */
    private void command_start(long chatID) throws TelegramApiException {
        // create welcome message
        String greeting = "Привет! Это бот системы SquadOS.\nВведи /help для просмотра команд.";

        // send
        execute(sendMessage(chatID).setText(greeting));
    }

    /**
     * @param chatID
     * @command /help
     * @action send all commands
     */
    private void command_help(long chatID) throws TelegramApiException {
        StringBuilder commands = new StringBuilder();

        // main commands of bot
        commands.append("Основные команды бота:\n");
        commands.append(" > /start - запустить бота.\n");
        commands.append(" > /help - помощь по всем командам.\n");
        commands.append(" > /info - информация о боте.\n");
        commands.append(" > /online - онлайн на дискорд сервере.\n");
        commands.append(" > /reg - регистрация в системе.\n");
        commands.append(" > /login - вход в аккаунт.\n");
        commands.append(" > /exit - выход из аккаунта.\n");
        commands.append(" > /dsl - приглашение на ds сервер 9ASquad.\n");

        // send help
        execute(sendMessage(chatID).setText(commands.toString()));
    }

    /**
     * @param chatID
     * @command /info
     * @action send info about bot
     */
    private void command_info(long chatID) throws TelegramApiException {
        StringBuilder info = new StringBuilder();

        // info about bot
        info.append("Информация о боте " + USERNAME + ":\n");                                  // title
        info.append(" > Название: " + NICKNAME + ".\n");                                       // bot name
        info.append(" > Версия: " + VERSION + ".\n");                                          // bot version
        info.append(" > Создатель: " + CREATOR + ".\n");                                       // bot creator
        info.append(" > Текущая дата: ").append(getStringDate(new Date())).append(".");        // current date

        // send info
        execute(sendMessage(chatID).setText(info.toString()));
    }

    /**
     * @param chatID
     * @command /online
     * @action send online users on the discord server
     */
    private void command_onlineUsers(long chatID) throws TelegramApiException {
        StringBuilder onlineUsers = new StringBuilder();

        // try send online users or error message
        try {
            // get list of online members
            List<Member> onlineMembers = DiscordBot.getOnlineUsers().stream()
                    .filter(member -> !member.getUser().isBot())
                    .collect(Collectors.toList());

            // if list is empty - send message tells everyone is offline, else send list of online users
            if (onlineMembers.size() == 0) {
                onlineUsers.append("Никто на данный момент не в сети.");
            } else {
                onlineUsers.append("Пользователи онлайн:\n");
                onlineMembers.forEach(member -> onlineUsers.append(" > ").append(member.getEffectiveName()).append("\n"));
            }
        }

        // catch problems with connection when discord module loading or stopped working
        catch (NoConnectionWithDiscordException | java.lang.IndexOutOfBoundsException e) {
            onlineUsers.append("Ошибка! Отсутствует связь с сервером.");
        }

        // send message
        execute(sendMessage(chatID).setText(onlineUsers.toString()));
    }

    /**
     * @param chatID
     * @command /dsl
     * @action send link to main ds sever
     */
    private void command_discordLink(long chatID) throws TelegramApiException {
        // get url and send message
        String link = DiscordBot.getInviteLink();
        execute(sendMessage(chatID).setText("Присоединяйся к нашему сообществу!\n\n" + link + "\n" + link + "\n" + link));
    }

    // >> ACCOUNT SYSTEM << //////////////////////////////////////////////////////////////////// >> ACCOUNT SYSTEM <<
    // >> ACCOUNT SYSTEM << //////////////////////////////////////////////////////////////////// >> ACCOUNT SYSTEM <<
    // >> ACCOUNT SYSTEM << //////////////////////////////////////////////////////////////////// >> ACCOUNT SYSTEM <<

    /**
     * @param chatID
     * @command /reg
     * @action send registration code
     */
    private void command_register(long chatID) throws TelegramApiException, SQLException {
        // check if account isn't created
        Map<Integer, List<Object>> accounts = dbManager.getData("users");
        for (List<Object> values : accounts.values()) {
            if (Long.parseLong(String.valueOf(values.get(2))) == chatID) {
                execute(sendMessage(chatID).setText("Вы уже зарегистрированы!\nВойдите через /login."));
                return;
            }
        }

        // create register task
        registerTasks.put(chatID, new RegisterForm());
        execute(sendMessage(chatID).setText("Введите желаемый никнейм"));
    }

    private void register(long chatID, String input) throws TelegramApiException {
        // get form
        RegisterForm form = registerTasks.get(chatID);

        // process
        if (form.username == null) {
            form.setUsername(input.trim());
            execute(sendMessage(chatID).setText("Введите пароль"));
        } else if (form.password == null) {
            form.setPassword(input.trim());
            dbManager.insertData("users", new String[]{"username", "password", "telegram_chatID"}, new String[]{form.username, form.password, String.valueOf(chatID)});

            execute(sendMessage(chatID).setText("Аккаунт создан! Авторизируйтесь через /login."));
            registerTasks.remove(chatID);
        }
    }

    private static class RegisterForm {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    /**
     * @param chatID
     * @command /login
     * @action login to account
     */
    private void command_login(long chatID) throws TelegramApiException {
        // get active sessions
        try {
            Map<Integer, List<Object>> activeSessions = dbManager.getData("sessions");

            // check if session inactive
            for (Map.Entry<Integer, List<Object>> session : activeSessions.entrySet()) {
                // DB `sessions` config: Integer - sessionID, list.get(0) - userID, list.get(1) - telegram_chatID.
                String telegram_chatID = String.valueOf(session.getValue().get(1));

                if (Long.parseLong(telegram_chatID) == chatID) {
                    execute(sendMessage(chatID).setText("Вы уже авторизированы!"));
                    return;
                }
            }

            // find account to send name and add to task manager
            String username = "[-]";
            Integer userID = -1;

            Map<Integer, List<Object>> accounts = dbManager.getData("users");
            for (Map.Entry<Integer, List<Object>> account : accounts.entrySet()) {
                // DB `users` config: Integer - userID, 0 - username, 1 - password, 2 - telegram_chatID, 3 - discord_userID.

                try {
                    long temp_telegram_chatID = Long.parseLong(String.valueOf(account.getValue().get(2)));
                    if (temp_telegram_chatID == chatID) {
                        username = String.valueOf(account.getValue().get(0));
                        userID = account.getKey();
                        break;
                    }
                } catch (java.lang.NumberFormatException ignored) {
                }
            }

            // if userID <= 0 - account didn't founded => user is not authorize
            if (userID <= 0) {
                execute(sendMessage(chatID).setText("Вы ещё не зарегистрировались!\nДля регистрации введите /reg."));
                return;
            }

            // put authorize task to task manager
            loginTasks.put(chatID, userID);

            // send message with password query
            execute(sendMessage(chatID).setText("Введите пароль к аккануту " + username + "."));

        } catch (SQLException throwable) {
            execute(sendMessage(chatID).setText("Ошибка! Нету связи с базой данных."));
        }
    }

    private void authorize(long chatID, String input) throws TelegramApiException, SQLException {
        // get userID and remove task
        Integer userID = loginTasks.get(chatID);
        loginTasks.remove(chatID);

        // check pass is correct
        Map<Integer, List<Object>> accounts = dbManager.getData("users");
        // DB `users` config: Integer - userID, 0 - username, 1 - password, 2 - telegram_chatID, 3 - discord_userID.
        String temp_pass = String.valueOf(accounts.get(userID).get(1));
        if (!temp_pass.equals(input.trim())) {
            execute(sendMessage(chatID).setText("Вы ввели неверный пароль!\n/login для повторной попытки."));

            return;
        }

        dbManager.insertData(
                "sessions",
                new String[]{"userID", "telegram_chatID", "time"},
                new String[]{String.valueOf(userID), String.valueOf(chatID), String.valueOf((Calendar.getInstance().getTimeInMillis() / 1000) + 3600)});

        execute(sendMessage(chatID).setText("Вы успешно авторизировались!"));
    }


    /**
     * @param chatID
     * @command /exit
     * @action exit from account
     */
    private void command_exit(long chatID) throws TelegramApiException {
        // delete session from database
        dbManager.deleteData("sessions", new String[]{"telegram_chatID=" + chatID});
        execute(sendMessage(chatID).setText("Вы вышли из аккаунта!"));
    }
}
