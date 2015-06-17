package GUI;

import org.eclipse.swt.graphics.Image;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
/**
 * 
 * @author  Sarusi Ran, Gershfeld Itzik 
 * @version 1.0
 * @since   2015-06-15
 */
public interface GameBoard {
	/**
	 * Displays the board in the GUI
	 * @param m the maze the function displays
	 */
	public void displayBoard(Maze m);
	/**
	 * check if the character can move from her current location to the selected direction
	 * @param i the i coordinate of the character
	 * @param j the j coordinate of the character
	 * @param dir the direction the character want to move
	 * @return returns true if it can move to the direction and false if it doesn't
	 */
	public boolean canMove(int i,int j,int dir);
	/**
	 * Set the character images
	 */
	public void setCharacterImages();
	/**
	 * sets a hint in the selected location
	 * @param x the i coordinate of the hint
	 * @param y the j coordinate of the hint
	 */
	public void setHint(int x,int y);
	/**
	 * Sets the character display on the selected position
	 * @param i the i coordinate of the character
	 * @param j the i coordinate of the character
	 */
	public void setCharacterPosition(int i, int j);
	/**
	 * deletes the board
	 */
	public void deleteBoard();
	/**
	 * Displays the solution of the board
	 * @param s the user want to display
	 */
	public void displaySolution(Solution s);
	/**
	 * Sets the i coordinate of the character
	 * @param x the i coordinate
	 */
	public void setX(int x);
	/**
	 * Sets the j coordinate of the character
	 * @param x the j coordinate
	 */
	public void setY(int y);
	/**
	 * 
	 * @return returns the i coordinate of the character
	 */
	public int getX();
	/**
	 * 
	 * @return returns the j coordinate of the character
	 */
	public int getY();
	/**
	 * Sets the direction of the character
	 * @param dir integar the represent the direction
	 */
	public void SetCharacterDirection(int dir);
	/**
	 * 
	 * @return returns the direction of the last move of the character
	 */
	public int GetCharacterDirection();
	/**
	 * This function the matched image to the maze's cell
	 * @param m the maze we want to display
	 * @param i the i coordinate of the cell
	 * @param j the j coordinate of the cell
	 * @return returns the suitable image
	 */
	public Image setSuitableImage(Maze m , int i , int j);
}
