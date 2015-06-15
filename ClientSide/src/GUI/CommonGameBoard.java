package GUI;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

import algorithms.search.Solution;

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
