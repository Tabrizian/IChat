package server;

import java.io.InputStream;
import java.io.ObjectInputStream;
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
		while (true) {
			try {
				Message message = null;

				InputStream in = socket.getInputStream();
				ObjectInputStream input = new ObjectInputStream(in);
				message = (Message) input.readObject();

				switch (message.getVerb()) {

				case Message.SEND: {
					User usr = UsersDatabase.getUsersDataBase().getUser(
							message.getDest_ID());
					message.send(ClientsDatabase.getClientsDatabase()
							.getHandler(usr).getSocket());
					break;
				}
				case Message.DELIVERED: {

					break;
				}
				case Message.LOGIN: {
					String[] tokens;
					tokens = message.getMessage().split(",");
					String username = tokens[0];
					String password = tokens[1];

					if (UsersDatabase.getUsersDataBase().isValid(username,
							password)) {
						new Message(Message.LOGIN, Message.SERVER,
								Message.CLIENT, "SUCCESS").send(socket);
						User user = UsersDatabase.getUsersDataBase().getUser(
								username);
						new Message(Message.USERNAME, Message.SERVER,
								Message.CLIENT, user.toString()).send(socket);
						ClientsDatabase.getClientsDatabase().add(this, user);

					} else {
						new Message(Message.LOGIN, Message.SERVER,
								Message.CLIENT, "FAILED").send(socket);
					}

					break;
				}
				case Message.SIGNUP: {
					String[] tokens;
					tokens = message.getMessage().split(",");
					String username = tokens[0];
					String password = tokens[1];
					String firstName = tokens[2];
					String lastName = tokens[3];

					boolean added = UsersDatabase.getUsersDataBase().addUser(
							new User(firstName, lastName, username, password),
							true);
					if (!added) {
						new Message(Message.AUTH, Message.SERVER,
								Message.CLIENT, "false").send(socket);
					} else {
						new Message(Message.AUTH, Message.SERVER,
								Message.CLIENT, "true").send(socket);
					}
					break;
				}
				case Message.AUTH: {
					boolean condition = (UsersDatabase.getUsersDataBase()
							.getUser(message.getMessage()) == null);
					if (!condition) {
						new Message(Message.AUTH, Message.SERVER,
								Message.CLIENT, "true").send(socket);
					} else {
						new Message(Message.AUTH, Message.SERVER,
								Message.CLIENT, "false").send(socket);
					}
					break;
				}
				case Message.USERNAME: {
					new Message(Message.USERNAME, Message.SERVER,
							Message.CLIENT, UsersDatabase.getUsersDataBase()
									.getUser(message.getMessage()).toString())
							.send(socket);

					break;
				}

				}
			} catch (Exception e) {
				ClientsDatabase.getClientsDatabase().refresh();
			}
		}

	}

	public boolean isRunning() {
		return !socket.isClosed();
	}

	public Socket getSocket() {
		return socket;
	}

}
