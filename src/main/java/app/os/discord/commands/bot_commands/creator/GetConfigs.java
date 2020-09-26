package app.os.discord.commands.bot_commands.creator;

import app.os.discord.commands.command.Command;
import app.os.discord.commands.command.CommandEvent;
import app.os.discord.configs.ConfigManager;
import app.os.discord.configs.ConfigProperties;

import java.util.List;
import java.util.Properties;

public class GetConfigs extends Command {
    public GetConfigs() {
        this.name = "config";
        this.arguments = "{тип: all/this}";
        this.help = "получить конфиг";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        String[] args = commandEvent.getMessage().getContentRaw().split(" ");
        if (args.length > 2) {
            commandEvent.getMessage().getChannel().sendMessage("Вы не правильно используете команду!\n**/commands** - для помощи.").queue();
            return;
        }

        List<Properties> allConfigs = ConfigManager.getAllConfigs();
        if (args[1].equals("all")) {
            commandEvent.getMessage().getChannel().sendMessage(allConfigs.toString()).queue();
        } else if (args[1].equals("this")) {
            if (ConfigManager.isExists(commandEvent.getGuild().getIdLong())) {
                allConfigs.forEach(config -> {
                    if (config.getProperty(ConfigProperties.GUILD_ID.getKey()).equals(commandEvent.getGuild().getId())) {
                        commandEvent.getMessage().getChannel().sendMessage(config.toString()).queue();
                    }
                });
            }
        } else {
            commandEvent.getMessage().getChannel().sendMessage("Unexpected argument!").queue();
        }
    }
}
