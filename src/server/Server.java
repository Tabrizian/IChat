package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import client.LoginFrame;

public class Server {

	public static void main(String[] args) {

		try {
			ServerSocket ss = new ServerSocket(1373);
			while (true) {
				Socket client = ss.accept();
				new Thread(new Handler(client)).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
