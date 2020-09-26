package app.os.discord.music.reaction;

import app.os.discord.music.self.GuildMusicManager;
import app.os.discord.music.self.MusicManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ReactionListener extends ListenerAdapter {
    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        execute(event.getReactionEmote().getEmoji(), event.getGuild(), event.getUser());
    }

    @Override
    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
        execute(event.getReactionEmote().getEmoji(), event.getGuild(), event.getUser());
    }

    private void execute(String emote, Guild guild, User user) {
        MusicManager musicManager = MusicManager.getInstance();
        GuildMusicManager guildMusicManager = musicManager.getGuildAudioPlayer(guild);

        if (user.isBot())
            return;

        if (emote.equals("⏯")) {
            guildMusicManager.player.setPaused(!guildMusicManager.player.isPaused());
        } else if (emote.equals("⏭")) {
            guildMusicManager.scheduler.nextTrack();
        }
    }
}
