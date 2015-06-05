package client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Styling {

	public static final Color blue = new Color(43, 129, 254);
	public static final Font defaultFont = new Font("Comic Sans MS", Font.BOLD,
			15);
	public static final Font labelFont = new Font("Comic Sans MS", Font.BOLD,
			30);
	public static final Font ichatFont = new Font("Kristen ITC", Font.BOLD, 40);
	public static final Font secondFont = new Font("Segoe Print", Font.BOLD, 15);
	public static final Color green = new Color(117, 180, 75);

	public static void makeStyledButton(JButton login) {
		login.setBackground(blue);
		login.setForeground(Color.white);
		login.setFont(defaultFont);
		login.setBorderPainted(false);
	}

	public static void makeStyledLabel(JLabel label) {
		label.setFont(labelFont);
		label.setForeground(blue);
		label.setBackground(Color.WHITE);
	}

	public static ImageIcon makeGoodIcon(String path) {
		ImageIcon icon = new ImageIcon(path);
		Image img = icon.getImage();
		Image newImage = img.getScaledInstance(30, 30,
				java.awt.Image.SCALE_SMOOTH);
		ImageIcon good = new ImageIcon(newImage);
		return good;
	}

	public static void makeStyledFrame(JPanel frame) {
		frame.setBorder(BorderFactory.createLineBorder(Color.GRAY, 5));
		frame.setBackground(Color.WHITE);
	}

	public static void makeStyledTextField(JTextField textfield) {
		textfield.setBorder(BorderFactory.createCompoundBorder(
				textfield.getBorder(),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));
	}

	public static void makeStyledButton2(JButton button) {
		button.setFont(secondFont);
		button.setForeground(Color.WHITE);
		button.setBackground(green);
	}
}
