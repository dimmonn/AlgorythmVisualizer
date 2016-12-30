package com.algo.visual.drawing;

import javax.swing.*;
import java.awt.*;

public class Circle extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int x; // leftmost pixel in circle has this x-coordinate
	private int y; // topmost pixel in circle has this y-coordinate

	public Circle(int x, int y) {
		this.x = x;
		this.y = y;
		setSize(800, 800);
		setLocation(x, y);
		setVisible(true);
	}

	// paint is called automatically when program begins, when window is
	// refreshed and when repaint() is invoked
	public void paint(Graphics g) {
		g.setColor(Color.yellow);
		g.fillOval(x, y, 100, 100);

	}

}