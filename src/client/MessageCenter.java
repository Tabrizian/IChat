package client;

import java.net.Socket;
import java.util.Iterator;
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

	public Message getMessage(String verb) {
		Message msg = null;
		
			Iterator<Message> iter = messages.iterator();
			while (msg == null) {
				while (iter.hasNext()) {
					Message message = (Message) iter.next();
					if (message.getVerb().equals(verb)) {
						messages.remove(message);
						msg = message;
					}

				}
			}
		
		return msg;
	}

	public void sendMessage(Message message) {
		message.send(socket);
	}

}
