package model;

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
	private Socket myClient;
	private PrintWriter outToServer;
	private String [] names;
	private Properties pro;
	boolean nameIsFine;


	/**
	   * This is the C'tor of MyModel. 
	   * <p>The first thing it does is initialize the ThreadPool.
	   * The second thing it does is getting all the data(name, maze, solution) from the database, from the MazeSolutionHibernate table..
	   * @param Nothing.
	   * @return Nothing.
	   */
	
	public MyModel(Properties pro) 
	{
		this.pro = pro;
		executor=Executors.newFixedThreadPool(pro.getThreadNumber());
		System.out.println("CLIENT SIDE");
		nameIsFine = true;
		//myClient = null;
		/*try 
		{
			myClient = new Socket(pro.getIpAddr(),pro.getPortNumber());
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
			outToServer = new PrintWriter(new OutputStreamWriter(myClient.getOutputStream()));
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		try 
		{
			compressObject(pro, myClient.getOutputStream());
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}*/

		/*try 
		{
			expandNames(myClient.getInputStream());
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}*/
		//names = new String[1];
		//names[0] = "gogo";
	}

	
	/**
	 * Compresses an object and send it the the OutputStream
	 * @param objectToCompress the object we want to compress
	 * @param outstream the OutputStream we write the compressed object to
	 */
	public void compressObject(Object objectToCompress, OutputStream outstream)
	{
		GZIPOutputStream gz = null;
		try 
		{
			gz = new GZIPOutputStream(outstream);
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(gz);
			} 
		catch (IOException e1) 
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
			outstream.flush();
		} 
	    catch (IOException e) 
	    {
			e.printStackTrace();
		}
		}
	}
	/**
	 * reads an array of strings that represent the maze names from an InputStream
	 * @param instream the InputStream we read the names from
	 */
	public void expandNames(InputStream instream)
	 {
			GZIPInputStream gs = null;
			try 
			{
				gs = new GZIPInputStream(instream);
			} 
			catch (IOException e) 
			{
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
			}
			   try 
			   {
				   names = (String []) ois.readObject();
			   } 
			   catch (ClassNotFoundException e) 
			   {
				e.printStackTrace();
			   } 
			   catch (IOException e) 
			   {
				e.printStackTrace();
			   } 
	  }
	
	
	 /**
	   * This method solves the maze form a state represented by a String with the specific maze name.
	   * @param s <b>(String) </b>This is the parameter to the getSolution method.
	   * @return sol <b>(Solution) </b>.
	   */
	@Override
	public Solution getSolution(String s) 
	{
		if(maze==null)
		{
			return null;
		}
		
		else
		{
			myClient = null;
			try 
			{
				myClient = new Socket(pro.getIpAddr(),pro.getPortNumber());
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
				outToServer = new PrintWriter(new OutputStreamWriter(myClient.getOutputStream()));
			} 
			catch (IOException e1) 
			{
				e1.printStackTrace();
			}
			try 
			{
				compressObject(pro, myClient.getOutputStream());
				expandNames(myClient.getInputStream());
			} 
			catch (IOException e1) 
			{
				e1.printStackTrace();
			}
			outToServer.println("hint maze"+ " " + mazeName + " " + s);
			outToServer.flush();	
			Future<Solution> future = executor.submit(new Callable<Solution>() 
			{

				@Override
				public Solution call() throws Exception 
				{
					expandSolution(myClient.getInputStream());
					return sol;
				}
			});
			try 
			{
				future.get();
			} 
			catch (InterruptedException e1) 
			{
				e1.printStackTrace();
			}
			catch (ExecutionException e1) 
			{
				e1.printStackTrace();
			}
			outToServer.println("exit");
			outToServer.flush();
			try 
			{
				myClient.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			return sol;
		}		
		
	}
	/**
	 * reads compressed String from the Server
	 * @param instream <b>(InputStream)</b>
	 * @return Nothing.
	 */
	/*private void expandString(InputStream instream) 
	{
		GZIPInputStream gs = null;
		try 
		{
			gs = new GZIPInputStream(instream);
		} 
		catch (IOException e) 
		{
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
		}
		   try 
		   {
			   @SuppressWarnings("unused")
			String temp = (String) ois.readObject();
		   } 
		   catch (ClassNotFoundException e) 
		   {
			e.printStackTrace();
		   } 
		   catch (IOException e) 
		   {
			e.printStackTrace();
		   }
		
	}*/


	 /**
	   * This method gets the Solution with the specific maze name.
	   * @param name <b>(String) </b>This is the parameter to the getSolution method.
	   * @return sol <b>(Solution) </b>.
	   */
	@Override
	public Solution getSolution() 
	{
		
		if(sol==null)
		{
			return null;
		}
		return sol;
	}
	/**
	 * read compressed Maze class from the InputStream
	 * @param instream the InputStream we read the compressed data from
	 */
	 public void expandMaze(InputStream instream)
	 {
			GZIPInputStream gs = null;
			try 
			{
				gs = new GZIPInputStream(instream);
			} 
			catch (IOException e) 
			{
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
			}
			   try 
			   {
				   maze = (Maze) ois.readObject();
			   } 
			   catch (ClassNotFoundException e) 
			   {
				e.printStackTrace();
			   } 
			   catch (IOException e) 
			   {
				e.printStackTrace();
			   } 
	  }
	 /**
	  * read compressed Solution class from the InputStream
	  * @param instream the InputStream we read the compressed Solution class from
	  */
	 public void expandSolution(InputStream instream)
	 {
			GZIPInputStream gs = null;
			try 
			{
				gs = new GZIPInputStream(instream);
			} 
			catch (IOException e) 
			{
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
			   } 
			   catch (ClassNotFoundException e) 
			   {
				e.printStackTrace();
			   } 
			   catch (IOException e) 
			   {
				e.printStackTrace();
			   } 
	  }
	
	/**
	 * generates a maze in the wanted size
	 * @param rows number of rows in the maze
	 * @param cols number of columns in the maze
	 */
	@Override
	public void generateMaze(int rows, int cols) 
	{
			myClient = null;
			try 
			{
				myClient = new Socket(pro.getIpAddr(),pro.getPortNumber());
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
				outToServer = new PrintWriter(new OutputStreamWriter(myClient.getOutputStream()));
			} 
			catch (IOException e1) 
			{
				e1.printStackTrace();
			}
			try 
			{
				compressObject(pro, myClient.getOutputStream());
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			try 
			{
				expandNames(myClient.getInputStream());
			} 
			catch (IOException e1) 
			{
				e1.printStackTrace();
			}
			outToServer.println("generate maze"+ " " + mazeName + " " + rows + " "+ cols);
			outToServer.flush();
			Future<Maze> future = executor.submit(new Callable<Maze>() 
			{

				@Override
				public Maze call() throws Exception 
				{
					expandMaze(myClient.getInputStream());
					return maze;
				}
			});
			try 
			{
				future.get();
			} 
			catch (InterruptedException e1) 
			{
				e1.printStackTrace();
			} 
			catch (ExecutionException e1) 
			{
				e1.printStackTrace();
			}
			outToServer.println("exit");
			outToServer.flush();
			try 
			{
				myClient.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			setChanged();
			notifyObservers("name is fine");
}


	 /**
	   * This method gets the maze with the specific name.
	   * @param name <b>(String) </b>This is the parameter to the getMaze method.
	   * @return maze <b>(Maze) </b>.
	   */
	
	@Override
	public Maze getMaze() 
	{
		if(maze==null)
		{
			return null;
		}
		return maze;
	}
	 /**
	   * This method solves the Maze m, in the end it adds the name of the maze, the maze itself and it's solution into the database.
	   * @param m <b>(Maze) </b>This is the parameter to the solveMaze method
	   * @return Nothing.
	   */
	@Override
	public void solveMaze(Maze m) 
	{
		myClient = null;
		try 
		{
			myClient = new Socket(pro.getIpAddr(),pro.getPortNumber());
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
			outToServer = new PrintWriter(new OutputStreamWriter(myClient.getOutputStream()));
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		try 
		{
			compressObject(pro, myClient.getOutputStream());
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		try 
		{
			expandNames(myClient.getInputStream());
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		outToServer.println("solve maze"+ " " + mazeName);
		outToServer.flush();
		Future<Solution> future = executor.submit(new Callable<Solution>() 
		{

			@Override
			public Solution call() throws Exception 
			{
				expandSolution(myClient.getInputStream());
				return sol;
			}
		});
		try 
		{
			future.get();
		} 
		catch (InterruptedException e1) 
		{
			e1.printStackTrace();
		} 
		catch (ExecutionException e1) 
		{
			e1.printStackTrace();
		}
		outToServer.println("exit");
		outToServer.flush();
		try 
		{
			myClient.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	/**
	   * This method sets the name of the maze.
	   * @param s <b>(String) </b>This is the parameter to the setName method
	   * @return Nothing.
	   */
	@Override
	public void setName(String name) 
	{
		this.mazeName = name;
	}
	/**
	 * sets the solution of the maze
	 */
	@Override
	public void setSol(Solution s) 
	{
		this.sol = s;
	}
	 /**
	   * This method stops the whole process, closes down the threadpool and socket if open.
	   * @param Nothing.
	   * @return Nothing.
	   */
	@Override
	public void stop() 
	{
		if(outToServer != null)
		{
			outToServer.println("exit");
			outToServer.flush();
			try 
			{
				myClient.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		if(executor.isShutdown())
			return;
		executor.shutdown();
		
	}
	 public boolean isNameIsFine() 
	 {
		return nameIsFine;
	}


	public void setNameIsFine(boolean nameIsFine) 
	{
		this.nameIsFine = nameIsFine;
	}
	
	 
}