package GUI;

import org.eclipse.swt.graphics.Image;

public interface GameCharacter {
	/**
	 * Sets the position of the character
	 * @param x the i coordinate of the character
	 * @param y the j coordinate of the character
	 */
	public void setPosition(int x,int y);
	/**
	 * Sets the image of the character after it moves to the right
	 * @param right the selected image
	 */
	public void setRightImage(Image right);
	/**
	 * Sets the image of the character after it moves to the left
	 * @param left the selected image
	 */
	public void setLeftImage(Image left);
	/**
	 * Sets the image of the character after it moves upside
	 * @param up the selected image
	 */
	public void setUpImage(Image up);
	/**
	 * Sets the image of the character after it moves down
	 * @param down the selected image
	 */
	public void setDownImage(Image down);
	/**
	 * drags the character to the selected direction
	 * @param dir the direction the mouse moved to
	 */
	public void dragCharacter(int dir);
	/**
	 * 
	 * @param dir the direction of the character
	 * @return returns the image that matches the direction
	 */
	public Image chooseOption(int dir);
}
