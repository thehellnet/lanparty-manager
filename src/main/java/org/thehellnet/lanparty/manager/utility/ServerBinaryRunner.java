package org.thehellnet.lanparty.manager.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thehellnet.lanparty.manager.exception.ServerBinaryException;
import org.thehellnet.lanparty.manager.model.persistence.Server;
import org.thehellnet.lanparty.manager.model.persistence.ServerBinary;
import org.thehellnet.utility.PathUtility;
import org.thehellnet.utility.StoppableThread;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class ServerBinaryRunner extends StoppableThread {

    private static final Logger logger = LoggerFactory.getLogger(ServerBinaryRunner.class);

    private final Server server;

    private Process process = null;

    private BufferedReader reader;
    private BufferedWriter writer;

    public ServerBinaryRunner(Server server) {
        this.server = server;
    }

    @Override
    protected void preStart() {
        super.preStart();

        ServerBinary serverBinary = server.getServerBinary();

        File workingDirectory = new File(serverBinary.getBaseDirectory());
        String binaryExecutable = PathUtility.absolute(workingDirectory, serverBinary.getExecutable());

        List<String> command = Arrays.asList(
                binaryExecutable,
                "+set", "dedicated", "2",
                "+set", "net_ip", server.getAddress(),
                "+set", "net_port", String.format("%d", server.getPort()),
                "+set", "fs_game", "mods/pml220",
                "+exec", "thehellnet",
                "+exec", "thehellnet-promod",
                "+set", "rcon_password", server.getRconPassword(),
                "+map_rotate"
        );

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
    protected void postStop() {
        super.postStop();

        try {
            reader.close();
        } catch (IOException ignored) {
        }

        reader = null;

        try {
            writer.close();
        } catch (IOException ignored) {
        }

        writer = null;

        process.destroyForcibly();

        try {
            process.waitFor();
        } catch (InterruptedException ignored) {
        }

        process = null;
    }

    @Override
    protected void job() throws Throwable {
        String line = reader.readLine();
        if (line == null) {
            return;
        }

        logger.debug(line);
    }
}
