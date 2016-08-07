package eg.edu.alexu.csd.oop.draw;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassFinder {
	
	public Set<Class<?>> findClasses(Class<?> Interface, String path) throws ClassNotFoundException, IOException {
		Set<Class<?>> classSet = new HashSet<Class<?>>(100);
		File file = new File(path);
		URL url = file.toURI().toURL();
		URLClassLoader cl = new URLClassLoader(new URL[]{ url }, ClassLoader.getSystemClassLoader());
		if (path.endsWith(".jar")) {
			JarFile jarFile = new JarFile(path);
			Enumeration<JarEntry> classes = jarFile.entries();
			while (classes.hasMoreElements()) {
				String className = ((JarEntry) classes.nextElement()).getName();
				if (className.endsWith(".class")) {
					className = (className.substring(0, className.length() - 6)).replace('/', '.');
					Class<?> clazz = cl.loadClass(className);
					if (Interface.isAssignableFrom(clazz) && !clazz.getSimpleName().equals("Shape")) {
						classSet.add(clazz);
					}
				}
			}
			jarFile.close();
		}
		cl.close();
		return classSet;
	}

	public Set<String> findClasses(Class<?> Interface) throws ClassNotFoundException, IOException {
		Set<String> classSet = new HashSet<String>(100);
		StringTokenizer st = new StringTokenizer(
				System.getProperty("java.class.path"),
				System.getProperty("path.separator"));
		while (st.hasMoreTokens()) {
			String s = st.nextToken();
			if (s.endsWith(".jar")) {
				JarFile jarFile = new JarFile(s);
				Enumeration<JarEntry> classes = jarFile.entries();
				while (classes.hasMoreElements()) {
					String className = ((JarEntry) classes.nextElement()).getName();
					if (className.endsWith(".class")) {
						className = (className.substring(0, className.length() - 6)).replace('/', '.');
						Class<?> clazz = Class.forName(className);
						if (Interface.isAssignableFrom(clazz) && !(clazz.getSimpleName().equals("Shape") || clazz.getSimpleName().equals("UniverseShape"))) {
							classSet.add(className);
						}
					}
				}
				jarFile.close();
			}
		}
		return classSet;
	}

}