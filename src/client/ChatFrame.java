package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import com.Message;
import com.User;

public class ChatFrame extends JFrame {

	private ChatPanel chatPanel;
	private Client client;
	private boolean good = true;

	public ChatFrame(Client client) {
		super("I Chat!");
		this.client = client;
		setLayout(new BorderLayout());
		chatPanel = new ChatPanel();

		WindowListener listener = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				good = false;
			}
		};
		addWindowListener(listener);

		setLocationRelativeTo(null);
		
		add(new BorderPanel(this), BorderLayout.NORTH);
		add(chatPanel, BorderLayout.CENTER);
		setUndecorated(true);
		setSize(200, 400);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private class ChatPanel extends JPanel {

		private JToolBar toolbar;
		private JButton newChat;
		private MessageCenter messageCenter = MessageCenter
				.getMessageCenter(client.getSocket());
		private JPanel chats;

		public ChatPanel() {

			super();
			setBackground(Color.WHITE);
			Styling.makeStyledFrame(this);
			setLayout(new BorderLayout());
			toolbar = new JToolBar() {
				@Override
				protected void addImpl(Component comp, Object constraints,
						int index) {
					super.addImpl(comp, constraints, index);
					if (comp instanceof JButton) {
						((JButton) comp).setOpaque(false);
						((JButton) comp).setContentAreaFilled(false);
						((JButton) comp).setBorderPainted(false);
					}
				}
			};

			newChat = new JButton(Styling.makeGoodIcon("pics/Plus.png"));

			newChat.setFocusable(false);
			chats = new JPanel();
			chats.setBackground(Color.WHITE);
			chats.setLayout(new BorderLayout());
			add(chats, BorderLayout.CENTER);
			messageCenter.sendMessage(new Message(Message.FILELIST,
					Message.CLIENT, Message.SERVER, "data/UserData/"
							+ client.getUser().getUsername()));

			// Getting List of Old Chats....................
			JPanel panel = new JPanel(new GridBagLayout());
			Message message = messageCenter.getMessage(Message.FILELIST);
			if (!message.getMessage().equals("-")) {
				String[] files = message.getMessage().split(",");
				int i = 0;
				for (String string : files) {
					GridBagConstraints gc = new GridBagConstraints();
					gc.weightx = 1;
					gc.weighty = 1;
					gc.fill = GridBagConstraints.HORIZONTAL;
					gc.anchor = GridBagConstraints.PAGE_START;

					gc.gridx = 0;
					gc.gridy = i;

					i++;
					ChatPack button = new ChatPack(string, client);
					Styling.makeStyledButton2(button.getButton());
					panel.add(button, gc);
				}
			}

			Runnable r = () -> {

				while (good) {
					System.out.println("Running");
					
					for (int i = 0; i < messageCenter.getMessages().size(); i++) {
						Message msg = messageCenter.getMessages().get(i);
						if (message.getVerb().equals(Message.SEND)) {
							messageCenter.sendMessage(new Message(Message.USERNAME,Message.CLIENT,Message.SERVER,msg.getSource_ID()));
							Message username = messageCenter.getMessage(Message.USERNAME);
							String[] tokens = username.getMessage().split(",");
							User user = new User(tokens[0],tokens[1],tokens[2],tokens[3]);
							String payam = user.getFirstName()
									+ ": " + message.getMessage();
							writeMessage(payam, user);
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

			chats.add(panel, BorderLayout.NORTH);

			toolbar.setFloatable(false);
			toolbar.add(newChat);
			toolbar.setOrientation(JToolBar.VERTICAL);
			toolbar.setOpaque(false);
			add(toolbar, BorderLayout.WEST);

			newChat.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String username = JOptionPane.showInputDialog(null);
					messageCenter.sendMessage(new Message(Message.USERNAME,
							Message.CLIENT, Message.SERVER, username));
					String msg = messageCenter.getMessage(Message.USERNAME)
							.getMessage();
					String[] tokens = msg.split(",");
					new ChatRoom(client, new User(tokens[0], tokens[1],
							tokens[2], tokens[3]));
				}
			});

		}

		public void writeMessage(String text,User user) {

			messageCenter.sendMessage(new Message(Message.HISTORY,
					"data/UserData/" + client.getUser().getUsername() + "/"
							+ user.getUsername(), "", text));

		}

	}

}
