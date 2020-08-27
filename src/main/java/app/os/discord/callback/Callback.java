package app.os.discord.callback;

import app.os.discord.callback.arguments.decision.CallbackDecision;
import net.dv8tion.jda.api.JDA;

import java.util.List;
import java.util.Properties;

public class Callback {
    private final Properties args;
    private String commandName;

    public Callback(Properties args) {
        this.args = args;
    }

    public void execute(JDA jda, List<CallbackDecision> callbackCommands) {
        callbackCommands.forEach(command -> {
            if (command.name.equalsIgnoreCase(commandName)) {
                command.execute(new CallbackEvent(jda.getGuildById(args.getProperty("guild_id")), args));
                return;
            }
        });
    }
}
