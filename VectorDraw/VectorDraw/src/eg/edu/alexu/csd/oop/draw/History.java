package eg.edu.alexu.csd.oop.draw;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Stack;

public class History implements Datastructure {

	private static History instance = null;
	private LinkedList<Shape> dummyload;
	private Stack<LinkedList<Shape>> prev = new Stack<LinkedList<Shape>>();
	private Stack<LinkedList<Shape>> next = new Stack<LinkedList<Shape>>();

	private History() {
	}

	public static void destoryInstance() {
		instance = null;
	}

	public static History getInstance() {
		if (instance == null) {
			instance = new History();
		}
		return instance;
	}

	public void adddummy() {
		dummyload = new LinkedList<Shape>();
		prev.add(dummyload);
	}

	@Override
	public void add(Shape shape) {
		LinkedList<Shape> shapes = new LinkedList<Shape>();
		next.clear();
		if (prev.isEmpty()) {
			shapes.add(shape);
			prev.push(shapes);
			return;
		}
		shapes = new LinkedList<Shape>(prev.peek());
		shapes.add(shape);
		prev.push(shapes);
		if (prev.size() == 21)
			prev.remove(0);
	}

	@Override
	public void removeShape(Shape shape) {
		if (prev.isEmpty())
			throw new NullPointerException();
		LinkedList<Shape> shapes = new LinkedList<Shape>(prev.peek());
		if (shapes.remove(shape) == true) {
			LinkedList<Shape> list = new LinkedList<Shape>(shapes);
			prev.push(list);
		} else
			throw new NullPointerException();
	}

	@Override
	public void updateShape(Shape oldshape, Shape newshape) {
		if (prev.isEmpty())
			throw new NullPointerException();
		LinkedList<Shape> shapes = new LinkedList<Shape>(prev.peek());
		if (shapes.remove(oldshape) == true) {
			LinkedList<Shape> list = new LinkedList<Shape>(shapes);
			list.add(newshape);
			prev.push(list);
		} else
			throw new NullPointerException();
	}

	@Override
	public int size() {
		if (prev.isEmpty())
			return 0;
		return prev.peek().size();
	}

	@Override
	public int nextSize() {
		if (next.size() == 0)
			return 0;
		return next.peek().size();
	}

	@Override
	public LinkedList<Shape> getCurrent() {
		if (prev.isEmpty() || prev.peek() == null)
			return new LinkedList<Shape>();
		return prev.peek();
	}

	@Override
	public void undo() {
		if (prev.isEmpty())
			throw new NullPointerException();
		next.push(prev.pop());
		if (dummyload != null && prev.peek() == dummyload)
			prev.push(next.pop());
	}

	@Override
	public void redo() {
		if (next.isEmpty())
			throw new NullPointerException();
		prev.push(next.pop());
	}

	@Override
	public void add(Shape[] shape) {
		prev.push(new LinkedList<Shape>(Arrays.asList(shape)));
	}
}
