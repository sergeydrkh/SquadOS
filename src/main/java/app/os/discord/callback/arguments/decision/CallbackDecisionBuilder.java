package app.os.discord.callback.arguments.decision;

import app.os.discord.callback.arguments.decision.commands.CBan;
import app.os.discord.callback.arguments.decision.commands.CWarn;

import java.util.ArrayList;
import java.util.List;

public class CallbackDecisionBuilder {
    public static List<CallbackDecision> getAllCommands() {
        List<CallbackDecision> callbackDecision = new ArrayList<>();

        callbackDecision.add(new CBan());
        callbackDecision.add(new CWarn());

        return callbackDecision;
    }
}
