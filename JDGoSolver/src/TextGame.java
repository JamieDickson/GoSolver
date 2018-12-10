import java.io.IOException;
import java.util.Scanner;

public class TextGame {
	static Target tar;
	public static void main(String[] args) {

		playGame();

	}

	/*
	 * Allows user to imput commands to load game, exit program
	 * loading a game prompts the user for filename and player information
	 */
	public static void playGame() {
		GameLogic game = new GameLogic();

		//game.loadBoard();
		//game.printBoard();
		//tar=game.getTar();
		boolean quit=false;
		while(!quit) {
			Scanner scanner = new Scanner(System.in);
			for(;;) {
				System.out.println("type load to load in a board or quit to quit");
				String in = scanner.next();
				if (in.equals("quit")){System.exit(0);}
				else if (in.equals("create")) {
					break;
				}
				else if(in.equals("load")) {

					String name;
					String player1, player2, player1C;
					int depth;
					for(;;) {
						System.out.print("enter file name");
						name= scanner.next();
						FileInputOutput file = new FileInputOutput();
						if(file.loadBoard("Solvable Boards/"+name)) {
							game.newGame(file.getLoadedBoard());
							tar=new Target(file.getTargetCoords(),game);
							break;
						}
						else {
							System.out.println("invalid filename");
						}
					}
					for(;;) {
						System.out.println("Set who plays player 1.  Type h for human, m for minimax, a for minimax with ab pruning");
						player1=scanner.next();
						if(player1.charAt(0)=='h'||player1.charAt(0)=='m'||player1.charAt(0)=='a') {
							break;
						}
						else {System.out.println("invalid input");}
					}
					for(;;) {
						System.out.println("Set who plays player 2.  Type h for human, m for minimax, a for minimax with ab pruning");
						player2=scanner.next();
						if(player2.charAt(0)=='h'||player2.charAt(0)=='m'||player2.charAt(0)=='a') {
							break;
						}
						else {System.out.println("invalid input");}
					}
					for(;;) {
						System.out.println("Set player 1 colour, enter b for black or w for white. Player 2 will automatically be set as other colour");
						player1C=scanner.next();
						if(player1C.charAt(0)=='b'||player1C.charAt(0)=='w') {
							break;
						}
						else {System.out.println("invalid input");}
					}
					for(;;) {
						System.out.println("enter max search depth. enter 0 for no max depth");
						depth=scanner.nextInt();
						if(depth>=0) {
							break;
						}
					}
					
					game.setPlayer1(player1, player1C,depth);
					game.setPlayer2(player2,depth);
					break;
				}
				else {System.out.println("invalid input");}
			}

			//scanner.close();
			play(game,tar,scanner);
		}

	}

	/*
	 * method that will play one game with a loaded in board
	 * save board command and end current came command in here
	 * allows user to input coordinates to place stones
	 */
	private static void play(GameLogic game, Target tar2, Scanner scanner) {

		int xCoord;
		int yCoord;
		boolean gameDone = false;
		//Scanner scanner = new Scanner(System.in);
		while (!gameDone) {
			System.out.println();
			game.printBoard();
			if (game.pickingPlayer.getType()=='h') {
				for(;;) {
					System.out.println("Enter the the row you want to play on(0-18). enter 100 to pass");
					xCoord = scanner.nextInt();
					if (xCoord<19&&xCoord>=0 || xCoord==100) {
						break;
					}
					else {
						System.out.println("invalid input");
					}

				}
				for(;;) {
					System.out.println("Enter the the column you want to play on(0-18). enter 100 to pass");
					yCoord = scanner.nextInt();
					if (yCoord<19&&yCoord>=0 || yCoord==100) {
						break;
					}
					else {
						System.out.println("invalid input");
					}
				}
				game.makeMove(xCoord, yCoord);
			}
			else if(game.pickingPlayer.getType()=='m') {
				Minimax mm = new Minimax(game,tar,game.pickingPlayer.getColour(),game.pickingPlayer.getDepth());
				int[] ans = mm.miniMaxSuggestMove();
				if(ans[0] == 100) {
					game.pass();
					System.out.println("Minimax passes");
				}
				else {
					game.makeMove(ans[0], ans[1]);
					System.out.println("Minimax plays at "+ans[0]+ans[1]);
				}
			}
			else {
				MinimaxAB mm = new MinimaxAB(game,tar,game.pickingPlayer.getColour(),game.pickingPlayer.getDepth());
				int[] ans = mm.miniMaxSuggestMove();
				if(ans[0] == 100) {
					game.pass();
					System.out.println("Minimax with pruning passes");
				}
				else {
					game.makeMove(ans[0], ans[1]);
					System.out.println("Minimax with pruning plays at "+ans[0]+ans[1]);
				}
			}

			//System.out.println(tar.captured());
			System.out.println("Type end to end this game, save to save the current board. Type anything else to cont.");
			String l = scanner.next();
			if (l.equals("end")) {
				//scanner.close();
				gameDone=true;
			}
			if(l.equals("save")) {
				System.out.println("enter file name");
				String in = scanner.next();

				FileInputOutput file = new FileInputOutput();
				try {
					file.outputFile(game.retBoard(),tar.getCoords(), in);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}


	}
}
