package model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.zip.GZIPOutputStream;

import presenter.Properties;

public class MazeHandler implements ClientHandler
{

	@Override
	public void handleClient(InputStream inFromClient, OutputStream outToClient) 
	{
		//need to do this part :) TODO
		BufferedReader reader = new BufferedReader(new InputStreamReader(inFromClient));
		//PrintWriter writer = new PrintWriter(new OutputStreamWriter(outToClient));
		String line = null;
		MyModel m = new MyModel(new Properties());
		try 
		{
			while(true){
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
			
			//writer.println("ACK");
			//writer.flush();
			}
			}
		} 
		catch (IOException e) 
		{
			 e.printStackTrace();
		}
		try {
			reader.close();
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
	

}
