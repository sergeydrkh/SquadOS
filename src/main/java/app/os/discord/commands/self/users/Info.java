package app.os.discord.commands.self.users;

import app.os.main.OS;
import app.os.discord.commands.command.Command;
import app.os.discord.commands.command.CommandEvent;
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

        infoMsg.setColor(OS.DEFAULT_COLOR);

        infoMsg.setTitle(OS.NAME);
        infoMsg.setDescription(OS.DESCRIPTION);
        infoMsg.setImage("https://sun9-32.userapi.com/y2Bhtmpmv84Moc_3cMLCAUdVxvZQpp9YcLXjVA/ogKcTeIZ41A.jpg");
        infoMsg.setFooter("© 2020 SquadOS, Inc.", "https://sun9-52.userapi.com/kCqPd1nYHIqx95X1c2YpM3vOnlXR_PtPInWgBA/Ohplrp5Bpm8.jpg");

        infoMsg.addField("Название", "SquadOS", true);
        infoMsg.addField("Версия.", "v" + OS.VERSION, true);
        infoMsg.addField("Серверов", String.valueOf(commandEvent.getJDA().getGuilds().size()), true);
        infoMsg.addField("Аптайм", String.format("%dd", (new Date().getTime() - OS.DATE_LAUNCH.getTime()) / (24 * 60 * 60 * 1000)), true);

        String[] wishes = new String[]{"Удачного дня", "Доброе утро", "Хорошего настроения", "Не унывать"};
        infoMsg.addField("Пожелание", wishes[(int) (Math.random() * wishes.length)], true);

        infoMsg.addField("", "", true);

        commandEvent.getChannel().sendMessage(infoMsg.build()).queue();
    }
}
