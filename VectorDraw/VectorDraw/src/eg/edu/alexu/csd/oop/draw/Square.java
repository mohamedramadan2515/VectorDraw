package eg.edu.alexu.csd.oop.draw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public class Square extends Rectangle {

	private int width;

	public Square() {
		width = 0;
		pos = new Point(0, 0);
		color = Color.BLACK;
		fillcolor = Color.WHITE;
	}

	private Square(Point p, int w, Color c, Color fc) {
		width = w;
		pos = p;
		color = c;
		fillcolor = fc;
	}

	@Override
	public boolean equals(Object other) {
		boolean result = false;
		if (other instanceof Square) {
			result = (this.color.getRGB() == ((Square) other).color.getRGB())
					&& (this.fillcolor.getRGB() == ((Square) other).fillcolor.getRGB())
					&& (this.pos.getX() == ((Square) other).pos.getX())
					&& (this.pos.getY() == ((Square) other).pos.getY()) 
					&& (this.width == ((Square) other).width);
		}
		return result;
	}

	@Override
	public void setProperties(Map<String, Double> properties) {
		if (properties.get("Width") != null) {
			width = properties.get("Width").intValue();
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
		if (width == 0) {
			throw new NullPointerException();
		}
		g2d.setColor(fillcolor);
		g2d.fillRect(pos.x - width / 2, pos.y - width / 2, width, width);
		g2d.setColor(color);
		g2d.drawRect(pos.x - width / 2, pos.y - width / 2, width, width);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return new Square(pos, width, color, fillcolor);
	}

}
