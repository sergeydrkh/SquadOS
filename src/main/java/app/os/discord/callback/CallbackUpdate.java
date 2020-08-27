package app.os.discord.callback;

import app.os.discord.callback.arguments.decision.CallbackDecisionBuilder;
import net.dv8tion.jda.api.JDA;

import java.util.List;

public class CallbackUpdate extends Thread {
    private final List<Callback> callbacks;
    private final JDA jda;

    public CallbackUpdate(List<Callback> callbacks, JDA jda) {
        this.setDaemon(true);
        this.setName("Callbacks update thread");

        this.callbacks = callbacks;
        this.jda = jda;
    }

    @Override
    public void run() {
        while (true) {
            callbacks.forEach(callback -> callback.execute(jda, CallbackDecisionBuilder.getAllCommands()));

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
