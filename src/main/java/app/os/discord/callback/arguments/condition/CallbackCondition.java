package app.os.discord.callback.arguments.condition;

import app.os.discord.callback.CallbackEvent;

public abstract class CallbackCondition {
    public final String name;
    public final String args;
    public final String help;

    public CallbackCondition(String name, String help, String args) {
        this.name = name;
        this.args = args;
        this.help = help;
    }

    public abstract boolean check(CallbackEvent event);
}
