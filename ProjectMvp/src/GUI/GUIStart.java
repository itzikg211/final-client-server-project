package GUI;

import model.MyModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;

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
		//put the selected start image in the right side of the window
		Image myImage = new Image( display, "resources/mainPic.png" );
		Label myLabel = new Label( shell, SWT.NONE );
		//sets the selected image
		myLabel.setImage( myImage );
		myLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL | SWT.TOP, true,true,1,2));
		myLabel.setSize((int)(shell.getSize().x-50),(int)(shell.getSize().y+30));
		shell.setSize(myLabel.getSize().x, myLabel.getSize().y);
		//create a button that opens the propertiesGUI window which helps the user to selected his own properties
		Button setProperties = new Button(shell, SWT.PUSH);
		setProperties.setText("Set The Properties");
		setProperties.setLayoutData(new GridData(SWT.NONE,SWT.NONE,false,false,1,1));
		////All the listeners
		startGame.addSelectionListener(new SelectionListener() 
		{
			
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				//remove the current window
				display.dispose();
				//sets the properties that the client chose in the model
				MyModel m=new MyModel(properties);
				//checks how the user chose to display the user interface
				//if the user chose GUI
				if(properties.getView() == WayOfDisplay.GUI)
				{	
					//opens a StartWindow
					StartWindow win=new StartWindow("Boat Maze", 1000, 800);
					//create presenter,model and view
					Presenter p = new Presenter(m,win);
					//add p as an observer to the model and the view
					m.addObserver(p);
					win.addObserver(p);
					//calling the function start which notifies the presenter that the view started working
					win.start();
					//pop the window StartWindow
					win.run();
				}
				//if the user chose command line interface
				else
				{
					//create new MyView,Presenter and Model
					MyView v = new MyView();
					Presenter p = new Presenter(m,v);
					//add p as an observer to the new MyModel and MyView
					m.addObserver(p);
					v.addObserver(p);
					//start the communication with the presenter and ready to start
					v.start();
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) 
			{
			}
		});
		//if the "set properties button is clicked"
		setProperties.addSelectionListener(new SelectionListener() 
		{
			
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				//remove the GUIStart window
				display.dispose();
				//create the new PropertiesGUI that help the client to select his own properties
				PropertiesGUI pgui = new PropertiesGUI("Set The Properties",500, 390);
				//start the window
				pgui.run();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) 
			{
			}
		});
		
		
	}
	/**
	 * 
	 * @return returns the properties the user chose
	 */
	public Properties getProperties() {
		return properties;
	}
	/**
	 * set the properties of the user
	 * @param properties the properties we want to inject to the user
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

}