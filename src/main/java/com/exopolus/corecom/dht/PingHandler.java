package com.exopolus.corecom.dht;

public class PingHandler extends MessageHandler<PingRequest, PingResponse>{

    public PingHandler(DHT dht) {
        super(dht);
    }

    @Override
    public void onMessage(PingRequest request, PingResponse response) {
        System.out.println("**** Ping from " + request.sender.getId());
    }

}
