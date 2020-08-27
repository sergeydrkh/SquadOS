package app.os.discord.callback.arguments.condition;

import app.os.discord.callback.arguments.condition.commands.CDate;
import app.os.discord.callback.arguments.condition.commands.CMessageContent;

import java.util.ArrayList;
import java.util.List;

public class CallbackConditionBuilder {
    public static List<CallbackCondition> getAllCommands() {
        List<CallbackCondition> callbackCondition = new ArrayList<>();

        callbackCondition.add(new CDate());
        callbackCondition.add(new CMessageContent());

        return callbackCondition;
    }
}
