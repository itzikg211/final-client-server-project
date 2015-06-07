package presenter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import model.Model;
import view.Command;
import view.StartWindow;
import view.View;
import algorithms.demo.MazeSearch;
import algorithms.mazeGenerators.Maze;
import algorithms.search.BFS;
import algorithms.search.Solution;
/**
 * This is the presenter we use in our project in the MVP pattern
 * @author user1
 *
 */
public class Presenter implements Observer{
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
		commands.put("generate maze",  new generateMaze());
		commands.put("display maze",  new displayMaze());
		commands.put("solve maze",  new solveMaze());
		commands.put("display solution",  new displaySolution());
		commands.put("exit",  new Exit());
		this.m=m;
		this.v=v;
	}
	
	/**
	 * the generate maze command. using the model and the view to generate a selected maze.
	 * @author Ran Sarussi
	 *
	 */
	public class generateMaze implements Command
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
	public class displayMaze implements Command
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
	public class solveMaze implements Command
	{
		public void doCommand(String path) throws FileNotFoundException,IOException
		{
			String[] ints = command.split(" ");
			m.setName(ints[0]);
			Maze maze = mazes.get(command);
			m.solveMaze(maze);
			System.out.println("Solution for " + command + " is ready!");			
			BufferedReader reader = new BufferedReader(new FileReader("names.txt"));
			String temp = "";
			String line = "";
			try 
			{
				while ((temp = reader.readLine()) != null)
				{
					line+=temp;
				}
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			reader.close();
			
			OutputStream out = new FileOutputStream(new File("names.txt"));
			String str = line + "#" + ints[0];
			out.write(str.getBytes());
			out.close();
		}		
	}
	/**
	 * The display maze command. using the model and the view to display a solution for a selected maze
	 * @author user1
	 *
	 */
	public class displaySolution implements Command
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
			System.exit(0);
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
				//System.out.println("bla bla : " + str);
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
					System.out.println("Doing gui solve maze command");
					String[] sw = str.split(" ");
					String name = sw[3];
					Solution s = setGuiSolution(name);
					v.setSolution(s);
				}
				else
				{
				if((String)arg=="start")
					v.setCommands(this.commands);
				else if((String)arg=="finish")
				{
					Exit e = new Exit();
					try {
						e.doCommand("null");
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else 
				{
					Command c = v.getUserCommand();
					this.command = (String)arg;
					try {
						
						c.doCommand(null);
						
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			}
		}
		if (o==m)
		{
			v.printMessage((String)arg);
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
	public Solution setGuiSolution(String name)
	{
		if(mazes.containsKey(name))
		{
			m.setName(name);
			m.solveMaze(mazes.get(name));
			System.out.println("Solution for " + name + " is ready!");	
			return m.getSolution();
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
		if(mazes.keySet().contains(name))
		{
			System.out.println("TRY");
			System.out.println("The maze already exist");
			return null;
		}
		m.generateMaze(rows,cols);
		Maze maze = m.getMaze();
		mazes.put(name, maze);
		
		m.setName(name);
		System.out.println("Solution for " + command + " is ready!");			
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("names.txt"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String temp = "";
		String line = "";
		try 
		{
			while ((temp = reader.readLine()) != null)
			{
				line+=temp;
			}
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		OutputStream out = null;
		try {
			out = new FileOutputStream(new File("names.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String str = line + "#" + name;
		try {
			out.write(str.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return maze;
	}

}