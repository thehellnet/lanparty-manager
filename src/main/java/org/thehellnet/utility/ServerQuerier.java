package org.thehellnet.utility;

import org.thehellnet.utility.exception.ServerQuerierException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public class ServerQuerier {

    private static final byte[] PACKET_HEADER = new byte[]{(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};

    private final String address;
    private final int port;
    private final String rconPassword;

    private DatagramSocket socket = null;
    private InetAddress inetAddress = null;

    public ServerQuerier(String address, int port, String rconPassword) {
        this.address = address;
        this.port = port;
        this.rconPassword = rconPassword;
    }

    public synchronized void open() {
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            throw new ServerQuerierException(e);
        }

        try {
            inetAddress = InetAddress.getByName(address);
        } catch (UnknownHostException e) {
            throw new ServerQuerierException(e);
        }
    }

    public synchronized void close() {
        if (socket != null) {
            socket.close();
        }

        socket = null;
    }

    public synchronized String sendCommand(String command) {
        checkSocketIsOpen();

        String completeCommand = String.format("rcon %s %s", rconPassword, command);
        byte[] completeCommandBytes = completeCommand.getBytes();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.writeBytes(PACKET_HEADER);
        byteArrayOutputStream.writeBytes(completeCommandBytes);
        byte[] rawCommand = byteArrayOutputStream.toByteArray();

        DatagramPacket datagramPacket = new DatagramPacket(rawCommand, rawCommand.length, inetAddress, port);

        try {
            socket.send(datagramPacket);
        } catch (IOException e) {
            throw new ServerQuerierException(e);
        }

        byte[] buffer = new byte[8192];
        datagramPacket = new DatagramPacket(buffer, buffer.length);

        try {
            socket.receive(datagramPacket);
        } catch (IOException e) {
            throw new ServerQuerierException(e);
        }


        byte[] data = Arrays.copyOfRange(datagramPacket.getData(), 0, datagramPacket.getLength());
        if (data.length <= 4) {
            throw new ServerQuerierException("Invalid response");
        }

        byte[] responseHeader = Arrays.copyOfRange(data, 0, 4);
        byte[] responseBody = Arrays.copyOfRange(data, 4, data.length);

        if (!Arrays.equals(responseHeader, PACKET_HEADER)) {
            throw new ServerQuerierException("Received packet doesn't start with expected header");
        }

        return new String(responseBody);
    }

    public String status() {
        return sendCommand("status");
    }

    public void mapRestart() {
        sendCommand("map_restart");
    }

    public void fastRestart() {
        sendCommand("fast_restart");
    }

    private void checkSocketIsOpen() {
        if (socket == null || socket.isClosed()) {
            throw new ServerQuerierException("Server Querier not opened");
        }
    }
}
