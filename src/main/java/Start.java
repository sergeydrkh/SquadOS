import os.Main;

public class Start {
    public static void main(String[] args) {
        new Start().run();
    }

    private void run() {
        Thread mainThread = new Main();
        mainThread.start();
    }
}
