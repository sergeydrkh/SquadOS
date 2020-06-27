package os;

import os.bots.discord.DiscordStartThread;
import os.bots.telegram.TelegramStartThread;
import os.bots.vk.VkStartThread;

public class MainThread extends Thread {
    public static final String MAIN_WEBSITE_URL = "test.com";

    @Override
    public void start() {
        // DS thread
        Thread discordThread = new DiscordStartThread();
        discordThread.start();

        // TG thread
        Thread telegramThread = new TelegramStartThread();
        telegramThread.start();

        // VK thread
        Thread vkThread = new VkStartThread();
        vkThread.start();
    }
}
