package client;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import com.User;

public class Client implements Serializable {

	private Socket socket;
	private static final String server = "Iman";
	private User user = null;

	private Client() {
		boolean run = false;
		int yes = 0;
		while (!run && yes == 0) {
			try {
				socket = (new Socket(InetAddress.getByName(server), 1373));
				socket.getOutputStream();
				socket.getInputStream();
				new Thread(MessageCenter.getMessageCenter(socket)).start();
				new LoginFrame(this);
				run = true;
			} catch (Exception e) {
				yes = JOptionPane.showConfirmDialog(null, "Server is not started! Do you want to try again?",
						"Error", JOptionPane.ERROR_MESSAGE);
				System.out.println(yes);
			}
		}
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
