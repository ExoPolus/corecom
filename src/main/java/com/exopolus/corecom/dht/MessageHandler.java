package com.exopolus.corecom.dht;

public abstract class MessageHandler<T extends MessageHeader, P extends MessageHeader> {
    private DHT dht;
    public MessageHandler(DHT dht) {
        this.dht = dht;
    }
    
    public void handleRequest(T request, P response){
        if(!request.networkId.equals(this.getDHT().networkId)){
            throw new DHTMessageException("Expected network ID " + this.getDHT().networkId + ", got " + request.networkId);
        }
        
        if(request.sender != null){
            this.getDHT().routes.update(request.sender);
        }
        
        response.sender = this.getDHT().routes.getNode();
        onMessage(request, response);
        
    }

    public abstract void onMessage(T request, P response);

    public DHT getDHT() {
        return dht;
    }
}
