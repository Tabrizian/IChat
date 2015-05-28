package com;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Message {

	public static final int SEND = 0;
	public static final int DELIVERED = 1;
	public static final int LOGIN = 2;
	public static final int SIGNUP = 3;
	public static final String SERVER = "SERVER";
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
	
	public void send(Socket socket){
		try {
			ObjectOutputStream obj = new ObjectOutputStream(socket.getOutputStream());
			obj.writeObject(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getVerb() {
		return verb;
	}

	public String getSource_ID() {
		return source_ID;
	}

	public String getDest_ID() {
		return dest_ID;
	}

	public int getLength() {
		return length;
	}

	public String getMessage() {
		return message;
	}

}
