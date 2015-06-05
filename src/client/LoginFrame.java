package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.Message;
import com.User;

public class LoginFrame extends JFrame {

	private LoginPanel panel;
	private Client client;

	public LoginFrame(Client client) {
		super("Login");

		setUndecorated(true);
		this.client = client;

		setLayout(new BorderLayout());
		panel = new LoginPanel();
		add(new BorderPanel(this), BorderLayout.PAGE_START);
		add(panel, BorderLayout.CENTER);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		setLocationRelativeTo(null);
		setSize(300, 300);
		setLocation(200, 200);
	}

	private class LoginPanel extends JPanel {
		private JButton login;
		private JButton signup;
		private JTextField username;
		private JPasswordField password;
		private JLabel ichat;
		private MessageCenter messageCenter = MessageCenter
				.getMessageCenter(client.getSocket());

		public LoginPanel() {
			setLayout(new GridBagLayout());
			Styling.makeStyledFrame(this);
			GridBagConstraints gc = new GridBagConstraints();
			gc.weightx = 1;
			gc.weighty = 1;

			login = new JButton("Login");
			signup = new JButton("Sign up");

			username = new JTextField(15);
			password = new JPasswordField(15);

			username.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
			password.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
			TextFieldFocus focus1 = new TextFieldFocus("Username", username);
			TextFieldFocus focus2 = new TextFieldFocus("Password", password);
			username.addFocusListener(focus1);
			username.addKeyListener(focus1);
			password.addFocusListener(focus2);
			password.addKeyListener(focus2);
			username.setSize((int) username.getPreferredSize().getWidth(), 40);
			
			ichat = new JLabel("iChat!");
			ichat.setFont(Styling.ichatFont);
			ichat.setForeground(Styling.blue);

			Styling.makeStyledButton(login);
			Styling.makeStyledButton(signup);
			Styling.makeStyledTextField(username);
			Styling.makeStyledTextField(password);

			int t = gc.anchor;

			gc.gridx = 0;
			gc.gridy = 0;
			gc.anchor = GridBagConstraints.LINE_START;
			gc.insets = new Insets(0, 20, 0, 0);
			add(ichat, gc);
			gc.anchor = t;

			gc.gridx = 0;
			gc.gridy = 1;
			add(username, gc);

			gc.gridx = 0;
			gc.gridy = 2;
			add(password, gc);

			gc.gridx = 0;
			gc.gridy = 3;
			gc.ipadx = 100 - (int) login.getPreferredSize().getWidth();
			add(login, gc);

			gc.gridx = 0;
			gc.gridy = 4;
			gc.ipadx = 100 - (int) signup.getPreferredSize().getWidth();
			add(signup, gc);

			login.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					messageCenter.sendMessage(new Message(Message.LOGIN, "",
							Message.SERVER, username.getText() + ","
									+ password.getText()));
					Message message = null;

					message = messageCenter.getMessage(Message.LOGIN);

					if (message.getMessage().equals("SUCCESS")) {
						JOptionPane.showMessageDialog(LoginFrame.this,
								"Login was successful!");

						Message ms = messageCenter.getMessage(Message.LOGIN);
						String[] tokens = ms.getMessage().split(",");
						User user = new User(tokens[0], tokens[1], tokens[2],
								tokens[3]);
						client.setUser(user);
						new ChatFrame(client);
						LoginFrame.this.dispose();

					} else {
						JOptionPane.showMessageDialog(LoginFrame.this,
								"Login failed!");
					}

				}
			});

			signup.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					new SignupFrame(client);

				}
			});
		}
	}

}
