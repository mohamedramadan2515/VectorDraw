package eg.edu.alexu.csd.oop.draw;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class GUI extends JFrame {

	private static final long serialVersionUID = 1234L;
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public GUI() {
		super("Vector Draw");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1500, 750);
		setResizable(false);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		final JComboBox comboBox = new JComboBox(Engine.getInstance().getShapes());
		comboBox.setBounds(28, 713, 1500, 25);

		final JToolBar toolBar = new JToolBar();
		toolBar.setBounds(0, 0, 1500, 30);
		toolBar.add(comboBox);
		toolBar.setFloatable(false);
		contentPane.add(toolBar);

		final JPanel panel = new Canvas();
		panel.setBorder(new TitledBorder(null, null));
		panel.setBackground(Color.WHITE);
		panel.setBounds(150, 35, 1345, 710);
		contentPane.add(panel);

		final JPanel panelShapesBtns = new JPanel();
		panelShapesBtns.setBorder(new TitledBorder(null, "Shapes", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelShapesBtns.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		contentPane.add(panelShapesBtns);

		final JPanel panelButtons = new JPanel();
		panelButtons.setBorder(new TitledBorder(null, "Manage", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelButtons.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		contentPane.add(panelButtons);

		final JButton btnUndo = new JButton("Undo");
		btnUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Engine.getInstance().undo();
					Shape[] shapes = Engine.getInstance().getShapes();
					comboBox.removeAllItems();
					for (Shape shape : shapes) {
						comboBox.addItem(shape);
					}
					panel.repaint();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "There's no more UNDOs");
				}
			}
		});
		btnUndo.setPreferredSize(new Dimension(120, 25));
		panelButtons.add(btnUndo);

		final JButton btnRedo = new JButton("Redo");
		btnRedo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Engine.getInstance().redo();
					Shape[] shapes = Engine.getInstance().getShapes();
					comboBox.removeAllItems();
					for (Shape shape : shapes) {
						comboBox.addItem(shape);
					}
					panel.repaint();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "There's no more REDOs");
				}
			}
		});
		btnRedo.setPreferredSize(new Dimension(120, 25));
		panelButtons.add(btnRedo);

		final JButton btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Shape selectedShape = (Shape) comboBox.getSelectedItem();
				if (selectedShape == null) {
					JOptionPane.showMessageDialog(null, "No item to remove");
					return;
				}
				try {
					Engine.getInstance().removeShape(selectedShape);
					Shape[] shapes = Engine.getInstance().getShapes();
					comboBox.removeAllItems();
					for (Shape shape : shapes) {
						comboBox.addItem(shape);
					}
					panel.repaint();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Invalid remove");
				}
			}
		});
		btnRemove.setPreferredSize(new Dimension(120, 25));
		panelButtons.add(btnRemove);

		final JButton btnMove = new JButton("Move");
		btnMove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				final Shape oldshape = (Shape) comboBox.getSelectedItem();
				if (oldshape == null) {
					JOptionPane.showMessageDialog(null, "There is no shape to move !!");
					return;
				}
				JOptionPane.showMessageDialog(null, "Press where you want to place the shape");
				panel.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent me) {
						Point position = me.getPoint();
						panel.removeMouseListener(this);
						Shape newshape;
						try {
							newshape = (Shape) oldshape.clone();
							newshape.setPosition(position);
							Engine.getInstance().updateShape(oldshape, newshape);
							Shape[] shapes = Engine.getInstance().getShapes();
							comboBox.removeAllItems();
							for (Shape shape : shapes) {
								comboBox.addItem(shape);
							}
							contentPane.revalidate();
							contentPane.repaint();
						} catch (Exception e) {
							JOptionPane.showMessageDialog(null, "An error occurred while trying to move the shape !!");
						}
					}
				});
			}
		});
		btnMove.setPreferredSize(new Dimension(120, 25));
		panelButtons.add(btnMove);

		final JButton btnResize = new JButton("Resize");
		btnResize.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Shape oldshape = (Shape) comboBox.getSelectedItem();
				if (oldshape == null) {
					JOptionPane.showMessageDialog(null, "There is no shape to update !!");
					return;
				}
				Map<String, Double> map = oldshape.getProperties();
				JPanel panel = new JPanel();
				panel.setPreferredSize(new Dimension(100, 100));
				panel.setLayout(new FlowLayout());
				JTextField[] txts = new JTextField[map.keySet().size()];
				int index = 0;
				SortedSet<String> sortedKeys = new TreeSet<String>(map.keySet());
				for (String string : sortedKeys) {
					panel.add(new JLabel(string));
					(txts[index++] = new JTextField()).setPreferredSize(new Dimension(120, 30));
					txts[index - 1].setText(map.get(string).toString());
					panel.add(txts[index - 1]);
				}
				UIManager.put("OptionPane.minimumSize", new Dimension(200, 60 * map.size() + 50));
				int result = JOptionPane.showConfirmDialog(null, panel, "Properties", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					index = 0;
					map = new HashMap<String, Double>();
					for (String string : sortedKeys) {
						map.put(string, Double.parseDouble(txts[index++].getText()));
					}
				}
				Shape newshape = null;
				try {
					newshape = (Shape) oldshape.clone();
					newshape.setProperties(map);
				} catch (CloneNotSupportedException e1) {
					JOptionPane.showMessageDialog(null, "Invalid update");
					return;
				}
				Engine.getInstance().updateShape(oldshape, newshape);
				Shape[] shapes = Engine.getInstance().getShapes();
				comboBox.removeAllItems();
				for (Shape shape : shapes) {
					comboBox.addItem(shape);
				}
				contentPane.revalidate();
				contentPane.repaint();
			}
		});
		btnResize.setPreferredSize(new Dimension(120, 25));
		panelButtons.add(btnResize);

		final JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jfl = new JFileChooser(System.getProperty("user.dir"));
				jfl.showOpenDialog(contentPane);
				File file = jfl.getSelectedFile();
				if (file != null) {
					try {
						Engine.getInstance().save(file.getAbsolutePath());
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "The chosen file is neither XML nor JSON !!");
					}
				}
			}
		});
		btnSave.setPreferredSize(new Dimension(120, 25));
		panelButtons.add(btnSave);

		final JButton btnLoad = new JButton("Load");
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfl = new JFileChooser(System.getProperty("user.dir"));
				jfl.showOpenDialog(contentPane);
				File file = jfl.getSelectedFile();
				if (file != null) {
					try {
						Engine.getInstance().load(file.getAbsolutePath());
						Shape[] shapes = Engine.getInstance().getShapes();
						comboBox.removeAllItems();
						for (Shape shape : shapes) {
							comboBox.addItem(shape);
						}
						panel.repaint();
					} catch (Exception f) {
						JOptionPane.showMessageDialog(null, "The chosen file is neither XML nor JSON !!");
					}
				}
			}
		});
		btnLoad.setPreferredSize(new Dimension(120, 25));
		panelButtons.add(btnLoad);

		final Command command = new Command(panel, comboBox);
		JButton btnPlugins = new JButton("Add Shapes");
		btnPlugins.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jfc = new JFileChooser();
				jfc.showOpenDialog(contentPane);
				File file = jfc.getSelectedFile();
				if (file != null) {
					LinkedList<Class<? extends Shape>> shapes = (LinkedList<Class<? extends Shape>>) Engine
							.getInstance().getSupportedShapes();
					try {
						ClassFinder cf = new ClassFinder();
						Set<Class<?>> classes = cf.findClasses(Shape.class, file.getAbsolutePath());
						for (Class<?> className : classes) {
							if (!shapes.contains(className)) {
								shapes.add((Class<? extends Shape>) className);
							}
						}
					} catch (ClassNotFoundException | IOException e) {
					}
					panelShapesBtns.removeAll();
					JButton[] buttons = new JButton[(int) shapes.size()];
					int index = 0;
					command.SetCL((URLClassLoader) shapes.get(shapes.size() - 1).getClassLoader());
					for (Class<? extends Shape> c : shapes) {
						buttons[index++] = new JButton(c.getSimpleName());
						buttons[index - 1].setPreferredSize(new Dimension(120, 25));
						buttons[index - 1].addActionListener(command);
						panelShapesBtns.add(buttons[index - 1]);
					}
					panelShapesBtns.setBounds(5, 35, 140, (shapes.size() + 1) * 30);
					panelButtons.setBounds(5, (shapes.size() * 30) + 70, 140, 270);
					contentPane.revalidate();
					contentPane.repaint();
				}
			}
		});
		btnPlugins.setPreferredSize(new Dimension(120, 25));
		panelButtons.add(btnPlugins);

		LinkedList<Class<? extends Shape>> shapes = (LinkedList<Class<? extends Shape>>) Engine.getInstance()
				.getSupportedShapes();
		JButton[] buttons = new JButton[(int) shapes.size()];
		int index = 0;
		for (Class<? extends Shape> c : shapes) {
			buttons[index++] = new JButton(c.getSimpleName());
			buttons[index - 1].setPreferredSize(new Dimension(120, 25));
			buttons[index - 1].addActionListener(command);
			panelShapesBtns.add(buttons[index - 1]);
		}
		panelShapesBtns.setBounds(5, 35, 140, (shapes.size() + 1) * 30);
		panelButtons.setBounds(5, (shapes.size() * 30) + 70, 140, 270);

	}

	public class Canvas extends JPanel {

		private static final long serialVersionUID = 1234L;

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Engine.getInstance().refresh(g);
		}

	}
}
