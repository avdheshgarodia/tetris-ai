package net.tetris;

import java.util.ArrayList;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class HumanPlayer extends BasicGameState{

	public HumanPlayer(int state){		
	}
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException{
		gc.setShowFPS(false);

		gc.setAlwaysRender(true);
		for(int i=0;i<7;i++)
		{
			bag.add(i);
		}	

		next_token = bag.remove((int) (bag.size()*Math.random()));
		next_rotation = (int) (4*Math.random());

	}
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException{

		g.setColor(Color.white);
		g.fillRect(0, 0, 240, 480);
		g.drawString("LC:"+linescleared, 250, 40);

		int[] xArray = xRotationArray[next_token][next_rotation];
		int[] yArray = yRotationArray[next_token][next_rotation];

		switch (next_token+1) {
		case 1: g.setColor(Color.cyan);
		break;
		case 2: g.setColor(Color.green);
		break;
		case 3:g.setColor(Color.orange);
		break;
		case 4: g.setColor(Color.magenta);
		break;
		case 5: g.setColor(Color.pink);
		break;
		case 6: g.setColor(Color.red);
		break;
		case 7: g.setColor(Color.blue);
		break;
		default:g.setColor(Color.darkGray);
		}

		for (int i=0;i<4;i++){    
			g.fillRect(250 + xArray[i]*24 +1 ,200+ yArray[i]*24+1,22,22);
		}

		for (int x=0;x<10;x++){
			for (int y=0;y<20;y++){
				if (occupied[x][y]==1)
				{
					// draw cell
					g.setColor(Color.white);
					g.fillRect(x*24,y*24,24,24);
					g.setColor(colors[x][y]);
					g.fillRect(x*24+1,y*24+1,22,22);
				}
			}
		}
	}
	public void drawToken(int x, int y, int[] xArray, int[] yArray,int t)
	{
		for (int i=0;i<4;i++)
		{
			drawCell(x+xArray[i],y+yArray[i],t);
		}
	}
	public void eraseToken(int x, int y, int[] xArray, int[] yArray)
	{
		for (int i=0;i<4;i++)
		{
			eraseCell(x+xArray[i],y+yArray[i]);
		}
	}
	public void drawCell(int x,int y,int c)
	{
		occupied[x][y] = 1;
		int d=c+1;
		switch (d) {
		case 1: colors[x][y]=Color.cyan;
		break;
		case 2: colors[x][y]=Color.green;
		break;
		case 3: colors[x][y]=Color.orange;
		break;
		case 4: colors[x][y]=Color.magenta;
		break;
		case 5: colors[x][y]=Color.pink;
		break;
		case 6: colors[x][y]=Color.red;
		break;
		case 7: colors[x][y]=Color.blue;
		break;
		default:colors[x][y]=Color.darkGray;

		}
	}

	public void eraseCell(int x,int y)
	{
		occupied[x][y] = 0;
	}
	public boolean isValidPosition(int x,int y, int token, int rotation)
	{
		int[] xArray = xRotationArray[token][rotation];
		int[] yArray = yRotationArray[token][rotation];

		for (int i=0;i<4;i++)  // loop over the four cells 
		{
			int xCell = x+xArray[i];
			int yCell = y+yArray[i];

			// range check
			if (xCell<0) return false;
			if (xCell>=10) return false;
			if (yCell<0) return false;
			if (yCell>=20) return false;

			// occupancy check
			if (occupied[xCell][yCell]==1) return false;
		}
		return true;
	}	
	public void addFallingToken()
	{
		x=5;
		y=0;
		
		
		token=next_token;
		rotation=next_rotation;
		//pick random token from bag
		next_token = bag.remove((int) (bag.size()*Math.random()));
		next_rotation = (int) (4*Math.random());

		if(bag.isEmpty())
		{
			for(int i=0;i<7;i++)
			{
				bag.add(i);
			}	

		}
		if (!isValidPosition(x,y,token,rotation))
		{
			piecefalling=false;
		}	

		xArray = xRotationArray[token][rotation];
		yArray = yRotationArray[token][rotation];

		drawToken(x,y,xArray,yArray,token);
		reachFloor=false;
	}

	public void clearCompleteRow(int[] completed)
	{
		// erase
		for (int i=0;i<completed.length;i++)
		{
			if (completed[i]==1)
			{
				for (int x=0;x<10;x++)
				{
					occupied[x][i]=0;
				}
			}
		}

	}
	public void shiftDown(int[] completed)
	{
		for (int row=0;row<completed.length;row++)
		{
			if (completed[row]==1)
			{
				for (int y=row;y>=1;y--)
				{
					for (int x=0;x<10;x++)
					{
						occupied[x][y] = occupied[x][y-1];
						colors[x][y]=colors[x][y-1];
					}
				}
			}
		}
	}
	public void checkRowCompletion()
	{
		int[] complete = new int[20];
		for (int y=0;y<20;y++)  // 20 rows
		{
			int filledCell = 0;
			for (int x=0;x<10;x++)  // 10 columns
			{
				if (occupied[x][y]==1) filledCell++;

			}
			if (filledCell==10) // row completed 
			{
				complete[y]=1;
				linescleared++;
			}else{
				complete[y]=0;
			}
		}

		clearCompleteRow(complete);
		shiftDown(complete); 

	} 
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException{
		counter+=delta;
		c+=delta;
		Input input = gc.getInput();

		if(!piecefalling)
		{
			piecefalling=true;
			checkRowCompletion();
			addFallingToken();

		}	 

		if(piecefalling){	 

			if(c>=60){
				c=0;
				eraseToken(x,y,xArray,yArray);
				if(input.isKeyDown(Input.KEY_LEFT))
					x-=1;
				if(!isValidPosition(x,y,token,rotation))
					x+=1;
				if(input.isKeyDown(Input.KEY_RIGHT))
					x+=1;
				if(!isValidPosition(x,y,token,rotation))
					x-=1;	
				if (input.isKeyDown(Input.KEY_DOWN) && isValidPosition(x,y+1,token,rotation)) y += 1;
				if (input.isKeyPressed(Input.KEY_UP) && isValidPosition(x,y,token,(rotation+1)%4)) 
				{
					rotation = (rotation+1)%4;
					xArray = xRotationArray[token][rotation];
					yArray = yRotationArray[token][rotation];  
				}
				if(input.isKeyPressed(Input.KEY_SPACE))
				{
					while(true)
					{

						y++;
						if(!isValidPosition(x,y,token,rotation))
						{

							y--;
							break;


						}
					}
				}	   

				drawToken(x,y,xArray,yArray,token);
			}	 


			if(!reachFloor && counter>=(275))
			{
				counter=0;
				eraseToken(x,y,xArray,yArray);
				
				y += 1;  // falling
				if (!isValidPosition(x,y,token,rotation)) // reached floor
				{
					reachFloor=true;
					piecefalling = false;
					y -= 1;  // restore position
				}
				drawToken(x,y,xArray,yArray,token);


			}
		}
	}


	@Override
	public int getID(){
		return 1;
	}
	int[][] occupied = new int[10][20];
	Color[][] colors=new Color[10][20];
	int counter = 0;
	int c=0;
	int x;
	int y;
	int token, rotation;
	int next_token, next_rotation;
	int[] xArray;
	int[] yArray;
	boolean reachFloor;
	boolean piecefalling =false;
	int linescleared=0;
	ArrayList<Integer> bag = new ArrayList<Integer>();

	static int[][][] xRotationArray = {
		{ {0,0,1,2}, {0,0,0,1}, {2,0,1,2}, {0,1,1,1} },  // token number 0
		{ {0,0,1,1}, {1,2,0,1}, {0,0,1,1}, {1,2,0,1} },  // token number 1
		{ {1,1,0,0}, {0,1,1,2}, {1,1,0,0}, {0,1,1,2} },  // token number 2
		{ {0,1,2,2}, {0,1,0,0}, {0,0,1,2}, {1,1,0,1} },  // token number 3
		{ {1,0,1,2}, {1,0,1,1}, {0,1,1,2}, {0,0,1,0} },  // token number 4
		{ {0,1,0,1}, {0,1,0,1}, {0,1,0,1}, {0,1,0,1} },  // token number 5
		{ {0,1,2,3}, {0,0,0,0}, {0,1,2,3}, {0,0,0,0} }   // token number 6
	};

	static int[][][] yRotationArray = {
		{ {0,1,0,0}, {0,1,2,2}, {0,1,1,1}, {0,0,1,2} },  // token number 0
		{ {0,1,1,2}, {0,0,1,1}, {0,1,1,2}, {0,0,1,1} },  // token number 1
		{ {0,1,1,2}, {0,0,1,1}, {0,1,1,2}, {0,0,1,1} },  // token number 2
		{ {0,0,0,1}, {0,0,1,2}, {0,1,1,1}, {0,1,2,2} },  // token number 3
		{ {0,1,1,1}, {0,1,1,2}, {0,0,1,0}, {0,1,1,2} },  // token number 4
		{ {0,0,1,1}, {0,0,1,1}, {0,0,1,1}, {0,0,1,1} },  // token number 5
		{ {0,0,0,0}, {0,1,2,3}, {0,0,0,0}, {0,1,2,3} }   // token number 6
	};

}
