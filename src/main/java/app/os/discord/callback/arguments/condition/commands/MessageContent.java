package app.os.discord.callback.arguments.condition.commands;

import app.os.discord.callback.arguments.condition.CallbackCondition;

public class MessageContent extends CallbackCondition {
    public MessageContent() {
        super("text", "проверка на текст сообщения", "{text}");
    }
}
