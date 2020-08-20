package app.os.bots.discord.commands.admin;

import app.os.bots.discord.DiscordBot;
import app.os.thread.OS;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;

public class VoiceChannels extends Command {
    public VoiceChannels() {
        this.name = "voice";
        this.help = "get voice channels on server";
        this.requiredRole = DiscordBot.ADMIN_ROLES;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        StringBuilder voiceChannels = new StringBuilder();

        voiceChannels.append("Голосовые комнаты на сервере **").append(commandEvent.getGuild().getName()).append("**\n");
        commandEvent.getGuild().getVoiceChannels().forEach(channel -> voiceChannels.append("- ").append(channel.getName()).append("\n"));

        commandEvent.getChannel().sendMessage(
                new EmbedBuilder()
                        .setDescription(voiceChannels.toString())
                        .setColor(OS.DEFAULT_COLOR).build()).queue();
    }
}
