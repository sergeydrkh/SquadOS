package app;

import app.os.thread.OS;

public class Start {
    public static void main(String[] args) {
        new Start().run();
    }

    private void run() {
        OS mainThread = new OS();
        mainThread.start();
    }
}
