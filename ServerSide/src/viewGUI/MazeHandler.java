package viewGUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class MazeHandler extends CommonClientHandler
{
	@Override
	public void handleClient(InputStream inFromClient, OutputStream outToClient) 
	{
		int counter = 1;
		//connected here the MVP architectural  pattern
		BufferedReader reader = new BufferedReader(new InputStreamReader(inFromClient));
		String line = null;
		try 
		{
			while(!(line = reader.readLine()).equals("exit"))
			{
					System.out.println(line);
					setChanged();
					notifyObservers(line);	
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
}
