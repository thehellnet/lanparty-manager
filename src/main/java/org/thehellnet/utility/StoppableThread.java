package org.thehellnet.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class StoppableThread {

    private static final Logger logger = LoggerFactory.getLogger(StoppableThread.class);

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

        preStart();

        thread = new Thread(this::loop);
        keepRunning = true;
        thread.start();

        postStart();
    }

    public synchronized void stop() {
        if (thread == null) {
            return;
        }

        preStop();

        keepRunning = false;
        thread.interrupt();

        postStop();
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
            try {
                job();
            } catch (Throwable throwable) {
                logger.error("Error executing loop job", throwable);
                break;
            }

            if (loopInterval > 0) {
                try {
                    //noinspection BusyWait
                    Thread.sleep(loopInterval);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }

    protected void preStart() {
    }

    protected void postStart() {
    }

    protected void preStop() {
    }

    protected void postStop() {
    }

    protected abstract void job() throws Throwable;
}
