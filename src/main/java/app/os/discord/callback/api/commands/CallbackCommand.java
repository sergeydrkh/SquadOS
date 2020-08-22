package app.os.discord.callback.api.commands;

import app.os.discord.callback.Callback;
import net.dv8tion.jda.api.entities.Guild;

public abstract class CallbackCommand {
    public String name;
    public String help;
    public String args;

    public abstract void execute(Callback callback, Guild guid);
}
