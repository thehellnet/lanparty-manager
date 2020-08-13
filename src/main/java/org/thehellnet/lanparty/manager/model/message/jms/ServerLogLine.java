package org.thehellnet.lanparty.manager.model.message.jms;

import org.joda.time.DateTime;
import org.thehellnet.lanparty.manager.model.persistence.Server;

import java.io.Serializable;
import java.util.Objects;

public class ServerLogLine implements Serializable {

    private final DateTime dateTime = DateTime.now();
    private final Server thnOlgServer;
    private final String line;

    public ServerLogLine(Server thnOlgServer, String line) {
        this.thnOlgServer = thnOlgServer;
        this.line = line;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public Server getThnOlgServer() {
        return thnOlgServer;
    }

    public String getLine() {
        return line;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerLogLine that = (ServerLogLine) o;
        return dateTime.equals(that.dateTime) &&
                thnOlgServer.equals(that.thnOlgServer) &&
                line.equals(that.line);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTime, thnOlgServer, line);
    }

    @Override
    public String toString() {
        return String.format("%s - %s", thnOlgServer, line);
    }
}
