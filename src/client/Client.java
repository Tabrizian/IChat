package client;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Serializable {
	
	private Socket socket;
	private static final String server = "Iman";
	
	private Client(){
		try {
			socket = new Socket("127.0.0.1", 1373);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new LoginFrame(this);
	}
	public static void main(String[] args) {
		new Client();
		while(true){
			
		}
		
	}
	
	public Socket getSocket(){
		return socket;
	}

}
