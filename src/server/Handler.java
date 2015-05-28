package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

import com.Message;
import com.User;

public class Handler implements Runnable {

	private Socket socket;

	public Handler(Socket socket) {
		this.socket = socket;
		ClientsDatabase.getClientsDatabase().add(this);
		;
	}

	@Override
	public void run() {
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
				if (UsersDatabase.getUsersDataBase()
						.isValid(username, password)) {
					new Message(Message.LOGIN, Message.SERVER, "CLIENT",
							"SUCCESS").send(socket);
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

				boolean added = UsersDatabase.getUsersDataBase()
						.addUser(
								new User(firstName, lastName, username,
										password), true);

				if (!added) {
					// TODO some codes.
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

	public boolean isRunning() {
		return !socket.isClosed();
	}

}
