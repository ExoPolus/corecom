package com.exoself.corecom.dht;

public class DHT {
    RoutingTable routes;
    String networkId;
    
    public DHT(Contact self, String networkId) {
        this.routes = new RoutingTable(self.getId());
        this.networkId = networkId;
    }
}
