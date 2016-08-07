package eg.edu.alexu.csd.oop.draw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Map;

public abstract class UniverseShape implements Shape, Cloneable {

	protected Point pos;
	protected Color color, fillcolor;
	protected Map<String, Double> properties;

	@Override
	public void setPosition(Point position) {
		pos = position;
	}

	@Override
	public Point getPosition() {
		if (pos == null) {
			return null;
		}
		return pos;
	}

	@Override
	public abstract void setProperties(Map<String, Double> properties);

	@Override
	public abstract Map<String, Double> getProperties();

	@Override
	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	public Color getColor() {
		if (color == null) {
			return null;
		}
		return color;
	}

	@Override
	public void setFillColor(Color color) {
		fillcolor = color;
	}

	@Override
	public Color getFillColor() {
		if (fillcolor == null) {
			return null;
		}
		return fillcolor;
	}

	@Override
	public abstract void draw(Graphics canvas);

	@Override
	public abstract Object clone() throws CloneNotSupportedException;

}
