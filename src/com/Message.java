package com;

public class Message {

	private static final int SEND = 0;
	private static final int DELIVERED = 1;
	private int verb;
	private String source_ID;
	private String dest_ID;
	private int length;
	private String message;

	public Message(int verb, String source_ID, String dest_ID, String message) {
		this.verb = verb;
		this.source_ID = source_ID;
		this.dest_ID = dest_ID;
		this.message = message;
		length = message.length();
	}

}
