package org.thehellnet.lanparty.manager.runner;

import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.thehellnet.lanparty.manager.init.InitializedEvent;

@Component
public abstract class AbstractRunner implements Runner {

    private final Object SYNC = new Object();

    private boolean running = false;

    @EventListener(InitializedEvent.class)
    public void onInitializedEvent() {
        start();
    }

    @EventListener(ContextClosedEvent.class)
    public void onContextStoppedEvent() {
        stop();
    }

    @Override
    public void start() {
        synchronized (SYNC) {
            if (running) {
                return;
            }

            startRunner();
            running = true;
        }
    }

    @Override
    public void stop() {
        synchronized (SYNC) {
            if (!running) {
                return;
            }

            stopRunner();
            running = false;
        }
    }

    @Override
    public void restart() {
        stop();
        start();
    }

    protected abstract void startRunner();

    protected abstract void stopRunner();
}
