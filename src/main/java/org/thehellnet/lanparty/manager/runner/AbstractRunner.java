package org.thehellnet.lanparty.manager.runner;

import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.thehellnet.lanparty.manager.init.InitializedEvent;

@Component
public abstract class AbstractRunner {

    private final Object SYNC = new Object();

    private boolean running = false;

    @EventListener(InitializedEvent.class)
    public void onInitializedEvent() {
        synchronized (SYNC) {
            if (running) {
                return;
            }

            start();
            running = true;
        }
    }

    @EventListener(ContextClosedEvent.class)
    public void onContextStoppedEvent() {
        synchronized (SYNC) {
            stop();
            running = false;
        }
    }

    protected abstract void start();

    protected abstract void stop();
}
