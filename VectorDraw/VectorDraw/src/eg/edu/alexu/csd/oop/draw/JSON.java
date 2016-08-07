package eg.edu.alexu.csd.oop.draw;

import java.awt.Color;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.Map;
import java.util.StringTokenizer;

public class JSON {
	
	public void save(Shape[] shapes, String path) throws Exception {
		BufferedWriter writer = new BufferedWriter(new FileWriter(path));
		writer.write("{ \n  \"Shapes\" : [");
		for(int i = 0;i < shapes.length;i++)
		{
			writer.write("\n\t     { \n");
			writer.write("\t     \"className\" : \"");
			writer.write(shapes[i].getClass().getName() + "\",\n");
			writer.write("\t     \"Position.X\" : \"");
			writer.write((shapes[i].getPosition() == null ? null : shapes[i].getPosition().x) + "\",\n");
			writer.write("\t     \"Position.Y\" : \"");
			writer.write((shapes[i].getPosition() == null ? null : shapes[i].getPosition().y) + "\",\n");
			writer.write("\t     \"Color\" : \"");
			writer.write((shapes[i].getColor() == null ? null : (shapes[i].getColor().toString().replace("java.awt.Color", ""))) + "\",\n");
			writer.write("\t     \"fillColor\" : \"");
			writer.write((shapes[i].getFillColor() == null ? null : (shapes[i].getFillColor().toString().replace("java.awt.Color", "").replace("]", ",a=") + shapes[i].getFillColor().getAlpha()) + "]") + "\"");
			Map<String, Double> map = shapes[i].getProperties();
			if(!(map == null)) {
				for (Object object : map.keySet()) {
					writer.write(",\n\t     \"" + ((String) object).replace(" ", "_") +"\" : \"" + map.get((String) object) + "\"");
				}
			}
			writer.write("\n\t     }");
			if (i != shapes.length-1) {
				writer.write(",");
			}
		}
		writer.write("\n\t     ]\n}");
		writer.flush();
		writer.close();
	}
	
	public Shape[] load(String path) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(path));
		reader.readLine();
		reader.readLine();
		LinkedList<Shape> ll = new LinkedList<Shape>();
		int counter = 0;
		while(reader.readLine().replace(" ", "").replace("\t", "").equals("{")) {
			ClassLoader cl = this.getClass().getClassLoader();
	    	if(Engine.getInstance().getValidShapes() != null && Engine.getInstance().getValidShapes().size() != 0) {
	    		cl = Engine.getInstance().getValidShapes().get(Engine.getInstance().getValidShapes().size() - 1).getClassLoader();
	    	}
			Class<?> clazz = Class.forName(reader.readLine().replace("	     \"className\" : ", "").replace("\"", "").replace(",", ""), false, cl);
			Shape shape = (Shape) clazz.newInstance();
			String sx = reader.readLine().replace("	     \"Position.X\" : ", "").replace("\"", "").replace(",", "");
			String sy = reader.readLine().replace("	     \"Position.Y\" : ", "").replace("\"", "").replace(",", "");
			if (sx.equals("null") || sy.equals("null")) {
				shape.setPosition(null);
			} else {
				shape.setPosition(new Point(Integer.parseInt(sx), Integer.parseInt(sy)));
			}
			String sc = reader.readLine().replace("	     \"Color\" : ", "").replace("\"", "").replace(",", "");
			if (sc.equals("null")) {
				shape.setColor(null);
			} else {
				StringTokenizer st = new StringTokenizer(sc.replace("[r=", "").replace("g", "").replace("b", "").replace("]", ""), "=");
				shape.setColor(new Color(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken())));
			}
			String sfc = reader.readLine().replace("	     \"fillColor\" : ", "").replace("\"", "").replace(",", "");
			if (sfc.equals("null")) {
				shape.setFillColor(null);
			} else {
				StringTokenizer st = new StringTokenizer(sfc.replace("[r=", "").replace("g", "").replace("b", "").replace("a", "").replace("]", ""), "=");
				shape.setFillColor(new Color(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken())));
			}
			Map<String, Double> map = shape.getProperties();
			if(!(map == null)) {
				for (Object object : map.keySet()) {
					String s = reader.readLine().replace("	     \"" + ((String) object).replace(" ", "_") + "\" : ", "").replace("\"", "").replace(",", "");
					if (!s.equals("null")) {
						map.put((String) object, Double.parseDouble(s));
					}
				}
			}
			shape.setProperties(map);
			ll.add(shape);
			reader.readLine();
			counter++;
		}
		reader.close();
		return ll.toArray(new Shape[counter]);
	}
	
}