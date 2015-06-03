package client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	private File thisHistory;
	private File destHistory;
	private String destFile;
	private String thisFile;
	private boolean destIsOnline;

	public ChatRoom(Client client, User dest) {
		destFile = "data/UserData/" + dest.getUsername() + "/"
				+ client.getUser().getUsername();
		thisFile = "data/UserData/" + client.getUser().getUsername() + "/"
				+ dest.getUsername();
		this.client = client;
		this.dest = dest;
		thisHistory = new File(thisFile);
		if (!thisHistory.isFile()) {
			try {
				new File(thisFile).createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		destHistory = new File(destFile);
		if (!destHistory.isFile()) {
			try {
				new File(thisFile).createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		chatPanel = new ChatPanel();
		profile = new JPanel();
		destName = new JLabel(dest.getFirstName() + " " + dest.getLastName());

		chatPanel.refreshStatus();
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

			Styling.makeStyledButton(send);

			setLayout(new BorderLayout());

			try {
				init();
			} catch (Exception e1) {
			}

			Runnable r = () -> {
				synchronized (this) {

					while (true) {
						try {
							wait();
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						Message msg = messageCenter.getMessage(Message.SEND);

						String payam = "\n" + ChatRoom.this.dest.getFirstName()
								+ ": " + msg.getMessage();
						messagePane.append(payam);
						writeMessage(payam);
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}
			};

			thread = new Thread(r);
			thread.start();

			sendArea.setLayout(new BorderLayout());
			sendArea.add(send, BorderLayout.EAST);
			sendArea.add(message, BorderLayout.CENTER);
			add(messagePane, BorderLayout.CENTER);
			add(sendArea, BorderLayout.SOUTH);

			send.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					messageCenter.sendMessage((new Message(Message.SEND, client
							.getUser().getUsername(), dest.getUsername(),
							message.getText())));
					String payam = "\n"
							+ ChatRoom.this.client.getUser().getFirstName()
							+ ": " + message.getText();
					messagePane.append(payam);
					writeMessage(payam);
					message.setText("");

				}
			});

		}

		public void writeMessage(String text) {
			try {
				OutputStream out1 = new FileOutputStream(thisHistory, true);

				PrintWriter pw1 = new PrintWriter(out1);
				pw1.print(text);
				out1.flush();
				pw1.flush();
				out1.close();
				pw1.close();
				chatPanel.refreshStatus();
				if (!destIsOnline) {
					OutputStream out2 = new FileOutputStream(destHistory, true);

					PrintWriter pw2 = new PrintWriter(out2);
					pw2.print(text);
					out2.flush();
					pw2.flush();
					out2.close();
					pw2.close();
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void init() {
			try {
				InputStream in = new FileInputStream(thisHistory);
				Scanner scanner = new Scanner(in);
				String input;
				while ((input = scanner.nextLine()) != null) {
					messagePane.append(input + "\n");
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void refreshStatus() {
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
