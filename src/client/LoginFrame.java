package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

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
	private static final Color blue = new Color(43, 129, 254);
	private static final Font defaultFont = new Font("Comic Sans MS",
			Font.BOLD, 15);
	private static final Font ichatFont = new Font("Kristen ITC", Font.BOLD, 40);
	private Client client;

	public LoginFrame(Client client) {
		super("Login");

		setUndecorated(true);
		this.client = client;

		setLayout(new BorderLayout());
		panel = new LoginPanel();
		add(panel, BorderLayout.CENTER);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		setSize(300, 300);
		setLocation(200, 200);
	}

	private class LoginPanel extends JPanel {
		private JButton login;
		private JButton signup;
		private JTextField username;
		private JPasswordField password;
		private JLabel ichat;

		public LoginPanel() {
			setLayout(new GridBagLayout());
			setBorder(BorderFactory.createLineBorder(Color.GRAY, 5));
			setBackground(Color.WHITE);
			GridBagConstraints gc = new GridBagConstraints();
			gc.weightx = 1;
			gc.weighty = 1;

			login = new JButton("Login");
			signup = new JButton("Sign up");

			username = new JTextField(15);
			password = new JPasswordField(15);

			username.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
			password.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
			username.setSize((int) username.getPreferredSize().getWidth(), 40);

			ichat = new JLabel("iChat!");
			ichat.setFont(ichatFont);
			ichat.setForeground(blue);

			makeStyledButton(login);
			makeStyledButton(signup);

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

					new Message(Message.LOGIN, "", Message.SERVER, username
							.getText() + "," + password.getText()).send(client
							.getSocket());
					Message message = null;
					try {
						InputStream in = client.getSocket().getInputStream();
						ObjectInputStream input = new ObjectInputStream(in);
						message = (Message) input.readObject();
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if (message.getMessage().equals("SUCCESS")) {
						JOptionPane.showMessageDialog(LoginFrame.this,
								"Login was successful!");
						try {
							InputStream in = client.getSocket()
									.getInputStream();
							ObjectInputStream input = new ObjectInputStream(in);
							Message ms = (Message) input.readObject();
							String[] tokens = ms.getMessage().split(",");
							User user = new User(tokens[0], tokens[1],
									tokens[2], tokens[3]);
							client.setUser(user);
						} catch (ClassNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
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

		private void makeStyledButton(JButton login) {
			login.setBackground(blue);
			login.setForeground(Color.white);
			login.setFont(defaultFont);
			login.setBorderPainted(false);
		}
	}
}
