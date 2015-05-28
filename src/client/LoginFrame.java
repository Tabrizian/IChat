package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.Message;

public class LoginFrame extends JFrame {

	private LoginPanel panel;
	private static final Color blue = new Color(43, 129, 254);
	private static final Font defaultFont = new Font("Comic Sans MS",
			Font.BOLD, 15);
	private static final Font ichatFont = new Font("Capture it", Font.BOLD, 30);
	private Client client;

	public LoginFrame(Client client) {
		super("Login");
		
		this.client = client;
//
//		try {
//			UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
//		} catch (ClassNotFoundException | InstantiationException
//				| IllegalAccessException | UnsupportedLookAndFeelException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		setLayout(new BorderLayout());
		panel = new LoginPanel();
		add(panel, BorderLayout.CENTER);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		setSize(300, 300);
	}

	private class LoginPanel extends JPanel {
		private JButton login;
		private JButton signup;
		private JTextField username;
		private JTextField password;
		private JLabel ichat;

		public LoginPanel() {
			setLayout(new GridBagLayout());

			GridBagConstraints gc = new GridBagConstraints();
			gc.weightx = 1;
			gc.weighty = 1;

			login = new JButton("Login");
			signup = new JButton("Sign up");

			username = new JTextField(15);
			password = new JPasswordField(15);

			username.setSize((int) username.getPreferredSize().getWidth(), 40);

			ichat = new JLabel("I Chat!");
			ichat.setFont(ichatFont);
			ichat.setForeground(Color.RED);

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
							+ "," + password).send(client.getSocket());

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
