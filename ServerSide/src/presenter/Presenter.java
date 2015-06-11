package presenter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.zip.GZIPOutputStream;

import model.Model;
import view.Command;
import viewGUI.ClientHandler;
import viewGUI.CommonClientHandler;

public class Presenter implements Observer
{
	CommonClientHandler v;
	Model m;
	OutputStream outToClient;
	HashMap<String, Command> commands = new HashMap<String, Command>();
	public Presenter(Model m, CommonClientHandler v) 
	{
		this.m = m;
		this.v = v;
		//setting up the commands
		commands.put("generate maze",  new GenerateMaze());
		commands.put("hint maze",  new HintMaze());
		commands.put("solve maze",  new SolveMaze());
		commands.put("exit",  new Exit());
	}
	
	@Override
	public void update(Observable arg0, Object arg1) 
	{
		String line = (String) arg1;
		String[] str = line.split(" ");
		//check if client requested for a maze
		if(str.length == 5)
		{
			if(str[0].equals("generate") && str[1].equals("maze"))
			{
				try 
				{
					commands.get("generate maze").doCommand(line);
				} 
				catch (FileNotFoundException e) 
				{
					e.printStackTrace();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
		//check if client requested for a solution of the maze
		if(str.length == 3)
		{
			if(str[0].equals("solve") && str[1].equals("maze"))
			{
				try 
				{
					commands.get("solve maze").doCommand(line);
				} 
				catch (FileNotFoundException e) 
				{
					e.printStackTrace();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
		//check if client requested for a hint for the maze
		if(str.length == 4)
		{
			if(str[0].equals("hint") && str[1].equals("maze"))
			{
				try 
				{
					commands.get("hint maze").doCommand(line);
				} 
				catch (FileNotFoundException e) 
				{
					e.printStackTrace();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public class Exit implements Command
	{

		@Override
		public void doCommand(String path) throws FileNotFoundException,IOException 
		{
			//v.closeServer();
		}
		
	}
	
	public class GenerateMaze implements Command
	{

		@Override
		public void doCommand(String path) throws FileNotFoundException,IOException 
		{
			String [] str = path.split(" ");
			m.setName(str[2]);
			m.generateMaze(Integer.parseInt(str[3]),Integer.parseInt(str[4]));
			compressObject(m.getMaze(),outToClient);
		}
		
	}
	public class SolveMaze implements Command
	{

		@Override
		public void doCommand(String path) throws FileNotFoundException,IOException 
		{
			String [] str = path.split(" ");
			m.setName(str[2]);
			m.solveMaze(m.getMaze());
			
			//in this part we are updating the names.txt file
			BufferedReader reader1 = null;
			try 
			{
				reader1 = new BufferedReader(new FileReader("names.txt"));
			} 
			catch (FileNotFoundException e1) 
			{
				e1.printStackTrace();
			}
			String temp = "";
			String line1 = "";
			try 
			{
				while ((temp = reader1.readLine()) != null)
				{
					line1+=temp;
				}
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			try 
			{
				reader1.close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			
			OutputStream out = null;
			try 
			{
				out = new FileOutputStream(new File("names.txt"));
			} 
			catch (FileNotFoundException e) 
			{
				e.printStackTrace();
			}
			String str1 = line1 + "#" + str[2];
			try 
			{
				out.write(str1.getBytes());
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			try 
			{
				out.close();
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
			
			//sending the data to the client
			compressObject(m.getSolution(),outToClient);
			try 
			{
				outToClient.flush();
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		
	}
	
	public class HintMaze implements Command
	{

		@Override
		public void doCommand(String path) throws FileNotFoundException,IOException 
		{
			String [] str = path.split(" "); 
			m.setName(str[2]);
			//m.solveMaze(m.getMaze());
			compressObject(m.getSolution(str[3]),outToClient);
		}
		
	}
	
	
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
		try 
		{
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
			} catch (IOException e) 
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
	
	
	
	public OutputStream getOutToClient() 
	{
		return outToClient;
	}
	
	public void setOutToClient(OutputStream outToClient) 
	{
		this.outToClient = outToClient;
	}
	
	

}
