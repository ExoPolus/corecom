package com.exoself.corecom.dht;

public class ContactRecord implements Comparable<ContactRecord> {
    private Contact node;
    private NodeID sortkey;

    public ContactRecord(Contact value, NodeID xor) {
        this.node = value;
        this.sortkey = xor;
    }

    @Override
    public int compareTo(ContactRecord o) {
        return node.getId().compareTo(o.sortkey);
    }
    
}
