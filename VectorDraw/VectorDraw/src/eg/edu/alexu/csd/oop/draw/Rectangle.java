package eg.edu.alexu.csd.oop.draw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public class Rectangle extends UniverseShape {

	private int width, height;

	private Rectangle(Point p, int w, int h, Color c, Color fc) {
		pos = p;
		width = w;
		height = h;
		color = c;
		fillcolor = fc;
	}

	public Rectangle() {
		width = height = 0;
		pos = new Point(0, 0);
		color = Color.BLACK;
		fillcolor = Color.WHITE;
	}

	@Override
	public boolean equals(Object other) {
		boolean result = false;
		if (other instanceof Rectangle) {
			result = (this.color.getRGB() == ((Rectangle) other).color.getRGB())
					&& (this.fillcolor.getRGB() == ((Rectangle) other).fillcolor.getRGB())
					&& (this.pos.getX() == ((Rectangle) other).pos.getX())
					&& (this.pos.getY() == ((Rectangle) other).pos.getY()) 
					&& (this.width == ((Rectangle) other).width)
					&& (this.height == ((Rectangle) other).height);
		}
		return result;
	}

	@Override
	public void setProperties(Map<String, Double> properties) {
		if (properties.get("Width") != null) {
			width = properties.get("Width").intValue();
		}
		if (properties.get("Height") != null) {
			height = properties.get("Height").intValue();
		}
		this.properties = properties;
	}

	@Override
	public Map<String, Double> getProperties() {
		if (properties == null) {
			Map<String, Double> map = new HashMap<String, Double>();
			if (width == 0) {
				map.put("Width", null);
			} else {
				map.put("Width", (double) width);
			}
			if (height == 0) {
				map.put("Height", null);
			} else {
				map.put("Height", (double) height);
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
			throw new NullPointerException();
		}
		if (width == 0 || height == 0) {
			throw new NullPointerException();
		}
		g2d.setColor(fillcolor);
		g2d.fillRect(pos.x - width / 2, pos.y - height / 2, width, height);
		g2d.setColor(color);
		g2d.drawRect(pos.x - width / 2, pos.y - height / 2, width, height);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return new Rectangle(pos, width, height, color, fillcolor);
	}

}
