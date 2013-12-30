package com.exopolus.corecom.dht;

public class PingAction {
    public static class PingRequest extends MessageHeader {
    }

    public static class PingResponse extends MessageHeader {
    }

    public static class PingHandler extends MessageHandler<PingRequest, PingResponse> {

        public PingHandler(DHT dht) {
            super(dht);
        }

        @Override
        public void onMessage(PingRequest request, PingResponse response) {
            System.out.println("**** Ping from " + request.getSender().getId());
        }

        @Override
        public PingRequest makeRequest(byte[] data) {
            PingRequest request = new PingRequest();
            
            return request;
        }

        @Override
        public PingResponse makeResponse(byte[] data) {
            PingResponse response = new PingResponse();
            response.setNetworkId(getDHT().getNetworkId());
            response.setSender(getDHT().getRoutes().getNode());
            return response;
        }

    }
}
