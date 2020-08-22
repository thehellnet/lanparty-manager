package org.thehellnet.lanparty.manager.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thehellnet.lanparty.manager.exception.ServerBinaryException;
import org.thehellnet.lanparty.manager.model.persistence.Server;
import org.thehellnet.lanparty.manager.model.persistence.ServerBinary;
import org.thehellnet.utility.PathUtility;
import org.thehellnet.utility.StoppableThread;
import org.thehellnet.utility.StringUtility;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServerBinaryRunner extends StoppableThread {

    private static final Logger logger = LoggerFactory.getLogger(ServerBinaryRunner.class);

    private final Server server;

    private Process process = null;

    private BufferedReader reader;
    private BufferedWriter writer; //NOSONAR

    public ServerBinaryRunner(Server server) {
        this.server = server;
    }

    @Override
    protected void preStart() {
        super.preStart();

        logger.info("Starting ServerBinaryRunner for server {}", server);

        ServerBinary serverBinary = server.getServerBinary();

        File workingDirectory = new File(serverBinary.getBaseDirectory());
        String binaryExecutable = PathUtility.absolute(workingDirectory, serverBinary.getExecutable());

        List<String> command = new ArrayList<>(Arrays.asList(
                binaryExecutable,
                "+set", "net_ip", server.getAddress(),
                "+set", "net_port", String.format("%d", server.getPort()),
                "+set", "rcon_password", server.getRconPassword()
        ));

        String serverBinaryExtraCommands = server.getServerBinaryExtraCommands();
        List<String> rows = StringUtility.splitLines(serverBinaryExtraCommands);
        for (String row : rows) {
            List<String> items = StringUtility.splitSpaces(row);
            command.addAll(items);
        }

        String fullCommand = StringUtility.joinSpaces(command);
        logger.debug("Starting server with command: \"{}\"", fullCommand);

        ProcessBuilder processBuilder = new ProcessBuilder(command)
                .directory(workingDirectory)
                .redirectInput(ProcessBuilder.Redirect.PIPE)
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectErrorStream(true);

        try {
            process = processBuilder.start();
        } catch (IOException e) {
            throw new ServerBinaryException("Unable to start server binary", e);
        }

        reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
    }

    @Override
    protected void preStop() {
        super.preStop();

        reader = null;
        writer = null;
    }

    @Override
    protected void postStop() {
        super.postStop();

        process.destroyForcibly();

        try {
            process.waitFor();
        } catch (InterruptedException ignored) { //NOSONAR
        }

        process = null;

        logger.info("ServerBinaryRunner stop for server {}", server);
    }

    @Override
    protected void job() throws Throwable {
        if (reader == null) {
            return;
        }

        String line = reader.readLine();
        if (line == null) {
            return;
        }

        if (evaluateIfLineIsLoggable(line)) {
            logger.debug(line);
        }
    }

    static boolean evaluateIfLineIsLoggable(String line) {
        if (line == null || line.strip().length() == 0) {
            return false;
        }

        if (line.startsWith("WARNING: unknown dvar")
                || line.startsWith("Adding channel:")) {
            return false;
        }

        String checkLine = line.strip().toLowerCase();

        return checkLine.contains("error")
                || checkLine.contains("warn");
    }
}
