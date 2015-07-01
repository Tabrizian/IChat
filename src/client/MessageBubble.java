package client;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;

import javax.swing.JLabel;

public class MessageBubble extends JLabel {

	private boolean isTurn;

	public MessageBubble(String text, boolean isTurn) {
		super("   " + text);
		setForeground(Color.WHITE);
		this.isTurn = isTurn;
	}

	@Override
	protected void paintComponent(Graphics g) {

		final Graphics2D graphics2D = (Graphics2D) g;
		RenderingHints qualityHints = new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		qualityHints.put(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		graphics2D.setRenderingHints(qualityHints);
		if (isTurn == true)
			graphics2D.setPaint(new Color(80, 150, 180));
		else
			graphics2D.setPaint(new Color(250, 10, 10));
		int width = (int) (getText().length() * 6.9);
		int height = 30;
		GeneralPath path = new GeneralPath();
		path.moveTo(5, 10);
		path.curveTo(5, 10, 7, 5, 0, 0);
		path.curveTo(0, 0, 12, 0, 12, 5);
		path.curveTo(12, 5, 12, 0, 20, 0);
		path.lineTo(width - 10, 0);
		path.curveTo(width - 10, 0, width, 0, width, 10);
		path.lineTo(width, height - 10);
		path.curveTo(width, height - 10, width, height, width - 10, height);
		path.lineTo(15, height);
		path.curveTo(15, height, 5, height, 5, height - 10);
		path.lineTo(5, 15);
		path.closePath();
		graphics2D.fill(path);
		super.paintComponent(g);
	}

}
