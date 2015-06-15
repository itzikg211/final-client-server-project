package GUI;

import org.eclipse.swt.graphics.Image;
/**
 * This class is in charge of the boat display on the maze
 * @author  Sarusi Ran, Gershfeld Itzik 
 * @version 1.0
 * @since   2015-06-02
 *
 */
public class Boat extends CommonGameCharacter
{
	int x,y;
	int w,h;
	Image boatImg;
	CommonGameBoard maze;
	
	/*public Boat(Board b) 
	{
		x=0;
		y=0;
		this.maze= b;
	}*/
	/*public Boat(Board board) {
		x=0;
		y=0;
		this.maze= board;
	}*/
	public Boat(CommonGameBoard board) {
		x=0;
		y=0;
		this.maze= board;
	}
	/**
	 * returns the selected image to display on the board
	 * @param the direction of the boat
	 * @param i the i coordinate of the position of the boat
	 * @param j the j coordinate of the position of the boat
	 * @return returns the selected image
	 */
	
	public Image chooseOption(int dir, int i, int j)
	{
		
		Image image2=null;
		x=i;
		y=j;
		if(dir==0)
		{
			//x=i+1;
			//y=j;
			image2 = new Image(null, "resources/boat-up.png");
			//setBoatImg(image2);
			
		}
		if(dir==1)
		{
			//x=i;
			//y=j-1;
			image2 = new Image(null, "resources/boat-right.png");
			//setBoatImg(image2);
		}
		if(dir==2)
		{
			//x=i-1;
			//y=j;
			image2 = new Image(null, "resources/boat-down.png");//jpg");//////////////changed here
			//setBoatImg(image2);
			
		}
		if(dir==3)
		{
			//x=i;
			//y=j+1;
			image2 = new Image(null, "resources/boat-left.png");
			//setBoatImg(image2);
		}
		//System.out.println("THE BOAT POSITION IS : " + i + "," +  j);
		return image2;
 	}
	/**
	 * 
	 * @return returns the boat's position
	 */
	public Image getBoatImg() {
		return boatImg;
	}
	/**
	 * sets the boat image to the selected parameter
	 * @param boatImg the image of the boat position
	 */
	public void setBoatImg(Image boatImg) {
		this.boatImg = boatImg;
	}
	
	
	public void dragBoat(int dir)
	{
		if(dir==0) //up
		{
			System.out.println("UP");
			if(maze.canMove(maze.getX(),maze.getY(), 0))
			{
				System.out.println("CAN MOVE");
				maze.SetCharacterDirection(0);
				maze.setCharacterPosition(maze.getX()-1,maze.getY());						
			}
			else
				System.out.println("CAN NOT MOVE");
		}
		if(dir==1) //right
		{
			System.out.println("RIGHT");
			if(maze.canMove(maze.getX(),maze.getY(), 1))
			{
				System.out.println("CAN MOVE");
				maze.SetCharacterDirection(1);
				maze.setCharacterPosition(maze.getX(),maze.getY()+1);						
			}
			else
				System.out.println("CAN NOT MOVE");
		}
		if(dir==2) //down
		{
			System.out.println("DOWN");
			if(maze.canMove(maze.getX(),maze.getY(), 2))
			{
				System.out.println("CAN MOVE");
				maze.SetCharacterDirection(2);
				maze.setCharacterPosition(maze.getX()+1,maze.getY());						
			}
			else
				System.out.println("CAN NOT MOVE");
		}
		if(dir==3) //left
		{
			System.out.println("LEFT");
			if(maze.canMove(maze.getX(),maze.getY(), 3))
			{
				System.out.println("CAN MOVE");
				maze.SetCharacterDirection(3);
				maze.setCharacterPosition(maze.getX(),maze.getY()-1);						
			}
			else
				System.out.println("CAN NOT MOVE");
		}
	}
	@Override
	public void setPosition(int x, int y) {
		// TODO Auto-generated method stub
		this.x=x;
		this.y=y;
	}
	@Override
	public void setRightImage(Image right) {
		// TODO Auto-generated method stub
		this.right=right;
	}
	@Override
	public void setLeftImage(Image left) {
		// TODO Auto-generated method stub
		this.left=left;
	}
	@Override
	public void setUpImage(Image up) {
		// TODO Auto-generated method stub
		this.up=up;
	}
	@Override
	public void setDownImage(Image down) {
		// TODO Auto-generated method stub
		this.down=down;
	}
	@Override
	public Image chooseOption(int dir) {
		if(dir==0)
		{
			return up;
		}
		if(dir==1)
		{
			return right;
		}
		if(dir==2)
		{
			return down;
		}
		if(dir==3)
		{
			return left;
		}
		return null;
	}
	
}