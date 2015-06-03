package client;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.User;

public class Client implements Serializable {

	private Socket socket;
	private static final String server = "Iman";
	private User user = null;

	private Client() {
		try {
			socket = (new Socket(InetAddress.getByName(server), 1373));
			socket.getOutputStream();
			socket.getInputStream();
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

	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public Socket getSocket() {
		return socket;
	}

}
