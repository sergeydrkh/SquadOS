package app.os.discord;

public class DiscordStartThread extends Thread {
    public DiscordStartThread() {
        setName("DiscordBot Starter");
    }

    @Override
    public void run() {
        DiscordBot bot = new DiscordBot();
        bot.launch();
    }
}
