package eg.edu.alexu.csd.oop.draw;

import java.awt.Graphics;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Engine implements DrawingEngine {

	private List<Class<? extends Shape>> shapes = null;
	private static Engine instance;

	private Engine() {
		History.destoryInstance();
	}

	public static void destoryInstance() {
		instance = null;
	}

	public static Engine getInstance() {
		if (instance == null) {
			instance = new Engine();
		}
		return instance;
	}

	@Override
	public void refresh(Graphics canvas) {
		Shape[] shapes = getShapes();
		for (Shape shape : shapes) {
			shape.draw(canvas);
		}
	}

	@Override
	public void addShape(Shape shape) {
		History.getInstance().add(shape);
	}

	@Override
	public void removeShape(Shape shape) {
		try {
			History.getInstance().removeShape(shape);
		} catch (NullPointerException e) {
			throw new NullPointerException();
		}

	}

	@Override
	public void updateShape(Shape oldShape, Shape newShape) {
		try {
			History.getInstance().updateShape(oldShape, newShape);
		} catch (NullPointerException e) {
			throw new NullPointerException();
		}
	}

	@Override
	public Shape[] getShapes() {
		Shape[] current = new Shape[History.getInstance().size()];
		LinkedList<Shape> shapes;
		try {
			shapes = History.getInstance().getCurrent();
		} catch (NullPointerException e) {
			throw new NullPointerException();
		}
		return shapes.toArray(current);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Class<? extends Shape>> getSupportedShapes() {
		shapes = new LinkedList<Class<? extends Shape>>();
		shapes.add(Line.class);
		shapes.add(Triangle.class);
		shapes.add(Rectangle.class);
		shapes.add(Square.class);
		shapes.add(Ellipse.class);
		shapes.add(Circle.class);
		try {
			ClassFinder cf = new ClassFinder();
			Set<?> classes = cf.findClasses(Shape.class);
			for (Object className : classes) {
				Class<?> clazz = Class.forName((String) className);
				if (!shapes.contains(clazz)) {
					shapes.add((Class<? extends Shape>) clazz);
				}
			}
		} catch (ClassNotFoundException | IOException e) {
		}
		return shapes;
	}

	public List<Class<? extends Shape>> getValidShapes() {
		return shapes;
	}
	
	@Override
	public void undo() {
		try {
			History.getInstance().undo();
		} catch (NullPointerException e) {
			throw new NullPointerException();
		}
	}

	@Override
	public void redo() {
		try {
			History.getInstance().redo();
		} catch (NullPointerException e) {
			throw new NullPointerException();
		}
	}

	@Override
	public void save(String path) {
		try {
			if ((path.toLowerCase()).endsWith(".xml")) {
				XML xml = new XML();
				xml.save(this.getShapes(), path);
			} else if ((path.toLowerCase()).endsWith(".json")) {
				JSON json = new JSON();
				json.save(this.getShapes(), path);
			} else {
				throw new InvalidPathException(path, "WRONG EXTENSION");
			}
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	@Override
	public void load(String path) {
		try {
			if ((path.toLowerCase()).endsWith(".xml")) {
				XML xml = new XML();
				Shape[] shapes = xml.load(path);
				History.destoryInstance();
				History.getInstance().adddummy();
				History.getInstance().add(shapes);
			} else if ((path.toLowerCase()).endsWith(".json")) {
				JSON json = new JSON();
				Shape[] shapes = json.load(path);
				History.destoryInstance();
				History.getInstance().adddummy();
				History.getInstance().add(shapes);
			} else {
				throw new InvalidPathException(path, "WRONG EXTENSION");
			}
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

}
