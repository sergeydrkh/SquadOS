package app.os.discord.callback;

import net.dv8tion.jda.api.entities.Guild;

import java.util.Properties;

public class CallbackEvent {
    private final Guild guild;
    private final Properties args;

    public CallbackEvent(Guild guild, Properties args) {
        this.guild = guild;
        this.args = args;
    }

    public Guild getGuild() {
        return guild;
    }

    public Properties getArgs() {
        return args;
    }
}
