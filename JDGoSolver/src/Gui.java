import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.io.IOException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Gui extends JFrame implements MouseListener,ActionListener{

	JComponent component;
	JPanel top,letters, numbers, side;
	JLabel a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,obj, target;
	static GameLogic game;
	Target tar;
	int size=19;
	char[][] board=new char[size][size];
	private JButton loadButton, nextButton, passButton,saveButton,undoButton,resetButton,createButton;
	boolean isHumanTurn = false;
	public static void main(String args[]) {
		game = new GameLogic();
		new Gui();


	}




	//initialize gui
	public Gui() {
		setSize(900,700);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		layoutCompontents();
		this.setVisible(true);
		this.getContentPane().setBackground(new java.awt.Color(244,164,96));

		this.addMouseListener(this);
	}

	//call the drawboard to set up board
	private void layoutCompontents() {
		layoutKey();
		component=new DrawBoard();

		this.add(component, BorderLayout.CENTER);
		layoutSide();
	}
	/*
	 * Laying out the the grid of buttons and target display
	 */
	private void layoutSide() {
		side= new JPanel(new GridLayout(4,2));
		loadButton = new JButton("Load Game");
		loadButton.addActionListener(this);
		target = new JLabel("Target Display");
		side.add(loadButton);
		side.add(target);
		nextButton = new JButton("Next Move");
		nextButton.addActionListener(this);
		passButton = new JButton("Pass");
		passButton.addActionListener(this);
		saveButton = new JButton("Save Board");
		saveButton.addActionListener(this);
		undoButton = new JButton("Undo");
		undoButton.addActionListener(this);
		side.add(undoButton);
		side.add(nextButton);
		side.add(passButton);
		side.add(saveButton);
		resetButton = new JButton("Reset");
		resetButton.addActionListener(this);
		side.add(resetButton);
		createButton = new JButton("Create Board");
		createButton.addActionListener(this);
		side.add(createButton);
		this.add(side,BorderLayout.EAST);

	}


	/*
	 * Action performed method
	 * handles what each button in our button grid does
	 */
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource()==loadButton)
		{
			JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
			int returnVal = chooser.showOpenDialog(getParent());
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				FileInputOutput file = new FileInputOutput();
				file.loadBoard(chooser.getSelectedFile().getAbsolutePath());
				game.newGame(file.getLoadedBoard());
				tar=new Target(file.getTargetCoords(),game);
				updateBoard();
			}
			String[] list = {"human", "minimax", "alphaBeta"};
			String[] listC = {"black","white"};
			JComboBox choices = new JComboBox(list);
			JComboBox choicesC = new JComboBox(listC);
			JTextArea depth = new JTextArea("Enter Depth");
			Object[] options= {"Player 1", choices, choicesC, depth};
			Object[] options2= {"Player 2", choices, depth};
			int option = JOptionPane.showConfirmDialog( null, options, "Who will play player 1", JOptionPane.OK_CANCEL_OPTION);
			if (option == JOptionPane.OK_OPTION)
			{
				String value1 = (String)choices.getSelectedItem();
				String value2 = (String)choicesC.getSelectedItem();
				int value3;
				try{
					value3 = Integer.parseInt((String)depth.getText());
				}
				catch(NumberFormatException e2) {
					value3=0;
				}
				game.setPlayer1(value1, value2, value3);
			}
			else {
				return;
			}
			int option2 = JOptionPane.showConfirmDialog( null, options2, "Who will play player 1", JOptionPane.OK_CANCEL_OPTION);
			if (option2 == JOptionPane.OK_OPTION)
			{
				String value1 = (String)choices.getSelectedItem();
				int value3;
				try{
					value3 = Integer.parseInt((String)depth.getText());
				}
				catch(NumberFormatException e2) {
					value3=0;
				}
				game.setPlayer2(value1,  value3);
			}
			else {
				return;
			}
			target.setText("Target is "+tar.getCoords()[0]+","+(char)(tar.getCoords()[1]+'A'));
			repaint();
			playGame();
		}
		else if(e.getSource()==nextButton){
			playGame();
		}
		else if(e.getSource()==passButton) {
			if(isHumanTurn) {
				game.pass();
			}
			else{return;}
		}
		else if(e.getSource()==saveButton) {
			String in = JOptionPane.showInputDialog("Enter filename");
			FileInputOutput file = new FileInputOutput();
			try {
				file.outputFile(game.retBoard(),tar.getCoords(), in);
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		}
		else if(e.getSource()==undoButton) {
			game.undo();
			updateBoard();
			repaint();
		}
		else if(e.getSource()==resetButton) {
			game.reset();
			updateBoard();
			repaint();
		}
		else if(e.getSource()==createButton) {
			game.reset();
			updateBoard();
			repaint();
			new CreateBoard();
		}
	}


	/*
	 * Method for playing a gui game
	 * will wait for user input on human turn and call either ai to make a move on their turn
	 * note next move must be clicked between moves
	 */
	private void playGame() {

		if(game.pickingPlayer.getType()=='h') {
			isHumanTurn=true;
		}
		else if(game.pickingPlayer.getType()=='m') {
			Minimax mm = new Minimax(game,tar,game.pickingPlayer.getColour(),game.pickingPlayer.getDepth());
			//long startTime = System.nanoTime();
			int[] ans = mm.miniMaxSuggestMove();
			
			//long endTime = System.nanoTime();

			//long duration = (endTime - startTime);
			//System.out.println(duration);
			if(ans[0] == 100) {
				game.pass();
				obj.setText("Minimax passes");
			}
			else {
				game.makeMove(ans[0], ans[1]);
				updateBoard();
				repaint();
				obj.setText("Minimax plays at "+ans[0]+(char)(ans[1]+'A'));
			}
		}
		else {
			MinimaxAB mm = new MinimaxAB(game,tar,game.pickingPlayer.getColour(),game.pickingPlayer.getDepth());
			//long startTime = System.nanoTime();
			int[] ans = mm.miniMaxSuggestMove();
			
			//long endTime = System.nanoTime();

			//long duration = (endTime - startTime);
			//System.out.println(duration);
			if(ans[0] == 100) {
				game.pass();
				obj.setText("Minimax with pruning passes");
			}
			else {
				game.makeMove(ans[0], ans[1]);
				updateBoard();
				repaint();
				obj.setText("Minimax with pruning plays at "+ans[0]+(char)(ans[1]+'A'));
			}
		}
	}




	//Laying out the text area at top of gui to communicate with user
	private void layoutKey() {
		top = new JPanel(new BorderLayout());
		obj = new JLabel("Welcome");
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

	//updates the contents of the game board
	public void updateBoard() {
		board = game.retBoard();
	}







	/*
	 * Mouse click events handled in this method
	 * will allow a human user to place a stone. 
	 * Legality will be checked
	 */
	public void mouseClicked(MouseEvent e) {
		if (isHumanTurn==false) {
			return;
		}

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

		boolean move = game.makeMove(yCoord,xCoord);
		if(move) {
			updateBoard();
			repaint();
			System.out.println(xCoord);
			System.out.println(yCoord);
			isHumanTurn=false;
		}
		else {
			obj.setText("Invalid Move: Ko, pick again");
		}
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}





	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}





	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}





	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}



