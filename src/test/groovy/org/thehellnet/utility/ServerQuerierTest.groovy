package org.thehellnet.utility

import org.thehellnet.utility.exception.ServerQuerierException
import spock.lang.Specification

class ServerQuerierTest extends Specification {

    private static final String SERVER_ADDRESS = "127.0.0.1"
    private static final int SERVER_PORT = 28960
    private static final String SERVER_RCONPASSWORD = "test"

    private static DatagramSocket datagramSocket
    private static Thread socketThread
    private static boolean socketKeepRunning

    private ServerQuerier serverQuerier

    def setupSpec() {
        datagramSocket = new DatagramSocket(SERVER_PORT)

        socketKeepRunning = true

        socketThread = new Thread(() -> {
            try {
                while (socketKeepRunning) {
                    byte[] rxBuffer = new byte[8192]
                    DatagramPacket rxPacket = new DatagramPacket(rxBuffer, rxBuffer.length)
                    datagramSocket.receive(rxPacket)

                    byte[] txBuffer = rxPacket.data
                    DatagramPacket txPacket = new DatagramPacket(txBuffer, 0, rxPacket.length, rxPacket.address, rxPacket.port)
                    datagramSocket.send(txPacket)
                }
            } catch (SocketException ignored) {
            }
        })
        socketThread.start()
    }

    def cleanupSpec() {
        socketKeepRunning = false
        datagramSocket.close()
        socketThread.interrupt()
        socketThread.join()
    }

    def setup() {
        serverQuerier = new ServerQuerier(SERVER_ADDRESS, SERVER_PORT, SERVER_RCONPASSWORD)
    }

    def cleanup() {
        serverQuerier.close()
    }

    def "status without open"() {
        when:
        serverQuerier.status()

        then:
        thrown ServerQuerierException
    }

    def "status closed"() {
        given:
        serverQuerier.open()
        serverQuerier.close()

        when:
        serverQuerier.status()

        then:
        thrown ServerQuerierException
    }

    def "status opened"() {
        given:
        serverQuerier.open()

        when:
        String response = serverQuerier.status()

        then:
        noExceptionThrown()
        response == "rcon ${SERVER_RCONPASSWORD} status"
    }
}
