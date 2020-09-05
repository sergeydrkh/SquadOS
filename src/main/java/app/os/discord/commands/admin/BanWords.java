package app.os.discord.commands.admin;

import app.os.discord.server_config.ConfigManager;
import app.os.discord.server_config.ConfigProperties;
import app.os.main.OS;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.Message;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class BanWords extends Command {
    public BanWords() {
        this.name = "banwords";
        this.arguments = "{add/remove} или без параметров для откл/вкл";
        this.help = "запретить определенные слова на сервере, при нарушении - удаление сообщения и варн";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        Message received = commandEvent.getMessage();

        String[] args = received.getContentRaw().split(" ");
        if (args.length > 3 || args.length < 1) {
            received.getChannel().sendMessage(this.arguments).queue();
            return;
        }

        Properties serverConfig = ConfigManager.getConfigByID(commandEvent.getGuild().getIdLong());

        if (args.length == 1) {
            serverConfig.setProperty(
                    ConfigProperties.BAN_WORDS_STATE.getKey(),
                    String.valueOf(!Boolean.parseBoolean(serverConfig.getProperty(ConfigProperties.BAN_WORDS_STATE.getKey())))
            );
        } else {
            List<String> banWordsList = Arrays.asList(serverConfig.getProperty(ConfigProperties.BAN_WORDS_LIST.getKey()).split(OS.DEFAULT_DELIMITER));

            if (args[1].equals("add")) {
                banWordsList.add(args[2]);
            } else if (args[1].equals("remove")) {
                banWordsList = banWordsList.stream().filter(banWord -> banWord.equals(args[2])).collect(Collectors.toList());
            } else {
                received.getChannel().sendMessage("Unexpected argument!").queue();
                return;
            }

            StringBuilder newList = new StringBuilder();
            banWordsList.forEach(banWord -> newList.append(banWord).append(","));

            serverConfig.setProperty(
                    ConfigProperties.BAN_WORDS_LIST.getKey(),
                    newList.substring(0, newList.toString().length() - 2)
            );
        }

        ConfigManager.saveConfig(serverConfig);
    }
}
