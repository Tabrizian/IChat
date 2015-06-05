package client;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class BorderPanel extends JPanel{


		private JLabel label;
		int pX, pY;

		public BorderPanel(JFrame frame) {
			label = new JLabel(" X ");
			label.setOpaque(true);
			label.setBackground(Color.RED);
			label.setForeground(Color.WHITE);

			setBackground(Color.gray);
			setLayout(new FlowLayout(FlowLayout.RIGHT));

			add(label);

			label.addMouseListener(new MouseAdapter() {
				public void mouseReleased(MouseEvent e) {
					System.exit(0);
				}
			});
			addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent me) {
					// Get x,y and store them
					pX = me.getX();
					pY = me.getY();

				}

				public void mouseDragged(MouseEvent me) {

					frame.setLocation(frame.getLocation().x
							+ me.getX() - pX, frame.getLocation().y
							+ me.getY() - pY);
				}
			});

			addMouseMotionListener(new MouseMotionAdapter() {
				public void mouseDragged(MouseEvent me) {

					frame.setLocation(frame.getLocation().x
							+ me.getX() - pX, frame.getLocation().y
							+ me.getY() - pY);
				}
			});
		}
	}

