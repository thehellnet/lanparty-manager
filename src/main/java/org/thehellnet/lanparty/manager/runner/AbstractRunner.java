package org.thehellnet.lanparty.manager.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.thehellnet.lanparty.manager.init.InitializedEvent;

@Component
public abstract class AbstractRunner {

    private final Object sync = new Object();

    private boolean running = false;

    @Autowired
    protected AbstractRunner() {
    }

    @EventListener(InitializedEvent.class)
    public void onInitializedEvent() {
        if (!autostart()) {
            return;
        }

        start();
    }

    @EventListener(ContextClosedEvent.class)
    public void onContextStoppedEvent() {
        stop();
    }

    public void start() {
        synchronized (sync) {
            if (running) {
                return;
            }

            startRunner();
            running = true;
        }
    }

    public void stop() {
        synchronized (sync) {
            if (!running) {
                return;
            }

            stopRunner();
            running = false;
        }
    }

    public void restart() {
        stop();
        start();
    }

    protected abstract boolean autostart();

    protected abstract void startRunner();

    protected abstract void stopRunner();
}
