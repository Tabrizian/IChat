package com;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.Scanner;

public class Message implements Serializable {

	public static final String SEND = "0";
	public static final String DELIVERED = "1";
	public static final String LOGIN = "2";
	public static final String SIGNUP = "3";
	public static final String AUTH = "4";
	public static final String USERNAME = "5";
	public static final String ADDTOCHAT = "6";
	public static final String SERVER = "SERVER";
	public static final String CLIENT = "CLIENT";

	private String verb;
	private String source_ID;
	private String dest_ID;
	private String length;
	private String message;

	public Message(String verb, String source_ID, String dest_ID, String message) {
		this.verb = verb;
		this.source_ID = source_ID;
		this.dest_ID = dest_ID;
		this.message = message;
		length = String.valueOf(message.length());
	}

	public void send(Socket socket) {
		try {
			OutputStream out = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(out);
//			String s = verb + "|" + source_ID + "|" + dest_ID + "|" + length + "|" + message;
			pw.println(verb);
			pw.println(source_ID);
			pw.println(dest_ID);
			pw.println(message);
//			pw.println(length);
			out.flush();
			pw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getVerb() {
		return verb;
	}

	public String getSource_ID() {
		return source_ID;
	}

	public String getDest_ID() {
		return dest_ID;
	}

	public String getLength() {
		return length;
	}

	public String getMessage() {
		return message;
	}

	public static Message recieveMessage(Socket socket) {
		Message message = null;
		try {
			InputStream in = socket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String v1 = br.readLine();
			String s1 = br.readLine();
			String s2 = br.readLine();
			String s3 = br.readLine();
			message = new Message(v1, s1, s2, s3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return message;
	}

}
