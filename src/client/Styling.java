package client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Styling {
	
	public static final Color blue = new Color(43, 129, 254);
	public static final Font defaultFont = new Font("Comic Sans MS",
			Font.BOLD, 15);
	public static final Font ichatFont = new Font("Kristen ITC", Font.BOLD, 40);

	
	public static void makeStyledButton(JButton login) {
		login.setBackground(blue);
		login.setForeground(Color.white);
		login.setFont(defaultFont);
		login.setBorderPainted(false);
	}
	
	public static ImageIcon makeGoodIcon(String path) {
		ImageIcon icon = new ImageIcon(path);
		Image img = icon.getImage();
		Image newImage = img.getScaledInstance(50, 50,
				java.awt.Image.SCALE_SMOOTH);
		ImageIcon good = new ImageIcon(newImage);
		return good;
	}
}
