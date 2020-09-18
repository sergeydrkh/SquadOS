package app.os.discord.commands.tread.command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface CommandListener {

    default void onCommand(CommandEvent event, Command command) {
    }

    default void onCompletedCommand(CommandEvent event, Command command) {
    }

    default void onTerminatedCommand(CommandEvent event, Command command) {
    }


    default void onNonCommandMessage(MessageReceivedEvent event) {
    }

    default void onCommandException(CommandEvent event, Command command, Throwable throwable) {
        throw throwable instanceof RuntimeException ? (RuntimeException) throwable : new RuntimeException(throwable);
    }
}
