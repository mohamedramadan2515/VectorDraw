package eg.edu.alexu.csd.oop.draw;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.colorchooser.AbstractColorChooserPanel;

public class Command implements ActionListener {

	private Point position;
	private Color color, fillcolor;
	private JPanel panel;
	private JComboBox<Shape> cb;
	private URLClassLoader cl = (URLClassLoader) ClassLoader.getSystemClassLoader();
	private boolean flag = false;

	public Command(JPanel panel, JComboBox<Shape> cb) {
		this.panel = panel;
		this.cb = cb;
	}
	
	public void SetCL(URLClassLoader cl) {
		this.cl = cl;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (flag == false) {
			final String string = e.getActionCommand();
			final JButton button = (JButton) e.getSource();
			button.setEnabled(false);
			flag = true;
			panel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					position = e.getPoint();
					flag = false;
					button.setEnabled(true);
					panel.removeMouseListener(this);
					if (getColors() == false) {
						return;
					}
					getShapeProperties(string);
					refreshCB();
				}
			});
		} else {
			UIManager.put("OptionPane.minimumSize", new Dimension(300, 80));
			JOptionPane.showMessageDialog(null, "Please finish defining the selected shape before drawing another");
		}
	}

	private void refreshCB() {
		cb.removeAllItems();
		Shape[] shapes = Engine.getInstance().getShapes();
		for (Shape shape : shapes) {
			cb.addItem(shape);
		}
		panel.repaint();
	}
	
	private boolean getColors() {
		JColorChooser palette = new JColorChooser(Color.WHITE);
		palette.setPreviewPanel(new JPanel());
		AbstractColorChooserPanel[] panels = palette.getChooserPanels();
		for (AbstractColorChooserPanel accp : panels) {
			if (accp.getDisplayName().equals("HSV")) {
				int result = JOptionPane.showConfirmDialog(null, accp, "Choose the border color",
						JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					color = palette.getColor();
				} else {
					return false;
				}
				palette.setColor(Color.WHITE);
				result = JOptionPane.showConfirmDialog(null, accp, "Choose the fill color",
						JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					fillcolor = palette.getColor();
				} else {
					return false;
				}
			}
		}
		return true;
	}

	private void getShapeProperties(String shapeName) {
		try {
			Class<?> clazz = Class.forName("eg.edu.alexu.csd.oop.draw." + shapeName, false, cl);
			final Shape shape = (Shape) clazz.newInstance();
			shape.setPosition(position);
			shape.setColor(color);
			shape.setFillColor(fillcolor);
			final Map<String, Double> map = shape.getProperties();
			if (map != null) {
				if (shapeName.equals("Line")) {
					JOptionPane.showMessageDialog(null, "Choose the other point");
					flag = true;
					panel.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							flag = false;
							Point position = e.getPoint();
							panel.removeMouseListener(this);
							map.put("Second Point X", position.getX());
							map.put("Second Point Y", position.getY());
							shape.setProperties(map);
							Engine.getInstance().addShape(shape);
							refreshCB();
						}
					});
				} else if (shapeName.equals("Triangle")) {
					JOptionPane.showMessageDialog(null, "Choose the second point");
					flag = true;
					panel.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							Point position = e.getPoint();
							panel.removeMouseListener(this);
							map.put("Second Point X", position.getX());
							map.put("Second Point Y", position.getY());
							panel.revalidate();
							panel.repaint();
							JOptionPane.showMessageDialog(null, "Choose the third point");
							panel.addMouseListener(new MouseAdapter() {
								@Override
								public void mouseClicked(MouseEvent e) {
									flag = false;
									Point position = e.getPoint();
									panel.removeMouseListener(this);
									map.put("Third Point X", position.getX());
									map.put("Third Point Y", position.getY());
									shape.setProperties(map);
									Engine.getInstance().addShape(shape);
									refreshCB();
								}
							});
						}
					});
				} else {
					JPanel panel = new JPanel();
					panel.setPreferredSize(new Dimension(100, 100));
					panel.setLayout(new FlowLayout());
					JTextField[] txts = new JTextField[map.keySet().size()];
					int index = 0;
					SortedSet<String> sortedKeys = new TreeSet<String>(map.keySet());
					for (String key : sortedKeys) {
						panel.add(new JLabel(key));
						(txts[index++] = new JTextField()).setPreferredSize(new Dimension(120, 30));
						panel.add(txts[index - 1]);
					}
					UIManager.put("OptionPane.minimumSize", new Dimension(200, 60 * map.size() + 50));
					int result = JOptionPane.showConfirmDialog(null, panel, "Properties", JOptionPane.OK_CANCEL_OPTION);
					if (result == JOptionPane.OK_OPTION) {
						index = 0;
						for (String key : sortedKeys) {
							map.put(key, Double.parseDouble(txts[index++].getText()));
						}
					} else {
						return;
					}
					shape.setProperties(map);
					Engine.getInstance().addShape(shape);
					refreshCB();
				}
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
		}
	}

}
