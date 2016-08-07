package eg.edu.alexu.csd.oop.draw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public class Triangle extends UniverseShape {

	private Point secondpoint, thirdpoint;

	private Triangle(Point p1, Point p2, Point p3, Color c, Color fc) {
		pos = p1;
		secondpoint = p2;
		thirdpoint = p3;
		color = c;
		fillcolor = fc;
	}

	public Triangle() {
		pos = new Point(0, 0);
		secondpoint = new Point(0, 0);
		thirdpoint = new Point(0, 0);
		color = Color.BLACK;
		fillcolor = Color.WHITE;
	}

	@Override
	public boolean equals(Object other) {
		boolean result = false;
		if (other instanceof Triangle) {
			result = (this.color.getRGB() == ((Triangle) other).color.getRGB())
					&& (this.fillcolor.getRGB() == ((Triangle) other).fillcolor.getRGB())
					&& (this.pos.getX() == ((Triangle) other).pos.getX())
					&& (this.pos.getY() == ((Triangle) other).pos.getY())
					&& (this.secondpoint.getX() == ((Triangle) other).secondpoint.getX())
					&& (this.secondpoint.getY() == ((Triangle) other).secondpoint.getY())
					&& (this.thirdpoint.getX() == ((Triangle) other).thirdpoint.getX())
					&& (this.thirdpoint.getY() == ((Triangle) other).thirdpoint.getY());
		}
		return result;
	}

	@Override
	public void setProperties(Map<String, Double> properties) {
		if (properties.get("Second Point X") != null) {
			secondpoint.x = properties.get("Second Point X").intValue();
		}
		if (properties.get("Second Point Y") != null) {
			secondpoint.y = properties.get("Second Point Y").intValue();
		}
		if (properties.get("Third Point X") != null) {
			thirdpoint.x = properties.get("Third Point X").intValue();
		}
		if (properties.get("Third Point Y") != null) {
			thirdpoint.y = properties.get("Third Point Y").intValue();
		}
		this.properties = properties;
	}

	@Override
	public Map<String, Double> getProperties() {
		if (properties == null) {
			Map<String, Double> map = new HashMap<String, Double>();
			if (secondpoint.x == 0) {
				map.put("Second Point X", null);
			} else {
				map.put("Second Point X", (double) secondpoint.x);
			}
			if (secondpoint.y == 0) {
				map.put("Second Point Y", null);
			} else {
				map.put("Second Point Y", (double) secondpoint.y);
			}
			if (thirdpoint.x == 0) {
				map.put("Third Point X", null);
			} else {
				map.put("Third Point X", (double) thirdpoint.x);
			}
			if (thirdpoint.y == 0) {
				map.put("Third Point Y", null);
			} else {
				map.put("Third Point Y", (double) thirdpoint.y);
			}
			properties = map;
		}
		return properties;
	}

	@Override
	public void draw(Graphics canvas) {
		Graphics2D g2d = (Graphics2D) canvas;
		setProperties(getProperties());
		if (pos == null || secondpoint == null || thirdpoint == null || color == null || fillcolor == null) {
			throw new NullPointerException();
		}
		int[] x = { pos.x, secondpoint.x, thirdpoint.x };
		int[] y = { pos.y, secondpoint.y, thirdpoint.y };
		g2d.setColor(fillcolor);
		g2d.fillPolygon(x, y, 3);
		g2d.setColor(color);
		g2d.drawPolygon(x, y, 3);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return new Triangle(pos, secondpoint, thirdpoint, color, fillcolor);
	}

}
