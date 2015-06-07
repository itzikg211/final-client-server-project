package view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Observable;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
/**
 * The view we use in this MVP pattern
 * @author Ran Saroussi
 *
 */
public class MyView extends Observable implements View {
	Command command;
	CliMVP c;
	HashMap<String, Command> commands = new HashMap<String, Command>();
	/**
	 * Constructs MyView
	 */
	public MyView() 
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		PrintWriter writer = new PrintWriter(System.out);
		this.c=new CliMVP(reader, writer);
		c.setView(this);
	}
	@Override
	/**
	 * The method where we start the project
	 */
	public void start() 
	{
		setChanged();
		notifyObservers("start");
		new Thread(this.c).run();
	}
	/**
	 * Using the setChanged() function.
	 */
	public void setChangedFunc()
	{
		setChanged();
	}
	/**
	 * Setting the commands to injected arrayList of Commands
	 */
	@Override
	public void setCommands(HashMap<String, Command> commands2)
	{
		this.commands = commands2;
		c.setHashMap(commands2);
	}
	/**
	 * Printing a string sent from the model
	 */
	public void printMessage(String str)
	{
		System.out.println(str);
	}
	/**
	 * returns the selected Command to the presenter
	 */
	@Override
	public Command getUserCommand() 
	{
		return command;
	}
	/**
	 * Dispplays the selected maze
	 * @param m the selected maze
	 */
	@Override
	public void displayMaze(Maze m) 
	{
		m.print();
	}
	/**
	 * Displays the selected solution
	 * @param s the selected solution
	 */
	@Override
	public void displaySolution(Solution s)
	{
		if(s!=null)
			s.displaySolution();
		else
			System.out.println("Solution is not exist yet");
	}
	/**
	 * Sets the maze in the GUI control
	 * @param m the selected maze
	 */
	@Override
	public void setGuiMaze(Maze m) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * sets the solution data member
	 * @param s the selected solution
	 */
	@Override
	public void setSolution(Solution s) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * Sets the solution data member that starts in the selected position
	 * @param sol String that states the first position of the solution
	 */
	@Override
	public void setStartSolution(Solution sol) {
		// TODO Auto-generated method stub
		
	}
	

}