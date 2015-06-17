package presenter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import model.Model;
import view.Command;
import view.View;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
/**
 * This is the presenter we use in our project in the MVP pattern
 * @author user1
 *
 */
public class Presenter implements Observer
{
	HashMap<String, Command> commands = new HashMap<String, Command>();
	HashMap<String, Maze> mazes = new HashMap<String, Maze>();
	String command;
	View v;
	Model m;
	/**
	 * Constructs and initializes the presenter
	 * @param m The injected model
	 * @param v The injected view
	 */
	public Presenter(Model m,View v) 
	{
		commands.put("generate maze",  new GenerateMaze());
		commands.put("display maze",  new DisplayMaze());
		commands.put("solve maze",  new SolveMaze());
		commands.put("display solution",  new DisplaySolution());
		commands.put("exit",  new Exit());
		this.m=m;
		this.v=v;
	}
	
	/**
	 * the generate maze command. using the model and the view to generate a selected maze.
	 * @author Ran Sarussi
	 *
	 */
	public class GenerateMaze implements Command
	{

		@Override
		public void doCommand(String path) throws FileNotFoundException,IOException 
		{
			String[] ints = command.split(" ");
			int rows=Integer.parseInt(ints[1]);
			int cols=Integer.parseInt(ints[2]);
			m.generateMaze(rows,cols);
			Maze maze = m.getMaze();
			mazes.put(ints[0], maze);
		}		
	}
	
	
	/**
	 * The display maze command. using the model and the view to to display selcted maze
	 * @author user1
	 *
	 */
	public class DisplayMaze implements Command
	{
		@Override
		public void doCommand(String path) throws FileNotFoundException,IOException
		{
			v.displayMaze(mazes.get(command));
		}	
	}
	/**
	 * The solve maze command. using the model and the view to solve a selected maze
	 * @author user1
	 *
	 */
	public class SolveMaze implements Command
	{
		public void doCommand(String path) throws FileNotFoundException,IOException
		{
			String[] ints = command.split(" ");
			m.setName(ints[0]);
			Maze maze = mazes.get(command);
			m.solveMaze(maze);
		}		
	}
	/**
	 * The display maze command. using the model and the view to display a solution for a selected maze
	 * @author user1
	 *
	 */
	public class DisplaySolution implements Command
	{
		@Override
		public void doCommand(String path) throws FileNotFoundException,IOException
		{
			v.displaySolution(m.getSolution());
			m.setSol(null);
		}		
	}
	/**
	 * The exit command. the job of this command is to close the project and close all the variables.
	 * @author user1
	 *
	 */
	public class Exit implements Command
	{
		@Override
		public void doCommand(String path) throws FileNotFoundException,IOException 
		{
			m.stop();
		}
		
	}
	@Override
	public void update(Observable o, Object arg)
	{
		
		if(o==v)
		{
			if(arg!=null)
			{
				String str = (String)arg;
				if(str.startsWith("generate maze"))
				{
					String [] w = str.split(" ");
					Maze m = setGuiMaze(Integer.parseInt(w[3]), Integer.parseInt(w[4]), w[2]);
					v.setGuiMaze(m);
				}
				else if(str.startsWith("hint"))
				{
					String[] hw = str.split(" ");
					int x = Integer.parseInt(hw[1]);
					int y = Integer.parseInt(hw[2]);
					String s = x +"," + y;
					Solution sol2=m.getSolution(s);
					v.setStartSolution(sol2);
				}	
				else if (str.startsWith("gui solve maze"))
				{
					String[] sw = str.split(" ");
					String name = sw[3];
					int i = Integer.parseInt(sw[4]);
					int j = Integer.parseInt(sw[5]);
					//String send = i+","+j;
					Solution s = setGuiSolution(name,i,j);
					v.setSolution(s);
				}
				else
				{
				if((String)arg=="start")
					v.setCommands(this.commands);
				else if((String)arg=="finish")
				{
					Exit e = new Exit();
					try 
					{
						e.doCommand("null");
					} 
					catch (FileNotFoundException e1) 
					{
						e1.printStackTrace();
					} 
					catch (IOException e1) 
					{
						e1.printStackTrace();
					}
				}
				else 
				{
					Command c = v.getUserCommand();
					this.command = (String)arg;
					try 
					{
						
						c.doCommand(null);
						
					} 
					catch (FileNotFoundException e)
					{
						e.printStackTrace();
					} 
					catch (IOException e) 
					{
						e.printStackTrace();
					}
				}
			}
			}
		}
		if (o==m)
		{
			if(arg!=null)
			{
				String str = (String)arg;
				if(str.startsWith("name in db"))
				{
					v.setHasName(false);
				}
				if(str.startsWith("name is fine"))
					v.setHasName(true);
				}
			//v.printMessage((String)arg);
		}
		//////////////addition to the original presenter

		
		
		
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public View getV() {
		return v;
	}
	public void setV(View v) {
		this.v = v;
	}
	public Model getM() {
		return m;
	}
	public void setM(Model m) {
		this.m = m;
	}
	/**
	 * 
	 * @param name the name of the maze
	 * @return returns the selected maze that matches the maze's name
	 */
	public Solution setGuiSolution(String name,int i,int j)
	{
		if(mazes.containsKey(name))
		{
			String loc = i +"," + j;
			m.setName(name);
			m.solveMaze(m.getMaze());
			return m.getSolution(loc);
		}
		return null;
	}
	/**
	 * creating a maze, returning it and puts it in the hibernate file
	 * @param rows number of rows in the new maze
	 * @param cols number of colons in the new maze
	 * @param name the name of the new maze
	 * @return returns a new generated maze using the parameters
	 */
	public Maze setGuiMaze(int rows,int cols,String name)
	{
		m.setName(name);
		m.setNameIsFine(!m.isNameIsFine());
		m.generateMaze(rows,cols);
		Maze maze = m.getMaze();
		mazes.put(name, maze);
		
		
		return maze;
	}

}