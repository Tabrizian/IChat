package client;

import java.net.Socket;
import java.util.Vector;

import com.Message;

public class MessageCenter implements Runnable {

	private static MessageCenter instance = null;
	private Socket socket;
	private Vector<Message> messages;

	private MessageCenter(Socket socket) {
		messages = new Vector<>();
		this.socket = socket;
		instance = this;
	}

	public static MessageCenter getMessageCenter(Socket socket) {
		if (instance == null)
			new MessageCenter(socket);
		return instance;
	}

	@Override
	public void run() {

		while (true) {

			Message message = Message.recieveMessage(socket);

			messages.addElement(message);

		}

	}

	public Message getMessage(String verb) {

		Message msg = null;
		while (msg == null) {
			for (int i = 0; i < messages.size(); i++) {
				if (messages.get(i).getVerb().equals(verb)) {
					msg = messages.get(i);
					messages.remove(i);
				}

			}
		}
		return msg;
	}

	public void sendMessage(Message message) {
		message.send(socket);
	}

	public Vector<Message> getMessages() {
		return messages;
	}

}
