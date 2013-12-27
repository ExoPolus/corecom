package com.exopolus.corecom;

import com.exopolus.corecom.dht.Contact;
import com.exopolus.corecom.dht.DHT;

public class CoreCom {
    private static final String NETWORK_ID = "CORECOM_01";
    private DHT dht; 
    public CoreCom() {
        dht = new DHT(NETWORK_ID);
    }
}
