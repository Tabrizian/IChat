package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.Message;
import com.User;

public class ChatPack extends JPanel {

	private String nameDest;
	private Client client;
	private MessageCenter messageCenter;
	private JButton btn;

	public ChatPack(String nameDest, Client client) {
		this.nameDest = nameDest;
		this.client = client;
		btn = new JButton(nameDest);
		
		
		
		setLayout(new BorderLayout());
		add(btn, BorderLayout.PAGE_START);
		messageCenter = MessageCenter.getMessageCenter(client.getSocket());

		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				messageCenter.sendMessage(new Message(Message.USERNAME,
						Message.CLIENT, Message.SERVER, nameDest));
				Message message = messageCenter.getMessage(Message.USERNAME);
				String[] tokens = message.getMessage().split(",");
				new ChatRoom(client, new User(tokens[0], tokens[1], tokens[2],
						tokens[3]));

			}
		});
	}
	public JButton getButton(){
		return btn;
	}

}
