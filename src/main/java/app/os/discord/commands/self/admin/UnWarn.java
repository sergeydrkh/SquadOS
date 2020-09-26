package app.os.discord.commands.self.admin;

import app.os.discord.DiscordBot;
import app.os.discord.commands.command.Command;
import app.os.discord.commands.command.CommandEvent;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;

import java.util.List;
import java.util.stream.Collectors;

public class UnWarn extends Command {
    public UnWarn() {
        this.name = "unwarn";
        this.arguments = "[users]";
        this.help = "снять варны пользователям";
        this.requiredRole = DiscordBot.ADMIN_ROLES;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        Message received = commandEvent.getMessage();

        List<Member> mentionedMembers = received.getMentionedMembers();
        if (mentionedMembers.isEmpty()) {
            received.getChannel().sendMessage("Вы не указали **ни одного** пользователя!").queue();
            return;
        }

        for (Member mentioned : mentionedMembers) {
            List<Role> memberWarnRoles = mentioned.getRoles()
                    .stream()
                    .filter(role -> role.getName().equalsIgnoreCase(DiscordBot.WARN_ROLES))
                    .collect(Collectors.toList());

            memberWarnRoles.forEach(role -> commandEvent.getGuild().removeRoleFromMember(mentioned.getId(), role).queue());
            received.getChannel().sendMessage(String.format("<@%s> сняты **все** варны!", mentioned.getId())).queue();
        }
    }
}
