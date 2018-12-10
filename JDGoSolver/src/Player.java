
public class Player {

		private char colour;
		private char type;
		private int depth=0;
		public Player() {
			
		}
		
		//set player colour
		public void setColour(char col) {
			this.colour=col;
			//System.out.println("set colour as" + colour);
		}
		//return player colour
		public char getColour() {
			return colour;
		}

		//return type of player
		public char getType() {
			return type;
		}

		//set type of player
		public void setType(char type) {
			this.type = type;
		}

		//return depth
		public int getDepth() {
			return depth;
		}

		//set depth (default 0 which to minimax is no depth limit)
		public void setDepth(int depth) {
			this.depth = depth;
		}
}
