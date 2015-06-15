package GUI;

import org.eclipse.swt.graphics.Image;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

public interface GameBoard {
	public void displayBoard(Maze m);
	public boolean canMove(int i,int j,int dir);
	public void setCharacterImages();
	public void setHint(int x,int y);
	public void setCharacterPosition(int i, int j);
	public void deleteBoard();
	public void displaySolution(Solution s);
	public void setX(int x);
	public void setY(int y);
	public int getX();
	public int getY();
	public void SetCharacterDirection(int dir);
	public int GetCharacterDirection();
	public Image setSuitableImage(Maze m , int i , int j);
}
