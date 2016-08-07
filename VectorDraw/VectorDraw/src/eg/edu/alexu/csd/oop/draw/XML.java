package eg.edu.alexu.csd.oop.draw;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class XML {

    public void save(Shape[] shapes, String path) throws Exception {
        XMLEncoder encoder = new XMLEncoder(
        					 new BufferedOutputStream(
        					 new FileOutputStream(path)));
        encoder.writeObject(shapes);
        encoder.close();  
    }

    public Shape[] load(String path) throws Exception {
    	ClassLoader cl = this.getClass().getClassLoader();
    	if(Engine.getInstance().getValidShapes() != null && Engine.getInstance().getValidShapes().size() != 0) {
    		cl = Engine.getInstance().getValidShapes().get(Engine.getInstance().getValidShapes().size() - 1).getClassLoader();
    	}
    	XMLDecoder decoder = new XMLDecoder(
        					 new BufferedInputStream(
        					 new FileInputStream(path)), null, null,
        					 cl);
        Object[] objects = (Object[]) decoder.readObject();
        Shape[] shapes = new Shape[objects.length];
        int i = 0;
        for (Object object : objects) {
        	shapes[i++] = (Shape) object;
        }
        decoder.close();
        return shapes;
    }

}