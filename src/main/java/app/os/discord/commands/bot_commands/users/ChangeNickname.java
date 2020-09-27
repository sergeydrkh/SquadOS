package app.os.discord.commands.bot_commands.users;

import app.os.discord.commands.command.Command;
import app.os.discord.commands.command.CommandEvent;
import net.dv8tion.jda.api.entities.Message;

public class ChangeNickname extends Command {
    public ChangeNickname() {
        this.name = "name";
        this.help = "установить никнейм";
        this.arguments = "[новый никнейм]";
        this.cooldown = 10;
    }

    @Override
    protected void execute(CommandEvent event) {
        Message received = event.getMessage();

        String oldNickname = received.getMember().getNickname();
        String newNickname = received.getContentRaw().substring(this.name.length() + 3).trim();
        received.getMember().modifyNickname(newNickname).queue(
                s -> received.getChannel().sendMessage(String.format("<@%s> изменил никнейм с **%s** на **%s**.",
                        received.getMember().getId(), oldNickname, newNickname)).queue());
    }
}
