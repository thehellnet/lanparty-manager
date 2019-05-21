package org.thehellnet.lanparty.manager.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogParsingRunner extends AbstractRunner {

    private static final Logger logger = LoggerFactory.getLogger(LogParsingRunner.class);

    @Override
    protected void start() {
        logger.info("START");
    }

    @Override
    protected void stop() {
        logger.info("END");
    }
}
