package com.exopolus.corecom.dht;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Random;

import org.zeromq.ZMQ;

public class DHT {
    RoutingTable routes;
    String networkId;

    public DHT(String networkId) {
        this.routes = new RoutingTable(new Contact());
        this.networkId = networkId;
        ZMQ.Context context = ZMQ.context(1);

        //  Socket to talk to server
        System.out.println("Connecting to hello world server");

        ZMQ.Socket socket = context.socket(ZMQ.REQ);
        String location = "tcp://localhost:" + getAvailablePort();
        socket.connect(location);

        for (int requestNbr = 0; requestNbr != 10; requestNbr++) {
            String request = "Hello";
            System.out.println("Sending Hello " + requestNbr);
            socket.send(request.getBytes(ZMQ.CHARSET), 0);

            byte[] reply = socket.recv(0);
            System.out.println("Received " + new String(reply, ZMQ.CHARSET) + " " + requestNbr);
        }

        socket.close();
        context.term();

    }

    private static int getAvailablePort() {
        int port = 0;
        do {
            port = new Random().nextInt(20000) + 10000;
        } while (!isPortAvailable(port));

        return port;
    }

    private static boolean isPortAvailable(final int port) {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            return true;
        } catch (final IOException e) {
        } finally {
            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e2) {
                }
            }
        }

        return false;
    }

}
