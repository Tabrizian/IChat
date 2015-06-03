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
	private File history;

	public ChatRoom(Client client, User dest) {
		this.client = client;
		this.dest = dest;
		history = new File("data/UserData/" + client.getUser().getUsername()
				+ "/" + dest.getUsername());
		if (!history.isFile()) {
			try {
				new File("data/UserData/" + client.getUser().getUsername()
						+ "/" + dest.getUsername()).createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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

			try {
				init();
			} catch (Exception e1) {
			}

			Runnable r = () -> {
				while (true) {

					Message msg = Message.recieveMessage(client.getSocket());
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
					new Message(Message.SEND, client.getUser().getUsername(),
							dest.getUsername(), message.getText()).send(client
							.getSocket());
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
				OutputStream out = new FileOutputStream(history, true);
				PrintWriter pw = new PrintWriter(out);
				pw.print(text);
				out.flush();
				pw.flush();
				out.close();
				pw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void init() {
			try {
				InputStream in = new FileInputStream(history);
				Scanner scanner = new Scanner(in);
				String input;
				while ((input = scanner.nextLine()) != null) {
					messagePane.append(input+"\n");
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
