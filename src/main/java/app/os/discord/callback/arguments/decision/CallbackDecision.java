package app.os.discord.callback.arguments.decision;

import app.os.discord.callback.CallbackEvent;

public abstract class CallbackDecision {
    public final String name;
    public final String args;
    public final String help;

    public CallbackDecision(String name, String help, String args) {
        this.name = name;
        this.args = args;
        this.help = help;
    }

    public abstract void execute(CallbackEvent event);
}
