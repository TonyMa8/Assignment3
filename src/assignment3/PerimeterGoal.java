package assignment3;

import java.awt.Color;

public class PerimeterGoal extends Goal{

	public PerimeterGoal(Color c) {
		super(c);
	}

	@Override
	public int score(Block board) {
		/*
		 * ADD YOUR CODE HERE
		 */
		int score=0;
		Color[][] colorBoard=board.flatten();
		for (int i=0; i< (int)Math.pow(2,board.getMaxDepth());i++){
			if (i==0 || i==(int)Math.pow(2,board.getMaxDepth())-1){
				for (int j=0;j<(int)Math.pow(2,board.getMaxDepth());j++){
					if (colorBoard[i][j]==this.targetGoal) {
						score++;
						if (j == 0 || j == (int) Math.pow(2, board.getMaxDepth()) - 1) {
							score++;
						}
					}
				}
			}
			else{
				if (colorBoard[i][0]==this.targetGoal){
					score++;
				}
				if (colorBoard[i][(int)Math.pow(2,board.getMaxDepth())-1]==this.targetGoal){
					score++;
				}
			}
		}
		return score;
	}

	@Override
	public String description() {
		return "Place the highest number of " + GameColors.colorToString(targetGoal)
				+ " unit cells along the outer perimeter of the board. Corner cell count twice toward the final score!";
	}

}
