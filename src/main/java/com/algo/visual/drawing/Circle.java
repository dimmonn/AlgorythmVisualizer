package com.algo.visual.drawing;

import javax.swing.*;
import java.awt.*;

public class Circle extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int x;
	private int y;

	public Circle(int x, int y) {
		this.x = x;
		this.y = y;
		setLocation(x, y);
	}

	public void paint(Graphics g) {
		g.setColor(Color.yellow);
		g.fillOval(x, y, 30, 30);

	}

}