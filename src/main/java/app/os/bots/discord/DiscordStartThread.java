package app.os.bots.discord;

public class DiscordStartThread extends Thread {
    @Override
    public void run() {
        DiscordBot bot = new DiscordBot();
        bot.load();
    }
}
