package org.thehellnet.lanparty.manager.init;

import org.springframework.context.ApplicationEvent;

public class InitializedEvent extends ApplicationEvent {

    InitializedEvent(Object source) {
        super(source);
    }
}
