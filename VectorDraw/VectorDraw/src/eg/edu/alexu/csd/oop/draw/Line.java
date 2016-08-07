package eg.edu.alexu.csd.oop.draw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public class Line extends UniverseShape {

	private Point endPoint;

	private Line(Point p, Point b, Color c, Color fc) {
		pos = p;
		endPoint = b;
		color = c;
		fillcolor = fc;
	}

	public Line() {
		pos = new Point(0, 0);
		endPoint = new Point(0, 0);
		color = Color.BLACK;
		fillcolor = Color.WHITE;
	}

	@Override
	public void setProperties(Map<String, Double> properties) {
		if (properties.get("Second Point X") != null) {
			endPoint.x = properties.get("Second Point X").intValue();
		}
		if (properties.get("Second Point Y") != null) {
			endPoint.y = properties.get("Second Point Y").intValue();
		}
		this.properties = properties;
	}

	@Override
	public Map<String, Double> getProperties() {
		if (properties == null) {
			Map<String, Double> map = new HashMap<String, Double>();
			if (endPoint.x == 0) {
				map.put("Second Point X", null);
			} else {
				map.put("Second Point X", (double) endPoint.x);
			}
			if (endPoint.y == 0) {
				map.put("Second Point Y", null);
			} else {
				map.put("Second Point Y", (double) endPoint.y);
			}
			properties = map;
		}
		return properties;
	}

	@Override
	public void draw(Graphics canvas) {
		Graphics2D g2d = (Graphics2D) canvas;
		setProperties(getProperties());
		if (pos == null || color == null || fillcolor == null) {
			System.out.println(pos + " " + color + " " + fillcolor);
			throw new NullPointerException();
		}
		if (endPoint == null) {
			throw new NullPointerException();
		}
		g2d.setColor(color);
		g2d.drawLine(pos.x, pos.y, endPoint.x, endPoint.y);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return new Line(pos, endPoint, color, fillcolor);
	}

	@Override
	public boolean equals(Object other) {
		boolean result = false;
		if (other instanceof Line) {
			result = (this.color.getRGB() == ((Line) other).color.getRGB())
					&& (this.pos.getX() == ((Line) other).pos.getX()) && (this.pos.getY() == ((Line) other).pos.getY())
					&& (this.endPoint.getX() == ((Line) other).endPoint.getX())
					&& (this.endPoint.getY() == ((Line) other).endPoint.getY());
		}
		return result;
	}

}
