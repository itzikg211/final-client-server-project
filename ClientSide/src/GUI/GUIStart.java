package GUI;

import java.beans.XMLDecoder;
import java.io.FileInputStream;
import java.io.IOException;

import model.MyModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;

import presenter.Presenter;
import presenter.Properties;
import presenter.Properties.WayOfDisplay;
import view.MyView;

/**
 * <h1>class GUIStart</h1>
* The GUIStart class extends BasicWindow.
* This is the first window that pops up in our GUI, it is a nice, presenting the project display.
*<p> 
* @author  Gershfeld Itzik, Sarusi Ran
* @version 1.0
* @since   2015-06-02 
*/



public class GUIStart extends BasicWindow
{
	private Properties properties;
	 /**
	   * This is the C'tor of GUIStart. 
	   * <p>The thing it does is initializing the title and the sizes of the GUIstart window.
	   * @param title <b>(String) </b>This is the first parameter to the GUIStart method
	   * @param width <b>(int) </b>This is the second parameter to the GUIStart method
	   * @param height <b>(int) </b>This is the third parameter to the GUIStart method
	   * @return Nothing.
	   */
	
	public GUIStart(String title, int width, int height)
	{
		super(title, width, height);
		properties = new Properties();
	}

	 /**
	   * This is the function that sets up all the widgets. 
	   * <p>The thing it does is initializing widgets and setting up their event handlers.
	   * @param Nothing.
	   * @return Nothing.
	   */
	
	@Override
	void initWidgets() 
	{
		
		////All the widgets
		shell.setLayout(new GridLayout(2,false));
		Button startGame = new Button(shell, SWT.PUSH);
		startGame.setText("Start the game");
		startGame.setLayoutData(new GridData(SWT.FILL,SWT.NONE,false,false,1,1));
		Image myImage = new Image( display, "resources/mainPic.png" );
		Label myLabel = new Label( shell, SWT.NONE );
		myLabel.setImage( myImage );
		myLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL | SWT.TOP, true,true,1,2));
		myLabel.setSize((int)(shell.getSize().x-50),(int)(shell.getSize().y+30));
		shell.setSize(myLabel.getSize().x, myLabel.getSize().y);
		Button setProperties = new Button(shell, SWT.PUSH);
		setProperties.setText("Set The Properties");
		setProperties.setLayoutData(new GridData(SWT.NONE,SWT.NONE,false,false,1,1));

		
		////All the listeners
		startGame.addSelectionListener(new SelectionListener() 
		{
			
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				display.dispose();
				MyModel m=new MyModel(properties);
				if(properties.getView() == WayOfDisplay.GUI)
				{
					StartWindow win=new StartWindow("Boat Maze", 1000, 800);
					Presenter p = new Presenter(m,win);
					m.addObserver(p);
					win.addObserver(p);
					win.start();
					win.run();
				}
				else
				{
					MyView v = new MyView();
					Presenter p = new Presenter(m,v);
					m.addObserver(p);
					v.addObserver(p);
					v.start();
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) 
			{
				// TODO Auto-generated method stub
				
			}
		});
		
		setProperties.addSelectionListener(new SelectionListener() 
		{
			
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				display.dispose();
				PropertiesGUI pgui = new PropertiesGUI("Set The Properties",500, 390);
				pgui.run();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) 
			{
			}
		});
		
		
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

}