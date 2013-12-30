package com.exopolus.corecom.dht;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Random;

import org.zeromq.ZMQ;

import com.exopolus.corecom.dht.PingAction.PingHandler;

public class DHT {
    private RoutingTable routes;
    private String networkId;
    private int port;
    private HashMap<String, MessageHandler> handlers = new HashMap<>();

    private DHT() {
        this.routes = new RoutingTable(new Contact());
        handlers.put("ping", new PingHandler(this));
    }

    public DHT(String networkId) {
        this();
        this.networkId = networkId;
        this.port = getAvailablePort();
    }

    public DHT(int port, String networkId) {
        this();
        this.networkId = networkId;
        this.port = port;
    }

    public RoutingTable getRoutes() {
        return routes;
    }

    public void setRoutes(RoutingTable routes) {
        this.routes = routes;
    }

    public String getNetworkId() {
        return networkId;
    }

    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void attach() {
        ZMQ.Context context = ZMQ.context(1);

        // Socket to talk to clients
        ZMQ.Socket responder = context.socket(ZMQ.REP);
        responder.bind("tcp://*:" + getPort());

        while (!Thread.currentThread().isInterrupted()) {
            // Wait for next request from the client
            byte[] request = responder.recv(0);
            String messageType = new String(request).split("/")[0];

            if (messageType.contains(messageType)) {
                MessageHeader response = handlers.get(messageType).handleRequest(request);

                
                
                responder.send("OK".getBytes(), 0);
            } else {
                responder.send("ERR".getBytes(), 0);
            }
        }

        responder.close();
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

    public Contact getNode() {
        return getRoutes().getNode();
    }
}
