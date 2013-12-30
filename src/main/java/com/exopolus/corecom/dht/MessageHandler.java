package com.exopolus.corecom.dht;

public abstract class MessageHandler<T extends MessageHeader, P extends MessageHeader> {
    private DHT dht;
    
    public MessageHandler(DHT dht) {
        this.dht = dht;
    }
    
    public void handleRequest(T request, P response){
        if(!request.getNetworkId().equals(this.getDHT().getNetworkId())){
            throw new DHTMessageException("Expected network ID " + this.getDHT().getNetworkId() + ", got " + request.getNetworkId());
        }
        
        if(request.getSender() != null){
            this.getDHT().getRoutes().update(request.getSender());
        }
        
        response.setSender(this.getDHT().getRoutes().getNode());
        onMessage(request, response);
        
    }

    public abstract void onMessage(T request, P response);

    public DHT getDHT() {
        return dht;
    }
}
