package view;

import java.util.Arrays;

import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
/**
 * 
 * This class defines the window that opens in the start of the project
 * @author  Sarusi Ran, Gershfeld Itzik 
 * @version 1.0
 * @since   2015-06-02
 */
 
public class Tile extends Canvas
{
	private Image beforeImage;
	private Image tileImg;
	private int clickI,clickJ;
	private int a,b,temp1,temp2;
	private Image arrowImage;
	private Boat boat;
	private Image boatImg;
	private boolean firstTile;
	private boolean hint = false;
	private boolean circle = false;
	private boolean finalImg = false;




	/**
	 * Constructs and initializes the class Tile
	 * @param 
	 * @param 
	 */
	public Tile(Composite parent, int style) {
		super(parent, style);
		addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) 
			{
					int width=getSize().x;
					int height=getSize().y;
			        ImageData data = tileImg.getImageData();
			        e.gc.drawImage(tileImg,0,0,data.width,data.height,temp1,temp2,width,height);
			        if(hint)
			        {
			        	/*e.gc.setForeground(new Color(null,255,0,0));
			        	e.gc.setBackground(new Color(null,255,0,0));
			        	e.gc.fillRectangle(width/3, height/3, width/3, height/3);*/
			        	Image galgal = new Image(null, "resources/galgal.jpg");
			        	ImageData data1 = galgal.getImageData();
			        	e.gc.drawImage(galgal,0,0,data1.width,data1.height,(int)(width/8),(int)(height/8),(int)(width*0.7),(int)(height*0.7));//(int)(Math.min(e.width,e.height) * 0.7), (int)(Math.min(e.width,e.height) * 0.7));
			        }
			        if(boatImg!=null)
			        {
			        	ImageData data1 = boatImg.getImageData();
						e.gc.drawImage(boatImg,0,0,data1.width,data1.height,(int)(width/8),(int)(height/8),(int)(width*0.7),(int)(height*0.7));//(int)(Math.min(e.width,e.height) * 0.7), (int)(Math.min(e.width,e.height) * 0.7));
			        }
			        if(firstTile)
			        {
			        	setBoatImage(new Image(null, "resources/boat-right.jpg"));
			        	ImageData data1 = new Image(null, "resources/boat-right.jpg").getImageData();
			        	e.gc.drawImage(new Image(null, "resources/boat-right.jpg"),0,0,data1.width,data1.height,(int)(width/8),(int)(height/8),(int)(width*0.7),(int)(height*0.7));
			        }
			        if(circle == true)
			        {
			        	//e.gc.setForeground(new Color(null,255,200,0));
						/*e.gc.setBackground(new Color(null,200,100,0));
						e.gc.fillOval(width/3, height/3, width/3, height/3);*/
			        	//e.gc.drawLine(0, 0, width, height);
			        	ImageData data1 = arrowImage.getImageData();
			        	e.gc.drawImage(arrowImage,0,0,data1.width,data1.height,(int)(width/8),(int)(height/8),(int)(width*0.7),(int)(height*0.7));//(int)(Math.min(e.width,e.height) * 0.7), (int)(Math.min(e.width,e.height) * 0.7));
			        }
			        if(finalImg == true)
			        {
			        	ImageData data1 = new Image(null, "resources/final.png").getImageData();
			        	e.gc.drawImage(new Image(null, "resources/final.png"),0,0,data1.width,data1.height,(int)(width/8),(int)(height/8),(int)(width*0.7),(int)(height*0.7));
			        }
			}
		});
			
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent arg0) //when you leave the mouse 
			{
				// TODO Auto-generated method stub
				int a = getDisplay().getCursorLocation().x;
				int b = getDisplay().getCursorLocation().y;
				String pos = "leave position : " + a + "," + b;
				System.out.println(pos);
				if(a==clickI && b==clickJ)
				{
					System.out.println("mouse didnt move!");
				}
			}
			
			@Override
			public void mouseDown(MouseEvent arg0) { //when you press the mouse
				// TODO Auto-generated method stub
				
				int a = getDisplay().getCursorLocation().x;
				int b = getDisplay().getCursorLocation().y;
				clickI=a;
				clickJ=b;
				String pos = "click position : " + a + "," + b;
				System.out.println(pos);
				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	
	
	
	/**
	 * Sets the tile's image to the parameter 
	 * @param image the selected image
	 */
	public void setImage(Image image)
	{
		/*if(this.tileImg!=null)
			this.tileImg.dispose();*/
		temp1=0;
		temp2=0;
		a=0;
		b=0;
		
		this.tileImg=image;
		redraw();
	}
	/**
	 * Sets the boat image
	 * @param image the selected boat image
	 */
	public void setBoatImage(Image image)
	{
		firstTile = false;
		this.boatImg = image;
		
	}
	/**
	 * Sets the image before the change
	 * @param image the selected image
	 */
	public void setBeforeImage(Image image)
	{
		this.beforeImage=image;
	}
	/**
	 * 
	 * @return returns the image that is restored here
	 */
	public Image getImage()
	{
		return this.tileImg;
	}
	/**
	 * Puts a circle in the selected place, puts circle for the solution display.
	 */
	/*public void putCircle()
	{
		circle = true;
		redraw();
	}*/
	public void putArrow(Image i)
	{
		circle = true;
		this.arrowImage = i;
		redraw();
	}
	/**
	 * sets a hint in the selected place
	 */
	public void setHint()
	{	
		hint=true;
		redraw();
	}
	/**
	 * removes the hint from the selected place
	 */
	public void removeHint()
	{
		hint = false;
		redraw();
	}
	/**
	 * removes the solution display in the selected place
	 */
	public void removeCircle()
	{
		
		circle=false;
		redraw();
	}
	/**
	 * 
	 * @return returns if there is a hint in this tile
	 */
	public boolean isHint()
	{
		return hint;
	}
	/**
	 * 
	 * @return returns if part of the solution display is in this tile
	 */
	public boolean isCircle()
	{
		return circle;
	}
	/**
	 * 
	 * @return returns if this tile is the first tile
	 */
	public boolean isFirstTile() 
	{
		return firstTile;
	}
	/**
	 * Sets if the tile is the first tile or not
	 * @param firstTile boolean variable that stated if this tile is the first tile
	 */
	public void setFirstTile(boolean firstTile) 
	{
		this.firstTile = firstTile;
	}
	public boolean isFinalImg() 
	{
		return finalImg;
	}

	public void setFinalImg(boolean finalImg) 
	{
		this.finalImg = finalImg;
	}
}
