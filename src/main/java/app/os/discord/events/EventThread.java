package app.os.discord.events;

import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventThread extends Thread {
    private List<Guild> guilds;

    public EventThread(List<Guild> guilds) {
        this.guilds = guilds;
        this.setDaemon(true);
    }

    @Override
    public void run() {
        Map<EventProperties, String> args = new HashMap<>();

        args.put(EventProperties.ID, "15561651");
        args.put(EventProperties.TYPE, "time");
        args.put(EventProperties.CONTENT, "load kofopefwkoppefwo");

        EventManager.generateEvent(args);

        EventManager.getEvents().forEach(event -> System.out.println(event.toString()));
    }
}
