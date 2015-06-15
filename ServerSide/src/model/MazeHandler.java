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
	public void handleClient(Socket s) 
	{
		InputStream inFromClient = null;
		try {
			inFromClient = s.getInputStream();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		OutputStream outToClient = null;
		try {
			outToClient = s.getOutputStream();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		ClientID++;
		int currentID = ClientID;
		clients.put(currentID, client);
		String str2 = "client added " + currentID;
		setChanged();
		notifyObservers(str2);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inFromClient));
		PropertiesServer pro = expandProperties(inFromClient);
		//PrintWriter writer = new PrintWriter(new OutputStreamWriter(outToClient));
		
		String line = null;
		MyModel m = new MyModel(pro);
		compressObject(m.getNames(), outToClient);
		try 
		{
			while(!(line = reader.readLine()).equals("exit"))
			{
			System.out.println(line);
			String[] str = line.split(" ");
			//check if client requested for a maze
			if(str.length == 5)
			{
				if(str[0].equals("generate") && str[1].equals("maze"))
				{
					m.setName(str[2]);
					m.generateMaze(Integer.parseInt(str[3]),Integer.parseInt(str[4]));
					compressObject(m.getMaze(),outToClient);
					String send = "generating maze " + currentID;
					setChanged();
					notifyObservers(send);
					//outToClient.flush();
				}
			}
			//check if client requested for a solution of the maze
			if(str.length == 3)
			{
				if(str[0].equals("solve") && str[1].equals("maze"))
				{
					m.setName(str[2]);
					m.solveMaze(m.getMaze());
					compressObject(m.getSolution(),outToClient);
					outToClient.flush();
					String send = "solving maze " + currentID;
					setChanged();
					notifyObservers(send);
				}
			}
			//check if client requested for a hint for the maze
			if(str.length == 4)
			{
				if(str[0].equals("hint") && str[1].equals("maze"))
				{
					m.setName(str[2]);
					m.solveMaze(m.getMaze());
					compressObject(m.getSolution(str[3]),outToClient);
					String send = "putting hint " + currentID;
					setChanged();
					notifyObservers(send);
				}
			}
			
			//writer.println("ACK");
			//writer.flush();
			}
			
		} 
		catch (IOException e) 
		{
			 e.printStackTrace();
			 
		}
		try {
			reader.close();
			System.out.println("finished communication with client");
			 String send = "remove client " + currentID;
			 setChanged();
			 notifyObservers(send);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void removeClient(int ID)
	{
		Socket s = clients.get(ID);
		try {
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void compressObject(Object objectToCompress, OutputStream outstream)
	{
		GZIPOutputStream gz = null;
		try {
			gz = new GZIPOutputStream(outstream);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(gz);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			try {
				oos.writeObject(objectToCompress);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				oos.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}		
		finally 
		{
	    try {
			//oos.close();
			//gz.flush();
			//outstream.flush();
			//gz.close();
			gz.finish();
			//outstream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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