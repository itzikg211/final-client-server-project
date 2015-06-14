package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import presenter.Properties;
import presenter.PropertiesServer;

public class MazeHandler extends CommonClientHandler
{

	int ClientID=0;
	Socket client;
	HashMap<Integer, Socket> clients = new HashMap<Integer, Socket>();
	
	
	
	@Override
	public void handleClient(Socket client) 
	{

		InputStream inFromClient=null;
		try 
		{
			inFromClient = client.getInputStream();
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		OutputStream outToClient = null;
		try 
		{
			outToClient = client.getOutputStream();
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		ClientID++;
		clients.put(ClientID, client);
		String str2 = "client added " + ClientID;
		setChanged();
		notifyObservers(str2);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inFromClient));
		String line = null;
		PropertiesServer pro = null;
		pro = expandProperties(inFromClient);
		MyModel m = new MyModel(pro);
		compressObject(m.getNames(), outToClient); 
		try 
		{
			while(!(line = reader.readLine()).equals("exit"))
			{
			System.out.println(line);
			String[] str = line.split(" ");
			//check if client requested for a maze
			if(line.startsWith("generate maze"))
			{
				if(str[0].equals("generate") && str[1].equals("maze"))
				{
					m.setName(str[2]);
					m.generateMaze(Integer.parseInt(str[3]),Integer.parseInt(str[4]));
					compressObject(m.getMaze(),outToClient);
					setChanged();
					notifyObservers("generating maze");
				}
			}
			//check if client requested for a solution of the maze
			if(str.length == 3)
			{
				if(str[0].equals("solve") && str[1].equals("maze"))
				{
					setChanged();
					notifyObservers("solving maze");
					m.setName(str[2]);
					m.solveMaze(m.getMaze());
					compressObject(m.getSolution(),outToClient);
					outToClient.flush();
				}
			}
			//check if client requested for a hint for the maze
			if(str.length == 4)
			{
				if(str[0].equals("hint") && str[1].equals("maze"))
				{
					setChanged();
					notifyObservers("putting hint");
					m.setName(str[2]);
					m.solveMaze(m.getMaze());
					compressObject(m.getSolution(str[3]),outToClient);
				}
			}
			}
		} 
		catch (IOException e) 
		{
			 e.printStackTrace();
		}
		try 
		{
			reader.close();
			System.out.println("finished communication with client");
		} catch (IOException e) 
		{
			setChanged();
			String send = "remove client";
			notifyObservers(send);
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
			} catch (IOException e) 
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
	
	public PropertiesServer expandProperties(InputStream instream)
	{
		PropertiesServer pro = new PropertiesServer();
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
			//reading the properties from the client
			Properties proc = (Properties) ois.readObject();
			//setting up the properties of the client
			pro.setDiagonal(proc.getDiagonal());
			pro.setMazeSolver(proc.getMazeSolver());
			pro.setMazeGenerator(proc.getMazeGenerator());
			pro.setDiagonalMovementCost(proc.getDiagonalMovementCost());
			pro.setView(proc.getView());
			pro.setMovementCost(proc.getMovementCost());
			
		} 
		catch (ClassNotFoundException e) 
		{
			
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			
			e.printStackTrace();
		} 
		return pro;
	}


	
	
}
