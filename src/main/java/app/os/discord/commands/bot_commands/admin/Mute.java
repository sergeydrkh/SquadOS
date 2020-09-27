package app.os.discord.commands.bot_commands.admin;

import app.os.discord.DiscordBot;
import app.os.discord.commands.command.Command;
import app.os.discord.commands.command.CommandEvent;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

import java.util.List;

public class Mute {
    // GIVE MUTE
    public static class GiveMute extends Command {
        public GiveMute() {
            this.name = "mute";
            this.arguments = "[users]";
            this.help = "замутить пользователей";
            this.requiredRole = DiscordBot.ADMIN_ROLES;
            this.cooldown = 3;
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
                commandEvent.getGuild().mute(mentioned, true)
                        .queue(success -> received.getChannel().sendMessage(String.format("<@%s> выдан мут!", mentioned.getId())).queue());
            }
        }
    }

    // REMOVE MUTE
    public static class RemoveMute extends Command {
        public RemoveMute() {
            this.name = "unmute";
            this.arguments = "[users]";
            this.help = "снять мут пользователям";
            this.requiredRole = DiscordBot.ADMIN_ROLES;
            this.cooldown = 3;
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
                commandEvent.getGuild().mute(mentioned, false)
                        .queue(success -> received.getChannel().sendMessage(String.format("<@%s> снят мут!", mentioned.getId())).queue());
            }
        }
    }


}
