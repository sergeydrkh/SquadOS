package app;

import app.os.MainThread;

public class Start {
    public static void main(String[] args) {
        new Start().run();
    }

    private void run() {
        Thread mainThread = new MainThread();
        mainThread.start();
    }
}
