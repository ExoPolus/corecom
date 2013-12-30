package com.exopolus.corecom.dht;

public class MessageHeader {
    private Contact sender;
	private String networkId;
	
    public Contact getSender() {
		return sender;
	}
	protected void setSender(Contact sender) {
		this.sender = sender;
	}
	public String getNetworkId() {
		return networkId;
	}
	protected void setNetworkId(String networkId) {
		this.networkId = networkId;
	}
}