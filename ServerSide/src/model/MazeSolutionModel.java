package model;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;

import presenter.PropertiesServer;
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
* The MazeSolutionModel class implements the Model interface.
* @author  Sarusi Ran, Gershfeld Itzik 
* @version 1.0
* @since   2015-05-18 
*/


public class MazeSolutionModel implements Model
{
	private ExecutorService executor;
	private Maze maze;
	private Solution sol;
	private String MazeName;
	private HashMap<String,HashMap<Maze, Solution>> msols;
	private PropertiesServer pro;
	private String [] names;
	

	/**
	   * This is the C'tor of MazeSolutionModel. 
	   * <p>The first thing it does is initialize the hashmap variables.
	   * The second thing it does is getting all the data(name, maze, solution) from the database, from the MazeSolutionHibernate table..
	   * @param Nothing.
	   * @return Nothing.
	   */
	
	public MazeSolutionModel() 
	{
		//Initialize the threadpool.
		//executor=Executors.newFixedThreadPool(pro.getThreadNumber());
		msols = new HashMap<String, HashMap<Maze,Solution>>();
		//getting all the data from the database.
      /*  Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        SessionFactory sessionFactory = configuration.buildSessionFactory(ssrb.build());
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<MazeSolutionHibernate> allList = session.createQuery("from model.MazeSolutionHibernate").list();
		Iterator<MazeSolutionHibernate> itr = allList.iterator();
		MazeSolutionHibernate msh;
		while(itr.hasNext())
		{
			msh = itr.next();
			HashMap<Maze, Solution> temp = new HashMap<Maze, Solution>();
			temp.put(msh.stringToMaze(msh.getMaze()), msh.stringToSolution(msh.getSol()));
			msols.put(msh.getId(), temp);
		}
		int i=0;
		names = new String [msols.keySet().size()];
		for(String key : msols.keySet())
		{
			names[i] = key;
			i++;
		}
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
	
	/**
	   * This method sets the PropertiesServer of the MazeSolutionModel.
	   * @param pro <b>(PropertiesServer) </b>This is the parameter to the setProperitesServer method
	   * @return Nothing.
	   */
	public void setProperitesServer(PropertiesServer pro)
	{
		this.pro = pro;
	}
	

	
}