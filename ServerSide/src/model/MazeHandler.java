package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class MazeHandler implements ClientHandler
{

	@Override
	public void handleClient(InputStream inFromClient, OutputStream outToClient) 
	{
		//need to do this part :) TODO
		BufferedReader reader = new BufferedReader(new InputStreamReader(inFromClient));
		String line = null;
		/*try {
			line = reader.readLine();
			System.out.println(line);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		try 
		{
			while(!(line = reader.readLine()).equals("exit"))
			{
			try 
			{
				System.out.println(line);
				outToClient.write("hello :)".getBytes());
				outToClient.flush();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			}
		} 
		catch (IOException e) 
		{
			 e.printStackTrace();
		}
		
	}

}
