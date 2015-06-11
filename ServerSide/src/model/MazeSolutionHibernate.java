package model;



import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import algorithms.search.State;

/**
* The MazeSolutionHibernate class that defines the connection between a maze, it's name and it's solution.
* <p> Latter, during the run of the project, the ORM layer of the Hibernate converts it into a table named MazeSolutionHibernate.
* So our database, in mysql environment, will have the MazeSolutionHibernate table and will contain there all the mazes and their solutions.
*
* @author  Sarusi Ran, Gershfeld Itzik 
* @version 1.0
* @since   2015-05-18 
*/


@Entity
public class MazeSolutionHibernate 
{
	@Id
	private String id;
	@Column(length=1000) 
	private String maze;
	@Column(length=1000) 
	private String sol;
	
	 /**
	   * This method is the getter of the id, in this case it returns a string.
	   * @param Nothing.
	   * @return s <b>(String) </b>
	   */
	public String getId() {
		return id;
	}
	
	 /**
	   * This method is the setter of the id.
	   * @param id <b>(String) </b> this is the parameter to the setId method.
	   * @return Nothing.
	   */
	
	public void setId(String id) {
		this.id = id;
	}
	 /**
	   * This method is the getter of the maze, in this case it returns a string.
	   * @param Nothing.
	   * @return s <b>(String) </b>
	   */
	public String getMaze() {
		return maze;
	}
	
	 /**
	   * This method is the setter of the maze.
	   * @param maze <b>(String) </b> this is the parameter to the setMaze method.
	   * @return Nothing.
	   */
	
	public void setMaze(String maze) {
		this.maze = maze;
	}
	 /**
	   * This method is the getter of the solution, in this case it returns a string.
	   * @param Nothing.
	   * @return s <b>(String) </b>
	   */
	public String getSol() {
		return sol;
	}
	
	 /**
	   * This method is the setter of the solution.
	   * @param sol <b>(String) </b> this is the parameter to the setSol method.
	   * @return Nothing.
	   */
	
	public void setSol(String sol) {
		this.sol = sol;
	}
	
	
	
	 /**
	   * This method converts a string representation of a maze into a maze.
	   * @param s <b>(String) </b>This is the parameter to the stringToMaze method
	   * @return maze <b>(Maze) </b>
	   */
	
	public Maze stringToMaze(String s)
	{
		String [] str = s.split("#");
		int count = 0;
		Maze maze = new Maze(Integer.parseInt(str[0]), Integer.parseInt(str[1]));
		String [] vals = str[2].split("(?<=\\G....)");
		for(int i=0;i<Integer.parseInt(str[0]);i++)
		{
			for(int j=0;j<Integer.parseInt(str[1]);j++)
			{
				String [] temp = vals[count].split(",");
				if(temp[0].equals("0"))
					maze.getCell(i, j).setHasBottomWall(false);
				else
					maze.getCell(i, j).setHasBottomWall(true);
				if(temp[1].equals("0"))
					maze.getCell(i, j).setHasLeftWall(false);
				else
					maze.getCell(i, j).setHasLeftWall(true);
				count++;
			}
		}
		
		return maze;
	}
	
	 /**
	   * This method converts a string representation of a solution into a Solution type.
	   * @param s <b>(String) </b>This is the parameter to the stringToSolution method
	   * @return sol <b>(Solution) </b>
	   */
	
	public Solution stringToSolution(String s)
	{
		String [] str = s.split(" ");
		Solution sol = new Solution();
		List<State> lst = new ArrayList<State>();
		for (int i=0;i<str.length;i++)
		{
			String [] temp = str[i].split(",");
			State st = new State(String.valueOf(temp[0].charAt(1))+","+String.valueOf(temp[1].charAt(0)));
			lst.add(st);
		}
		sol.setList(lst);
		return sol;
	}
}
