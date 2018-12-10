import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class CreateBoard extends JFrame implements MouseListener,ActionListener{
	
	int size=19;
	char[][] board=new char[size][size];
	JComponent component;
	private JPanel top;
	private JLabel obj;
	private char colour = 'b';
	private JPanel side;
	private JRadioButton blackButton;
	private JRadioButton closeBoundaryButton;
	private JRadioButton whiteButton;
	private JRadioButton openBoundaryButton;
	private JButton save ;
	
	//initialize create a board
	public CreateBoard() {
	for (int i=0;i<size;i++) {
		for (int j=0;j<size;j++) {
			board[i][j]='-';
		}
	}
		
	setSize(900,700);
	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	
	this.setVisible(true);
	this.getContentPane().setBackground(new java.awt.Color(244,164,96));
	this.addMouseListener(this);
	layoutKey();
	component=new DrawBoard();
	
	this.add(component, BorderLayout.CENTER);
	layoutSide();
	}
	/*
	 * Laying out the the radio buttons of buttons 
	 */
	private void layoutSide() {
		side= new JPanel(new GridLayout(5,1));
		side.setBackground(new java.awt.Color(244,164,96));
		blackButton = new JRadioButton();
	    blackButton.setText("Black");
	    blackButton.setBackground(new java.awt.Color(244,164,96));
	    blackButton.addActionListener(this);
	    whiteButton = new JRadioButton();
	    whiteButton.setText("White");
	    whiteButton.setBackground(new java.awt.Color(244,164,96));
	    whiteButton.addActionListener(this);
	    openBoundaryButton = new JRadioButton();
	    openBoundaryButton.setText("Open Boundary");
	    openBoundaryButton.setBackground(new java.awt.Color(244,164,96));
	    openBoundaryButton.addActionListener(this);
	    closeBoundaryButton = new JRadioButton();
	    closeBoundaryButton.setText("Close Boundary");
	    closeBoundaryButton.setBackground(new java.awt.Color(244,164,96));
	    closeBoundaryButton.addActionListener(this);
	    ButtonGroup group = new ButtonGroup();
	    group.add(blackButton);
	    group.add(whiteButton);
	    group.add(openBoundaryButton);
	    group.add(closeBoundaryButton);
	    save = new JButton("Save Board");
	    save.addActionListener(this);
	    
	    
	    side.add(blackButton);
	    side.add(whiteButton);
	    side.add(openBoundaryButton);
	    side.add(closeBoundaryButton);
	    side.add(save);
		this.add(side,BorderLayout.EAST);
		
	}
	
	//Layout message at top of gui
	private void layoutKey() {
		top = new JPanel(new BorderLayout());
		obj = new JLabel("Board Creator");
		top.setBackground(new java.awt.Color(244,164,96));
		top.add(obj, BorderLayout.NORTH);
		add(top, BorderLayout.NORTH);
		
	}
	/*
	 * Nested Class to draw and display the board on the interface
	 */
	private class DrawBoard extends JComponent{
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			String[] letterKey = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S"};
			String[] numberKey= {"0","1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18"};
			Graphics2D graph = (Graphics2D) g;
			graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			graph.setPaint(Color.BLACK);
			for (int i=0; i<size; i++){
				graph.drawString(letterKey[i], 45+30*i, 30);
				graph.drawLine(50, (50+30*i), 590, (50+30*i));
				graph.drawString(numberKey[i], 15, 55+30*i);
				graph.drawLine((50+30*i), 50, (50+30*i), 590);
			}

			for(int i=0; i<size; i++)
			{

				for(int j=0; j<size; j++)
				{
					if(board[i][j]=='b') {
						graph.setPaint(Color.BLACK);
						graph.fillOval((37+30*j), (37+30*i),25,25);
					}
					else if (board[i][j]=='w') {
						graph.setPaint(Color.WHITE);
						graph.fillOval((37+30*j), (37+30*i),25,25);
					}
					else if (board[i][j]==' ') {
						graph.setPaint(Color.BLACK);
						graph.drawOval((37+30*j), (37+30*i),25,25);
					}
				}
			}
		}
	}

	
	
	
	/*
	 * Action performed method
	 * handling button presses to change colours and save board
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==blackButton) {
			colour='b';
		}
		else if(e.getSource()==whiteButton) {
			colour='w';
		}
		else if(e.getSource()==openBoundaryButton) {
			colour=' ';
		}
		else if(e.getSource()==closeBoundaryButton) {
			colour='-';
		}
		else if(e.getSource()==save) {
			int[]coords=new int[2];
			String[] list = {"A", "B", "C","D","E","F", "G", "H","I","J","K", "L", "M","N","O","P", "Q", "R","S"};
			String[] listC = {"0", "1", "2","3","4","5", "6", "7","8","9","10", "11", "12","13","14","15", "16", "17","18"};
			JComboBox choicesCol = new JComboBox(list);
			JComboBox choicesRow = new JComboBox(listC);
			Object[] options= {"Set Target", choicesCol, choicesRow};
			int option = JOptionPane.showConfirmDialog( null, options, "Set Target", JOptionPane.OK_CANCEL_OPTION);
			   if (option == JOptionPane.OK_OPTION)
			   {
			       String value1 = (String)choicesCol.getSelectedItem();
			       int valueRow = Integer.parseInt((String) choicesRow.getSelectedItem());
			       int valueCol = value1.charAt(0)-'A';
			     
			       coords[0]= valueCol;
			       coords[1]=valueRow;
			   }
			   else {
				   return;
			   }
			
			
			String in = JOptionPane.showInputDialog("Enter filename");
			FileInputOutput file = new FileInputOutput();
			try {
				file.outputFile(board,coords, in);
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			setVisible(false); //you can't see me!
			dispose();		}
	}
	
	
	/*
	 * Mouse click events handled in this method
	 * will allow a human to place stones/boundaries
	 * No logic checked
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		int xCoord = 100;
		int yCoord = 100;

		int x = e.getX();
		int y = e.getY();
		for (int i=0;i<size;i++){
			if (x>(50+30*i)&&x<(70+30*i)){
				xCoord=0+i;
				break;
			}
		}
		for (int i=0;i<size;i++){
			if (y>(90+30*i)&&y<(110+30*i)){
				yCoord=0+i;
				break;
			}
		}
		if(yCoord<0||yCoord>18||xCoord<0||xCoord>18) {
			obj.setText("Please click closer to the point");
			return;
		}
		
		board[yCoord][xCoord]=colour;
		repaint();
		System.out.println(xCoord);
		System.out.println(yCoord);
		
		
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
