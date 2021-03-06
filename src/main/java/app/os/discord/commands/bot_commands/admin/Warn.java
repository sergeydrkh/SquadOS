package app.os.discord.commands.bot_commands.admin;

import app.os.discord.DiscordBot;
import app.os.discord.commands.command.Command;
import app.os.discord.commands.command.CommandEvent;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;

import java.util.List;
import java.util.stream.Collectors;

public class Warn {
    // GIVE WARN
    public static class GiveWarn extends Command {
        public GiveWarn() {
            this.name = "warn";
            this.arguments = "[users]";
            this.help = "выдать варн";
            this.requiredRole = DiscordBot.ADMIN_ROLES;
            this.cooldown = 5;
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
                List<Role> serverWarnRoles = commandEvent.getGuild().getRoles()
                        .stream()
                        .filter(role -> role.getName().equalsIgnoreCase(DiscordBot.WARN_ROLES))
                        .collect(Collectors.toList());

                List<Role> memberRoles = mentioned.getRoles()
                        .stream()
                        .filter(role -> role.getName().equalsIgnoreCase(DiscordBot.WARN_ROLES))
                        .collect(Collectors.toList());

                serverWarnRoles.removeAll(memberRoles);
                if (serverWarnRoles.isEmpty()) {
                    new Ban().execute(commandEvent);
                } else {
                    commandEvent.getGuild().addRoleToMember(mentioned.getId(), serverWarnRoles.get(serverWarnRoles.size() - 1))
                            .queue(success -> received.getChannel().sendMessage(String.format("<@%s> выдан **варн**!", mentioned.getId())).queue());
                }
            }
        }
    }

    // REMOVE WARN
    public static class RemoveWarn extends Command {
        public RemoveWarn() {
            this.name = "unwarn";
            this.arguments = "[users]";
            this.help = "снять варны пользователям";
            this.requiredRole = DiscordBot.ADMIN_ROLES;
            this.cooldown = 5;
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

}
