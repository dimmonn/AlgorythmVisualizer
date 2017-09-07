package com.algo.visual.drawing;

import javax.swing.*;
import java.awt.*;

class Circle extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int x;
	private final int y;

	public Circle(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void paint(Graphics g) {
		g.setColor(Color.yellow);
		g.fillOval(x, y, 30, 30);

	}

}