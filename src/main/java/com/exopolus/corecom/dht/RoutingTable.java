package com.exopolus.corecom.dht;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class RoutingTable {
    private Contact node;
    // TODO: This isn't the best data structure for this. But no optimize now.
    private ArrayList[] buckets = new ArrayList[DHTConstants.ID_LENGTH * 8];

    public RoutingTable(Contact id) {
        this.setNode(id);
        for (int i = 0; i < DHTConstants.ID_LENGTH * 8; i++) {
            buckets[i] = new ArrayList<>();
        }
    }

    public void update(Contact contact) {
        int prefixLength = Contact.getPrefixLength(contact.xor(getNode()));
        ArrayList bucket = buckets[prefixLength];
        Contact found = null;
        for (int i = 0; i < bucket.size(); i++) {
            Contact el = (Contact) bucket.get(i);
            if (el.equals(getNode())) {
                found = contact;
                break;
            }
        }

        if (found == null) {
            if (bucket.size() <= DHTConstants.BUCKET_SIZE) {
                bucket.add(0, found);
            } else {
                System.out.println("**** Routing table needs GC ");
            }
        }
    }

    private void copyToVector(Iterator start, Contact end, ArrayList vector, Contact target) {
        for (Iterator elt = start; elt.hasNext();) {
            Contact value = (Contact) elt.next();
            if (value == end){
                break;
            }
            vector.add(0, new ContactRecord(value, value.xor(target)));
        }
    }
    
    public ArrayList findClosest(Contact target, int count){
        ArrayList ret = new ArrayList<>();
        
        int bucket_num = Addressable.getPrefixLength(target.xor(this.getNode()));
        ArrayList bucket = buckets[bucket_num];
        copyToVector(bucket.iterator(), null, ret, target);
        
        for (int i = 1; (bucket_num-i >=0 || bucket_num+i < DHTConstants.ID_LENGTH * 8) && ret.size() < count; i++) {
            if (bucket_num -i >= 0) {
                bucket = this.buckets[bucket_num - i];
                copyToVector(bucket.iterator(), null, ret, target);
            }
            
            if (bucket_num + i < DHTConstants.ID_LENGTH * 8){
                bucket = this.buckets[bucket_num + i];
                copyToVector(bucket.iterator(), null, ret, target);
            }
        }
        
        Collections.sort(ret);
        if (ret.size() > count){
            ret = new ArrayList(ret.subList(0, count));
        }
        
        return ret;
    }

    public Contact getNode() {
        return node;
    }

    public void setNode(Contact node) {
        this.node = node;
    }
}
