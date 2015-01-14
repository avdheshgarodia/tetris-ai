package net.tetris;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Game extends StateBasedGame{

	public static final String gamename = "Tetris";
	public static final int menu = 0;
	public static final int human_player = 1;
	public static final int ai_player = 2;


	public Game(String gamename){
		super(gamename);

		this.addState(new Menu(menu));
		this.addState(new HumanPlayer(human_player));
		this.addState(new Aiplayer(ai_player));

	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		this.getState(menu).init(gc, this);
		this.getState(human_player).init(gc, this);
		this.getState(ai_player).init(gc,this);
		this.enterState(menu);
	}
	
	public static void main(String[] args) {
		AppGameContainer appgc;
		try{
			appgc = new AppGameContainer(new Game(gamename));
			appgc.setDisplayMode(480, 480, false);
			appgc.start();
		}catch(SlickException e)

		{
			e.printStackTrace();
		}
	}

}
