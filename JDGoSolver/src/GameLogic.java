import java.awt.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;



public class GameLogic {

	private final static int size = 19;
	private static int numStones;
	private static char[][] board= new char[size][size];
	private static char[][] originalBoard= new char[size][size];
	private static Player player1;
	private static Player player2;
	Player pickingPlayer;
	Target tar;
	private static Scanner scanner;
	public static  ArrayList<ArrayList<char[][]>> previousStates = new ArrayList<ArrayList<char[][]>>();
	private static ArrayList<char[][]> lastMove = new ArrayList<char[][]>();
	ArrayList <int[]> coords = new ArrayList<int[]>();


	public GameLogic() {

	}

	//start a new game, empty arraylists and set up
	public void newGame(char[][] b) {

		previousStates.clear();
		for (int i =0;i<361;i++)
		{
			previousStates.add(new ArrayList<char [][]>());
		}
		lastMove.clear();
		board=copyThis(b);
		pickingPlayer=player1;
		originalBoard=copyThis(b);
		lastMove.add(copyBoard());
	}

	//print the current gameboard
	public  void printBoard() {
		for(int i=0; i<size; i++)
		{

			for(int j=0; j<size; j++)
			{
				System.out.print(board[i][j]);
			}
			System.out.println();
		}
	}



	//return the number of stones on a given gameboard
	public static int getNumStones(char[][] state) {
		int stones=0;
		for(int i=0; i<size; i++)
		{

			for(int j=0; j<size; j++)
			{
				if (state[i][j]=='b'||state[i][j]=='w') {
					stones++;
				}
			}
		}
		return stones;
	}


	/*
	 * check if a stone is to be removed
	 * this will call other methods to get the chain of stones
	 * and to count all there liberties
	 */
	private char[][] checkRemove(int xCoord, int yCoord, char colour, char[][] state) 
	{

		//System.out.println("checkRemove reached");

		if(colour=='b') {
			getChain(xCoord,yCoord, 'w',state);
		}
		else {
			getChain(xCoord,yCoord, 'b',state);
		}

		
		if(chain.size()>0) {
			if(checkRemoveChain(state)) {
				for (int i=0; i<chain.size();i++) 
				{
					int x = chain.get(i)[0];
					int y = chain.get(i)[1];
					state[x][y]=' ';
					if(state==board) {
						numStones--;
					}
				}

			}
		}
		chain.clear();
		return state;
	}
	static ArrayList<int[]> chain = new ArrayList<int[]>(); 
	
	//get the whole chain of stones a stone is a part of
	private static void getChain(int xCoord, int yCoord, char colour, char[][] state)
	{
		//System.out.println("getChain reached");
		if(xCoord<0||xCoord>18||yCoord<0||yCoord>18) {
			return;
		}
		boolean contains = false;
		if (state[xCoord][yCoord]==colour) 
		{
			for (int i=0; i<chain.size();i++) {
				if (Arrays.equals(new int[]{xCoord,yCoord}, chain.get(i))){
					//System.out.println("duplicate reached");
					contains=true;
					break;
				}
			}
			if(!contains) {
				chain.add(new int[] {xCoord,yCoord});
				state[xCoord][yCoord]='g';
				getChain(xCoord+1,yCoord,colour,state);
				getChain(xCoord-1,yCoord,colour,state);
				getChain(xCoord,yCoord+1,colour,state);
				getChain(xCoord,yCoord-1,colour,state);
			}
		}
	}

//check if a single stone has any liberties
	private static boolean checkLiberties(int xCoord, int yCoord, char[][] state)
	{
		boolean liberty = false;
		if(xCoord<18) {
			if (state[xCoord+1][yCoord]=='-' ||state[xCoord+1][yCoord]==' ') {
				liberty=true;
			}
		}
		if(xCoord>0) {
			if (state[xCoord-1][yCoord]=='-' || state[xCoord-1][yCoord]==' ') {
				liberty=true;
			}
		}
		if(yCoord<18) {
			if (state[xCoord][yCoord+1]=='-' || state[xCoord][yCoord+1]==' ') {
				liberty=true;
			}
		}
		if(yCoord>0) {
			if (state[xCoord][yCoord-1]=='-' || state[xCoord][yCoord-1]==' ') {
				liberty=true;
			}
		}
		return liberty;
	}
	//cycle through a chain of stones and check if an have liberties
	private static boolean checkRemoveChain(char[][] state) {
		boolean remove=true;
		boolean done = false;
		while(!done) 
		{
			for (int i=0; i<chain.size();i++) 
			{

				int x = chain.get(i)[0];
				int y = chain.get(i)[1];
				if(checkLiberties(x,y,state)) {
					//chain.clear();
					remove=false;
					done=true;
				}
				//System.out.println(x+" , "+y+" , "+checkLiberties(x,y));
			}
			done=true;
		}

		return remove;
	}
	
	/*generates an arraylist containing valid moves
	 * used extensively by minimax
	 */
	public ArrayList<char[][]> validMoves(char[][] copyBoard, char colour) {
		ArrayList<char[][]> moveList = new ArrayList<char[][]>();
		coords.clear();
		char removeColour;
		if (colour == 'b') {
			removeColour ='w';
		}
		else {
			removeColour='b';
		}
		for(int i = 0; i< size; i++) {
			for(int j = 0; j< size; j++) {
				//System.out.println(i);
				//System.out.println(j);

				if(copyBoard[i][j]==' ') {
					char[][] move = copyThis(copyBoard);
					move[i][j]=colour; 
					move=checkRemove(i+1,j,colour,move);
					move=checkRemove(i-1,j,colour,move);
					move=checkRemove(i,j+1,colour,move);
					move=checkRemove(i,j-1,colour,move);
					turnColour(colour,move);
					move=checkRemove(i,j,removeColour,move);
					turnColour(removeColour,move);
					if(!checkKo(move,getNumStones(copyBoard))) {
						moveList.add(move);	
						int [] c = {i,j};
						coords.add(c);
					}
				}
			}
		}

		//System.out.println(moveList.size());
		//printBoard();
		//printMoveList(moveList);
		return moveList;

	}

	//returns the list of legal move coordinates
	public ArrayList<int[]> getCoords(){
		ArrayList<int[]> clone = new ArrayList<int[]>(coords.size());
		    for (int[] c : coords) {
		    	clone.add(c);
		    }
		    return clone;
		
		
	}

	//check if move breaks ko rule
	private static boolean checkKo(char[][] move, int stones) {
		
		boolean ko = false;
		for(int i=0; i<previousStates.get(getNumStones(move)).size(); i++) {
			char[][] check = previousStates.get(getNumStones(move)).get(i);
			if(Arrays.deepEquals(check,move)) {
				ko=true;	
			}
		}


		return ko;


	}


	//print the given board 
	public static  void printBoard(char[][] boardy) {
		for(int i=0; i<size; i++)
		{

			for(int j=0; j<size; j++)
			{
				System.out.print(boardy[i][j]);
			}
			System.out.println();
		}
	}



	//creates a copy of the current game board in a new array
	public char[][] copyBoard() {
		char[][] copy = new char[size][size];
		for(int i=0;i<size;i++) {
			for (int j=0; j<size; j++) {
				copy[i][j]=board[i][j];
			}
		}
		return copy;
	}

	//creates a copy of an inputed board in a new char array
	public char[][] copyThis(char[][] copyThis) {
		char[][] copy = new char[size][size];
		for(int i=0;i<size;i++) {
			for (int j=0; j<size; j++) {
				copy[i][j]=copyThis[i][j];
			}
		}
		return copy;
	}

	
	/*
	 * print out the list of boards in an arraylist
	 * useful in debugging our array lists for valid moves and ko checker
	 */
	private static void printMoveList(ArrayList<char[][]> moveList) {
		System.out.println("STARLIST");
		for (int i=0; i<moveList.size(); i++) {
			char[][] move = moveList.get(i);
			for(int k=0; k<size; k++)
			{

				for(int j=0; j<size; j++)
				{
					System.out.print(move[k][j]);
				}
				System.out.println();
			}
			System.out.println("\n \n \n");

		}
		System.out.println("ENDLIST");
		/*for cycling through each set of moves for(;;) {
			Scanner in = new Scanner(System.in);
			String bi =in.next();
			if (bi.equals("c")) {
				//in.close();
				break;
			}
			}*/
	}
	
	//return board so other classes can access easily
	public char[][] retBoard(){
		return board;
	}
	
	//pass a turn
	public void pass() {
		if (pickingPlayer==player1) {
			pickingPlayer=player2;
		}
		else {
			pickingPlayer=player1;
		}
	}

	/*
	 * method to place a stone on the board
	 * returns true if move successful
	 * returns false if move is illegal
	 */
	public boolean makeMove(int xCoord, int yCoord) {
		if (board[xCoord][yCoord]=='-'||board[xCoord][yCoord]==' ') {
			board[xCoord][yCoord]=pickingPlayer.getColour();
			numStones++;
			checkRemove(xCoord+1,yCoord,pickingPlayer.getColour(),board);
			checkRemove(xCoord-1,yCoord,pickingPlayer.getColour(),board);
			checkRemove(xCoord,yCoord+1,pickingPlayer.getColour(),board);
			checkRemove(xCoord,yCoord-1,pickingPlayer.getColour(),board);


			turnColour(pickingPlayer.getColour(),board);
			if(pickingPlayer.getColour()=='b') {
				checkRemove(xCoord,yCoord,'w',board);
				turnColour('w',board);
			}
			else {
				checkRemove(xCoord,yCoord,'b',board);
				turnColour('b',board);
			}
			char[][] move = copyBoard();
			if(checkKo(move, getNumStones(move))) {
				System.out.println("Invalid move: Ko");
				board=lastMove.get(lastMove.size()-1);
				numStones=getNumStones(board);
				return false;
			}
			previousStates.get(getNumStones(move)).add(move);
			lastMove.add(move);

		}
		else {
			System.out.println("Occupied! Pick again");
			return false;
		}
		if(pickingPlayer==player1) {
			pickingPlayer=player2;
		}
		else {
			pickingPlayer=player1;
		}


		return true;

	}
	/*
	 * turn the colour of a stone a chosen colour
	 * used when checking if stones are captured to avoid entering a loop of checking the same stones
	 * used to turn colours back to original colour if they have liberties
	 */
	private void turnColour(char colour, char[][]state) {
		if(colour=='b') {
			for(int i = 0; i<size; i++) {
				for (int j = 0; j<size;j++) {
					if (state[i][j]=='g') {
						state[i][j]='w';
					}
				}
			}
		}
		else {
			for(int i = 0; i<size; i++) {
				for (int j = 0; j<size;j++) {
					if (state[i][j]=='g') {
						state[i][j]='b';
					}
				}
			}
		}
	}
	
	//methos to figure out what players turn it is
	public boolean isPlayer1Turn(){
		boolean turn = false;
		if(pickingPlayer==player1) {
			turn=true;
		}

		return turn;
	}
	
	//method to access target
	public Target getTar() {
		return tar;
	}
	
	//set up player 1
	public void setPlayer1(String player, String player1C, int depth) {
		player1 = new Player();
		player1.setColour(player1C.charAt(0));
		player1.setType(player.charAt(0));
		player1.setDepth(depth);
		pickingPlayer=player1;
	}
	
	//set up player 2
	public void setPlayer2(String player,int depth) {
		player2 = new Player();
		player2.setType(player.charAt(0));
		player2.setDepth(depth);
		if(player1.getColour()=='b') {
			player2.setColour('w');
		}
		else {
			player2.setColour('b');
		}
		
		
	}
	//undo last move
	public void undo() {
		if(lastMove.size()>1) {
		pass();
		for(int i=0; i<previousStates.get(getNumStones(board)).size(); i++) {
			char[][] check = previousStates.get(getNumStones(board)).get(i);
			if(Arrays.deepEquals(check,board)) {
				previousStates.get(getNumStones(board)).remove(i);
				
			}
		}
		board=copyThis(lastMove.get(lastMove.size()-2));
		
		lastMove.remove(lastMove.size()-1);
	
		}
	
	}
	//reset current game
	public void reset() {
		newGame(originalBoard);
	}
}

