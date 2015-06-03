package client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.Message;
import com.User;

public class ChatRoom extends JFrame {

	private Client client;
	private User dest;
	private ChatPanel chatPanel;
	private Thread thread;
	private JPanel profile;
	private JLabel destName;

	public ChatRoom(Client client, User dest) {
		this.client = client;
		this.dest = dest;

		chatPanel = new ChatPanel();
		profile = new JPanel();
		destName = new JLabel(dest.getFirstName() + " " + dest.getLastName());

		profile.add(destName, BorderLayout.WEST);
		// profile.add(,BorderLayout.EAST)

		setLayout(new BorderLayout());
		add(profile, BorderLayout.NORTH);
		add(chatPanel, BorderLayout.CENTER);

		setVisible(true);
		setSize(500, 500);
	}

	private class ChatPanel extends JPanel {

		private JTextArea messagePane;
		private JTextField message;
		private JButton send;
		private JPanel sendArea;
		public ChatPanel() {

			message = new JTextField();
			messagePane = new JTextArea();
			send = new JButton("Send");
			sendArea = new JPanel();
			
			Styling.makeStyledButton(send);

			setLayout(new BorderLayout());

			Runnable r = () -> {
				while (true) {

					Message msg = Message.recieveMessage(client.getSocket());
					messagePane.append("\n" + ChatRoom.this.dest.getFirstName()
							+ ": " + msg.getMessage());
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
			
			sendArea.setLayout(new BorderLayout());
			sendArea.add(send,BorderLayout.EAST);
			sendArea.add(message,BorderLayout.CENTER);
			add(messagePane, BorderLayout.CENTER);
			add(sendArea, BorderLayout.SOUTH);

			send.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					new Message(Message.SEND, client.getUser().getUsername(),
							dest.getUsername(), message.getText()).send(client
							.getSocket());
					messagePane.append("\n" + ChatRoom.this.client.getUser().getFirstName()+ ": " + message.getText());
					message.setText("");

				}
			});

		}
	}

}
