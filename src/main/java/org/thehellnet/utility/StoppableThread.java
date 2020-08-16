package org.thehellnet.utility;

public abstract class StoppableThread {

    private Thread thread;
    private boolean keepRunning = false;
    private final long loopInterval;

    protected StoppableThread() {
        this(0);
    }

    protected StoppableThread(long loopInterval) {
        if (loopInterval >= 0) {
            this.loopInterval = loopInterval;
        } else {
            this.loopInterval = 0;
        }
    }

    public synchronized void start() {
        if (thread != null) {
            return;
        }

        thread = new Thread(this::loop);
        keepRunning = true;
        thread.start();
    }

    public synchronized void stop() {
        if (thread == null) {
            return;
        }

        keepRunning = true;

        try {
            thread.join();
        } catch (InterruptedException ignored) {
        }
    }

    public void join() {
        if (thread == null) {
            return;
        }

        try {
            thread.join();
        } catch (InterruptedException ignored) {
        }
    }

    private void loop() {
        while (keepRunning) {
            job();

            if (loopInterval > 0) {
                try {
                    //noinspection BusyWait
                    Thread.sleep(loopInterval);
                } catch (InterruptedException ignored) {
                    break;
                }
            }
        }
    }

    protected abstract void job();
}
