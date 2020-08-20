package app.os.bots.discord.commands.admin;

import app.os.bots.discord.DiscordBot;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

import java.util.List;

public class UnMute extends Command {
    public UnMute() {
        this.name = "unmute";
        this.arguments = "[users]";
        this.help = "снять мут пользователям";
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
            commandEvent.getGuild().mute(mentioned, false)
                    .queue(success -> received.getChannel().sendMessage(String.format("<@%s> снят мут!", mentioned.getId())).queue());
        }
    }
}
