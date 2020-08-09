package org.thehellnet.utility.log;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class LogTailer {

    private static final Logger logger = LoggerFactory.getLogger(LogTailer.class);
    private static final long DELAY_MILLIS = 1000;

    private final LineHandler lineHandler;
    private final Tailer tailer;
    private Thread thread;

    public LogTailer(File logFile, LineHandler lineHandler) {
        this.lineHandler = lineHandler;
        tailer = new Tailer(logFile, prepareTailerListener(), DELAY_MILLIS, true);
    }

    public synchronized void start() {
        logger.debug("Starting logTailer for {}", tailer.getFile());

        thread = new Thread(tailer);
        thread.setDaemon(true);
        thread.start();
    }

    public synchronized void stop() {
        logger.debug("Stopping logTailer for {}", tailer.getFile());

        tailer.stop();
        thread.interrupt();
        thread = null;
    }

    public void join() {
        if (thread == null) {
            return;
        }

        try {
            thread.join();
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    private TailerListener prepareTailerListener() {
        return new TailerListener() {
            @Override
            public void init(Tailer tailer) {
                logger.debug("logTailer initialized for {}", tailer.getFile());
            }

            @Override
            public void fileNotFound() {
                logger.error("File not found");
                tailer.stop();
            }

            @Override
            public void fileRotated() {
                logger.info("File rotated");
            }

            @Override
            public void handle(String line) {
                logger.debug("New line for {}: {}", tailer.getFile(), line);
                lineHandler.handleLine(line);
            }

            @Override
            public void handle(Exception ex) {
                logger.error(ex.getMessage());
            }
        };
    }
}
