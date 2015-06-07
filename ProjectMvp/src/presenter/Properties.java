package presenter;

import java.io.Serializable;
/**
 * This class job is to manage the user's selected properties.
 * 
 * @author  Sarusi Ran, Gershfeld Itzik 
 * @version 1.0
 * @since   2015-05-19 
 */
public class Properties implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 933599711108468L;
	/**
	 * enums to match the users' selscetion
	 */
	public enum MazeGenerator 
	{
		DFS_ALGO,RANDOM_ALGO
	};
	
	public enum MazeSolver 
	{
		ASTAR_MANHATTAN_DISTANCE,ASTAR_AIR_DISTANCE,BFS_DIAGONAL,BFS_NO_DIAGONAL
	};
	public enum WayOfDisplay
	{
		GUI,ECLIPSE_CONSOLE
	};
	private int threadNumber;
	private MazeSolver mazeSolver;
	private MazeGenerator mazeGenerator;
	private Boolean diagonal;
	private double regCost;
	private double diagonalCost;
	private WayOfDisplay view;
	private String IpAddr;
	private int portNumber;
	/**
	 * Constructs the class Properties and intializes the basic Settings
	 */
	public Properties()
	{
		this.threadNumber=10;
		this.mazeSolver=MazeSolver.ASTAR_MANHATTAN_DISTANCE;
		this.mazeGenerator=MazeGenerator.DFS_ALGO;
		this.diagonal=false;
		this.regCost=10;
		this.diagonalCost=15;
		this.view = WayOfDisplay.GUI;
		this.IpAddr = "127.0.0.1";
		this.portNumber = 5040;
	}
	/**
	 * Constructs the class Properties and intializes the class with the injected fiels
	 * 
	 */
	public Properties(MazeSolver mazeSolver,int threadNumber,MazeGenerator mazeGenerator, Boolean diagonal,double regCost,double diagonalCost)
	{
		this.threadNumber=threadNumber;
		this.mazeSolver=mazeSolver;
		this.mazeGenerator=mazeGenerator;
		this.diagonal=diagonal;
		this.regCost=regCost;
		this.diagonalCost=diagonalCost;
	}
	/**
	 * 
	 * @return returns the number of threads the user decided on
	 */
	public int getThreadNumber() 
	{
		return threadNumber;
	}
	/**
	 * Sets the number of threads to the user's choice
	 * @param threadNumber the number of thereads the user decided
	 */
	public void setThreadNumber(int threadNumber) 
	{
		this.threadNumber = threadNumber;
	}
	/**
	 * 
	 * @return returns the maze solver the user decided on
	 */
	public MazeSolver getMazeSolver() 
	{
		return mazeSolver;
	}
	/**
	 * sets the maze solver to the selected maze solver
	 * @param mazeSolver the selected maze solver
	 */
	public void setMazeSolver(MazeSolver mazeSolver) 
	{
		this.mazeSolver = mazeSolver;
	}
	/**
	 * @return returns the maze generator the user decided on
	 */
	public MazeGenerator getMazeGenerator() 
	{
		return mazeGenerator;
	}
	/**
	 * sets the maze generator to the implemented maze generator
	 * @param mazeGenerator The selected maze generator
	 */
	public void setMazeGenerator(MazeGenerator mazeGenerator) 
	{
		this.mazeGenerator = mazeGenerator;
	}
	/**
	 * @return returns if the maze solver solvs the maze with diagonals or not
	 */
	public Boolean getDiagonal() 
	{
		return diagonal;
	}
	/**
	 * sets if the maze solver solvs the maze with diagonals or not
	 * @param diagonal the selcted choice
	 */
	public void setDiagonal(Boolean diagonal) 
	{
		this.diagonal = diagonal;
	}
	/**
	 * @return returns the cost of the maze solution
	 */
	public double getMovementCost() 
	{
		return regCost;
	}
	/**
	 * sets the cost of the maze solution
	 * @param movementCost the selected cost
	 */
	public void setMovementCost(double movementCost) 
	{
		this.regCost = movementCost;
	}
	/**
	 * @return returns the cost of the maze solution with diagonal
	 */
	public double getDiagonalMovementCost() 
	{
		return diagonalCost;
	}
	/**
	 * sets the cost of the maze solution with diagonal
	 * @param diagonalCost the selected maze solution
	 */
	public void setDiagonalMovementCost(double diagonalCost) 
	{
		this.diagonalCost = diagonalCost;
	}
	
	/**
	 * @return returns the way of display
	 */
	
	public WayOfDisplay getView()
	{
		return view;
	}
	/**
	 * sets the view of the program
	 * @param view the selected way of display
	 */
	public void setView(WayOfDisplay view)
	{
		this.view = view;
	}
	public String getIpAddr() 
	{
		return IpAddr;
	}
	public void setIpAddr(String ipAddr) 
	{
		IpAddr = ipAddr;
	}
	public int getPortNumber()
	{
		return portNumber;
	}
	public void setPortNumber(int portNumber) 
	{
		this.portNumber = portNumber;
	}
	
	
	
}