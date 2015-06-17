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
	
		public Boat(CommonGameBoard board) {
		x=0;
		y=0;
		this.maze= board;
	}
	/**
	 * 
	 * @param dir the direction of the character
	 * @return returns the image that matches the direction
	 */
	public void dragCharacter(int dir)
	{
		//checks if the character can go upside
		if(dir==0) //up
		{
			if(maze.canMove(maze.getX(),maze.getY(), 0))
			{
				//sets the character direction move to up
				maze.SetCharacterDirection(0);
				//sets the boat image 
				maze.setCharacterPosition(maze.getX()-1,maze.getY());						
			}
		}
		//checks if the character can go to the right
		if(dir==1) //right
		{
			if(maze.canMove(maze.getX(),maze.getY(), 1))
			{
				//sets the character direction move to the right
				maze.SetCharacterDirection(1);
				//sets the boat image 
				maze.setCharacterPosition(maze.getX(),maze.getY()+1);						
			}
		}
		//checks if the character can go down
		if(dir==2) //down
		{
			if(maze.canMove(maze.getX(),maze.getY(), 2))
			{
				//sets the character direction move to down
				maze.SetCharacterDirection(2);
				//sets the boat image 
				maze.setCharacterPosition(maze.getX()+1,maze.getY());						
			}
		}
		//checks if the character can go to the left
		if(dir==3) //left
		{
			if(maze.canMove(maze.getX(),maze.getY(), 3))
			{
				//sets the character direction move to the left
				maze.SetCharacterDirection(3);
				//sets the boat image 
				maze.setCharacterPosition(maze.getX(),maze.getY()-1);						
			}
		}
	}
	/**
	 * Sets the position of the character
	 * @param x the i coordinate of the character
	 * @param y the j coordinate of the character
	 */
	@Override
	public void setPosition(int x, int y) {
		// TODO Auto-generated method stub
		this.x=x;
		this.y=y;
	}
	/**
	 * Sets the image of the character after it moves to the right
	 * @param right the selected image
	 */
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
	/**
	 * Sets the image of the character after it moves upside
	 * @param up the selected image
	 */
	@Override
	public void setUpImage(Image up) {
		// TODO Auto-generated method stub
		this.up=up;
	}
	/**
	 * Sets the image of the character after it moves down
	 * @param down the selected image
	 */
	@Override
	public void setDownImage(Image down) {
		// TODO Auto-generated method stub
		this.down=down;
	}
	/**
	 * 
	 * @param dir the direction of the character
	 * @return returns the image that matches the direction
	 */
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