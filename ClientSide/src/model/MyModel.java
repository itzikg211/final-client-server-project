package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
	//private HashMap<String,HashMap<Maze, Solution>> msols;
	private Properties pro;
	private Socket myServer;
	//private ObjectInputStream inFromServer;
	private PrintWriter outToServer;
	
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
		//msols = new HashMap<String, HashMap<Maze,Solution>>();
		System.out.println("CLIENT SIDE");
		myServer = null;
		try 
		{
			myServer = new Socket(pro.getIpAddr(),pro.getPortNumber());
		} catch (UnknownHostException e) 
		{
			System.out.println("unknown host! You should check if the server is even open...");
			System.exit(0);
			//e.printStackTrace();
		} 
		catch (IOException e) 
		{
			System.out.println("Wrong I/O");
			System.out.println("You should check if the server is even open...");
			System.exit(0);
			//e.printStackTrace();
		}
		try 
		{
			outToServer = new PrintWriter(new OutputStreamWriter(myServer.getOutputStream()));
		} catch (IOException e1) 
		{
			// TODO Auto-generated catch block
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
	
		
		/*inFromServer = null;
		try 
		{
			inFromServer = new BufferedReader(new InputStreamReader(myServer.getInputStream()));
		} catch (IOException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		outToServer = null;
		try 
		{
			outToServer = new PrintWriter(new OutputStreamWriter(myServer.getOutputStream()));
		} catch (IOException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		inFromUser = new BufferedReader(new InputStreamReader(System.in));
		String line = null;
		try 
		{
			while(!(line = inFromUser.readLine()).equals("exit"))
			{
				//System.out.println(line);
				outToServer.println(line);
				outToServer.flush();
				
				String serverMSG = inFromServer.readLine();
				System.out.println(serverMSG);
			}
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		outToServer.println(line);
		outToServer.flush();
		try 
		{
			System.out.println(inFromServer.readLine());
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//getting all the data from the database.
       /* Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        SessionFactory sessionFactory = configuration.buildSessionFactory(ssrb.build());
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		MazeSolutionHibernate ms = new MazeSolutionHibernate();
		String [] names = null;
		try 
		{
			BufferedReader reader = new BufferedReader(new FileReader("names.txt"));
			String line;
			try 
			{
				while ((line = reader.readLine()) != null)
				{
					names = line.split("#");
				}
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(names!=null)
			{
				
				for(int i=1;i<names.length;i++)
				{
					ms = (MazeSolutionHibernate) session.get(MazeSolutionHibernate.class,names[i]);
					HashMap<Maze, Solution> temp = new HashMap<Maze, Solution>();
					temp.put(ms.stringToMaze(ms.getMaze()), ms.stringToSolution(ms.getSol()));
					msols.put(ms.getId(), temp);
					
				}
			}
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		//System.out.println(ms.getId()+" "+ms.getMaze()+" "+ms.getSol());
		
		session.close();*/
	}

/*	public void continueConnection()
	{
		try 
		{
			outToServer = new PrintWriter(new OutputStreamWriter(myServer.getOutputStream()));
		} catch (IOException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}*/
	
	
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
			future = executor.submit(new Callable<Solution>() 
					{
						@Override
						public Solution call() throws Exception 
						{		
							expandSolution(myServer.getInputStream());
							return sol;
						}
			});
			try {
				future.get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
				e.printStackTrace();
			} 
			  /* finally 
			   {
			    try 
			    {
					gs.close();
					ois.close();
				} catch (IOException e) 
			    {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			   }*/
	  }
	
	
	@Override
	public void generateMaze(int rows, int cols) 
	{
		outToServer.println("generate maze"+ " " + mazeName + " " + rows + " "+ cols);
		outToServer.flush();
		try 
		{
			expandMaze(myServer.getInputStream());
		} catch (IOException e1) 
		{
			e1.printStackTrace();
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
		outToServer.println("solve maze"+ " " + mazeName);
		outToServer.flush();
		Future<Solution> future = null;
		future = executor.submit(new Callable<Solution>() 
				{
					@Override
					public Solution call() throws Exception 
					{
						expandSolution(myServer.getInputStream());		
						return sol;
					}
		});
		try {
			future.get();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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