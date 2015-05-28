package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.Message;

public class SignupFrame extends JFrame {

	private SignupPanel signupPanel;
	private Client client;
	private static final Color blue = new Color(43, 129, 254);
	private static final Font defaultFont = new Font("Comic Sans MS",
			Font.BOLD, 15);

	public SignupFrame(Client client) {
		super("Sign up");
		this.client = client;
		setLayout(new BorderLayout());

		signupPanel = new SignupPanel();
		add(signupPanel, BorderLayout.CENTER);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(300, 400);
		setVisible(true);

	}

	private class SignupPanel extends JPanel {

		private JTextField username;
		private JPasswordField password;
		private JTextField firstname;
		private JTextField lastname;
		private JButton signup;

		public SignupPanel() {
			super();
			username = new JTextField(15);
			password = new JPasswordField(15);
			firstname = new JTextField(15);
			lastname = new JTextField(15);
			signup = new JButton("Sign up");

			makeStyledButton(signup);

			setLayout(new GridBagLayout());
			GridBagConstraints gc = new GridBagConstraints();
			gc.weightx = 1;
			gc.weighty = 1;

			gc.gridx = 0;
			gc.gridy = 0;
			add(username, gc);

			gc.gridy = 1;
			add(password, gc);

			gc.gridy = 2;
			add(firstname, gc);

			gc.gridy = 3;
			add(lastname, gc);

			gc.gridy = 4;
			gc.weighty = 0.5;
			add(signup, gc);

			signup.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {

					new Message(Message.SIGNUP, "", Message.SERVER, username
							.getText()
							+ ","
							+ password.getText()
							+ ","
							+ firstname.getText() + "," + lastname.getText())
							.send(client.getSocket());
					;

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
