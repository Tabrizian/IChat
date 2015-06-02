package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
		private JLabel check;

		public SignupPanel() {
			super();
			username = new JTextField(15);
			password = new JPasswordField(15);
			firstname = new JTextField(15);
			lastname = new JTextField(15);
			signup = new JButton("Sign up");
			check = new JLabel();

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

			gc.weighty = 1;
			gc.weightx = 0.2;
			gc.gridx = 1;
			gc.gridy = 0;
			add(check, gc);

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
					
					Message message = Message.recieveMessage(client.getSocket());
					if(message.getMessage().equals("true")){
						JOptionPane.showMessageDialog(SignupFrame.this, "Signup was successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
						SignupFrame.this.dispose();
					}else{
						JOptionPane.showMessageDialog(SignupFrame.this, "Username already exist!", "Failure", JOptionPane.ERROR_MESSAGE);
					}
				}
			});

			username.addKeyListener(new KeyListener() {

				@Override
				public void keyTyped(KeyEvent e) {
					new Message(Message.AUTH, Message.CLIENT, Message.SERVER,
							username.getText()).send(client.getSocket());
					if (Message.recieveMessage(client.getSocket()).getMessage()
							.equals("true")) {
						check.setText("Check!");
					} else {
						check.setText("X");
					}
				}

				@Override
				public void keyReleased(KeyEvent e) {
					new Message(Message.AUTH, Message.CLIENT, Message.SERVER,
							username.getText()).send(client.getSocket());
					if (Message.recieveMessage(client.getSocket()).getMessage()
							.equals("true")) {
						check.setText("Check!");
					} else {
						check.setText("X");
					}

				}

				@Override
				public void keyPressed(KeyEvent e) {
					new Message(Message.AUTH, Message.CLIENT, Message.SERVER,
							username.getText()).send(client.getSocket());
					if (Message.recieveMessage(client.getSocket()).getMessage()
							.equals("true")) {
						check.setText("Check!");
					} else {
						check.setText("X");
					}
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
