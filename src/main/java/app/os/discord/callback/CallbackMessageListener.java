package app.os.discord.callback;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CallbackMessageListener extends ListenerAdapter {
    private final List<Callback> callbacks;

    public CallbackMessageListener(List<Callback> callbacks) {
        this.callbacks = callbacks;
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {

    }
}
