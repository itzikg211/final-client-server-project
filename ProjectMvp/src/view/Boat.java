package view;

import org.eclipse.swt.graphics.Image;
/**
 * This class is in charge of the boat display on the maze
 * @author  Sarusi Ran, Gershfeld Itzik 
 * @version 1.0
 * @since   2015-06-02
 *
 */
public class Boat
{
	int x,y;
	int w,h;
	Image boatImg;

	
	public Boat() 
	{
	
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
		x=0;
		y=0;
		Image image2=null;
		
		if(dir==0)
		{
			x=i+1;
			y=j;
			image2 = new Image(null, "resources/boat-up.jpg");
			//setBoatImg(image2);
			
		}
		if(dir==1)
		{
			x=i;
			y=j-1;
			image2 = new Image(null, "resources/boat-right.jpg");
			//setBoatImg(image2);
		}
		if(dir==2)
		{
			x=i-1;
			y=j;
			image2 = new Image(null, "resources/boat-down.jpg");
			//setBoatImg(image2);
			
		}
		if(dir==3)
		{
			x=i;
			y=j+1;
			image2 = new Image(null, "resources/boat-left.jpg");
			//setBoatImg(image2);
		}
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
}