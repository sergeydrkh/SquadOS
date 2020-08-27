package app.os.discord.callback.arguments.condition;

public abstract class CallbackCondition {
    public final String name;
    public final String args;
    public final String help;

    public CallbackCondition(String name, String help, String args) {
        this.name = name;
        this.args = args;
        this.help = help;
    }
}
