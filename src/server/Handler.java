package server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Handler implements Runnable {

	private Socket socket;
	
	public Handler(Socket socket) {
		this.socket = socket;
		ClientsDatabase.getClientsDatabase().add(this);;
	}
	
	@Override
	public void run() {
//		InputStream  in = socket.getInputStream();
//		OutputStream out = socket.getOutputStream();
	}
	
	public boolean isRunning(){
		return  !socket.isClosed();
	}

}
