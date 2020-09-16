package app.os.discord.commands.users;

import app.os.main.OS;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Date;

public class Info extends Command {
    public Info() {
        this.name = "info";
        this.help = "получить информацию о боте";
        this.cooldown = 50;
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        EmbedBuilder infoMsg = new EmbedBuilder();

        Date currentDate = new Date();
        infoMsg.setColor(OS.DEFAULT_COLOR);

        infoMsg.setTitle(String.format("%s (Revision %s)", OS.NAME, OS.VERSION));
        infoMsg.setDescription(OS.DESCRIPTION);
        infoMsg.setImage("https://sun9-32.userapi.com/y2Bhtmpmv84Moc_3cMLCAUdVxvZQpp9YcLXjVA/ogKcTeIZ41A.jpg");
        infoMsg.setFooter("© 2020 SquadOS, Inc.", "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fa/Apple_logo_black.svg/1000px-Apple_logo_black.svg.png");

        infoMsg.addField("Name", "SquadOS", true);
        infoMsg.addField("Ver.", "v" + OS.VERSION, true);
        infoMsg.addField("Guilds", String.valueOf(commandEvent.getJDA().getGuilds().size()), true);

        commandEvent.getChannel().sendMessage(infoMsg.build()).queue();
    }
}
