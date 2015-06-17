package GUI;

import org.eclipse.swt.graphics.Image;
/**
 * 
 * This is an abstractic class that implements from gameCharacter and includes the combined 
 * data members for all gameCharacters
 * @author  Sarusi Ran, Gershfeld Itzik 
 * @version 1.0
 * @since   2015-06-15
 *
 * 
 *
 */
public abstract class CommonGameCharacter implements GameCharacter{

	int x,y;
	Image up,right,down,left;
	public void dragCharacter(int dir) 
	{
	}

}

