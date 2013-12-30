package com.exopolus.corecom.dht;

public abstract class MessageHandler<T extends MessageHeader, P extends MessageHeader> {
    private DHT dht;

    public MessageHandler(DHT dht) {
        this.dht = dht;
    }

    public P handleRequest(byte[] data) {
        T request = makeRequest(data);
        P response = makeResponse(data);

        if (!request.getNetworkId().equals(this.getDHT().getNetworkId())) {
            throw new DHTMessageException("Expected network ID " + this.getDHT().getNetworkId() + ", got "
                    + request.getNetworkId());
        }

        if (request.getSender() != null) {
            this.getDHT().getRoutes().update(request.getSender());
        }

        response.setSender(this.getDHT().getRoutes().getNode());

        onMessage(request, response);
        
        return response;
    }

    public DHT getDHT() {
        return dht;
    }

    public abstract void onMessage(T request, P response);

    public abstract T makeRequest(byte[] data);

    public abstract P makeResponse(byte[] data);
}