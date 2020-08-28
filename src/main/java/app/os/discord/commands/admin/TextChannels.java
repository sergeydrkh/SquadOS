package app.os.discord.commands.admin;

import app.os.discord.DiscordBot;
import app.os.main.OS;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;

public class TextChannels extends Command {
    public TextChannels() {
        this.name = "text";
        this.help = "получить все текстовые чаты на сервере";
        this.requiredRole = DiscordBot.ADMIN_ROLES;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        StringBuilder textChannels = new StringBuilder();

        textChannels.append("Текстовые чаты на сервере **").append(commandEvent.getGuild().getName()).append("**\n");
        commandEvent.getGuild().getTextChannels().forEach(channel -> textChannels.append("- <#").append(channel.getId()).append(">\n"));

        commandEvent.getChannel().sendMessage(
                new EmbedBuilder()
                        .setDescription(textChannels.toString())
                        .setColor(OS.DEFAULT_COLOR).build()).queue();
    }
}
