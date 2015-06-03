package client;

import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;

import com.Message;

public class MessageCenter implements Runnable {

	private static MessageCenter instance = null;
	private Socket socket;
	private Vector<Message> messages;
	private boolean flag = true;

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
		synchronized (this) {

			while (true) {

				Message message = Message.recieveMessage(socket);
				if (message.getVerb().equals(Message.SEND)) {
					notify();
				}
				messages.add(message);

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
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

}
