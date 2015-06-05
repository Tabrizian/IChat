package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) {

		try {
			ServerSocket ss = new ServerSocket(1373);
			while (true) {
				if (ClientsDatabase.getClientsDatabase().sizeIsGood()) {
					Socket client = (ss.accept());
					new Thread(new Handler(client)).start();
				}
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IOException e) {

			// e.printStackTrace();
		}

	}
}
