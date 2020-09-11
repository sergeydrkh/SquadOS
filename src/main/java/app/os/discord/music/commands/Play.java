package app.os.discord.music.commands;

import app.os.discord.DiscordBot;
import app.os.discord.music.MusicManager;
import app.os.main.OS;
import app.os.utilities.JSONReader;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.Message;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class Play extends Command {
    private final String googleApiKey;

    public Play(String googleApiKey) {
        this.googleApiKey = googleApiKey;

        this.name = "play";
        this.help = "включить музыку";
        this.arguments = "{ссылка}";
        this.requiredRole = DiscordBot.DJ_ROLE;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        Message received = commandEvent.getMessage();
        String[] args = received.getContentRaw().split(OS.DEFAULT_MESSAGE_DELIMITER);
        if (args.length < 2) {
            received.getChannel().sendMessage(String.format("**Ошибка!** Используйте: %s.", this.arguments)).queue();
            return;
        }

        // Never Gonna Give You Up
        String link = "";

        try {
            new URL(args[1]);
            link = args[1];
        } catch (MalformedURLException malformedURLException) {
            String apiQuery = String.format("https://www.googleapis.com/youtube/v3/search?part=snippet&q=%s&type=video&maxResults=10&key=%s",
                    received.getContentRaw().substring(args[0].length()).trim(),
                    googleApiKey);

            try {
                JSONObject allData = JSONReader.readJsonFromUrl(apiQuery);
                JSONArray videos = allData.getJSONArray("items");

                link = "https://www.youtube.com/watch?v=" + videos.getJSONObject(0).getJSONObject("id").getString("videoId");
            } catch (Exception exception) {
                received.getChannel().sendMessage("**Не удалось** найти видео!").queue();
                return;
            }
        }

        System.out.println(link);

        MusicManager musicManager = MusicManager.getInstance();
        musicManager.loadAndPlay(received.getTextChannel(), link);

    }
}
