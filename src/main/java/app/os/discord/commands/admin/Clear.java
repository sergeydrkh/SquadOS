package app.os.discord.commands.admin;

import app.os.discord.DiscordBot;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.Message;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class Clear extends Command {
    public Clear() {
        this.name = "clear";
        this.requiredRole = DiscordBot.ADMIN_ROLES;
        this.help = "удалить все сообщения за последнюю неделю";
        this.cooldown = 10;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        OffsetDateTime twoWeeksAgo = OffsetDateTime.now().minus(2, ChronoUnit.WEEKS);

        new Thread(() -> {
            while (true) {
                List<Message> messages = commandEvent.getChannel().getHistory().retrievePast(50).complete();
                messages.removeIf(m -> m.getTimeCreated().isBefore(twoWeeksAgo));

                if (messages.isEmpty() || messages.size() < 2)
                    return;

                commandEvent.getTextChannel().deleteMessages(messages).complete();
            }
        }).start();
    }
}
