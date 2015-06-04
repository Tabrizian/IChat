package client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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

	private boolean destIsOnline;
	private boolean good = true;

	public ChatRoom(Client client, User dest) {

		this.client = client;
		this.dest = dest;
		chatPanel = new ChatPanel();
		profile = new JPanel();
		destName = new JLabel(dest.getFirstName() + " " + dest.getLastName());
		chatPanel.refreshStatus();
		WindowListener listener = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				good = false;
			}
		};
		addWindowListener(listener);
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
		private MessageCenter messageCenter = MessageCenter
				.getMessageCenter(client.getSocket());

		public ChatPanel() {

			message = new JTextField();
			messagePane = new JTextArea();
			send = new JButton("Send");
			sendArea = new JPanel();
			JScrollPane js = new JScrollPane(messagePane);

			Styling.makeStyledButton(send);

			setLayout(new BorderLayout());

			init();

			Runnable r = () -> {

				while (good) {
					System.out.println("Running");

					for (int i = 0; i < messageCenter.getMessages().size(); i++) {
						Message message = messageCenter.getMessages().get(i);
						if (message.getVerb().equals(Message.SEND)) {

							String payam = ChatRoom.this.dest.getFirstName()
									+ ": " + message.getMessage();
							messagePane.append("\n" + payam);
							messageCenter.getMessages().remove(i);
							writeMessage(payam);

						}

					}

					try {
						Thread.sleep(500);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}

			};

			thread = new Thread(r);

			thread.start();

			sendArea.setLayout(new BorderLayout());
			sendArea.add(send, BorderLayout.EAST);
			sendArea.add(message, BorderLayout.CENTER);
			add(js, BorderLayout.CENTER);
			add(sendArea, BorderLayout.SOUTH);

			send.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					refreshStatus();
					if (destIsOnline) {
						messageCenter.sendMessage((new Message(Message.SEND,
								client.getUser().getUsername(), dest
										.getUsername(), message.getText())));
					}
					String payam = ChatRoom.this.client.getUser()
							.getFirstName() + ": " + message.getText();
					messagePane.append("\n" + payam);
					writeMessage(payam);
					message.setText("");

				}
			});

		}

		public void writeMessage(String text) {

			chatPanel.refreshStatus();
			if (!destIsOnline) {
				messageCenter.sendMessage(new Message(Message.HISTORY,
						"data/UserData/" + client.getUser().getUsername() + "/"
								+ dest.getUsername(), "data/UserData/"
								+ dest.getUsername() + "/"
								+ client.getUser().getUsername(), text));

			} else {
				messageCenter.sendMessage(new Message(Message.HISTORY,
						"data/UserData/" + client.getUser().getUsername() + "/"
								+ dest.getUsername(), "", text));
			}
		}

		public void init() {
			messageCenter.sendMessage(new Message(Message.INITHISTORY,
					Message.CLIENT, Message.SERVER, "data/UserData/"
							+ client.getUser().getUsername() + "/"
							+ dest.getUsername()));
			Message message = messageCenter.getMessage(Message.INITHISTORY);
			String[] tokens = message.getMessage().split("\\+");
			System.out.println(tokens.length);
			for (String string : tokens) {
				messagePane.append(string + "\n");
			}
		}

		public void refreshStatus() {
			System.out.println(dest.getUsername());
			messageCenter.sendMessage(new Message(Message.STATUS,
					Message.CLIENT, Message.SERVER, dest.getUsername()));
			Message msg = messageCenter.getMessage(Message.STATUS);
			if (msg.getMessage().equals("true")) {
				JOptionPane.showMessageDialog(null, "Is online.");
				destIsOnline = true;
			} else {
				JOptionPane.showMessageDialog(null, "Is offline");
				destIsOnline = false;
			}
		}
	}

}
