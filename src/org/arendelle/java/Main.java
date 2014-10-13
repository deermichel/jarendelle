
//
//  JArendelle - Java Portation of the Arendelle Language
//  Copyright (c) 2014 Micha Hanselmann <h@arendelle.org>
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.
//

package org.arendelle.java;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.arendelle.java.engine.CodeScreen;
import org.arendelle.java.engine.MasterEvaluator;

public class Main {

	public static void main(String[] args) {
		
		/*String code = "";
		CodeScreen screen = new CodeScreen(80, 24);
		
		try {
			MasterEvaluator.evaluate(code, screen);
		} catch(Exception e) {
			System.err.println(e.toString());
		}*/
		
		new ArendelleDemo();

	}

}


/**ArendelleDemo real-time interpreter for Java
 * 
 * @author mh
 * @version PreAlpha
 */
class ArendelleDemo implements KeyListener, ActionListener {
	
	//GUI objects
	JFrame window;
	JTextArea textCode = new JTextArea(0, 35);
	Panel panelResult = new Panel();
	JButton buttonOpen = new JButton("Open");
	JButton buttonSave = new JButton("Save");
	JButton buttonExportImage = new JButton("Export Image");
	
	//pointer
	int x = 0;
	int y = 0;
	int color = 1;
	
	//arendelle variables
	String path = "";
	
	//drawing variables
	int width = 0;
	int height = 0;
	int cellWidth = 0;
	int cellHeight = 0;
	final int cellsX = 40;
	final int cellsY = 30;
	int result[][] = new int[cellsX][cellsY];
	

	//main method
	public static void main(String[] args) {
		new ArendelleDemo();
	}
	
	//constructor
	public ArendelleDemo() {
		
		path = System.getProperty("user.dir");

		window = new JFrame("JArendelle");
		//window.setSize(800, 600);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		
		//textCode.addActionListener(this);
		textCode.addKeyListener(this);
		buttonOpen.addActionListener(this);
		buttonSave.addActionListener(this);
		buttonExportImage.addActionListener(this);
		panelResult.setPreferredSize(new Dimension(800, 600));
		
		window.setLayout(new BorderLayout());
		
		JScrollPane scrollPane = new JScrollPane(textCode, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		JPanel bottomBar = new JPanel();
		bottomBar.add(buttonOpen);
		bottomBar.add(buttonSave);
		bottomBar.add(buttonExportImage);
		
		window.add(panelResult, BorderLayout.WEST);
		window.add(scrollPane, BorderLayout.EAST);
		window.add(bottomBar, BorderLayout.SOUTH);
		
		window.pack();
		window.setVisible(true);
		
		width = panelResult.getWidth();
		height = panelResult.getHeight();
		cellWidth = width / cellsX;
		cellHeight = height / cellsY;
		
	}
	
	//compile code
	private void compile(String code) {
		
		CodeScreen screen = new CodeScreen(cellsX, cellsY, path);
		
		try {
			MasterEvaluator.evaluate(code, screen);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
		
		result = screen.screen;
		window.setTitle(screen.title);
		
		/*for (int i = 0; i < code.length(); i++) {
			
			switch (code.charAt(i)) {
			
			case 'p':
				result[x][y] = color;
				break;
				
			case 'r':
				x++;
				if (x > cellsX) x = cellsX;
				break;
				
			case 'l':
				x--;
				if (x < 0) x = 0;
				break;
				
			case 'd':
				y++;
				if (y > cellsY) y = cellsY;
				break;
				
			case 'u':
				y--;
				if (y < 0) y = 0;
				break;
				
			case 'n':
				color++;
				if (color > 4) color = 1;
				break;
				
			case 'i':
				x = 0;
				y = 0;
				break;
				
			case '[':
				
				String mathExpr = "";
				for (int j = i + 1; code.charAt(j) != ','; j++) {
					mathExpr += String.valueOf(code.charAt(j));
					i = j;
				}
				do {
					i++;
				} while (code.charAt(i) == ' ');
				
				ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
				int loopTimes = 0;
				try {
					Object result = engine.eval(mathExpr);
					if (result instanceof Integer) {
						loopTimes = (Integer)result;
					} else if (result instanceof Double) {
						loopTimes = ((Double)result).intValue();
					}
				} catch (ScriptException e) {
					System.err.println(e.toString());
				}
				
				String loopCode = "";
				for (int j = i; j != code.lastIndexOf(']'); j++) {
					loopCode += String.valueOf(code.charAt(j));
					i = j;
				}				
				i++;
				
				for (int j = 0; j < loopTimes; j++) compile(loopCode);
				
				break;
			
			}
			
		}*/
		
	}
	
	
	//panel class
	class Panel extends JPanel {
		
		//needed by Java
		private static final long serialVersionUID = 1253980742428501651L;

		
		//constructor
		Panel() {
			
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			for (int x = 0; x < cellsX; x++) {
				for (int y = 0; y < cellsY; y++) {
					
					switch (result[x][y]) {
					
					case 0:
						g.setColor(Color.BLACK);
						g.fillRect(x * cellWidth, y * cellHeight, cellWidth, cellHeight);
						break;
						
					case 1:
						g.setColor(Color.WHITE);
						g.fillRect(x * cellWidth, y * cellHeight, cellWidth, cellHeight);
						break;
						
					case 2:
						g.setColor(Color.LIGHT_GRAY);
						g.fillRect(x * cellWidth, y * cellHeight, cellWidth, cellHeight);
						break;
						
					case 3:
						g.setColor(Color.GRAY);
						g.fillRect(x * cellWidth, y * cellHeight, cellWidth, cellHeight);
						break;
						
					case 4:
						g.setColor(Color.DARK_GRAY);
						g.fillRect(x * cellWidth, y * cellHeight, cellWidth, cellHeight);
						break;
					
					}
					
				}
			}
			
		}
		
	}


	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	
	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	
	@Override
	public void keyReleased(KeyEvent e) {
		
		//real-time compiling
		if (e.getSource() == textCode) {
			compile(textCode.getText());
			panelResult.repaint();
		}
		
	}

	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource()==buttonOpen) {
			
			JFileChooser fc = new JFileChooser();
			fc.showOpenDialog(null);
			
			try {
				
				path = fc.getSelectedFile().getParent();
				String code = new String(Files.readAllBytes(fc.getSelectedFile().toPath()), StandardCharsets.UTF_8);
				
				textCode.setText(code);
				compile(code);
				panelResult.repaint();

				
			} catch (Exception e1) {
				System.err.println(e1.toString());
			}
			
			
		} else if (e.getSource()==buttonExportImage) {
			
			BufferedImage image = new BufferedImage(panelResult.getWidth(), panelResult.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics g = image.createGraphics();
			panelResult.paint(g);
			g.dispose();
			
			JFileChooser fc = new JFileChooser();
			fc.setFileFilter(new FileNameExtensionFilter("PNG file", "png"));
			fc.showSaveDialog(null);
			
			try {
				ImageIO.write(image, "png", new File(fc.getSelectedFile().toString() + ".png"));
			} catch (Exception e1) {
				System.err.println(e1.toString());
			}
			
		} else if (e.getSource()==buttonSave) {
			
			JFileChooser fc = new JFileChooser();
			fc.setFileFilter(new FileNameExtensionFilter("Arendelle file", "arendelle"));
			fc.showSaveDialog(null);
			
			try {
				PrintWriter writer = new PrintWriter(fc.getSelectedFile().toString() + ".arendelle");
				writer.print(textCode.getText());
				writer.close();
			} catch (Exception e1) {
				System.err.println(e1.toString());
			}
			
		}
		
	}

}
