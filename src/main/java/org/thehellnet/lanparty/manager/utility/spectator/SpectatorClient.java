package org.thehellnet.lanparty.manager.utility.spectator;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thehellnet.lanparty.manager.model.spectator.SpectatorCommand;
import org.thehellnet.lanparty.manager.model.spectator.SpectatorCommandAction;
import org.thehellnet.lanparty.manager.model.spectator.SpectatorCommandSerializer;
import org.thehellnet.utility.StoppableThread;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalDateTime;

public class SpectatorClient extends StoppableThread {

    private static final int LOOP_INTERVAL = 1000;

    private static final Logger logger = LoggerFactory.getLogger(SpectatorClient.class);

    private final String address;
    private final int port;

    private boolean enableNextPlayer = true;
    private int nextPlayerInterval = 10;
    private LocalDateTime lastNextPlayer = LocalDateTime.now();

    public SpectatorClient(String address, int port) {
        super(LOOP_INTERVAL);

        this.address = address;
        this.port = port;
    }

    public boolean isEnableNextPlayer() {
        return enableNextPlayer;
    }

    public void setEnableNextPlayer(boolean enableNextPlayer) {
        this.enableNextPlayer = enableNextPlayer;
    }

    public int getNextPlayerInterval() {
        return nextPlayerInterval;
    }

    public void setNextPlayerInterval(int nextPlayerInterval) {
        this.nextPlayerInterval = nextPlayerInterval;
    }

    public synchronized void sendCommand(SpectatorCommand command) {
        logger.debug("Sending command {}", command);

        String rawCommand = SpectatorCommandSerializer.serialize(command);
        byte[] rawBytes = rawCommand.getBytes();

        Socket socket = null;
        OutputStream outputStream = null;

        try {
            socket = new Socket(address, port);
            outputStream = socket.getOutputStream();
            outputStream.write(rawBytes);
            outputStream.flush();
        } catch (IOException e) {
            logger.error("Unable to send command", e);
        }

        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException ignored) { //NOSONAR
            }
        }

        if (socket != null) {
            try {
                socket.close();
            } catch (IOException ignored) { //NOSONAR
            }
        }
    }

    @Override
    protected void job() {
        if (!enableNextPlayer) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        if (lastNextPlayer.plusSeconds(nextPlayerInterval).isAfter(now)) {
            return;
        }

        SpectatorCommand command = new SpectatorCommand(SpectatorCommandAction.NEXT_PLAYER);
        sendCommand(command);
        lastNextPlayer = LocalDateTime.now();
    }
}
