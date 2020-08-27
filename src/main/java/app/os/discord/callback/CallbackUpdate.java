package app.os.discord.callback;

import java.util.List;

public class CallbackUpdate extends Thread {
    private List<Callback> callbacks;

    public CallbackUpdate(List<Callback> callbacks) {
        this.setDaemon(true);
        this.setName("Callbacks update thread");

        this.callbacks = callbacks;
    }

    @Override
    public void run() {
        while (true) {

        }
    }
}
