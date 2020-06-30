package os.bots.telegram;

import net.dv8tion.jda.api.entities.Member;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
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
    public static final String CREATOR = "@serezkaaaa";

    public static DBManager dbManager;

    static {
        // create database connection
        try {
            dbManager = new DBManager("localhost", 3306, "squados");
        } catch (SQLException throwable) {
            Console.errln("Ошибка! " + throwable.getMessage());
        }
    }

    public void onUpdateReceived(Update update) {
        System.out.println(update.toString());

        // process data
        try {
            // if message has text
            if (update.hasMessage() && update.getMessage().hasText()) {
                // get received data
                Message receivedMessage = update.getMessage();
                String receivedText = receivedMessage.getText();
                long chatID = receivedMessage.getChatId();
                int msgID = receivedMessage.getMessageId();

                if (receivedMessage.isCommand()) {
                    // check if user use commands in tasks
                    if (tasksManager.contains(chatID)) {
                        tasksManager.removeTask(chatID);
                        execute(sendMessage(chatID).setText("Вы прервали операцию командой!"));
                    }

                    // start command
                    if (receivedText.startsWith("/start"))
                        command_start(chatID);

                        // help command
                    else if (receivedText.startsWith("/help"))
                        command_help(chatID);

                        // info command
                    else if (receivedText.startsWith("/inf"))
                        command_info(chatID);

                        // onlineUsers command
                    else if (receivedText.startsWith("/onl"))
                        command_onlineUsers(chatID);

                        // account
                    else if (receivedText.startsWith("/acc"))
                        command_acc(chatID);

                        // discord link command
                    else if (receivedText.startsWith("/dsl"))
                        command_discordLink(chatID);

                        // unknown command
                    else
                        execute(sendMessage(chatID).setText("Такой команды не существует!\n/help - все доступные команды."));
                }

                // another tasks
                else if (tasksManager.contains(chatID)) {
                    tasksManager.getTask(chatID).doIt(chatID, msgID, receivedText);
                }

                // if user stupid
                else {
                    execute(sendMessage(chatID).setText("Я вас не понимаю!"));
                }
            }

            // if message has CallbackQuery
            if (update.hasCallbackQuery()) {
                // get received data
                CallbackQuery receivedQuery = update.getCallbackQuery();

                int msgID = receivedQuery.getMessage().getMessageId();
                long chatID = receivedQuery.getMessage().getChatId();
                String data = receivedQuery.getData();


                // process

                // delete message inlcommand
                if (data.equals("delete_msg")) {
                    tasksManager.removeTask(chatID);
                    deleteMessage(chatID, msgID);
                }

                // get register inlcommand
                if (data.equals("reg"))
                    inlcommand_getRegister(chatID, msgID);

                // get login inlcommand
                if (data.equals("login"))
                    inlcommand_getLogin(chatID, msgID);

                // exit from account inlcommand
                if (data.equals("exit"))
                    inlcommand_exitFromAccount(chatID, msgID);

                // get main control main page inlcommand
                if (data.equals("control_main"))
                    inlcommand_getMainPage(chatID, msgID);

                // get settings control page inlcommand
                if (data.equals("control_settings"))
                    inlcommand_getSettings(chatID, msgID);

            }


        } catch (TelegramApiException e) {
            e.printStackTrace();
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

    private static final TasksManager tasksManager = new TasksManager();

    // structure of the tasks
    // queue manager
    private static class TasksManager {
        private static final Map<Long, Task> tasks = new HashMap<>();

        // add task to pull
        public void addTask(long chatID, Task task) {
            tasks.put(chatID, task);
        }

        // remove task by key
        public void removeTask(long chatID) {
            tasks.remove(chatID);
        }

        // check key in list
        public boolean contains(long chatID) {
            return tasks.containsKey(chatID);
        }

        // get task
        public Task getTask(long chatID) {
            return tasks.get(chatID);
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            for (Map.Entry<Long, Task> entry : tasks.entrySet())
                result.append(entry.getKey()).append(" ").append(entry.getValue().taskType);

            return result.toString();
        }
    }

    // task
    private abstract static class Task {
        private final TaskType taskType;

        public Task(TaskType taskType) {
            this.taskType = taskType;
        }

        public TaskType getType() {
            return taskType;
        }

        public abstract void doIt(long chatID, int msgID, String input) throws TelegramApiException;
    }

    // type of task
    private enum TaskType {
        LOGIN, REGISTER
    }
    // ---------------------

    // tasks
    // login task
    private class LoginTask extends Task {
        public String username;
        public String password;
        public long chatID;

        public LoginTask(long chatID) {
            super(TaskType.LOGIN);
            this.chatID = chatID;
        }

        @Override
        public void doIt(long chatID, int msgID, String input) throws TelegramApiException {
            // delete message with pass
            deleteMessage(chatID, msgID - 1);
            deleteMessage(chatID, msgID);

            // check if pass is right
            User user = db_getUser(chatID);
            if (user != null) {
                if (user.getPassword().equals(input.trim())) {
                    if(dbManager.insertData("sessions", new String[]{"telegram_chatID"}, new String[]{String.valueOf(chatID)})) {
                        command_acc(chatID);
                    } else {
                       execute(sendMessage(chatID).setText("Произошла ошибка во время входа!\nПопробуйте войти позже."));
                    }
                } else {
                    InlineKeyboardMarkup againLogin = getKeyboardMarkup();
                    addButtons_acc_login(againLogin);
                    execute(sendMessage(chatID).setText("Вы ввели неверный логин/пароль!").setReplyMarkup(againLogin));
                }
            } else {
                execute(sendMessage(chatID).setText("Произошла ошибка во время входа!\nПопробуйте войти позже."));
            }
        }
    }

    // register task
    private class RegisterTask extends Task {
        public String username;
        public String password;

        public RegisterTask() {
            super(TaskType.REGISTER);
        }

        @Override
        public void doIt(long chatID, int msgID, String input) throws TelegramApiException {
            deleteMessage(chatID, msgID - 1);
            deleteMessage(chatID, msgID);

            if (username == null) {
                username = input.trim();
                execute(sendMessage(chatID).setText("Придумайте пароль: "));
            } else if (password == null) {
                password = input.trim();

                if (db_register(new User(this.username, this.password, chatID)))
                    command_acc(chatID);
                else
                    execute(sendMessage(chatID).setText("При регистрации произошла ошибка!\nПопробуйте позже."));
            }
        }
    }

    // >> DATABASE UTILS <<

    // user
    private static class User {
        private Integer ID;
        private final String username;
        private final String password;
        private final long telegram_chatID;
        private String discord_userID;

        public User(String username, String password, long telegram_chatID) {
            this.username = username;
            this.password = password;
            this.telegram_chatID = telegram_chatID;
        }

        public void setDiscordUserID(String discord_userID) {
            this.discord_userID = discord_userID;
        }

        public Integer getID() {
            return ID;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public long getTelegram_chatID() {
            return telegram_chatID;
        }

        public String getDiscord_userID() {
            return discord_userID;
        }

        @Override
        public String toString() {
            return "ID: " + ID + "\n" +
                    "Username: " + username + "\n" +
                    "Password: " + password + "\n" +
                    "tg chat id: " + telegram_chatID + "\n" +
                    "ds user id: " + discord_userID + "\n";
        }
    }

    private boolean db_register(User user) {
        return dbManager.insertData(
                "users",
                new String[]{"username", "password", "telegram_chatID"},
                new String[]{user.getUsername(), user.getPassword(), String.valueOf(user.getTelegram_chatID())});
    }

    private List<Long> db_getSessions() {
        List<Long> sessions = new ArrayList<>();
        try {
            dbManager.getData("sessions").forEach((key, value) -> sessions.add(Long.valueOf((String) value.get(1))));
        } catch (SQLException throwables) {
            Console.errln("Ошибка! " + throwables.getMessage());
            return new ArrayList<>();
        }
        return sessions;
    }

    private User db_getUser(long telegram_chatID) {
        try {
            // get users from database
            Map<Integer, List<Object>> users = dbManager.getData("users");

            // find user by chatID
            for (Map.Entry<Integer, List<Object>> user : users.entrySet())
                if (Long.parseLong(String.valueOf(user.getValue().get(2))) == telegram_chatID)
                    return new User((String) user.getValue().get(0), (String) user.getValue().get(1), telegram_chatID);
        } catch (SQLException throwables) {
            Console.errln("Ошибка! " + throwables.getMessage());
        }

        // if nothing founded
        return null;
    }

    // account command

    /**
     * @param chatID
     * @throws TelegramApiException
     * @command /acc
     * @action get account control
     */
    private void command_acc(long chatID) throws TelegramApiException {
        // get user and sessions
        List<Long> sessions = db_getSessions();
        User account = db_getUser(chatID);

        // generate inline keyboard
        InlineKeyboardMarkup keyboard = getKeyboardMarkup();

        // send msg and add keyboard
        // if account != null => account is created
        if (account != null) {
            // check if acc tg_chatID in sessions list
            if (sessions.contains(account.getTelegram_chatID())) {
                addButtons_acc_control_main(keyboard);
                execute(sendMessage(chatID).setText("Аккаунт").setReplyMarkup(keyboard));
            }
            // need login to account
            else {
                addButtons_acc_login(keyboard);
                execute(sendMessage(chatID).setText("Здравствуйте, " + account.getUsername() + "! Войдите в акканут.").setReplyMarkup(keyboard));
            }
        }
        // account does't created
        else {
            addButtons_acc_register(keyboard);
            execute(sendMessage(chatID).setText("Вы ещё не зарегистрированы!").setReplyMarkup(keyboard));
        }
    }

    // inline keyboard utils
    // get harvesting for future keyboard
    private InlineKeyboardMarkup getKeyboardMarkup() {
        return new InlineKeyboardMarkup();
    }

    // get buttons for registration
    private void addButtons_acc_register(InlineKeyboardMarkup markup) {
        // create buttons
        InlineKeyboardButton button_register = new InlineKeyboardButton()
                .setText("Регистрация")
                .setCallbackData("reg");

        InlineKeyboardButton button_cancel = new InlineKeyboardButton()
                .setText("Закрыть")
                .setCallbackData("delete_msg");

        // fill rows
        List<InlineKeyboardButton> buttons_row_1 = new ArrayList<>();
        buttons_row_1.add(button_register);
        buttons_row_1.add(button_cancel);

        // add to keyboard
        List<List<InlineKeyboardButton>> all_buttons = new ArrayList<>();
        all_buttons.add(buttons_row_1);

        markup.setKeyboard(all_buttons);
    }

    // get buttons for login
    private void addButtons_acc_login(InlineKeyboardMarkup markup) {
        // create buttons
        InlineKeyboardButton button_login = new InlineKeyboardButton()
                .setText("Войти")
                .setCallbackData("login");

        InlineKeyboardButton button_cancel = new InlineKeyboardButton()
                .setText("Закрыть")
                .setCallbackData("delete_msg");

        // fill rows
        List<InlineKeyboardButton> buttons_row_1 = new ArrayList<>();
        buttons_row_1.add(button_login);
        buttons_row_1.add(button_cancel);

        // add to keyboard
        List<List<InlineKeyboardButton>> all_buttons = new ArrayList<>();
        all_buttons.add(buttons_row_1);

        markup.setKeyboard(all_buttons);
    }

    // get buttons for acc control
    private void addButtons_acc_control_main(InlineKeyboardMarkup markup) {
        // create buttons
        InlineKeyboardButton button_notifications = new InlineKeyboardButton()
                .setText("Уведомления")
                .setCallbackData("notifications");

        InlineKeyboardButton button_settings = new InlineKeyboardButton()
                .setText("Настройки")
                .setCallbackData("control_settings");

        InlineKeyboardButton button_exit = new InlineKeyboardButton()
                .setText("Выйти")
                .setCallbackData("exit");

        InlineKeyboardButton button_close = new InlineKeyboardButton()
                .setText("Закрыть")
                .setCallbackData("delete_msg");

        // fill rows
        List<InlineKeyboardButton> buttons_row_1 = new ArrayList<>();
        List<InlineKeyboardButton> buttons_row_2 = new ArrayList<>();

        buttons_row_1.add(button_notifications);
        buttons_row_1.add(button_settings);

        buttons_row_2.add(button_exit);
        buttons_row_2.add(button_close);

        // add to keyboard
        List<List<InlineKeyboardButton>> all_buttons = new ArrayList<>();
        all_buttons.add(buttons_row_1);
        all_buttons.add(buttons_row_2);

        markup.setKeyboard(all_buttons);
    }

    private void addButtons_acc_control_settings(InlineKeyboardMarkup markup) {
        // create buttons
        InlineKeyboardButton button_notifications = new InlineKeyboardButton()
                .setText("Изменить пароль")
                .setCallbackData("notifications");

        InlineKeyboardButton button_settings = new InlineKeyboardButton()
                .setText("Изменить логин")
                .setCallbackData("settings");

        InlineKeyboardButton button_exit = new InlineKeyboardButton()
                .setText("Удалить аккаунт")
                .setCallbackData("exit");

        InlineKeyboardButton button_close = new InlineKeyboardButton()
                .setText("<<<")
                .setCallbackData("control_main");

        // fill rows
        List<InlineKeyboardButton> buttons_row_1 = new ArrayList<>();
        List<InlineKeyboardButton> buttons_row_2 = new ArrayList<>();

        buttons_row_1.add(button_notifications);
        buttons_row_1.add(button_settings);

        buttons_row_2.add(button_exit);
        buttons_row_2.add(button_close);

        // add to keyboard
        List<List<InlineKeyboardButton>> all_buttons = new ArrayList<>();
        all_buttons.add(buttons_row_1);
        all_buttons.add(buttons_row_2);

        markup.setKeyboard(all_buttons);
    }

    // INLINE COMMANDS

    /**
     * @param chatID - where message
     * @param msgID  - witch message
     * @throws TelegramApiException
     * @action delete selected message
     */
    private void deleteMessage(long chatID, int msgID) throws TelegramApiException {
        // generate delete message query
        DeleteMessage deleteMessage_bot = new DeleteMessage()
                .setChatId(chatID)
                .setMessageId(msgID);

        // execute it
        execute(deleteMessage_bot);
    }


    /**
     * @param chatID - where edit
     * @param msgID  - what edit
     * @throws TelegramApiException
     * @action get reg form
     */
    private void inlcommand_getRegister(long chatID, int msgID) throws TelegramApiException {
        // delete message
        deleteMessage(chatID, msgID);

        // create registration task
        RegisterTask register = new RegisterTask();
        tasksManager.addTask(chatID, register);

        // send start register message
        execute(sendMessage(chatID).setText("Придумайте логин:"));
    }


    /**
     * @throws TelegramApiException
     * @action start login procedure
     */
    private void inlcommand_getLogin(long chatID, int msgID) throws TelegramApiException {
        // delete message
        deleteMessage(chatID, msgID);

        // create registration task
        LoginTask login = new LoginTask(chatID);
        tasksManager.addTask(chatID, login);

        // send start register message
        execute(sendMessage(chatID).setText("Введите пароль:"));
    }

    /**
     * @throws TelegramApiException
     * @action get notification settings
     */
    private void inlcommand_getNotificationCenter(long chatID, int msgID) throws TelegramApiException {
        // check if user doesn't login
        if (!db_getSessions().contains(chatID)) {
            InlineKeyboardMarkup loginKeyboard = getKeyboardMarkup();
            addButtons_acc_login(loginKeyboard);
            execute(new EditMessageText().setChatId(chatID).setMessageId(msgID).setText("Для продолжения войдите.").setReplyMarkup(loginKeyboard));
            return;
        }

        // delete message
        deleteMessage(chatID, msgID);

        // create registration task
        LoginTask login = new LoginTask(chatID);
        tasksManager.addTask(chatID, login);

        // send start register message
        execute(sendMessage(chatID).setText("Введите пароль:"));
    }

    /**
     * @throws TelegramApiException
     * @action get settings page
     */
    private void inlcommand_getSettings(long chatID, int msgID) throws TelegramApiException {
        // check if user doesn't login
        if (!db_getSessions().contains(chatID)) {
            InlineKeyboardMarkup loginKeyboard = getKeyboardMarkup();
            addButtons_acc_login(loginKeyboard);
            execute(new EditMessageText().setChatId(chatID).setMessageId(msgID).setText("Для продолжения войдите.").setReplyMarkup(loginKeyboard));
            return;
        }

        // change message
        InlineKeyboardMarkup newKeyboard = getKeyboardMarkup();
        addButtons_acc_control_settings(newKeyboard);

        EditMessageText loginMessage = new EditMessageText()
                .setChatId(chatID)
                .setMessageId(msgID)
                .setText("Настройки")
                .setReplyMarkup(newKeyboard);

        execute(loginMessage);
    }

    /**
     * @throws TelegramApiException
     * @action get main page of control
     */
    private void inlcommand_getMainPage(long chatID, int msgID) throws TelegramApiException {
        // check if user doesn't login
        if (!db_getSessions().contains(chatID)) {
            InlineKeyboardMarkup loginKeyboard = getKeyboardMarkup();
            addButtons_acc_login(loginKeyboard);
            execute(new EditMessageText().setChatId(chatID).setMessageId(msgID).setText("Для продолжения войдите.").setReplyMarkup(loginKeyboard));
            return;
        }

        // change message
        InlineKeyboardMarkup newKeyboard = getKeyboardMarkup();
        addButtons_acc_control_main(newKeyboard);

        EditMessageText loginMessage = new EditMessageText()
                .setChatId(chatID)
                .setMessageId(msgID)
                .setText("Аккаунт")
                .setReplyMarkup(newKeyboard);

        execute(loginMessage);
    }

    /**
     * @throws TelegramApiException
     * @action exit from account
     */
    private void inlcommand_exitFromAccount(long chatID, int msgID) throws TelegramApiException {
        // check if user doesn't login
        if (!db_getSessions().contains(chatID)) {
            InlineKeyboardMarkup loginKeyboard = getKeyboardMarkup();
            addButtons_acc_login(loginKeyboard);
            execute(new EditMessageText().setChatId(chatID).setMessageId(msgID).setText("Для продолжения войдите.").setReplyMarkup(loginKeyboard));
            return;
        }

        // try delete session
        if (dbManager.deleteData("sessions", new String[]{"telegram_chatID=" + chatID})) {
            // change message
            InlineKeyboardMarkup newKeyboard = getKeyboardMarkup();
            addButtons_acc_login(newKeyboard);

            EditMessageText loginMessage = new EditMessageText()
                    .setChatId(chatID)
                    .setMessageId(msgID)
                    .setText("Вы вышли из аккаунта.")
                    .setReplyMarkup(newKeyboard);

            execute(loginMessage);
        } else {
            // send error message
            execute(sendMessage(chatID).setText("При выходе произошла ошибка!\nПовторите операцию позже."));
        }
    }
}
