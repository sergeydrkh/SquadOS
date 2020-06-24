package os.bots.telegram;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import os.utils.Console;

public class TelegramStartThread extends Thread {
    @Override
    public void run() {
        ApiContextInitializer.init();
        Console.println("Starting telegram bot...");
        TelegramBotsApi telegramBots = new TelegramBotsApi();
        TelegramLongPollingBot telegramBot = new TelegramBot();
        try {
            telegramBots.registerBot(telegramBot);
        } catch (TelegramApiRequestException e) {
            Console.errln("Произошла ошибка при регистрации бота! " + e.getMessage() + ".");
            return;
        }

        Console.println("TG >> Successfully logged in!");
    }
}
