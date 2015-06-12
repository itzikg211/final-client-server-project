package model;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import presenter.Properties;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;


/**
* The MyModel class, extends observable and implements the Model interface.
* @author  Sarusi Ran, Gershfeld Itzik 
* @version 1.0
* @since   2015-05-18 
*/


public class MyModel extends Observable implements Model
{
	private ExecutorService executor;
	private Maze maze;
	private Solution sol;
	private String mazeName;
	private Properties pro;
	private Socket myServer;
	private PrintWriter outToServer;
	private String [] names;
	int nameIsFine;
	 /**
	   * This is the C'tor of MyModel. 
	   * <p>The first thing it does is initialize the ThreadPool.
	   * The second thing it does is getting all the data(name, maze, solution) from the database, from the MazeSolutionHibernate table..
	   * @param Nothing.
	   * @return Nothing.
	   */
	
	public MyModel(Properties pro) 
	{
		//Initialize the threadpool.
		this.pro = pro;
		executor=Executors.newFixedThreadPool(pro.getThreadNumber());
		System.out.println("CLIENT SIDE");
		nameIsFine = 0;
		myServer = null;
		try 
		{
			myServer = new Socket(pro.getIpAddr(),pro.getPortNumber());
		} 
		catch (UnknownHostException e) 
		{
			System.out.println("unknown host! You should check if the server is even open...");
			System.exit(0);
		} 
		catch (IOException e) 
		{
			System.out.println("Wrong I/O");
			System.out.println("You should check if the server is even open...");
			System.exit(0);
		}
		try 
		{
			outToServer = new PrintWriter(new OutputStreamWriter(myServer.getOutputStream()));
		} catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		try 
		{
			compressObject(pro, myServer.getOutputStream());
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		names = new String[1];
		names[0] = "gogo";
	/*	try 
		{
			expandNames(myServer.getInputStream());
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}*/
	}

	
	
	public void compressObject(Object objectToCompress, OutputStream outstream)
	{
		GZIPOutputStream gz = null;
		try {
			gz = new GZIPOutputStream(outstream);
		} catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(gz);
		} catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		try 
		{
			try 
			{
				oos.writeObject(objectToCompress);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			try {
				oos.flush();
			} 
			catch (IOException e) 
			{
				
				e.printStackTrace();
			}
			}		
		finally 
		{
	    try 
	    {
			gz.finish();
		} 
	    catch (IOException e) 
	    {
			e.printStackTrace();
		}
		}
	}
	
	public void expandNames(InputStream instream)
	 {
			GZIPInputStream gs = null;
			try 
			{
				gs = new GZIPInputStream(instream);
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			   ObjectInputStream ois = null;
			try 
			{
				ois = new ObjectInputStream(gs);
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			   try 
			   {
				   names = (String []) ois.readObject();
			   } catch (ClassNotFoundException e) 
			   {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			   catch (IOException e) 
			   {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	  }
	
	
	
	@Override
	public Solution getSolution(String s) 
	{
		//System.out.println("GETTING THE HINT SOLUTION");
		if(maze==null)
		{
			//System.out.println("The hint maze is null");
			return null;
		}
		else
		{
			//System.out.println("The hint maze is NOT null");
			Future <Solution> future = null;
			outToServer.println("hint maze"+ " " + mazeName + " " + s);
			outToServer.flush();
//			future = executor.submit(new Callable<Solution>() 
//					{
//						@Override
//						public Solution call() throws Exception 
//						{		
							try {
								expandSolution(myServer.getInputStream());
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
//							return sol;
//						}
//			});
//			try {
//				future.get();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (ExecutionException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			return sol;
		}		
		
	}
	
	@Override
	public Solution getSolution() 
	{
		
		if(sol==null)
		{
			//System.out.println("No solution yet");
			return null;
		}
		return sol;
	}

	 public void expandMaze(InputStream instream)
	 {
			GZIPInputStream gs = null;
			try 
			{
				gs = new GZIPInputStream(instream);
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			   ObjectInputStream ois = null;
			try 
			{
				ois = new ObjectInputStream(gs);
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			   try 
			   {
				   maze = (Maze) ois.readObject();
			   } catch (ClassNotFoundException e) 
			   {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			   catch (IOException e) 
			   {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			   /*try 
			   {
				  InputStream temp = new BufferedInputStream(ois);
				  temp.reset();
			   } 
			   catch (IOException e) 
			   {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
	  }
	 public void expandSolution(InputStream instream)
	 {
			GZIPInputStream gs = null;
			try 
			{
				gs = new GZIPInputStream(instream);
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			   ObjectInputStream ois = null;
			try 
			{
				ois = new ObjectInputStream(gs);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
				System.out.println("null solution - does problems sometimes");
				sol=null;
				return;
			}
			   try 
			   {
				   sol = (Solution) ois.readObject();
			   } catch (ClassNotFoundException e) 
			   {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			   catch (IOException e) 
			   {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} 
			  /* try 
			   {
				  InputStream temp = new BufferedInputStream(ois);
				  temp.reset();
			   } 
			   catch (IOException e) 
			   {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
	  }
	
	
	@Override
	public void generateMaze(int rows, int cols) 
	{
		for(String name : names)
		{
			if(name.equals(mazeName))
			{
				nameIsFine+=1;
				setChanged();
				notifyObservers("name in db");
			}
		}
		if(nameIsFine % 2 == 0)
		{
			outToServer.println("generate maze"+ " " + mazeName + " " + rows + " "+ cols);
			outToServer.flush();
			nameIsFine = 0;
			try 
			{
				expandMaze(myServer.getInputStream());
			} catch (IOException e1) 
			{
				e1.printStackTrace();
			}
			setChanged();
			notifyObservers("name is fine");
		}
		
	}

	
	
	@Override
	public Maze getMaze() 
	{
		if(maze==null)
		{
			return null;
		}
		return maze;
	}

	@Override
	public void solveMaze(Maze m) 
	{
		//names[names.length] = mazeName;
		outToServer.println("solve maze"+ " " + mazeName);
		outToServer.flush();
//		Future<Solution> future = null;
//		future = executor.submit(new Callable<Solution>() 
//				{
//					@Override
//					public Solution call() throws Exception 
//					{
						try {
							expandSolution(myServer.getInputStream());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}		
//						return sol;
//					}
//		});
//		try {
//			future.get();
//		} catch (InterruptedException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (ExecutionException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
	}

	@Override
	public void setName(String name) 
	{
		this.mazeName = name;
	}

	@Override
	public void setSol(Solution s) 
	{
		this.sol = s;
	}
	 /**
	   * This method stops the whole process, shuts down the threadpool.
	   * @param Nothing.
	   * @return Nothing.
	   */
	
	@Override
	public void stop() 
	{
		outToServer.println("exit");
		outToServer.flush();
		System.out.println("stop");
		try {
			//inFromServer.close();
			//inFromUser.close();
			//outToServer.close();
			myServer.close();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(executor.isShutdown())
			return;
		executor.shutdown();
		
	}
	
	 /**
	   * This method sets the name of the maze.
	   * @param s <b>(String) </b>This is the parameter to the setName method
	   * @return Nothing.
	   */
	
	/*@Override
	public void setName(String string) 
	{
		this.mazeName = string;
		
	}

	 /**
	   * This method sets the solution of the maze.
	   * @param s <b>(Solution) </b>This is the parameter to the setSol method
	   * @return Nothing.
	   */
	
	/*@Override
	public void setSol(Solution s) 
	{
		this.sol = s;
	}

	@Override
	public Solution getSolution(String s) 
	{
		System.out.println("GETTING THE HINT SOLUTION");
		if(maze==null)
		{
			System.out.println("The hint maze is null");
			return null;
		}
		else
		{
			System.out.println("The hint maze is NOT null");
			MazeSearch ms1 = new MazeSearch(maze,false);
			ms1.setStartState(s);
			BFS sol1 = new BFS();
			Solution sol2 = sol1.search(ms1);
			return sol2;
		}		
		
	}*/

	
}