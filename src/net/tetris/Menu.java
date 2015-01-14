package net.tetris;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Menu extends BasicGameState{

	public Menu(int state){		
	}
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{
		
	}
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException{
		g.drawString("Tetris Game!", 50, 150);
		g.drawString("Press 1 to play tetris", 50, 215);
		g.drawString("Press 2 to watch the AI play", 50, 230);
		g.drawString("Controls for AI Mode", 50, 260);
		g.drawString("A/S Raise or Lower piece fall delay", 50, 275);
		g.drawString("D/F toggle fall animation", 50, 290);
		g.drawString("P/O toggle if human is playing", 50, 305);
		g.drawString("KEYS: 1,2,3,4 Select Search Depth", 50, 320);
		g.drawString("Searching higher than a depth of 4", 50, 335);
		g.drawString("must be manually set. Takes to long", 50, 350);
	}
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException{
		Input input = gc.getInput();
		
		if (input.isKeyDown(Input.KEY_1))
			sbg.enterState(1);
		if (input.isKeyDown(Input.KEY_2))
			sbg.enterState(2);

	}
	public int getID(){
		return 0;
	}
	
	
}