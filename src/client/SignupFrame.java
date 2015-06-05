package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.Message;

public class SignupFrame extends JFrame {

	private SignupPanel signupPanel;
	private Client client;

	public SignupFrame(Client client) {
		super("Sign up");
		this.client = client;
		setLayout(new BorderLayout());

		signupPanel = new SignupPanel();
		add(new BorderPanel(this), BorderLayout.NORTH);
		add(signupPanel, BorderLayout.CENTER);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setUndecorated(true);

		setLocationRelativeTo(null);
		setLocation(500, 300);
		setSize(300, 400);
		setVisible(true);

	}

	private class SignupPanel extends JPanel {

		private JTextField username;
		private JPasswordField password;
		private JTextField firstname;
		private JTextField lastname;
		private JButton signup;
		private JLabel check;
		private JLabel title;
		private MessageCenter messageCenter = MessageCenter
				.getMessageCenter(client.getSocket());

		public SignupPanel() {
			super();
			username = new JTextField(15);
			password = new JPasswordField(15);
			firstname = new JTextField(15);
			lastname = new JTextField(15);
			signup = new JButton("Sign up");
			check = new JLabel();
			title = new JLabel("Sign up");

			TextFieldFocus focus1 = new TextFieldFocus("Username", username);
			TextFieldFocus focus2 = new TextFieldFocus("Password", password);
			TextFieldFocus focus3 = new TextFieldFocus("First Name", firstname);
			TextFieldFocus focus4 = new TextFieldFocus("Last Name", lastname);

			username.addFocusListener(focus1);
			username.addKeyListener(focus1);
			password.addFocusListener(focus2);
			password.addKeyListener(focus2);
			firstname.addFocusListener(focus3);
			firstname.addKeyListener(focus3);
			lastname.addFocusListener(focus4);
			lastname.addKeyListener(focus4);

			setBackground(Color.WHITE);
			Styling.makeStyledFrame(this);
			Styling.makeStyledLabel(title);
			Styling.makeStyledButton(signup);
			Styling.makeStyledTextField(firstname);
			Styling.makeStyledTextField(lastname);
			Styling.makeStyledTextField(username);
			Styling.makeStyledTextField(password);

			setLayout(new GridBagLayout());
			GridBagConstraints gc = new GridBagConstraints();
			gc.weightx = 1;
			gc.weighty = 1;

			gc.gridx = 0;
			gc.gridy = 0;
			int anchor = gc.anchor;
			gc.anchor = GridBagConstraints.LINE_START;
			gc.insets = new Insets(0, 10, 0, 0);
			add(title, gc);

			gc.gridx = 0;
			gc.gridy = 1;
			gc.insets = new Insets(0, 0, 0, 0);
			gc.anchor = anchor;
			add(username, gc);

			gc.gridy = 2;
			add(password, gc);

			gc.gridy = 3;
			add(firstname, gc);

			gc.gridy = 4;
			add(lastname, gc);

			gc.gridy = 5;
			gc.weighty = 0.5;
			add(signup, gc);

			gc.weighty = 1;
			gc.weightx = 0.2;
			gc.gridx = 1;
			gc.gridy = 1;
			add(check, gc);

			signup.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if ("".equals(username.getText())
							|| "".equals(password.getText())
							|| "".equals(firstname.getText())
							|| "".equals(lastname.getText())) {
						JOptionPane.showMessageDialog(signupPanel,
								"All of the fields must be filled.", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else {
						messageCenter.sendMessage(new Message(Message.SIGNUP,
								"", Message.SERVER, username.getText() + ","
										+ password.getText() + ","
										+ firstname.getText() + ","
										+ lastname.getText()));

						Message message = messageCenter
								.getMessage(Message.SIGNUP);
						if (message.getMessage().equals("true")) {
							JOptionPane.showMessageDialog(SignupFrame.this,
									"Signup was successful!", "Success",
									JOptionPane.INFORMATION_MESSAGE);
							SignupFrame.this.dispose();
						} else {
							JOptionPane.showMessageDialog(SignupFrame.this,
									"Username already exist!", "Failure",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			});

			username.addKeyListener(new KeyListener() {

				@Override
				public void keyTyped(KeyEvent e) {
					messageCenter.sendMessage(new Message(Message.AUTH,
							Message.CLIENT, Message.SERVER, username.getText()));
					if (messageCenter.getMessage(Message.AUTH).equals("true")) {
						check.setText("Check!");
					} else {
						check.setText("X");
					}
				}

				@Override
				public void keyReleased(KeyEvent e) {
					messageCenter.sendMessage(new Message(Message.AUTH,
							Message.CLIENT, Message.SERVER, username.getText()));
					if (messageCenter.getMessage(Message.AUTH).equals("true")) {
						check.setText("Check!");
					} else {
						check.setText("X");
					}
				}

				@Override
				public void keyPressed(KeyEvent e) {
					messageCenter.sendMessage(new Message(Message.AUTH,
							Message.CLIENT, Message.SERVER, username.getText()));
					if (messageCenter.getMessage(Message.AUTH).equals("true")) {
						check.setText("Check!");
					} else {
						check.setText("X");
					}
				}
			});

		}
	}

}
