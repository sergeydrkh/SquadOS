package app.os.discord.commands.self.admin;

import app.os.discord.DiscordBot;
import app.os.main.OS;
import app.os.discord.commands.command.Command;
import app.os.discord.commands.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;

public class VoiceChannels extends Command {
    public VoiceChannels() {
        this.name = "voice";
        this.help = "получить все голосовые команты на сервере";
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
