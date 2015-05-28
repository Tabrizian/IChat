package server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import com.Message;
import com.User;

public class Handler implements Runnable {

	private Socket socket;

	public Handler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		User user = null;
		boolean run = true;
		while (run) {
			try {
				InputStream in = socket.getInputStream();
				ObjectInputStream input = new ObjectInputStream(in);
				Message message = (Message) input.readObject();
				System.out.println("Hello");
				String[] tokens;
				switch (message.getVerb()) {
				case Message.LOGIN: {
					tokens = message.getMessage().split(",");
					String username = tokens[0];
					String password = tokens[1];
					if (UsersDatabase.getUsersDataBase().isValid(username,
							password)) {
						new Message(Message.LOGIN, Message.SERVER, "CLIENT",
								"SUCCESS").send(socket);
						user = UsersDatabase.getUsersDataBase().getUser(
								username);
						ObjectOutputStream out = new ObjectOutputStream(
								socket.getOutputStream());
						out.writeObject(user);
						run = false;
					} else {
						new Message(Message.LOGIN, Message.SERVER, "CLIENT",
								"FAILED").send(socket);
					}
				}
					break;

				case Message.SIGNUP: {
					tokens = message.getMessage().split(",");
					String username = tokens[0];
					String password = tokens[1];
					String firstName = tokens[2];
					String lastName = tokens[3];

					boolean added = UsersDatabase.getUsersDataBase().addUser(
							new User(firstName, lastName, username, password),
							true);

					if (!added) {
						
					}
				}
					break;
				default:
					break;
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String username = null;
		PrintWriter pw = null;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			username = br.readLine();
			pw = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ClientsDatabase.getClientsDatabase().add(this,user);
		if (UsersDatabase.getUsersDataBase().getUser(username) != null) {
			pw.write("true");
		} else {
			pw.write("false");
		}

	}

	public boolean isRunning() {
		return !socket.isClosed();
	}

}
