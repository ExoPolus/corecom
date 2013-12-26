package com.exoself.corecom.dht;

import java.util.Random;

import javax.xml.bind.DatatypeConverter;

public class NodeID implements Comparable<NodeID> {
    private byte[] id;
    
    protected NodeID() {
        id = createRandomId();
    }
    
    private NodeID(byte[] id){
        this.id = id;
    }

    private static byte[] createRandomId() {
        Random r = new Random();
        byte[] id = new byte[DHTConstants.ID_LENGTH];
        for (int i = 0; i < DHTConstants.ID_LENGTH; i++) {
            id[i] = (byte) r.nextInt(256);
        }
        return id;
    }

    private static byte[] idFromString(String data) {
        return DatatypeConverter.parseHexBinary(data);
    }

    private static String idToString(byte[] data) {
        return DatatypeConverter.printHexBinary(data);
    }

    public boolean equals(NodeID other) {
        for (int i = 0; i < DHTConstants.ID_LENGTH; i++) {
            if (this.id[i] != other.id[i]) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int compareTo(NodeID other) {
        for (int i = 0; i < DHTConstants.ID_LENGTH; i++) {
            if (this.id[i] != other.id[i]) {
                if (this.id[i] < other.id[i]) {
                    return -1;
                } else {
                    return 1;
                }
            }
        }
        return 0;
    }

    protected NodeID xor(NodeID other) {
        byte[] id = new byte[DHTConstants.ID_LENGTH];
        for (int i = 0; i < DHTConstants.ID_LENGTH; i++) {
            id[i] = (byte) (this.id[i] ^ other.id[i]);
        }
        return new NodeID(id);
    }
    
    protected int getPrefixLength(){
        for (int i = 0; i < DHTConstants.ID_LENGTH; i++) {
            for (int j = 0; j < 8; j++) {
                // TODO: Is this right? \/\/\/\/\/\/\/\/\/\/
                if (((this.id[i] >> (7 - j)) & 0x1) != 0){
                    return i * 8 + j;
                }
            }
        }
        return DHTConstants.ID_LENGTH * 8 - 1;
    }
    
    
}
