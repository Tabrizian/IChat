package com;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;

public class Message implements Serializable{

	public static final int SEND = 0;
	public static final int DELIVERED = 1;
	public static final int LOGIN = 2;
	public static final int SIGNUP = 3;
	public static final int AUTH = 4;
	public static final int USERNAME = 5;
	public static final String SERVER = "SERVER";
	public static final String CLIENT = "CLIENT";
	
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
			OutputStream out = socket.getOutputStream();
			ObjectOutputStream obj = new ObjectOutputStream(out);
			obj.writeObject(this);
			out.flush();
			
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
	
	public static Message recieveMessage(Socket socket){
		Message message = null;
		try {
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			message = (Message) in.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}

}
