package eg.edu.alexu.csd.oop.draw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.util.HashMap;
import java.util.Map;

public class Circle extends Ellipse {

	private int diameter;

	private Circle(Point p, int d, Color c, Color fc) {
		pos = p;
		diameter = d;
		color = c;
		fillcolor = fc;
	}

	public Circle() {
		diameter = 0;
		pos = new Point(0, 0);
		color = Color.BLACK;
		fillcolor = Color.WHITE;
	}

	@Override
	public boolean equals(Object other) {
		boolean result = false;
		if (other instanceof Circle) {
			result = (this.color.getRGB() == ((Circle) other).color.getRGB())
					&& (this.fillcolor.getRGB() == ((Circle) other).fillcolor.getRGB())
					&& (this.pos.getX() == ((Circle) other).pos.getX())
					&& (this.pos.getY() == ((Circle) other).pos.getY()) && (this.diameter == ((Circle) other).diameter);
		}
		return result;
	}

	@Override
	public void setProperties(Map<String, Double> properties) {
		if (properties.get("Diameter") != null) {
			diameter = properties.get("Diameter").intValue();
		}
		this.properties = properties;
	}

	@Override
	public Map<String, Double> getProperties() {
		if (properties == null) {
			Map<String, Double> map = new HashMap<String, Double>();
			if (diameter == 0) {
				map.put("Diameter", null);
			} else {
				map.put("Diameter", (double) diameter);
			}
			properties = map;
		}
		return properties;
	}

	@Override
	public void draw(Graphics canvas) {
		Graphics2D g2d = (Graphics2D) canvas;
		setProperties(getProperties());
		if (pos == null || color == null || fillcolor == null || diameter == 0) {
			throw new NullPointerException();
		}
		g2d.setColor(fillcolor);
		g2d.fill(new Ellipse2D.Double(pos.x - diameter / 2, pos.y - diameter / 2, diameter, diameter));
		g2d.setColor(color);
		g2d.draw(new Ellipse2D.Double(pos.x - diameter / 2, pos.y - diameter / 2, diameter, diameter));
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return new Circle(pos, diameter, color, fillcolor);
	}

}
