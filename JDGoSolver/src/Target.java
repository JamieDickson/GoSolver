
public class Target {
	
	private int x;
	private int y;
	private char colour;
	GameLogic game;
	boolean attack;
	//constructor, set up coordinates and colour of stone the life and death fight is based on
	public Target(int[] captureCoords, GameLogic game) {
		x = captureCoords[0];
		y = captureCoords[1];
		colour =game.copyBoard()[x][y];
		this.game=game;
	}
	//return target coords
	public int[] getCoords() {
		int[] coords = {x,y};
		return coords;
	}
	//return colour of target stone
	public char getColour() {
		return colour;
	}
	//return boolean describing if it has been captured
	public boolean captured(char[][] board) {
		if(board[x][y]==colour) {
			return false;
		}
		
		else {return true;}
		
	}
	//checks if player is attacking
	public char getAttackingColour() {
		if (colour == 'b') {
			return'w';
		}
		else {
			return 'b';
		}
	}
}
