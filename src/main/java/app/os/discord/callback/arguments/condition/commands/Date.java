package app.os.discord.callback.arguments.condition.commands;

import app.os.discord.callback.arguments.condition.CallbackCondition;

public class Date extends CallbackCondition {
    public Date() {
        super("date", "выполнить действие по дате", "{dd.MM.yyyy hh:ss}");
    }
}
