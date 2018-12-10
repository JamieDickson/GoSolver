import java.util.ArrayList;
import java.util.Scanner;

public class Minimax {

	GameLogic game;
	char[][] boardState;
	Target tar;
	char maxColour, minColour, pickingColour;
	boolean maxPlayer;
	int maxDepth = Integer.MAX_VALUE;
	ArrayList <int[]> coords;
	int size = 19;
	//constructor
	public Minimax(GameLogic game, Target tar, char pickingColour, int maxDepth) 
	{
		this.game=game;
		this.boardState=game.copyBoard();
		this.tar=tar;
		this.pickingColour = pickingColour;
		this.minColour= tar.getColour();
		this.maxColour= tar.getAttackingColour();

		if(pickingColour==minColour) {
			maxPlayer=false;
		}
		else {maxPlayer = true;}

		if (maxDepth!=0) {
			this.maxDepth = maxDepth;
		}

	}


	/*
	 * First layer of minimax, will return the coordinates of best move available
	 * will return {100,100} if no good moves available
	 */
	public int[] miniMaxSuggestMove() {

		ArrayList<char[][]> legalMoves=game.validMoves(boardState, pickingColour);
		ArrayList <int[]> coords = new ArrayList<int[]>();
		coords = game.getCoords();
		//this is a pass
		int[] suggestion = {100,100};
		if(tar.captured(game.copyBoard())) {
			return suggestion;
		}		
		if(legalMoves.size()==0) {
			System.out.println("No legal moves");
			return suggestion;
		}

		int index  = 0;	
		if(maxPlayer) {
			int max=-1;
			for(char[][] move : legalMoves) {
				int numStones=game.getNumStones(move);
				game.previousStates.get(numStones).add(move);
				int minimum = miniMax(move,0, false, minColour,false);
				game.previousStates.get(numStones).remove(game.previousStates.get(numStones).size()-1);
				if (minimum>max) {
					max=minimum;
					suggestion = coords.get(index);

				}
				index++;
			}
		}
		else {
			int num = 1;
			for(char[][] move : legalMoves) {
				int numStones=game.getNumStones(move);
				game.previousStates.get(numStones).add(move);
				int max = miniMax(move, 0, true, maxColour,false);
				game.previousStates.get(numStones).remove(game.previousStates.get(numStones).size()-1);
				if (max<num)
				{
					num=max;
					suggestion = coords.get(index);			
				}

				index++;
			}
		}

		return suggestion;
	}


	/*
	 * recursive minimax
	 * returns 1 if target captured
	 * -1 if target lives and no legal moves left
	 * 0 if target lives and search depth reached
	 */
	public int miniMax(char[][] copyBoard, int depth, boolean maxPlayer, char colour, boolean passedLast) {
		ArrayList<char[][]> legalMoves=game.validMoves(copyBoard, colour);
		if(tar.captured(copyBoard)) {
			return 1;
		}
		else if(legalMoves.size()==0) {

			return -1;
		}
		if (depth>=maxDepth) {
			return 0;
		}
		if(maxPlayer) {
			int num = -1;
			for(char[][] move : legalMoves) {
				int numStones=game.getNumStones(move);
				game.previousStates.get(numStones).add(move);
				int min = miniMax(move, depth+1, false, minColour,false);
				game.previousStates.get(numStones).remove(game.previousStates.get(numStones).size()-1);
				if (min>num)
				{
					num=min;
				}

			}

			return num;
		}

		else {
			int num = 1;
			for(char[][] move : legalMoves) {
				int numStones=game.getNumStones(move);
				game.previousStates.get(numStones).add(move);
				int max = miniMax(move, depth+1, true, maxColour,false);
				game.previousStates.get(numStones).remove(game.previousStates.get(numStones).size()-1);
				if (max<num)
				{
					num=max;
				}

			}
			if (num==1) {
				if (!passedLast) {
					int max = miniMax(copyBoard, depth+1, false, maxColour,true);
					if(max<num) {
						num=max;
					}
				}
			}
			return num;
		}


	}


}
