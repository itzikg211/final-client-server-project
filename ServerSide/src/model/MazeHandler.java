package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import algorithms.mazeGenerators.Maze;
import presenter.PropertiesServer;
import presenter.Properties;

public class MazeHandler implements ClientHandler
{

	@Override
	public void handleClient(InputStream inFromClient, OutputStream outToClient) 
	{
		//need to do this part :) TODO
		BufferedReader reader = new BufferedReader(new InputStreamReader(inFromClient));
		//PrintWriter writer = new PrintWriter(new OutputStreamWriter(outToClient));
		
		PropertiesServer pro = expandMaze(inFromClient);
		String line = null;
		MyModel m = new MyModel(pro);
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
				}
			}
			//check if client requested for a solution of the maze
			if(str.length == 3)
			{
				if(str[0].equals("solve") && str[1].equals("maze"))
				{
					m.setName(str[2]);
					m.solveMaze(m.getMaze());
					//in this part we are updating the names.txt file
					BufferedReader reader1 = null;
					try {
						reader1 = new BufferedReader(new FileReader("names.txt"));
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
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
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						reader1.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					OutputStream out = null;
					try {
						out = new FileOutputStream(new File("names.txt"));
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String str1 = line1 + "#" + str[2];
					try {
						out.write(str1.getBytes());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					//sending the data to the client
					compressObject(m.getSolution(),outToClient);
					outToClient.flush();
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
			System.out.println("finished communication with the client");
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}
	
	public void compressObject(Object objectToCompress, OutputStream outstream)
	{
		GZIPOutputStream gz = null;
		try 
		{
			gz = new GZIPOutputStream(outstream);
		} catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		ObjectOutputStream oos = null;
		try 
		{
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
	
	public PropertiesServer expandMaze(InputStream instream)
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
