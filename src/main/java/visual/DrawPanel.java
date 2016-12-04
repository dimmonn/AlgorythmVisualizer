package visual;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JPanel;

public class DrawPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Color color;
	private final AtomicInteger atomicXReference = new AtomicInteger();
	private final AtomicInteger atomicYReference = new AtomicInteger();

	public DrawPanel() {
		this.color = Color.YELLOW;
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

	public int getHHeight() {
		return this.getHeight() - 40 - getYY();
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

	private int getYY() {
		return atomicYReference.get();
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(1));
		g2.setColor(color);
		g2.draw(new Line2D.Float(atomicXReference.get(), atomicYReference.get(), atomicXReference.get(),
				this.getHeight() - 20));
	}
}