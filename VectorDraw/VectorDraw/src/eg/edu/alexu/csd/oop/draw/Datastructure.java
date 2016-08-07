package eg.edu.alexu.csd.oop.draw;

import java.util.LinkedList;

public interface Datastructure {

	public void add(Shape shape);

	public void add(Shape[] shape);

	public void removeShape(Shape shape);

	public void updateShape(Shape oldshape, Shape newshape);

	public LinkedList<Shape> getCurrent();

	public void undo();

	public void redo();

	public int size();

	public int nextSize();

}
