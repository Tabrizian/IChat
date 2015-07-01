package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.Message;
import com.User;

public class ChatRoom extends JFrame implements KeyListener {

	private Client client;
	private User dest;
	private ChatPanel chatPanel;
	private Thread thread;
	private JPanel profile;
	private JLabel destName;
	private GridBagConstraints gc;
	private boolean destIsOnline;
	private boolean good = true;

	public ChatRoom(Client client, User dest) {

		this.client = client;
		this.dest = dest;
		chatPanel = new ChatPanel();
		JPanel allOfComponents = new JPanel(new BorderLayout());
		Styling.makeStyledFrame(allOfComponents);
		profile = new JPanel(new BorderLayout());
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

		setUndecorated(true);
		profile.add(destName, BorderLayout.WEST);
		profile.add(new BorderPanel(this), BorderLayout.NORTH);
		// profile.add(,BorderLayout.EAST)

		setLayout(new BorderLayout());
		allOfComponents.add(profile, BorderLayout.NORTH);
		allOfComponents.add(chatPanel, BorderLayout.CENTER);

		setLocationRelativeTo(null);

		add(allOfComponents, BorderLayout.CENTER);
		setVisible(true);
		setSize(500, 500);
	}

	private class ChatPanel extends JPanel {

		private JPanel messagePane;
		private JTextField message;
		private JButton send;
		private JPanel sendArea;
		private MessageCenter messageCenter = MessageCenter
				.getMessageCenter(client.getSocket());

		public ChatPanel() {

			message = new JTextField();
			messagePane = new JPanel();
			messagePane.setLayout(new GridBagLayout());
			send = new JButton("Send");
			sendArea = new JPanel();
			JScrollPane js = new JScrollPane(messagePane);

			// Makes Jscroll pane scroll to the bottom.
			js.getVerticalScrollBar().addAdjustmentListener(
					new AdjustmentListener() {
						public void adjustmentValueChanged(AdjustmentEvent e) {
							e.getAdjustable().setValue(
									e.getAdjustable().getMaximum());
						}
					});

			Styling.makeStyledButton(send);

			setBackground(Color.WHITE);
			setLayout(new BorderLayout());
			gc = new GridBagConstraints();
			gc.weightx = 1;
			gc.weighty = 1;
			gc.gridy = GridBagConstraints.RELATIVE;
			gc.gridx = 0;
			gc.anchor = GridBagConstraints.LINE_START;
			gc.ipady = 15;
			gc.insets = new Insets(10, 10, 10, 10);

			message.addKeyListener(ChatRoom.this);
			init();

			Runnable r = () -> {

				while (good) {
					System.out.println("Running");

					for (int i = 0; i < messageCenter.getMessages().size(); i++) {
						Message message = messageCenter.getMessages().get(i);
						if (message.getVerb().equals(Message.SEND)) {
							String payam = ChatRoom.this.dest.getFirstName()
									+ ": " + message.getMessage();
							gc.ipadx = 100;
							messagePane
									.add(new MessageBubble(payam, false), gc);
							messageCenter.getMessages().remove(i);
							writeMessage(payam);
							messagePane.revalidate();

						} else if (message.getVerb().equals(Message.DELIVERED)) {
							gc.ipadx = 100;
							messagePane.add(new MessageBubble("Delivered",
									false), gc);
							messageCenter.getMessages().remove(i);
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
					gc.ipadx = 100;
					messagePane.add(new MessageBubble(payam, true), gc);
					writeMessage(payam);
					message.setText("");
					messagePane.revalidate();

				}
			});

		}

		public JButton getSend() {
			return send;
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
				gc.ipadx = 100;
				if (string.length() > 0) {
					boolean good = string.indexOf(dest.getFirstName()) > -1;
					messagePane.add(new MessageBubble(string, !good), gc);
					messagePane.revalidate();
				}
			}
		}

		public void refreshStatus() {
			messageCenter.sendMessage(new Message(Message.STATUS,
					Message.CLIENT, Message.SERVER, dest.getUsername()));
			Message msg = messageCenter.getMessage(Message.STATUS);
			if (msg.getMessage().equals("true")) {

				destIsOnline = true;
			} else {

				destIsOnline = false;
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == '\n')
			chatPanel.getSend().doClick();
	}

}
