package GUI;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

import algorithms.search.Solution;
/**
 * This is an abstractic class that implements from gameBoard and includes the combined 
 * data members for all gameBoards
 * @author  Sarusi Ran, Gershfeld Itzik 
 * @version 1.0
 * @since   2015-06-15
 *
 */
public abstract class CommonGameBoard extends Composite implements GameBoard {

	int BoardRows;
	int BoardCols;
	Image image;
	int characterI,characterJ;
	int hintI = -1,hintJ = -1;
	Image beforeImage;
	int dir;
	CommonGameCharacter gameCharacter;
	Solution s;
	public CommonGameBoard(Composite parent, int style)
	{
		super(parent, style);
	}
	

	
}

