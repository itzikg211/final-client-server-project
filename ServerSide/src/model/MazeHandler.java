package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import presenter.Properties;
import presenter.PropertiesServer;
/**
 * a client handler that handles the maze clients
 * @author saroussira
 *
 */
public class MazeHandler extends CommonClientHandler
{
	//counter of connected clients
	int ClientID=0;
	Socket client;
	//hashmap that includes the socket and their ID
	HashMap<Integer, Socket> clients = new HashMap<Integer, Socket>();
	@Override
	public void handleClient(Socket s) 
	{
		InputStream inFromClient = null;
		try {
			//sets the inputStream data member to the socket's inputStream
			inFromClient = s.getInputStream();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		OutputStream outToClient = null;
		try {
			//sets the OutputStream data member to the socket's OutputStream
			outToClient = s.getOutputStream();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Boolean solve = false;
		//adding 1 to the client counter
		ClientID++;
		int currentID = ClientID;
		//put the client's socket and his ID in the hashMap
		clients.put(currentID, client);
		//Notifies the presenter that a new client connected and the selected ID
		String str2 = "client added " + currentID;
		setChanged();
		notifyObservers(str2);
		//reades the Strings the client sends 
		BufferedReader reader = new BufferedReader(new InputStreamReader(inFromClient));
		//reads the properties from the  
		PropertiesServer pro = expandProperties(inFromClient);
		//PrintWriter writer = new PrintWriter(new OutputStreamWriter(outToClient));
		String line = null;
		MyModel m = new MyModel(pro);
		//compressObject(m.getNames(), outToClient);
		try 
		{
			//checks id the string is "exit"
			while(!(line = reader.readLine()).equals("exit"))
			{
			System.out.println(line);
			String[] str = line.split(" ");
			//check if client requested for a maze
			if(str.length == 5)
			{
				//checks if the client requested to generate a maze
				if(str[0].equals("generate") && str[1].equals("maze"))
				{
					//sets the name of the maze
					m.setName(str[2]);
					//generate the maze in the selected size
					m.generateMaze(Integer.parseInt(str[3]),Integer.parseInt(str[4]));
					//compress the maze using GZIP is send it to the client *out to client is the server's input stream
					compressObject(m.getMaze(),outToClient);
					//notifies the presenter that a maze is generated and the client's ID
					String send = "generating maze " + currentID;
					setChanged();
					notifyObservers(send);
					//outToClient.flush();
				}
			}
			
			if(str.length == 3)
			{
				//check if client requested for a solution of the maze
				if(str[0].equals("solve") && str[1].equals("maze"))
				{
					//sets the name of the maze from the character's location
					m.setName(str[2]);
					//solves the maze
					m.solveMaze(m.getMaze());
					//compresses the solution and send it to the client
					compressObject(m.getSolution(),outToClient);
					outToClient.flush();
					//notifies the poresenter that the client requested a solution
					String send = "solving maze " + currentID;
					setChanged();
					notifyObservers(send);
					//sets the solve boolean to true (to prevent the hint notification in the server GUI) 
					solve = true;
				}
			}
			
			if(str.length == 4)
			{
				//check if client requested for a hint for the maze
				if(str[0].equals("hint") && str[1].equals("maze"))
				{
					//sets the maze's name
					m.setName(str[2]);
					//solves the maze
					m.solveMaze(m.getMaze());
					//compresses the solution to GZIP and sends it to the client
					compressObject(m.getSolution(str[3]),outToClient);
					String send = "putting hint " + currentID;
					//checks if the maze was solved (to prevent the hint notification in the server GUI)
					if(solve==false)
					{
						setChanged();
						notifyObservers(send);
					}
					else
						solve = false;
				}
			}
			
			}
			
		} 
		catch (IOException e) 
		{
			 e.printStackTrace();
			 
		}
		try {
			//close the communication with the client
			reader.close();
			System.out.println("finished communication with client");
			//notifies the presenter that a client has disconnected
			 String send = "remove client " + currentID;
			 setChanged();
			 notifyObservers(send);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 * Removes the selected client from the hashmap and closes it
	 * @param ID the selected client
	 */
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
	/**
	 * compresses an object to GZIP file and sends it to the client
	 * @param objectToCompress the object the function compresses to GZIP
	 * @param outstream sends the compressed to the outStream
	 */
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

	@Override
	public void setServer(ServerSocket server) 
	{
		this.server = server;
	}
	public void closeServer()
	{
		System.out.println("CLOSING THE SERVER");
		try {
			
			this.server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}