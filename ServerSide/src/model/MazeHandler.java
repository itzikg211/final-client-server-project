package model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.zip.GZIPOutputStream;

public class MazeHandler implements ClientHandler
{

	@Override
	public void handleClient(InputStream inFromClient, OutputStream outToClient) 
	{
		//need to do this part :) TODO
		BufferedReader reader = new BufferedReader(new InputStreamReader(inFromClient));
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(outToClient));
		String line = null;
		
		/*GZIPOutputStream zipOutMaze=new GZIPOutputStream(new FileOutputStream("maze.zip"));
		InputStream in= new FileInputStream(fileName);
		zipOutMaze.write(fromStream(in).getBytes());
		zipOutMaze.flush();
		zipOutMaze.close();
		in.close();*/
		try 
		{
			while(!(line = reader.readLine()).equals("exit"))
			{
			System.out.println(line);
			writer.println("hello :)");
			writer.flush();
			}
		} 
		catch (IOException e) 
		{
			 e.printStackTrace();
		}
		
	}

}
