package app.os.discord.callback;

import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CallbackThread extends Thread {
    private List<Guild> guilds;

    public CallbackThread(List<Guild> guilds) {
        this.guilds = guilds;
        this.setDaemon(true);
    }

    @Override
    public void run() {
        Map<CallbackProperties, String> args = new HashMap<>();

        args.put(CallbackProperties.ID, "15561651");
        args.put(CallbackProperties.TYPE, "time");
        args.put(CallbackProperties.CONTENT, "load kofopefwkoppefwo");

        CallbackManager.generateEvent(args);

        CallbackManager.getEvents().forEach(event -> System.out.println(event.toString()));
    }
}
