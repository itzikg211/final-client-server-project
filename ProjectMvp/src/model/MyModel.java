package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Observable;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import presenter.Properties;
import algorithms.demo.MazeSearch;
import algorithms.mazeGenerators.DFSMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MazeGenerator;
import algorithms.mazeGenerators.RandomMazeGenerator;
import algorithms.search.AStar;
import algorithms.search.BFS;
import algorithms.search.MazeAirDistance;
import algorithms.search.MazeManhattanDistance;
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
	private String MazeName;
	private HashMap<String,HashMap<Maze, Solution>> msols;
	private Properties pro;
	//private Socket myClient;
	//private PrintWriter outToServer;
	private String [] names;
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
		//Initialize the threadpool.
		this.pro = pro;
		executor=Executors.newFixedThreadPool(pro.getThreadNumber());
		msols = new HashMap<String, HashMap<Maze,Solution>>();
		/*System.out.println("CLIENT SIDE");
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

	
	
	 /**
	   * This method generates a (rows * cols) size maze .
	   * @param rows <b>(int) </b>This is the first parameter to the generateMaze method
	   * @param cols <b>(int) </b>This is the second parameter to the generateMaze method
	   * @return Nothing.
	   */
	
	@Override
	public void generateMaze(int rows, int cols) 
	{
		if(msols.containsKey(MazeName))//maze name already in the database
		{
			maze = msols.get(MazeName).keySet().iterator().next();
		}
		else
		{
			switch(pro.getMazeGenerator()) 
			{
			case DFS_ALGO:
		    			MazeGenerator mg=new DFSMazeGenerator();
		    			maze = mg.generateMaze(rows,cols);
		    			HashMap<Maze, Solution> temp = new HashMap<Maze, Solution>();
		    			temp.put(maze, null);
		    			msols.put(MazeName,temp);
				break;
			case RANDOM_ALGO:
		    			MazeGenerator mg1=new RandomMazeGenerator();
		    			maze = mg1.generateMaze(rows,cols);
		    			HashMap<Maze, Solution> temp1 = new HashMap<Maze, Solution>();
		    			temp1.put(maze, null);
		    			msols.put(MazeName,temp1);
				break;
			default:
				break;
			}
		}
		
		
	}
	 /**
	   * This method gets the maze with the specific name.
	   * @param name <b>(String) </b>This is the parameter to the getMaze method.
	   * @return maze <b>(Maze) </b>.
	   */
	
	@Override
	public Maze getMaze() 
	{
		HashMap<Maze, Solution> temp = msols.get(MazeName);
		maze = temp.keySet().iterator().next();
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
		if(msols.containsKey(MazeName))
		{
			HashMap <Maze, Solution> temp = new HashMap<Maze, Solution>(); 
			temp = msols.get(MazeName);
			sol = temp.get(m);
		}
		else
		{
			switch(pro.getMazeSolver())
			{
			case BFS_DIAGONAL:
       			MazeSearch ms2 = new MazeSearch(m,true);
       			BFS sol3 = new BFS();
       			sol = sol3.search(ms2);
			break;
			case BFS_NO_DIAGONAL:
           		MazeSearch ms1 = new MazeSearch(m,false);
           		BFS sol1 = new BFS();
           		sol = sol1.search(ms1);
			break;
			case ASTAR_MANHATTAN_DISTANCE:
       			MazeSearch ams1 = new MazeSearch(m,false);
       			AStar sol5 = new AStar();
       			sol5.setH(new MazeManhattanDistance());
       			sol = sol5.search(ams1);
			break;
			case ASTAR_AIR_DISTANCE:
           		MazeSearch ams2 = new MazeSearch(m,true);
           		AStar sol7 = new AStar();
           		sol7.setH(new MazeAirDistance());
           		sol = sol7.search(ams2);
			break;
			}
			/////here we communicate with the database
			
			/*Configuration configuration = new Configuration();
			configuration.configure("hibernate.cfg.xml");
			StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
			SessionFactory sessionFactory = configuration.buildSessionFactory(ssrb.build());
			Session session = sessionFactory.openSession();
			session.beginTransaction();
			MazeSolutionHibernate msh = new MazeSolutionHibernate();
			if(msols.containsKey(MazeName))
			{
				session.close();
				return;
			}
			msh.setMaze(maze.toString());
			msh.setSol(sol.toString());
			msh.setId(MazeName);
			session.save(msh);
			session.getTransaction().commit();*/
			HashMap <Maze, Solution> temp1 = new HashMap<Maze, Solution>(); 
			temp1.put(maze, sol);
			msols.put(MazeName, temp1);
			//session.close();
			
		}
		
	}

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
	   * This method stops the whole process, shuts down the threadpool.
	   * @param Nothing.
	   * @return Nothing.
	   */
	
	@Override
	public void stop() 
	{
		if(executor.isShutdown())
			return;
		executor.shutdown();
		
	}
	
	 /**
	   * This method sets the name of the maze.
	   * @param s <b>(String) </b>This is the parameter to the setName method
	   * @return Nothing.
	   */
	
	@Override
	public void setName(String string) 
	{
		this.MazeName = string;
		
	}

	 /**
	   * This method sets the solution of the maze.
	   * @param s <b>(Solution) </b>This is the parameter to the setSol method
	   * @return Nothing.
	   */
	
	@Override
	public void setSol(Solution s) 
	{
		this.sol = s;
	}

	/**
	 * returns a solution from the place represented by the string s (state)
	 * @param String s the string representing the state
	 * @return s <b>(Solution) </b>
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
			switch(pro.getMazeSolver())
			{
			case BFS_DIAGONAL:
       			MazeSearch ms2 = new MazeSearch(maze,true);
       			ms2.setStartState(s);
       			BFS sol3 = new BFS();
       			sol = sol3.search(ms2);
       			break;
			case BFS_NO_DIAGONAL:
           		MazeSearch ms1 = new MazeSearch(maze,false);
           		ms1.setStartState(s);
           		BFS sol2 = new BFS();
           		sol = sol2.search(ms1);
           		break;
			case ASTAR_MANHATTAN_DISTANCE:
       			MazeSearch ams1 = new MazeSearch(maze,false);
       			ams1.setStartState(s);
       			AStar sol5 = new AStar();
       			sol5.setH(new MazeManhattanDistance());
       			sol = sol5.search(ams1);
       			break;
			case ASTAR_AIR_DISTANCE:
           		MazeSearch ams2 = new MazeSearch(maze,true);
           		ams2.setStartState(s);
           		AStar sol7 = new AStar();
           		sol7.setH(new MazeAirDistance());
           		sol = sol7.search(ams2);
           		break;
			}
		}
		return sol;
		
	}
	@Override
	public String[] getNames() 
	{
		return names;
	}
	/**
	   * This method sets the names of the mazes in the DB.
	   * @param s <b>(String []) </b>This is the parameter to the setNames method
	   * @return Nothing.
	   */
	@Override
	public void setNames(String[] names) 
	{
		this.names = names;
	}
	
	@Override
	public void setNameIsFine(boolean nameIsFine) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public boolean isNameIsFine() {
		// TODO Auto-generated method stub
		return false;
	}
	

	
}