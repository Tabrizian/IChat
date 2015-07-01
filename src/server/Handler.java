package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.NoSuchElementException;
import java.util.Scanner;

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
				message = Message.recieveMessage(socket);

				switch (message.getVerb()) {

				case Message.SEND: {
					User usr = UsersDatabase.getUsersDataBase().getUser(
							message.getDest_ID());
					message.send(ClientsDatabase.getClientsDatabase()
							.getHandler(usr).getSocket());
					break;
				}
				case Message.DELIVERED: {
					User usr = UsersDatabase.getUsersDataBase().getUser(
							message.getDest_ID());
					message.send(ClientsDatabase.getClientsDatabase()
							.getHandler(usr).getSocket());
					break;
				}
				case Message.LOGIN: {
					String[] tokens;
					tokens = message.getMessage().split(",");
					String username = tokens[0];
					String password = tokens[1];

					if (UsersDatabase.getUsersDataBase().isValid(username,
							password)
							&& !ClientsDatabase.getClientsDatabase()
									.isAvailable(username)) {
						new Message(Message.LOGIN, Message.SERVER,
								Message.CLIENT, "SUCCESS").send(socket);
						User user = UsersDatabase.getUsersDataBase().getUser(
								username);
						new Message(Message.LOGIN, Message.SERVER,
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
						new Message(Message.SIGNUP, Message.SERVER,
								Message.CLIENT, "false").send(socket);
					} else {
						new Message(Message.SIGNUP, Message.SERVER,
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

				case Message.STATUS: {
					if (ClientsDatabase.getClientsDatabase().isAvailable(
							message.getMessage()))
						new Message(Message.STATUS, Message.SERVER,
								Message.CLIENT, "true").send(socket);
					else
						new Message(Message.STATUS, Message.SERVER,
								Message.CLIENT, "false").send(socket);
					break;
				}
				case Message.HISTORY: {
					if ("".equals(message.getDest_ID())) {
						File thisHistory = new File(message.getSource_ID());

						if (!thisHistory.isFile()) {
							try {
								new File(message.getSource_ID())
										.createNewFile();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						try {
							OutputStream out1 = new FileOutputStream(
									thisHistory, true);

							PrintWriter pw1 = new PrintWriter(out1);
							pw1.println(message.getMessage());
							out1.flush();
							pw1.flush();
							out1.close();
							pw1.close();
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						File destHistory = new File(message.getDest_ID());
						File thisHistory = new File(message.getSource_ID());
						if (!destHistory.isFile()) {
							try {
								new File(message.getDest_ID()).createNewFile();
							} catch (IOException e) {
								System.out.println(message.getSource_ID());
								e.printStackTrace();
							}
						}
						if (!thisHistory.isFile()) {
							try {
								new File(message.getSource_ID())
										.createNewFile();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						try {
							OutputStream out1 = new FileOutputStream(
									thisHistory, true);

							PrintWriter pw1 = new PrintWriter(out1);
							pw1.println(message.getMessage());
							out1.flush();
							pw1.flush();
							out1.close();
							pw1.close();

							OutputStream out2 = new FileOutputStream(
									destHistory, true);

							PrintWriter pw2 = new PrintWriter(out2);
							pw2.println(message.getMessage());
							out2.flush();
							pw2.flush();
							out2.close();
							pw2.close();
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;

				}
				case Message.FILELIST: {
					try {
						String str = message.getMessage();
						String[] list = new File(str).list();
						String msg = null;
						if (list.length > 0)
							msg = list[0];
						for (int i = 1; i < list.length; i++) {
							msg = msg + "," + list[i];
						}
						if (msg == null) {
							msg = "-";
						}
						new Message(Message.FILELIST, Message.CLIENT,
								Message.SERVER, msg).send(socket);
					} catch (Exception e) {

					}
					break;
				}

				case Message.INITHISTORY: {

					StringBuilder str = new StringBuilder("");
					Scanner scanner = null;
					InputStream in = null;
					try {
						System.out.println(message.getMessage());
						File file = new File(message.getMessage());
						if (file.isFile()) {

							try {
								in = new FileInputStream(file);
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							scanner = new Scanner(in);
							String input;
							while ((input = scanner.nextLine()) != null) {
								str.append(input);
								str.append('+');
							}

							new Message(Message.INITHISTORY, Message.SERVER,
									Message.CLIENT, str.toString())
									.send(socket);

							scanner.close();
						} else {
							new Message(Message.INITHISTORY, Message.SERVER,
									Message.CLIENT, "").send(socket);
						}
					} catch (NoSuchElementException e) {
						// TODO Auto-generated catch block
						new Message(Message.INITHISTORY, Message.SERVER,
								Message.CLIENT, str.toString()).send(socket);
						scanner.close();

					}
					break;
				}
				}
			} catch (Exception e) {
				ClientsDatabase.getClientsDatabase().remove(this);
				break;
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
