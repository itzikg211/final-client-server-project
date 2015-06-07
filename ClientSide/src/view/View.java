package view;

import java.util.HashMap;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
/**
 * generic interface for a view in MVP pattern
 * @author user1
 *
 */
public interface View {
	/**
	 * starts the connection with the presenter
	 */
	void start();  
	/**
	 * @return returns the selected user command
	 */
	Command getUserCommand(); 
	/**
	 * Dispplays the selected maze
	 * @param m the selected maze
	 */
	void displayMaze(Maze m); 
	/**
	 * Displays the selected solution
	 * @param s the selected solution
	 */
	void displaySolution(Solution s);
	/**
	 * Sets the hashmap of commands in the view
	 * @param commands2
	 */
	void setCommands(HashMap<String, Command> commands2);
	/**
	 * prints a message sent from the model
	 * @param str
	 */
	public void printMessage(String str);
	/**
	 * Sets the maze in the GUI control
	 * @param m the selected maze
	 */
	void setGuiMaze(Maze m);
	/**
	 * sets the solution data member
	 * @param s the selected solution
	 */
	void setSolution(Solution s);
	/**
	 * Sets the solution data member that starts in the selected position
	 * @param sol String that states the first position of the solution
	 */
	void setStartSolution(Solution sol);
}