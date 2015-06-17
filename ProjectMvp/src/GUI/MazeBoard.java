package GUI;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

public class MazeBoard extends CommonGameBoard
{
	Maze m;
	Tile[][] tiles;
	public MazeBoard(Composite parent, int style,CommonGameCharacter game) {
		super(parent, style);
		gameCharacter = game;
		this.characterI=0;
		this.characterJ=0;
		addPaintListener(new PaintListener() 
		{
			@Override
			public void paintControl(PaintEvent arg0) 
			{
				if(tiles!=null)
				{
					for(int i=0;i<tiles.length;i++)
						for(int j=0;j<tiles[0].length;j++)
						{
							tiles[i][j].setCharacter(gameCharacter);
							tiles[i][j].redraw();
						}
				}
				if(tiles==null){
					int width=(int)(parent.getSize().x*0.80);
					int height=(int)(parent.getSize().y*0.9);
					ImageData data = new ImageData("resources/mainPic.png");
					arg0.gc.drawImage(new Image(getDisplay(),"resources/mainPic.png"),0,0,data.width,data.height,0,0,width, height);
				}
			}
		});
	}
	/**
	 * Displays the board in the GUI
	 * @param m the maze the function displays
	 */
	@Override
	public void displayBoard(Maze m) {
		this.m=m;
		if(tiles!=null) 
			deleteBoard();
		BoardRows=m.getRows();
		BoardCols=m.getCols();

		
		GridLayout layout=new GridLayout(BoardCols, true);
		layout.horizontalSpacing=0;
		layout.verticalSpacing=0;
		setLayout(layout);
		tiles=new Tile[BoardRows][BoardCols];
		for(int i=0;i<BoardRows;i++)
			for(int j=0;j<BoardCols;j++)
			{
				tiles[i][j]=new Tile(this,SWT.NONE);
				tiles[i][j].setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
				Image temp = setSuitableImage(m, i, j);
				tiles[i][j].setImage(temp);
				tiles[i][j].setBeforeImage(temp);
			}
    	tiles[0][0].setFirstTile(true);
    	tiles[BoardRows-1][BoardCols-1].setFinalImg(true);
		layout();
	}
	/**
	 * check if the character can move from her current location to the selected direction
	 * @param i the i coordinate of the character
	 * @param j the j coordinate of the character
	 * @param dir the direction the character want to move
	 * @return returns true if it can move to the direction and false if it doesn't
	 */
	@Override
	public boolean canMove(int i, int j, int dir)
	{
		tiles[0][0].redraw();
		characterI = i;
		characterJ = j;
		//0 means top
		if(dir==0)
		{
			if(i==0)
				return false;
			else
			{
				if(this.m.getCell(i-1, j).getHasBottomWall()==false)
				{
					return true;
				}
				return false;
			}
		}
		//1 means right
		if(dir==1)
		{
			if(j==this.m.getCols()-1)
			{
				return false;
			}
			else
			{
				if(this.m.getCell(i, j).getHasLeftWall()==false)
				{
					return true;
				}
				return false;
			}
		}
		//2 means bottom
		if(dir==2)
		{	if(i==this.m.getRows()-1)
			{
				return false;
			}
			else
			{
				if(this.m.getCell(i, j).getHasBottomWall()==false)
					return true;
				return false;
			}
		}
		//3 means left
		if(dir==3)
		{
			if(j==0)
			{
				return false;
			}
			else
			{
				if(this.m.getCell(i, j-1).getHasLeftWall()==false)
					return true;
				return false;
			}
		}
		//4 means up-left
		if(dir==4)
		{
			if(j==0 || i==0)
			{
				return false;
			}
			else
			{
				if((this.m.getCell(i, j-1).getHasLeftWall()==false && this.m.getCell(i-1, j-1).getHasBottomWall()==false ) || (this.m.getCell(i-1, j-1).getHasLeftWall()==false && this.m.getCell(i-1, j).getHasBottomWall()==false ))
					return true;
				return false;
			}
		}
		//5 means up-right
		if(dir==5)
		{
			if(i==0 || j==this.m.getCols()-1)
			{
				return false;
			}
			else
			{
				if((this.m.getCell(i,j).getHasLeftWall()==false && this.m.getCell(i-1, j+1).getHasBottomWall()==false ) || (this.m.getCell(i-1, j).getHasLeftWall()==false && this.m.getCell(i-1, j).getHasBottomWall()==false ))
					return true;
				return false;
			}
		}
		//6 means down-left
		if(dir==6)
		{
			if(j==0 || i==this.m.getRows()-1)
			{
				return false;
			}
			else
			{
				if((this.m.getCell(i, j-1).getHasLeftWall()==false && this.m.getCell(i, j-1).getHasBottomWall()==false ) || (this.m.getCell(i+1, j-1).getHasLeftWall()==false && this.m.getCell(i, j).getHasBottomWall()==false ))
					return true;
				return false;
			}
		}
		//7 means down-right
		if(dir==7)
		{
			if(j==this.m.getCols()-1 || i==this.m.getRows()-1)
			{
				return false;
			}
			else
			{
				if((this.m.getCell(i, j).getHasLeftWall()==false && this.m.getCell(i, j+1).getHasBottomWall()==false ) || (this.m.getCell(i+1, j).getHasLeftWall()==false && this.m.getCell(i, j).getHasBottomWall()==false ))
					return true;
				return false;
			}
		}
		return false;
	}
	/**
	 * Set the characted images
	 */
	@Override
	public void setCharacterImages()
	{
		this.gameCharacter = new Boat(this); //fix this... boat cant get in a constructor a gameboard data member
		Image image = new Image(null, "resources/boat-down.png");
		gameCharacter.setDownImage(image);
		Image image2= new Image(null, "resources/boat-left.png");
		gameCharacter.setLeftImage(image2);
		Image image3= new Image(null, "resources/boat-right.png");
		gameCharacter.setRightImage(image3);
		Image image4= new Image(null, "resources/boat-up.png");
		gameCharacter.setUpImage(image4);
		
	}
	/**
	 * sets a hint in the selected location
	 * @param x the i coordinate of the hint
	 * @param y the j coordinate of the hint
	 */
	@Override
	public void setHint(int x, int y) 
	{
		if((hintI == -1) & (hintJ == -1))
		{
			hintI = x;
			hintJ =y;	
		}
		else
		{
			tiles[hintI][hintJ].removeHint();
			hintI = x;
			hintJ =y;	
		}
		if(tiles[x][y].isCircle())
		{
			tiles[x][y].removeCircle();
		}
		tiles[x][y].setHint();
	}
	/**
	 * Sets the character display on the selected position
	 * @param i the i coordinate of the character
	 * @param j the i coordinate of the character
	 */
	@Override
	public void setCharacterPosition(int i, int j)
	{
		tiles[characterI][characterJ].setcharacterImage(null);
		tiles[characterI][characterJ].redraw();
		
		if(tiles[i][j].isCircle())
		{
			if((i==1 && j==0) || (i==0 && j==1))
			{
				tiles[0][0].removeCircle();
			}
			tiles[i][j].removeCircle();
		}
		if(tiles[i][j].isHint())
		{
			tiles[i][j].removeHint();
		}
		tiles[i][j].setcharacterImage(gameCharacter.chooseOption(dir));
		tiles[i][j].redraw();
		characterI = i;
		characterJ = j;
	}
	/**
	 * deletes the board
	 */
	@Override
	public void deleteBoard()
	{
		for(int i=0;i<tiles.length;i++)
			for(int j=0;j<tiles[0].length;j++)
			{
				tiles[i][j].dispose();
			}
		
	}
	/**
	 * Displays the solution of the board
	 * @param s the user want to display
	 */
	@Override
	public void displaySolution(Solution s)
	{
		ArrayList<Integer> arr = s.SolutionToArray();
		int x=0;
		int y=0;
		int a=getX();
		int b=getY();
		s.displaySolution();
		for(int i=3;i<arr.size();i+=2)
		{
			Image arrow;
			y=arr.get(arr.size()-i);
			x=arr.get(arr.size()-i-1);
			if(y == b-1 && x == a-1) //direction is up left
			{
				arrow = new Image(null, "resources/arrow-up-left.png"); 
				tiles[a][b].putArrow(arrow);
				a=x;
				b=y;
			}
			else if(y == b-1 && x == a+1) //direction is up right
			{
				arrow = new Image(null, "resources/arrow-down-left.png"); 
				tiles[a][b].putArrow(arrow);
				a=x;
				b=y;
			}
			else if(y == b+1 && x == a-1) //direction is down left
			{
				arrow = new Image(null, "resources/arrow-up-right.png"); 
				tiles[a][b].putArrow(arrow);
				a=x;
				b=y;
			}
			else if(y == b+1 && x == a+1) //direction is down right
			{
				arrow = new Image(null, "resources/arrow-down-right.png");
				tiles[a][b].putArrow(arrow);
				a=x;
				b=y;
			}
			else if(x == a+1) //direction is right
			{
				arrow = new Image(null, "resources/arrow-down.png"); 
				tiles[a][b].putArrow(arrow);
				a=x;
			}
			else if(x == a-1) //direction is left
			{
				arrow = new Image(null, "resources/arrow-up.png"); 
				tiles[a][b].putArrow(arrow);
				a=x;
				
			}
			else if(y == b+1) //direction is down
			{
				
				arrow = new Image(null, "resources/arrow-right.png"); 
				tiles[a][b].putArrow(arrow);
				b=y;
			}
			else if(y == b-1) //direction is up
			{
				arrow = new Image(null, "resources/arrow-left.png"); 
				tiles[a][b].putArrow(arrow);
				b=y;
			}
			
			redraw();
		}
		
	}
	/**
	 * Sets the i coordinate of the character
	 * @param x the i coordinate
	 */
	@Override
	public void setX(int x) {
		// TODO Auto-generated method stub
		this.characterI=x;
	}
	/**
	 * Sets the j coordinate of the character
	 * @param x the j coordinate
	 */
	@Override
	public void setY(int y) {
		// TODO Auto-generated method stub
		this.characterJ=y;
	}
	/**
	 * 
	 * @return returns the i coordinate of the character
	 */
	@Override
	public int getX() {
		// TODO Auto-generated method stub
		return this.characterI;
	}
	/**
	 * 
	 * @return returns the j coordinate of the character
	 */
	@Override
	public int getY() {
		// TODO Auto-generated method stub
		return this.characterJ;
	}
	/**
	 * Sets the direction of the character
	 * @param dir integar the represent the direction
	 */
	@Override
	public void SetCharacterDirection(int dir) 
	{
		this.dir = dir;
	}
	/**
	 * 
	 * @return returns the direction of the last move of the character
	 */
	@Override
	public int GetCharacterDirection() {
		// TODO Auto-generated method stub
		return dir;
	}
	/**
	 * This function the matched image to the maze's cell
	 * @param m the maze we want to display
	 * @param i the i coordinate of the cell
	 * @param j the j coordinate of the cell
	 * @return returns the suitable image
	 */
	@Override
	public Image setSuitableImage(Maze maze, int i, int j) {
		if(i==0 && j==0)
        {
	          if(maze.getCell(i, j).getHasLeftWall() && maze.getCell(i, j).getHasBottomWall())
	        	  return new Image(getDisplay(),new ImageData("resources/state 4.png"));
	          else 
	          {
	        	  if(!maze.getCell(i, j).getHasBottomWall() && !maze.getCell(i, j).getHasLeftWall())
	        		  return new Image(getDisplay(),new ImageData("resources/state 12.png"));
	        	  else
	        	  {
	        		  if(maze.getCell(i, j).getHasBottomWall())
	        			  return new Image(getDisplay(),new ImageData("resources/state 9.png"));
	        		  else
	        			  return new Image(getDisplay(),new ImageData("resources/state 5.png"));
	        	  }
      	  }
        }
        else
        {
      	  	if(i==0 && j!=0)
      	  	{
		          if(maze.getCell(i, j).getHasLeftWall() && maze.getCell(i, j).getHasBottomWall())
		        	  return new Image(getDisplay(),new ImageData("resources/state 16.png"));
		          else 
		          {
		        	  if(!maze.getCell(i, j).getHasBottomWall() && !maze.getCell(i, j).getHasLeftWall())
		        		  return new Image(getDisplay(),new ImageData("resources/state 7.png"));
		        	  else
		        	  {
		        		  if(maze.getCell(i, j).getHasBottomWall())
		        			  return new Image(getDisplay(),new ImageData("resources/state 6.png"));
		        		  else
		        			  return new Image(getDisplay(),new ImageData("resources/state 13.png"));
		        	  }
	        	  } 
	          }
        
        else
        {
      	  if(j==0 && i!=0)
	          {
		          if(maze.getCell(i, j).getHasLeftWall() && maze.getCell(i, j).getHasBottomWall())
		        	  return new Image(getDisplay(),new ImageData("resources/state 10.png"));
		          else 
		          {
		        	  if(!maze.getCell(i, j).getHasBottomWall() && !maze.getCell(i, j).getHasLeftWall())
		        		  return new Image(getDisplay(),new ImageData("resources/state 8.png"));
		        	  else
		        	  {
		        		  if(maze.getCell(i, j).getHasBottomWall())
		        			  return new Image(getDisplay(),new ImageData("resources/state 14.png"));
		        		  else
		        			  return new Image(getDisplay(),new ImageData("resources/state 15.png"));
		        	  }
	        	  }
	          }
      	  else
	          {
		          if(maze.getCell(i, j).getHasLeftWall() && maze.getCell(i, j).getHasBottomWall())
		        	  return new Image(getDisplay(),new ImageData("resources/state 4.png"));
		          else 
		          {
		        	  if(!maze.getCell(i, j).getHasBottomWall() && !maze.getCell(i, j).getHasLeftWall())
		        		  return new Image(getDisplay(),new ImageData("resources/state 3.png"));
		        	  else
		        	  {
		        		  if(maze.getCell(i, j).getHasBottomWall())
		        			  return new Image(getDisplay(),new ImageData("resources/state 2.png"));
		        		  else
		        			  return new Image(getDisplay(),new ImageData("resources/state 1.png"));		        	  }
	        	  }
	          }
        }
        }
	}

}
