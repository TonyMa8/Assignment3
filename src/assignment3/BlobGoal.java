package assignment3;

import java.awt.Color;

public class BlobGoal extends Goal{

	public BlobGoal(Color c) {
		super(c);
	}

	@Override
	public int score(Block board) {
		/*
		 * ADD YOUR CODE HERE
		 */
		int score=0;
		int curScore;
		int boardDimensions = (int)Math.pow(2, board.getMaxDepth());
		Color[][] cList = board.flatten();
		boolean[][] bList = new boolean[boardDimensions][boardDimensions];
		for (int i=0; i<boardDimensions;i++){
			for (int j=0; j<boardDimensions;j++){
				curScore = this.undiscoveredBlobSize(i,j,cList,bList);
				if(curScore>score){
					score=curScore;
				}
			}
		}
		return score;
	}

	@Override
	public String description() {
		return "Create the largest connected blob of " + GameColors.colorToString(targetGoal)
				+ " blocks, anywhere within the block";
	}


	public int undiscoveredBlobSize(int i, int j, Color[][] unitCells, boolean[][] visited) {
		/*
		 * ADD YOUR CODE HERE
		 */
		if (i<0 || i>unitCells.length-1 || j<0 || j>unitCells.length-1 || unitCells[i][j]!=this.targetGoal || visited[i][j] ){
			return 0;
		}
		visited[i][j]=true;
		return 1+undiscoveredBlobSize(i+1,j,unitCells,visited)+undiscoveredBlobSize(i-1,j,unitCells,visited)+undiscoveredBlobSize(i,j+1,unitCells,visited)+undiscoveredBlobSize(i,j-1,unitCells,visited);

	}

}
