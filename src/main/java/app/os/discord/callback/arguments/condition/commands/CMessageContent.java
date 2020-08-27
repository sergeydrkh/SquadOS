package app.os.discord.callback.arguments.condition.commands;

import app.os.discord.callback.CallbackEvent;
import app.os.discord.callback.arguments.condition.CallbackCondition;

public class CMessageContent extends CallbackCondition {
    public CMessageContent() {
        super("text", "проверка на текст сообщения", "{text}");
    }

    @Override
    public boolean check(CallbackEvent event) {
        return false;
    }
}
