package app.os.discord.commands.self.admin;

import app.os.discord.DiscordBot;
import app.os.main.OS;
import app.os.discord.commands.command.Command;
import app.os.discord.commands.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.util.List;
import java.util.stream.Collectors;

public class Admins extends Command {
    public Admins() {
        this.name = "admins";
        this.help = "получить список администраторов";
        this.requiredRole = DiscordBot.ADMIN_ROLES;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        List<Member> admins = commandEvent.getGuild().getMembers()
                .stream()
                .filter(member -> member.getRoles().containsAll(commandEvent.getGuild().getRolesByName(DiscordBot.ADMIN_ROLES, true)))
                .collect(Collectors.toList());

        StringBuilder messageText = new StringBuilder();

        messageText.append(String.format("Администраторы сервера **%s** *(online)*:", commandEvent.getGuild().getName())).append("\n");
        admins.forEach(admin -> messageText.append(" - <@").append(admin.getId()).append(">\n"));

        commandEvent.getChannel().sendMessage(
                new EmbedBuilder()
                        .setDescription(messageText.toString())
                        .setColor(OS.DEFAULT_COLOR).build()).queue();
    }
}
