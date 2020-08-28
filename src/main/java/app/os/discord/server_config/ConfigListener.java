package app.os.discord.server_config;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ConfigListener extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (!ConfigManager.isExists(event.getGuild().getIdLong()))
            ConfigManager.createConfig(event.getGuild().getIdLong());
    }
}
