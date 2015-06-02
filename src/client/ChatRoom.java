package client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import com.Message;
import com.User;

public class ChatRoom extends JFrame {

	private Client client;
	private User dest;
	private ChatPanel chatPanel;
	private Thread thread;

	public ChatRoom(Client client, User dest) {
		this.client = client;
		this.dest = dest;

		chatPanel = new ChatPanel();

		setVisible(true);
		setSize(500, 500);
		setLayout(new BorderLayout());
		add(chatPanel, BorderLayout.CENTER);
	}

	private class ChatPanel extends JPanel {

		private JTextArea messagePane;
		private JTextField message;
		private JButton send;

		public ChatPanel() {

			message = new JTextField();
			messagePane = new JTextArea();
			send = new JButton("Send");

			setLayout(new BorderLayout());

			Runnable r = () -> {
				while (true) {

					Message msg = Message.recieveMessage(client.getSocket());

					messagePane.append(msg.getMessage());
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			thread = new Thread(r);
			thread.start();
			add(messagePane, BorderLayout.CENTER);
			add(message, BorderLayout.SOUTH);
			add(send, BorderLayout.NORTH);

			send.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					new Message(Message.SEND, client.getUser().getUsername(),
							dest.getUsername(), message.getText()).send(client
							.getSocket());

					message.setText("");

				}
			});

		}

	}

}
