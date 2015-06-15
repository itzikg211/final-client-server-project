package GUI;

import org.eclipse.swt.graphics.Image;

public interface GameCharacter {
	public void setPosition(int x,int y);
	public void setRightImage(Image right);
	public void setLeftImage(Image left);
	public void setUpImage(Image up);
	public void setDownImage(Image down);
	public void dragBoat(int dir);
	public Image chooseOption(int dir);
}
