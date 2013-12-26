package com.exoself.corecom.dht;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class RoutingTable {
    private NodeID id;
    // TODO: This isn't the best data structure for this. But no optimize now.
    private ArrayList[] buckets = new ArrayList[DHTConstants.ID_LENGTH * 8];

    public RoutingTable(NodeID id) {
        this.id = id;
        for (int i = 0; i < DHTConstants.ID_LENGTH * 8; i++) {
            buckets[i] = new ArrayList<>();
        }
    }

    public void update(Contact contact) {
        int prefixLength = contact.getId().xor(id).getPrefixLength();
        ArrayList bucket = buckets[prefixLength];
        Contact found = null;
        for (int i = 0; i < bucket.size(); i++) {
            Contact el = (Contact) bucket.get(i);
            if (el.getId().equals(id)) {
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

    private void copyToVector(Iterator start, Contact end, ArrayList vector, NodeID target) {
        for (Iterator elt = start; elt.hasNext();) {
            Contact value = (Contact) elt.next()
            if (value == end){
                break;
            }
            vector.add(0, new ContactRecord(value, value.getId().xor(target)));
        }
    }
    
    public ArrayList findClosest(NodeID target, int count){
        ArrayList ret = new ArrayList<>();
        
        int bucket_num = target.xor(this.id).getPrefixLength();
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
}
