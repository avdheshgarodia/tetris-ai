package net.tetris;

public class Position {

	int[][] board = new int[20][10];
	double score;
	public int x;
	public int r;

	public double[] chromosome = new double[] {0.162371,0.953728,0.223481, 0.090453};

	public Position() {
		score = 0;
	}

	public Position(int[][] array) {
		for (int y = 0; y < 20; y++) {
			for (int x = 0; x < 10; x++) {
				board[y][x] = array[x][y];

			}
		}
		score = 0;
	}

	public void eval() {

		int single_holes = 0;
		int blocks_above_holes = 0;
		boolean check = false;
		int[] columns = new int[10];
		int height_variance = 0;

		for (int x = 0; x < 10; x++) {
			check = false;
			for (int y = 0; y < 20; y++) {
				if (board[y][x] == 1) {
					check = true;
				} else {
					if (check) {
						single_holes++;
					}
				}
			}
		}

		for (int x = 0; x < 10; x++) {
			check = false;
			for (int y = 19; y >= 0; y--) {
				if (board[y][x] == 0)
					check = true;
				else {
					if (check)
						blocks_above_holes++;
				}
			}
		}

		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 20; y++) {
				if (board[y][x] == 1) {
					columns[x] = 20 - y;
					break;
				}
			}
		}
		int maxheight = columns[9];
		for (int i = 0; i < 9; i++) {
			height_variance += Math.abs(columns[i] - columns[i + 1]);
			if (columns[i] > maxheight) {
				maxheight = columns[i];
			}
		}

		score += chromosome[0] * blocks_above_holes;
		score += chromosome[1] * single_holes;
		score += chromosome[2] * height_variance;
		score += chromosome[3] * maxheight;
		
	}

	public double getScore() {
		return score;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setR(int r) {
		this.r = r;
	}

}

/*
 * public void score() {
 * 
 * for(int y=0;y<20;y++){ for(int x=0;x<10;x++) {
 * 
 * if(possible[x][y]==1) { score+=(20-y)*3.84876; }
 * 
 * } }
 * 
 * for(int y=18;y>0;y--){ for(int x=0;x<10;x++) {
 * 
 * if(possible[x][y]==1 && possible[x][y+1]==0) { score+=3.33*(20-y); xmax=x;
 * ymax=y;
 * 
 * if(y!=18 && possible[x][y+2]==0) { score+=9.11*(19-y);
 * 
 * 
 * if(y!=17 && possible[x][y+3]==0) { score+=11.71*(18-y);
 * 
 * 
 * if(y!=16 && possible[x][y+4]==0) { score += 13.51*(17-y);
 * 
 * 
 * }
 * 
 * 
 * }
 * 
 * 
 * }
 * 
 * 
 * }
 * 
 * } } for(int y=0;y<20;y++) { for(int x=0;x<10;x++) {
 * 
 * if(possible[x][y]==1 &&(x==0 || x==9)) { score-=.942222; }
 * 
 * } }
 * 
 * 
 * score-=2.1798*possibleclears;
 * 
 * for(int h=ymax;h>0;h--) { if(possible[xmax][h]==1) { score+=10.656; }
 * 
 * 
 * }
 * 
 * 
 * }
 */

