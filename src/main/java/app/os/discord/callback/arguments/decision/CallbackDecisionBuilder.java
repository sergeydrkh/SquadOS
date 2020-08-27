package app.os.discord.callback.arguments.decision;

import app.os.discord.callback.Callback;

import java.util.ArrayList;
import java.util.List;

public class CallbackDecisionBuilder {
    private final List<CallbackDecision> callbackCommands = new ArrayList<>();

    public void addCommand(CallbackDecision callbackCommand) {
        callbackCommands.add(callbackCommand);
    }

    public List<Callback> build() {
        return null;
    }
}
