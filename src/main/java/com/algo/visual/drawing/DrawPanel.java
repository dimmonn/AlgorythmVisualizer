package com.algo.visual.drawing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class DrawPanel extends JPanel {
	private Color color;
	private AtomicInteger atomicXReference = new AtomicInteger();
	private AtomicInteger atomicYReference = new AtomicInteger();

	public DrawPanel() {
		this.color = Color.BLUE;
	}

	public DrawPanel(DrawPanel d) {
		this.atomicXReference = new AtomicInteger(d.getAtomicXReference().get());
		this.atomicYReference = new AtomicInteger(d.getAtomicYReference().get());
		this.color = d.color;

	}

	public AtomicInteger getAtomicXReference() {
		return atomicXReference;
	}

	public AtomicInteger getAtomicYReference() {
		return atomicYReference;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setXX(int x) {
		this.atomicXReference.set(x);
	}

	public void setYY(int y) {
		this.atomicYReference.set(y);
	}

	public int getXX() {
		return atomicXReference.get();
	}

	public int getYY() {
		return atomicYReference.get();
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(1));
		g2.setColor(color);
		g2.draw(new Line2D.Float(atomicXReference.get(), atomicYReference.get(), atomicXReference.get(),
				SwingUtilities.getWindowAncestor(this).getHeight() - 260));
	}
}