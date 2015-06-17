package GUI;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import model.MyModel;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;

import presenter.Presenter;
import presenter.Properties;
import presenter.Properties.WayOfDisplay;
import view.Command;
import view.MyView;
import view.View;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
/**
 * This class defines the window that opens in the start of the project
* @author  Sarusi Ran, Gershfeld Itzik 
* @version 1.0
* @since   2015-02-06
 */
public class StartWindow extends BasicWindow implements View
{
	private Properties properties;
	int numR = 0;
	int numC = 0;
	HashMap<String, Command> commands = new HashMap<String, Command>();
	Command command;
	Maze myMaze;
	CommonGameBoard gameBoard;
	Solution sol;
	Solution sol2;
	int NumberOfMoves = 0;
	Boat b;
	boolean solvedAlready = false;
	boolean hasWonForMusic;
	boolean nameIsFine;
	/**
	 * Constructs the start window 
	 * @param title the title of the start window
	 * @param width the width of the start window
	 * @param height the height of the start window
	 */
	public StartWindow(String title, int width, int height) 
	{
		super(title, width, height);
		properties = new Properties();
	}

	@Override
	/**
	 * starts the connection with the presenter
	 */
	public void start() 
	{
		setChanged();
		notifyObservers("start");
		
	}
	/**
	 * @return returns the selected user command
	 */
	@Override
	public Command getUserCommand() 
	{
		return command;
	}
	
	@Override
	public void displayMaze(Maze m) 
	{
	}
	/**
	 * Displays the selected solution
	 * @param s the selected solution
	 */
	@Override
	public void displaySolution(Solution s) 
	{
	}
	/**
	 * Sets the hashmap of commands in the view
	 * @param commands2
	 */
	@Override
	public void setCommands(HashMap<String, Command> commands2) 
	{
		this.commands = commands2;
		
	}
	
	/**
	 * shows a message box with the received string to the user
	 */
	@Override
	public void printMessage(String str) 
	{
		MessageBox mb = new MessageBox(shell,0);
		mb.setMessage(str);
		mb.setText("Message");
		mb.open();
		
	}
	/**
	 * the function that sets the widgets in the start window
	 */
	@Override
	void initWidgets() 
	{
		////All the widgets
		shell.setLayout(new GridLayout(2,false));
		
	    // Create the bar menu
	    Menu menuBar = new Menu(shell, SWT.BAR);
	    // Create the File item's dropdown menu
	    Menu fileMenu = new Menu(menuBar);
	    Menu HelpMenu = new Menu(menuBar);
	    // Create all the items in the bar menu
	    MenuItem fileItem = new MenuItem(menuBar, SWT.CASCADE);
	    fileItem.setText("File");
	    fileItem.setMenu(fileMenu);
	    MenuItem HelpItem = new MenuItem(menuBar, SWT.CASCADE);
	    HelpItem.setText("Help");
	    HelpItem.setMenu(HelpMenu);
	    // Create all the items in the File dropdown menu
	    MenuItem setPropertiesItem = new MenuItem(fileMenu, SWT.NONE);
	    setPropertiesItem.setText("Set Properties");
	    MenuItem exitItem = new MenuItem(fileMenu, SWT.NONE);
	    exitItem.setText("Exit");
	    MenuItem rules = new MenuItem(HelpMenu, SWT.NONE);
	    rules.setText("Explaining the rules");
	    MenuItem web = new MenuItem(HelpMenu, SWT.NONE);
	    web.setText("Search in the web");
	    shell.setMenuBar(menuBar);
		Label numOfRows = new Label(shell,SWT.NONE);
		numOfRows.setText("Choose the number of rows");
		numOfRows.setLayoutData(new GridData(SWT.FILL,SWT.FILL, false,false,1,1));
		//CREATES THE NEW GENERIC BOARD! you can change mazeBoard with any other gameBoard
		//    ||
		//    ||
		//    \/
		gameBoard=new MazeBoard(shell, SWT.BORDER,new Boat(gameBoard));
		gameBoard.setCharacterImages();
		gameBoard.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,true,1,9));
		
		Combo rows = new Combo(shell, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
		rows.setLayoutData(new GridData(SWT.FILL,SWT.FILL, false,false,1,1));
		for(int i=2;i<36;i++)
		{
			rows.add(i + " rows");
		}
		Label numOfCols = new Label(shell, SWT.NONE);
		numOfCols.setText("Choose the number of columns");
		numOfCols.setLayoutData(new GridData(SWT.FILL,SWT.NONE, false,false,1,1));
		Combo cols = new Combo(shell, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
		cols.setLayoutData(new GridData(SWT.FILL,SWT.NONE, false,false,1,1));
		for(int i=2;i<36;i++)
		{
			cols.add(i + " columns");
		}
		Label name = new Label(shell, SWT.NONE);
		name.setText("Insert the name of the maze:");
		name.setLayoutData(new GridData(SWT.FILL,SWT.NONE, false,false,1,1));
		Text t = new Text(shell, SWT.BORDER);
		t.setLayoutData(new GridData(SWT.FILL,SWT.TOP, false,false,1,1));
		t.setText("");
		Button a=new Button(shell, SWT.PUSH);
		a.setText("Generate the maze");
		a.setLayoutData(new GridData(SWT.FILL,SWT.NONE, false,false,1,1));
		Button hint=new Button(shell, SWT.PUSH);
		hint.setText("Give me a hint");
		hint.setLayoutData(new GridData(SWT.FILL,SWT.NONE, false,false,1,1));
		Button solve=new Button(shell, SWT.PUSH);
		solve.setText("Solve the maze");
		solve.setLayoutData(new GridData(SWT.FILL,SWT.NONE, false,false,1,1));
		
		
		////All the listeners
		/**
		 * handles the mouse wheel movement
		 */
		gameBoard.addMouseWheelListener(new MouseWheelListener() {
			
			@Override
			public void mouseScrolled(MouseEvent e) 
			{
				if((e.stateMask & SWT.CTRL) != 0)
				{
					if(e.count > 0)
					{
						if((int)((gameBoard.getSize().x)*1.1)<= shell.getSize().x && (int)((gameBoard.getSize().y)*1.1)<= shell.getSize().y)
						{
							gameBoard.setSize((int)((gameBoard.getSize().x)*1.1),(int)((gameBoard.getSize().y)*1.1));
							gameBoard.redraw();
						}
					}
					else 
					{
						if((gameBoard.getSize().x)>=0 && (gameBoard.getSize().y)>=0)
						{
							gameBoard.setSize((int)((gameBoard.getSize().x)/1.1),(int)((gameBoard.getSize().y)/1.1));
							gameBoard.redraw();
						}
					}
						
				}
				
			}
		});
		
		
		shell.addListener(SWT.Close, new Listener() 
		{
		      public void handleEvent(Event event) 
		      {
					int style = SWT.ICON_QUESTION |SWT.YES | SWT.NO;
					MessageBox mb = new MessageBox(shell,style);
					mb.setMessage("Exit the game ?");
					mb.setText("Confirm Exit");
					int rc = mb.open();
					switch(rc)
					{
					case SWT.YES:
						setChanged();
						notifyObservers("finish");
						display.dispose();				
					break;
					case SWT.NO:
						break;
					}
		      }
	    });
		
		/**
		 * handles the selected number of colons
		 */
		cols.addSelectionListener(new SelectionListener() 
		{
			
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				for(int i=2;i<36;i++)
				{
					StringBuilder str= new StringBuilder();
					str.append(i);
					str.append(" columns");
					if(cols.getText().equals(str.toString()))
						numC = i;
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) 
			{
			}
		});
		/**
		 * handles the selected number of rows
		 */
		rows.addSelectionListener(new SelectionListener() 
		{
			
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				for(int i=2;i<36;i++)
				{
					StringBuilder str= new StringBuilder();
					str.append(i);
					str.append(" rows");
					if(rows.getText().equals(str.toString()))
						numR = i;
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) 
			{
			}
		});
		/**
		 * handles the generate maze button
		 */
		a.addSelectionListener(new SelectionListener() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				NumberOfMoves = 0;
				//Setting up the properties before lunching the game
				XMLEncoder xmle = null;
				try 
				{
					xmle = new XMLEncoder(new FileOutputStream("src/properties.xml"));
					xmle.writeObject(properties);
				} 
				catch (FileNotFoundException e1) 
				{
					e1.printStackTrace();
				}
				xmle.flush();
				xmle.close();
				if(numR != 0 && numC != 0)
				{
					
					String str = t.getText();
					if(str.equals(""))
					{
						MessageBox m2 = new MessageBox(shell);
						m2.setText("BAD INPUT");
						m2.setMessage("You didnt input maze's name");
						m2.open();
					}
					else
					{
						gameBoard.redraw();
						solvedAlready = false;
						hasWonForMusic = false;
						gameBoard.setX(0);
						gameBoard.setY(0);
						String send = "generate maze ";
						send = send + str;
						send = send + " " + numR + " " + numC ;
						setChanged();
						notifyObservers(send);

						if(myMaze!=null)
						{
							gameBoard.displayBoard(myMaze);
							gameBoard.forceFocus();
						}
					}
				}
				else
				{
					MessageBox mb = new MessageBox(shell,SWT.ICON_ERROR);
					mb.setText("Error");
					if(numR==0)
						mb.setMessage("You didn't choose the number of rows");
					else
						mb.setMessage("You didn't choose the number of columns");
					mb.open();
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		/**
		 * handles the hint button that the user asks for
		 */
		hint.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0)
			{
				String str = "hint " + gameBoard.getX() + " " + gameBoard.getY();
				setChanged();
				notifyObservers(str);
				//put the relevant string representing the current state here
				if(sol2!=null)
				{
					sol2.displaySolution();
					int size = sol2.getList().size();
					int j = sol2.SolutionToArray().get(size*2-3);
					int i = sol2.SolutionToArray().get(size*2-4);
					gameBoard.setHint(i, j);					
					gameBoard.forceFocus();
				}

				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) 
			{
			}
		});
		/**
		 * handles the hint button that the user asks for
		 */
		solve.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				/////
				String send = "gui solve maze ";
				send += t.getText();
				int x =  gameBoard.getX();
				int y = gameBoard.getY();
				String add = " " + x + " " + y;
				send += add;
				if(solvedAlready == false)
				{
					setChanged();
					notifyObservers(send);
					if(sol==null)
					{
					}
					else
					{
						
						gameBoard.displaySolution(sol);
						gameBoard.forceFocus();
						solvedAlready = true;
					}
				}
				else
				{
					MessageBox mb = new MessageBox(shell);
					mb.setMessage("You already solved the maze");
					mb.open();
					gameBoard.forceFocus();
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) 
			{
			}
		});
	
		
		
		MessageBox m = new MessageBox(shell);
		m.setText("You finished");
		/**
		 * handles the user's key events
		 */
		gameBoard.addKeyListener(new KeyListener()
		{
			@Override
			public void keyPressed(KeyEvent e) {
				
				if(e.keyCode == SWT.ESC)
				{
					int style = SWT.ICON_QUESTION |SWT.YES | SWT.NO;
					MessageBox mb = new MessageBox(shell,style);
					mb.setMessage("Exit the game ?");
					mb.setText("Confirm Exit");
					int rc = mb.open();
					switch(rc)
					{
					case SWT.YES:
						setChanged();
						notifyObservers("finish");
						display.dispose();				
					break;
					case SWT.NO:
						break;
					
					}
				}
				if(e.keyCode == SWT.ARROW_UP)
				{
					if(gameBoard.canMove(gameBoard.getX(),gameBoard.getY(), 0))
					{
						gameBoard.SetCharacterDirection(0);
						gameBoard.setCharacterPosition(gameBoard.getX()-1,gameBoard.getY());
						NumberOfMoves++;
					}
				}
					 
				
			if(e.keyCode == SWT.ARROW_DOWN)
			{
				if(gameBoard.canMove(gameBoard.getX(),gameBoard.getY(), 2))
				{
					gameBoard.SetCharacterDirection(2);
					gameBoard.setCharacterPosition(gameBoard.getX()+1,gameBoard.getY());
					NumberOfMoves++;
				}
			}
			if(e.keyCode == SWT.ARROW_LEFT)
			{
				if(gameBoard.canMove(gameBoard.getX(),gameBoard.getY(), 3))
				{
					gameBoard.SetCharacterDirection(3);
					gameBoard.setCharacterPosition(gameBoard.getX(),gameBoard.getY()-1);
					NumberOfMoves++;
				}
			}
			if(e.keyCode == SWT.ARROW_RIGHT)
			{
				if(gameBoard.canMove(gameBoard.getX(),gameBoard.getY(), 1))
				{
					gameBoard.SetCharacterDirection(1);
					gameBoard.setCharacterPosition(gameBoard.getX(),gameBoard.getY()+1);
					NumberOfMoves++;
				}
				
			}
			if(e.keyCode == SWT.KEYPAD_7)
			{
				if(gameBoard.canMove(gameBoard.getX(),gameBoard.getY(), 4))
				{
					gameBoard.SetCharacterDirection(3);
					gameBoard.setCharacterPosition(gameBoard.getX()-1,gameBoard.getY()-1);	
					NumberOfMoves++;
				}
			}
			if(e.keyCode == SWT.KEYPAD_9)
			{
				if(gameBoard.canMove(gameBoard.getX(),gameBoard.getY(), 5))
				{
					gameBoard.SetCharacterDirection(1);
					gameBoard.setCharacterPosition(gameBoard.getX()-1,gameBoard.getY()+1);
					NumberOfMoves++;
				}
			}
			if(e.keyCode == SWT.KEYPAD_1)
			{
				if(gameBoard.canMove(gameBoard.getX(),gameBoard.getY(), 6))
				{
					gameBoard.setCharacterPosition(gameBoard.getX()+1,gameBoard.getY()-1);	
					NumberOfMoves++;
				}
			}
			if(e.keyCode == SWT.KEYPAD_3)
			{
				if(gameBoard.canMove(gameBoard.getX(),gameBoard.getY(), 7))
				{
					gameBoard.SetCharacterDirection(1);
					gameBoard.setCharacterPosition(gameBoard.getX()+1,gameBoard.getY()+1);	
					NumberOfMoves++;
				}
			}
			
			}
			@Override
			public void keyReleased(KeyEvent e) {
				if(!hasWonForMusic)
				{
					if(gameBoard.getX()==gameBoard.BoardRows-1 & gameBoard.getY()==gameBoard.BoardCols-1)
					{
						File file = new File("resources/music/winnerMusic.wav");
						AudioInputStream stream;
						Clip clip;
						try 
						{
							
						    stream = AudioSystem.getAudioInputStream(file);
						    clip = AudioSystem.getClip();
						    clip.open(stream);
						    clip.start();
						    hasWonForMusic = true;
						  
						}
						catch (Exception ex) 
						{
						    ex.printStackTrace();
						}
						m.setMessage("Congrats! you finished the maze using " + NumberOfMoves + " moves!");
						m.open();
					}
				
				}
			}
			
		});
		/**
		 *  handles the exit choice in the menu bar
		 */
		exitItem.addSelectionListener(new SelectionListener() 
		{
			
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				int style = SWT.ICON_QUESTION |SWT.YES | SWT.NO;
				MessageBox mb = new MessageBox(shell,style);
				mb.setMessage("Exit the game ?");
				mb.setText("Confirm Exit");
				int rc = mb.open();
				switch(rc)
				{
				case SWT.YES:
					setChanged();
					notifyObservers("finish");
					display.dispose();				
				break;
				case SWT.NO:
					break;
				}
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) 
			{
			}
		});
		/**
		 *  handles the proprties settings in the menu bar
		 */
		setPropertiesItem.addSelectionListener(new SelectionListener() 
		{
			
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				FileDialog fd=new FileDialog(shell,SWT.OPEN);
				fd.setText("open");
				fd.setFilterPath("");
				String[] filterExt = { "*.xml" };
				fd.setFilterExtensions(filterExt);
				String fileName = fd.open();
				XMLDecoder d = null;
				try 
				{
					d = new XMLDecoder(new FileInputStream(fileName));
					properties=(Properties)d.readObject();
				} 
				catch (FileNotFoundException e1) 
				{
					e1.printStackTrace();
				}
				
				d.close();
				
				//we check here if we need to change the way of display
				if(properties.getView() == WayOfDisplay.ECLIPSE_CONSOLE)
				{
					display.dispose();
					MyModel m = new MyModel(properties);
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
			}
		});
		/**
		 *  handles the web choice in the help bar
		 */
		web.addSelectionListener(new SelectionListener() 
		{
			
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				org.eclipse.swt.program.Program.launch("https://www.google.co.il/webhp?sourceid=chrome-instant&ion=1&espv=2&ie=UTF-8#q=solve%20a%20maze");
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) 
			{
			}
		});
		/**
		 *  handles the rules display in the help bar
		 */
		rules.addSelectionListener(new SelectionListener() 
		{
			
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				MessageBox messageBox = new MessageBox(shell, SWT.ICON_INFORMATION);
			    messageBox.setMessage("Move the Boat using the arrows keys or by dragging the boat.\nYou may set the properties as you like by loading an xml file or just set them up at the beginning, before the game. If you wish to "
			    		+ "move in diagonals you may use the number keys(1,3,7,9) Enjoy!");
			    messageBox.setText("Information");
			    messageBox.open();

			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0)
			{
			}
		});
		
	}
	/**
	 * 
	 * @return returns the command data member
	 */
	public Command getCommand()
	{
		return command;
	}
	/**
	 * Sets the selected command
	 * @param command the selected command
	 */
	public void setCommand(Command command) {
		this.command = command;
	}
	/**
	 * sets the solution data member
	 * @param s the selected solution
	 */
	@Override
	public void setGuiMaze(Maze m) 
	{
		this.myMaze = m;
		
	}
	/**
	 * sets the solution data member
	 * @param s the selected solution
	 */

	@Override
	public void setSolution(Solution s) 
	{
		sol = s;
		
	}
	/**
	 * Sets the solution data member that starts in the selected position
	 * @param sol String that states the first position of the solution
	 */
	@Override
	public void setStartSolution(Solution sol) 
	{
		sol2=sol;		
	}

	public void setGameBoard(CommonGameBoard cgb)
	{
		gameBoard = cgb;
	}

	
}
