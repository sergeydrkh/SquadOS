package os.bots.vk;

import os.utils.Console;

public class VkStartThread extends Thread {
    @Override
    public void run() {
        Console.println("Starting vk bot...");
        VkBot vkBot = new VkBot();
        vkBot.run();
    }
}
