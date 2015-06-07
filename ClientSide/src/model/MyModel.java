package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import presenter.Properties;
import algorithms.demo.MazeSearch;
import algorithms.mazeGenerators.Maze;
import algorithms.search.BFS;
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
	private ObjectInputStream inFromServer;
	private PrintWriter outToServer;
	private BufferedReader inFromUser;
	
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) 
		
		{
			// TODO Auto-generated catch block
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
			outToServer.println("hint maze"+ " " + mazeName + s);
			outToServer.flush();
			Solution sol2 = null;
			try 
			{
				sol2 = (Solution) inFromServer.readObject();
				
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			catch (ClassNotFoundException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return sol2;
			
			
			/*MazeSearch ms1 = new MazeSearch(maze,false);
			ms1.setStartState(s);
			BFS sol1 = new BFS();
			Solution sol2 = sol1.search(ms1);
			return sol2;*/
		}		
		
	}
	
	@Override
	public Solution getSolution() 
	{
		
		if(sol==null)
		{
			System.out.println("No solution yet");
			return null;
		}
		return sol;
	}

	@Override
	public void generateMaze(int rows, int cols) 
	{
		outToServer.println("generate maze"+ " " + mazeName + rows + " "+ cols);
		outToServer.flush();
		try 
		{
			maze = (Maze) inFromServer.readObject();
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("generated a maze");
		
	}

	
	
	@Override
	public Maze getMaze() 
	{
		if(maze==null)
		{
			System.out.println("No maze yet");
			return null;
		}
		return maze;
	}

	@Override
	public void solveMaze(Maze m) 
	{
		outToServer.println("solve maze"+ " " + mazeName);
		outToServer.flush();
		try 
		{
			sol = (Solution) inFromServer.readObject();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("solved the maze "+ mazeName);
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
	   * This method generates a (rows * cols) size maze .
	   * @param rows <b>(int) </b>This is the first parameter to the generateMaze method
	   * @param cols <b>(int) </b>This is the second parameter to the generateMaze method
	   * @return Nothing.
	   */
	
/*	@Override
	public void generateMaze(int rows, int cols) 
	{
		
		switch(pro.getMazeGenerator()) 
		{
		case DFS_ALGO:
			Future<Maze> future = executor.submit(new Callable<Maze>()
					{
	            @Override
	            public Maze call() throws Exception 
	            {
	    			MazeGenerator mg=new DFSMazeGenerator();
	    			maze = mg.generateMaze(rows,cols);
	    			return maze;
	             }
	             });
			notifyObservers("Genrate completed");
			try 
			{
				Maze temp = future.get();
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			} catch (ExecutionException e) 
			{
				e.printStackTrace();
			}
			break;
		case RANDOM_ALGO:
			Future<Maze> future1 = executor.submit(new Callable<Maze>()
					{
	            @Override
	            public Maze call() throws Exception 
	            {
	    			MazeGenerator mg=new RandomMazeGenerator();
	    			maze = mg.generateMaze(rows,cols);
	    			return maze;
	             }
	             });
				notifyObservers("Genrate completed");
				try 
				{
					Maze temp = future1.get();
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				} catch (ExecutionException e) 
				{
					e.printStackTrace();
				}
			break;
		default:
			break;
		}
		
		
		
	}
	 /**
	   * This method gets the maze with the specific name.
	   * @param name <b>(String) </b>This is the parameter to the getMaze method.
	   * @return maze <b>(Maze) </b>.
	   */
	
	/*@Override
	public Maze getMaze() 
	{
		//HashMap<Maze, Solution> temp = msols.get(name);
		//Maze mz = temp.keySet().iterator().next();
		
		if(maze==null)
		{
			System.out.println("No maze yet");
			return null;
		}
		return maze;
	}

	 /**
	   * This method solves the Maze m, in the end it adds the name of the maze, the maze itself and it's solution into the database.
	   * @param m <b>(Maze) </b>This is the parameter to the solveMaze method
	   * @return Nothing.
	   */
	
	/*@Override
	public void solveMaze(Maze m) 
	{
		if(msols.containsKey(mazeName))
		{
			HashMap <Maze, Solution> temp = new HashMap<Maze, Solution>(); 
			temp = msols.get(mazeName);
			sol = temp.get(m);
			setChanged();
			notifyObservers(4);
		}
		else
		{
			Future<Solution> future = null;
			switch(pro.getMazeSolver())
			{
			case BFS_DIAGONAL:
			future = executor.submit(new Callable<Solution>()
			{
                @Override
                public Solution call() throws Exception 
                {
                	notifyObservers("\nSolution BFS with diagonals");
        			MazeSearch ms2 = new MazeSearch(m,true);
        			BFS sol3 = new BFS();
        			sol = sol3.search(ms2);
        			return sol;
                 }
            });
			break;
			case BFS_NO_DIAGONAL:
			future = executor.submit(new Callable<Solution>()
			{
                @Override
                public Solution call() throws Exception 
                {
            		System.out.println("\nSolution BFS without diagonals");
            		MazeSearch ms1 = new MazeSearch(m,false);
            		BFS sol1 = new BFS();
            		sol = sol1.search(ms1);
            		return sol;
                 }
            });
			break;
			case ASTAR_MANHATTAN_DISTANCE:
			future = executor.submit(new Callable<Solution>()
			{
                @Override
                public Solution call() throws Exception 
                {
                	notifyObservers("\nSolution A* without diagonals");
        			MazeSearch ams1 = new MazeSearch(m,false);
        			AStar sol5 = new AStar();
        			sol5.setH(new MazeManhattanDistance());
        			sol = sol5.search(ams1);
        			return sol;
                 }
            });
			break;
			case ASTAR_AIR_DISTANCE:
			future = executor.submit(new Callable<Solution>()
			{
                @Override
                public Solution call() throws Exception 
                {
            		System.out.println("\nSolution A* with diagonals");
            		MazeSearch ams2 = new MazeSearch(m,true);
            		AStar sol7 = new AStar();
            		sol7.setH(new MazeAirDistance());
            		sol = sol7.search(ams2);
            		return sol;
                 }
            });
			break;
			}
			
			Solution sol1;
			try {
				//////////////////////////added here the get function!!!!!!!!!!!!!!!!!!!!!!!!!!
				sol1 = future.get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/////here we communicate with the database
			
			/*Configuration configuration = new Configuration();
			configuration.configure("hibernate.cfg.xml");
			StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
			SessionFactory sessionFactory = configuration.buildSessionFactory(ssrb.build());
			Session session = sessionFactory.openSession();
			session.beginTransaction();
			MazeSolutionHibernate msh = new MazeSolutionHibernate();*/
			/*Set<String> names = msols.keySet();
			if(!names.contains(mazeName))
			{
				/*msh.setMaze(maze.toString());
				msh.setSol(sol.toString());
				msh.setId(mazeName);
				session.save(msh);
				session.getTransaction().commit();*/
				/*HashMap <Maze, Solution> temp1 = new HashMap<Maze, Solution>(); 
				temp1.put(maze, sol);
				this.msols.put(mazeName,temp1);
			}
			else
			{
				System.out.println("You entered the same name for two different mazes, This maze and solution won't go inside the database.");
			}
			//session.close();
			
		}
		
	}

	 /**
	   * This method gets the Solution with the specific maze name.
	   * @param name <b>(String) </b>This is the parameter to the getSolution method.
	   * @return sol <b>(Solution) </b>.
	   */
	
	/*@Override
	public Solution getSolution() 
	{
		
		if(sol==null)
		{
			System.out.println("No solution yet");
			return null;
		}
		return sol;
	}
*/
	
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
			inFromServer.close();
			inFromUser.close();
			outToServer.close();
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