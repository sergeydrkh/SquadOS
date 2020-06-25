package os.bots.telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import os.utils.Console;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TelegramBot extends TelegramLongPollingBot {
    private static final String TOKEN = "1216336612:AAGcae_ck98BvTPekF9mzg_IE9gRpSQt3q8";
    public static final String USERNAME = "@SquadOS_bot";
    public static final String NICKNAME = "SquadOS";
    public static final String VERSION = "0.1.1_beta";
    public static final String CREATOR = "@s3r3zka";
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm");

    public void onUpdateReceived(Update update) {
        Message receivedMessage = update.getMessage();
        String receivedText = receivedMessage.getText();
        long chatID = receivedMessage.getChatId();

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
                    if (receivedText.startsWith("/info"))
                        command_info(chatID);

                } else {
                    execute(sendMessage(chatID).setText("Я вас не понимаю!\n/help - помощь по командам."));
                }
            }

            // if message has CallbackQuery
            if (update.hasCallbackQuery()) {
                // todo: make callback query support
            }


        } catch (TelegramApiException e) {
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

    // >> BOT COMMANDS <<

    /**
     * @param chatID
     * @command /start
     * @action send start message
     */
    private void command_start(long chatID) throws TelegramApiException {
        String greeting = "Привет! Это бот системы SquadOS.\nВведи /help для просмотра команд.";
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
        info.append(" > Текущая дата: ").append(DATE_FORMAT.format(new Date())).append(".");   // current date

        execute(sendMessage(chatID).setText(info.toString()));
    }
}
