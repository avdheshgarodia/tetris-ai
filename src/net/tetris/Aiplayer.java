package net.tetris;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Aiplayer extends BasicGameState {

	public Aiplayer(int state) {
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		gc.setShowFPS(false);
		pf = 0;

		linescleared = 0;

		for (int y = 0; y < 20; y++) {
			for (int x = 0; x < 10; x++) {
				occupied[x][y] = 0;
			}
		}

		gc.setAlwaysRender(true);
		if (bag.isEmpty()) {
			for (int i = 0; i < 7; i++) {
				bag.add(i);
			}
			for (int i = 0; i < token.length; i++) {
				token[i] = bag.remove((int) (bag.size() * Math.random()));
			}
		}

		cutoff = max_depth;

		if (cutoff > 3) {
			cutoff = 3;
		}
	}

	public void drawToken(int x, int y, int[] xArray, int[] yArray, int t) {
		for (int i = 0; i < 4; i++) {
			drawCell(x + xArray[i], y + yArray[i], t);
		}
	}

	public void eraseToken(int x, int y, int[] xArray, int[] yArray) {
		for (int i = 0; i < 4; i++) {
			eraseCell(x + xArray[i], y + yArray[i]);
		}
	}

	public void drawCell(int x, int y, int c) {
		occupied[x][y] = 1;
	}

	public void eraseCell(int x, int y) {
		occupied[x][y] = 0;
	}

	public boolean isValidPosition(int x, int y, int tokenNumber,
			int rotationNumber) {
		int[] xArray = xRotationArray[tokenNumber][rotationNumber];
		int[] yArray = yRotationArray[tokenNumber][rotationNumber];

		for (int i = 0; i < 4; i++) // loop over the four cells
		{
			int xCell = x + xArray[i];
			int yCell = y + yArray[i];

			// range check
			if (xCell < 0)
				return false;
			if (xCell >= 10)
				return false;
			if (yCell < 0)
				return false;
			if (yCell >= 20)
				return false;

			// occupancy check
			if (occupied[xCell][yCell] == 1)
				return false;
		}
		return true;
	}

	public void clearCompleteRow(int[] completed) {
		// erase
		for (int i = 0; i < completed.length; i++) {
			if (completed[i] == 1) {
				for (int x = 0; x < 10; x++) {
					occupied[x][i] = 0;
				}
			}
		}

	}

	public void shiftDown(int[] completed) {
		for (int row = 0; row < completed.length; row++) {
			if (completed[row] == 1) {
				for (int y = row; y >= 1; y--) {
					for (int x = 0; x < 10; x++) {
						occupied[x][y] = occupied[x][y - 1];
					}
				}
			}
		}
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		g.setColor(Color.white);
		g.drawString("Lines:" + linescleared, 250, 40);
		g.setColor(Color.white);
		g.fillRect(0, 0, 240, 480);
		g.drawString("Heuristic Eval:" + score, 250, 60);
		g.drawString("Fall delay:" + delay, 250, 80);
		g.drawString("Total pieces:" + piececount, 250, 100);
		g.drawString("Search Depth:" + max_depth, 250, 120);

		if (human)
			g.drawString("Human Playing", 250, 140);
		else
			g.drawString("Computer Playing", 250, 140);

		if (animation)
			g.drawString("animation: ON", 250, 160);
		else
			g.drawString("animation: OFF", 250, 160);

		for (int d = 1; d <= max_depth; d++) {

			int[] xArray = xRotationArray[token[d]][2];
			int[] yArray = yRotationArray[token[d]][2];

			for (int i = 0; i < 4; i++) {
				g.setColor(Color.red);
				g.fillRect(250 + xArray[i] * 24 + 1, 100 + (d * 100)
						+ yArray[i] * 24 + 1, 22, 22);
			}
		}

		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 20; y++) {
				if (occupied[x][y] == 1) {
					// draw cell
					g.setColor(Color.white);
					g.fillRect(x * 24, y * 24, 24, 24);
					g.setColor(Color.red);
					g.fillRect(x * 24 + 1, y * 24 + 1, 22, 22);
				}
			}
		}
	}

	public void drawToken(int[][] array, int x, int y, int[] xArray,
			int[] yArray, int t) {
		for (int i = 0; i < 4; i++) {
			drawCell(array, x + xArray[i], y + yArray[i], t);
		}
	}

	public void drawCell(int[][] array, int x, int y, int c) {
		array[x][y] = 1;
	}

	public boolean isValidPosition(int[][] array, int x, int y,
			int tokenNumber, int rotationNumber) {
		int[] xArray = xRotationArray[tokenNumber][rotationNumber];
		int[] yArray = yRotationArray[tokenNumber][rotationNumber];

		for (int i = 0; i < 4; i++) // loop over the four cells
		{
			int xCell = x + xArray[i];
			int yCell = y + yArray[i];

			// range check
			if (xCell < 0)
				return false;
			if (xCell >= 10)
				return false;
			if (yCell < 0)
				return false;
			if (yCell >= 20)
				return false;

			// occupancy check
			if (array[xCell][yCell] == 1)
				return false;
		}
		return true;
	}

	public void addFallingToken() {

		x = 5;
		y = 0;

		for (int i = 0; i < max_depth; i++) {
			token[i] = token[i + 1];
		}
		token[max_depth] = bag.remove((int) (bag.size() * Math.random()));

		if (bag.isEmpty()) {
			for (int i = 0; i < 7; i++) {
				bag.add(i);
			}
		}

		piececount++;
		xArray = xRotationArray[token[0]][0];
		yArray = yRotationArray[token[0]][0];

		reachFloor = false;
	}

	public void clearCompleteRow(int[][] array, int[] completed) {
		// erase
		for (int i = 0; i < completed.length; i++) {
			if (completed[i] == 1) {
				for (int x = 0; x < 10; x++) {
					array[x][i] = 0;
				}
			}
		}

	}

	public void shiftDown(int[][] array, int[] completed) {
		for (int row = 0; row < completed.length; row++) {
			if (completed[row] == 1) {
				for (int y = row; y >= 1; y--) {
					for (int x = 0; x < 10; x++) {
						array[x][y] = array[x][y - 1];

					}
				}
			}
		}
	}

	public void checkRowCompletion(int[][] array, boolean clear) {
		int[] complete = new int[20];
		for (int y = 0; y < 20; y++) // 20 rows
		{
			int filledCell = 0;
			for (int x = 0; x < 10; x++) // 10 columns
			{
				if (array[x][y] == 1)
					filledCell++;

			}
			if (filledCell == 10) // row completed
			{
				complete[y] = 1;
				if (clear) {
					linescleared++;
				}
			}
		}

		clearCompleteRow(array, complete);
		shiftDown(array, complete);
	}

	public boolean generatePositions(int[][] board, int depth) {

		if (depth == 0) {
			positions.add(new Position(board));
			positions.get(positions.size() - 1).setX(sizecounts);
			positions.get(positions.size() - 1).setR(counts);
			return true;
		} else {

			int[][] tempBoard = new int[10][20];

			for (int y = 0; y < 20; y++) {
				for (int x = 0; x < 10; x++) {
					tempBoard[x][y] = board[x][y];
				}
			}

			for (int rotation_index = 0; rotation_index < rotationindeces[token[max_depth
					- depth]]; rotation_index++) {

				for (int xpos = 0; xpos < 10; xpos++) {

					if (isValidPosition(board, xpos, 0,
							token[max_depth - depth], rotation_index)) {

						int height = 0;
						while (isValidPosition(board, xpos, height,
								token[max_depth - depth], rotation_index)) {
							height++;
						}
						height--;
						xArray = xRotationArray[token[max_depth - depth]][rotation_index];
						yArray = yRotationArray[token[max_depth - depth]][rotation_index];
						drawToken(board, xpos, height, xArray, yArray,
								token[max_depth - depth]);
						checkRowCompletion(board, false);

						int[][] sendBoard = new int[10][20];

						for (int y = 0; y < 20; y++) {
							for (int x = 0; x < 10; x++) {
								sendBoard[x][y] = board[x][y];
							}
						}

						if (depth == max_depth) {
							sizecounts = xpos;
							counts = rotation_index;

						}
						generatePositions(sendBoard, depth - 1);

						for (int y = 0; y < 20; y++) {
							for (int x = 0; x < 10; x++) {
								board[x][y] = tempBoard[x][y];
							}
						}
					}
				}
			}
		}
		return true;
	}

	public void generatePositions() {
		int[][] tempBoard = new int[10][20];

		for (int y = 0; y < 20; y++) {
			for (int x = 0; x < 10; x++) {
				tempBoard[x][y] = occupied[x][y];
			}
		}
		generatePositions(tempBoard, max_depth);

	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		c += delta;
		counter += delta;
		counter2 += delta;
		Input input = gc.getInput();
		
		if (input.isKeyDown(Input.KEY_A))
			delay += 1;
		else if (input.isKeyDown(Input.KEY_S)) {
			delay -= 1;
			if (delay < 0)
				delay = 0;
		}

		if (input.isKeyDown(Input.KEY_1))
			max_depth = 1;
		else if (input.isKeyDown(Input.KEY_2))
			max_depth = 2;
		else if (input.isKeyDown(Input.KEY_3))
			max_depth = 3;
		else if (input.isKeyDown(Input.KEY_4))
			max_depth = 4;
		else if (input.isKeyDown(Input.KEY_D))
			animation = true;

		if (pf == 0) {
			if (input.isKeyDown(Input.KEY_F))
				animation = false;
			else if (input.isKeyDown(Input.KEY_P))
				human = true;
			else if (input.isKeyDown(Input.KEY_O))
				human = false;

			checkRowCompletion(occupied, true);
			pf = 1;
			addFallingToken();
			rotation = 0;

		}
		if (pf == 1) {
			if (human) {
				if (c >= 60) {
					c = 0;
					eraseToken(x, y, xArray, yArray);
					if (input.isKeyDown(Input.KEY_LEFT))
						x -= 1;
					if (!isValidPosition(x, y, token[0], rotation))
						x += 1;
					if (input.isKeyDown(Input.KEY_RIGHT))
						x += 1;
					if (!isValidPosition(x, y, token[0], rotation))
						x -= 1;
					if (input.isKeyDown(Input.KEY_DOWN)
							&& isValidPosition(x, y + 1, token[0], rotation))
						y += 1;
					if (input.isKeyPressed(Input.KEY_UP)
							&& isValidPosition(x, y, token[0],
									(rotation + 1) % 4)) {
						rotation = (rotation + 1) % 4;
						xArray = xRotationArray[token[0]][rotation];
						yArray = yRotationArray[token[0]][rotation];
					}
					if (input.isKeyPressed(Input.KEY_SPACE)) {
						while (true) {

							y++;
							if (!isValidPosition(x, y, token[0], rotation)) {

								y--;
								break;

							}
						}
						pf = 0;
					}

					drawToken(x, y, xArray, yArray, token[0]);
				}

				if (!reachFloor && counter2 >= (275)) {
					counter2 = 0;
					eraseToken(x, y, xArray, yArray);

					y += 1; // falling
					if (!isValidPosition(x, y, token[0], rotation)) // reached
																	// floor
					{
						reachFloor = true;
						pf = 0;
						y -= 1; // restore position
					}
					drawToken(x, y, xArray, yArray, token[0]);

				}
			}

			if (!human) {
				
				positions.clear();
				generatePositions();
				double c = 0.0;
				if (!positions.isEmpty()) {

					positions.get(0).eval();
					c = positions.get(0).getScore();

					index = 0;
					for (int i = 1; i < positions.size(); i++) {
						positions.get(i).eval();
						if (c > positions.get(i).getScore()) {
							c = positions.get(i).getScore();
							index = i;
						}
					}

					score = c;

					if (animation) {
						drawToken(
								occupied,
								positions.get(index).x,
								y,
								xRotationArray[token[0]][positions.get(index).r % 4],
								yRotationArray[token[0]][positions.get(index).r % 4],
								token[0]);
						pf = 3;
					}
					if (!animation) {

						int h = 0;
						while (isValidPosition(occupied,
								positions.get(index).x, h, token[0],
								positions.get(index).r % 4)) {
							h++;
						}
						h--;

						drawToken(
								occupied,
								positions.get(index).x,
								h,
								xRotationArray[token[0]][positions.get(index).r % 4],
								yRotationArray[token[0]][positions.get(index).r % 4],
								token[0]);
						pf = 0;
					}

				} else {
					pf = 4;
				}
			}
		}

		if (pf == 3 && counter >= delay && animation) {


				counter = 0;
				eraseToken(positions.get(index).x, y,
						xRotationArray[token[0]][positions.get(index).r % 4],
						yRotationArray[token[0]][positions.get(index).r % 4]);
				y++;

				if (!isValidPosition(occupied, positions.get(index).x, y,
						token[0], positions.get(index).r % 4)) {
					y--;
					pf = 0;
				}
				drawToken(occupied, positions.get(index).x, y,
						xRotationArray[token[0]][positions.get(index).r % 4],
						yRotationArray[token[0]][positions.get(index).r % 4],
						token[0]);
			
		}
		if (pf == 4) {
			System.out.println(linescleared);
			init(gc, sbg);
		}
	}

	@Override
	public int getID() {
		return 2;
	}

	int[][] occupied = new int[10][20];
	// search depth
	// 1-3 run at fast speeds while 4+ requires lots of memory and will
	// experience slow downs
	public int max_depth = 2;
	int[] token = new int[6];

	ArrayList<Position> positions = new ArrayList<Position>();
	ArrayList<Integer> bag = new ArrayList<Integer>();

	double score;
	int index;
	int counter = 0;
	int counter2 = 0;
	int c = 0;
	int x;
	int y;

	int[] xArray;
	int[] yArray;
	boolean reachFloor;
	int pf = 0;
	int linescleared = 0;
	int piececount = 0;

	private boolean animation = false;
	private boolean human = false;
	int delay = 13;
	int rotation = 0;

	public static int counts = 0;
	public static int sizecounts = 0;
	int cutoff = 0;

	int[] rotationindeces = { 4, 2, 2, 4, 4, 1, 2 };

	static int[][][] xRotationArray = {
			{ { 0, 0, 1, 2 }, { 0, 0, 0, 1 }, { 2, 0, 1, 2 }, { 0, 1, 1, 1 } }, // token
																				// number
																				// 0
			{ { 0, 0, 1, 1 }, { 1, 2, 0, 1 }, { 0, 0, 1, 1 }, { 1, 2, 0, 1 } }, // token
																				// number
																				// 1
			{ { 1, 1, 0, 0 }, { 0, 1, 1, 2 }, { 1, 1, 0, 0 }, { 0, 1, 1, 2 } }, // token
																				// number
																				// 2
			{ { 0, 1, 2, 2 }, { 0, 1, 0, 0 }, { 0, 0, 1, 2 }, { 1, 1, 0, 1 } }, // token
																				// number
																				// 3
			{ { 1, 0, 1, 2 }, { 1, 0, 1, 1 }, { 0, 1, 1, 2 }, { 0, 0, 1, 0 } }, // token
																				// number
																				// 4
			{ { 0, 1, 0, 1 }, { 0, 1, 0, 1 }, { 0, 1, 0, 1 }, { 0, 1, 0, 1 } }, // token
																				// number
																				// 5
			{ { 0, 1, 2, 3 }, { 0, 0, 0, 0 }, { 0, 1, 2, 3 }, { 0, 0, 0, 0 } } // token
																				// number
																				// 6
	};

	static int[][][] yRotationArray = {
			{ { 0, 1, 0, 0 }, { 0, 1, 2, 2 }, { 0, 1, 1, 1 }, { 0, 0, 1, 2 } }, // token
																				// number
																				// 0
			{ { 0, 1, 1, 2 }, { 0, 0, 1, 1 }, { 0, 1, 1, 2 }, { 0, 0, 1, 1 } }, // token
																				// number
																				// 1
			{ { 0, 1, 1, 2 }, { 0, 0, 1, 1 }, { 0, 1, 1, 2 }, { 0, 0, 1, 1 } }, // token
																				// number
																				// 2
			{ { 0, 0, 0, 1 }, { 0, 0, 1, 2 }, { 0, 1, 1, 1 }, { 0, 1, 2, 2 } }, // token
																				// number
																				// 3
			{ { 0, 1, 1, 1 }, { 0, 1, 1, 2 }, { 0, 0, 1, 0 }, { 0, 1, 1, 2 } }, // token
																				// number
																				// 4
			{ { 0, 0, 1, 1 }, { 0, 0, 1, 1 }, { 0, 0, 1, 1 }, { 0, 0, 1, 1 } }, // token
																				// number
																				// 5
			{ { 0, 0, 0, 0 }, { 0, 1, 2, 3 }, { 0, 0, 0, 0 }, { 0, 1, 2, 3 } } // token
																				// number
																				// 6
	};

}
