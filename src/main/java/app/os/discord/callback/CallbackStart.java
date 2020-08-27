package app.os.discord.callback;

import app.os.discord.callback.arguments.decision.CallbackDecisionBuilder;
import app.os.discord.callback.arguments.decision.commands.CBan;
import app.os.discord.callback.arguments.decision.commands.CWarn;

public class CallbackStart {
    public void start() {
        CallbackDecisionBuilder builder = new CallbackDecisionBuilder();

        builder.addCommand(new CBan());
        builder.addCommand(new CWarn());

        new CallbackUpdate(builder.build()).start();
    }
}
