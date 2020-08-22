package org.thehellnet.utility

import org.thehellnet.utility.exception.ServerQuerierException
import spock.lang.Specification

class ServerQuerierTest extends Specification {

    private static final class UdpEchoListener extends StoppableThread {

        private DatagramSocket datagramSocket

        @Override
        protected void preStart() {
            super.preStart()
            datagramSocket = new DatagramSocket(SERVER_PORT)
        }

        @Override
        protected void preStop() {
            super.preStop()
            datagramSocket.close()
        }

        @Override
        protected void job() throws Throwable {
            byte[] rxBuffer = new byte[8192]
            DatagramPacket rxPacket = new DatagramPacket(rxBuffer, rxBuffer.length)
            datagramSocket.receive(rxPacket)

            byte[] txBuffer = rxPacket.data
            DatagramPacket txPacket = new DatagramPacket(txBuffer, 0, rxPacket.length, rxPacket.address, rxPacket.port)
            datagramSocket.send(txPacket)
        }
    }

    private static final String SERVER_ADDRESS = "127.0.0.1"
    private static final int SERVER_PORT = 28960
    private static final String SERVER_RCONPASSWORD = "test"

    private UdpEchoListener udpEchoListener
    private ServerQuerier serverQuerier

    def setup() {
        udpEchoListener = new UdpEchoListener()
        serverQuerier = new ServerQuerier(SERVER_ADDRESS, SERVER_PORT, SERVER_RCONPASSWORD)
    }

    def cleanup() {
        serverQuerier.close()
        udpEchoListener.stop()
    }

    def "open with wrong address"() {
        given:
        String inputAddress = "#####";
        int inputPort = 28960
        String inputRconPassword = "rconPassword"

        ServerQuerier querier = new ServerQuerier(inputAddress, inputPort, inputRconPassword)

        when:
        querier.open()

        then:
        thrown ServerQuerierException
    }

    def "status without open"() {
        when:
        serverQuerier.status()

        then:
        thrown ServerQuerierException
    }

    def "status closed"() {
        given:
        udpEchoListener.start()
        serverQuerier.open()
        serverQuerier.close()
        udpEchoListener.stop()

        when:
        serverQuerier.status()

        then:
        thrown ServerQuerierException
    }

    def "status opened"() {
        given:
        udpEchoListener.start()
        serverQuerier.open()

        when:
        String response = serverQuerier.status()

        then:
        noExceptionThrown()
        response == "rcon ${SERVER_RCONPASSWORD} status"
    }
}
