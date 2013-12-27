package com.exopolus.corecom.dht;

public class ContactRecord extends Addressable {
    private Contact node;

    public ContactRecord(Contact value, byte[] xor) {
        super(xor);
        this.node = value;
    }
}
