package client;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

public class TextFieldFocus implements FocusListener, KeyListener {

	private String good;

	public TextFieldFocus(String good, JTextField textField) {
		this.good = good;
		textField.setForeground(Color.GRAY);
		textField.setText(good);
	}

	@Override
	public void focusGained(FocusEvent e) {
		JTextField test = (JTextField) e.getSource();
		if (test.getText().equals(good)
				&& test.getForeground().equals(Color.GRAY)) {
			test.setCaretPosition(0);
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		JTextField test = (JTextField) e.getSource();
		if (test.getText().equals("")) {
			test.setForeground(Color.GRAY);
			test.setText(good);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		JTextField test = (JTextField) e.getSource();
		if (test.getText().equals(good)
				&& test.getForeground().equals(Color.GRAY)) {
			test.setText("");
			test.setForeground(Color.BLACK);
		}
	}

}
