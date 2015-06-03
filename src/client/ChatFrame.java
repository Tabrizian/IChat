package client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
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

	public ChatFrame(Client client) {
		super("I Chat!");
		this.client = client;
		setLayout(new BorderLayout());
		chatPanel = new ChatPanel();
		add(chatPanel, BorderLayout.CENTER);
		setSize(600, 600);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private class ChatPanel extends JPanel {

		private JToolBar toolbar;
		private JButton newChat;
		private MessageCenter messageCenter = MessageCenter
				.getMessageCenter(client.getSocket());

		public ChatPanel() {

			super();
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

	}

}
