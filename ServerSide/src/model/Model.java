package model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;


/**
* The Model interface that defines all the model's methods.
* @author  Sarusi Ran, Gershfeld Itzik 
* @version 1.0
* @since   2015-05-18 
*/


public interface Model 
{
	public Solution getSolution(String s);
	 /**
	   * This method generates a (rows * cols) size maze .
	   * @param rows <b>(int) </b>This is the first parameter to the generateMaze method
	   * @param cols <b>(int) </b>This is the second parameter to the generateMaze method
	   * @return Nothing.
	   */
	void generateMaze(int rows,int cols);
	 /**
	   * This method gets the maze with the specific name.
	   * @param name <b>(String) </b>This is the parameter to the getMaze method.
	   * @return maze <b>(Maze) </b>.
	   */
	Maze getMaze();
	 /**
	   * This method solves the Maze m.
	   * @param m <b>(Maze) </b>This is the parameter to the solveMaze method
	   * @return Nothing.
	   */
	void solveMaze(Maze m); 
	
	 /**
	   * This method gets the Solution with the specific maze name.
	   * @return sol <b>(Solution) </b>.
	   */

	Solution getSolution();
	 /**
	   * This method stops the whole process, shuts down the threadpool.
	   * @param Nothing.
	   * @return Nothing.
	   */
	void stop();
	
	 /**
	   * This method sets the name of the maze.
	   * @param s <b>(String) </b>This is the parameter to the setName method
	   * @return Nothing.
	   */
	
	void setName(String s);
	
	 /**
	   * This method sets the solution of the maze.
	   * @param s <b>(Solution) </b>This is the parameter to the setSol method
	   * @return Nothing.
	   */
	
	void setSol(Solution s);
}