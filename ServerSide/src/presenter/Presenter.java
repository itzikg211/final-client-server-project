package presenter;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.zip.GZIPOutputStream;

import model.MazeHandler;
import model.MyTCPIPServer;
import view.Command;
import viewGUI.View;

public class Presenter implements Observer
{
	View v;
	MazeHandler m;
	OutputStream outToClient;
	HashMap<String, Command> commands = new HashMap<String, Command>();
	public Presenter(MazeHandler m, View v) 
	{
		this.m = m;
		this.v = v;
	}
	
	@Override
	public void update(Observable arg0, Object arg1) 
	{
		if(arg0==v)
		{
			String str = (String)arg1;
			if(str.equals("start server"))
			{
				System.out.println("STARTING THE SERVER.....");
				MyTCPIPServer start = new MyTCPIPServer(v.getProperties().getPortNumber(),m);
				start.startServer(v.getProperties().getNumOfClients());
			}
		}
		if(arg0==m)
		{
			System.out.println("Getting a message from client handler");
			String str = (String)arg1;
			if(str.startsWith("client added"))
			{
				String[] temp = str.split(" ");
				int ID = Integer.parseInt(temp[2]);
				System.out.println("PRESENTER : CLIENT " + ID + " ADDED!");
				v.addClient(ID);
			}
			if(str.equals("generating maze"))
			{
				System.out.println("PRESENTER : GENERATING MAZE");
			}
			if(str.equals("solving maze"))
			{
				System.out.println("PRESENTER : SOLVING MAZE");
			}
			if(str.equals("putting hint"))
			{
				System.out.println("PRESENTER : HINT ");
			}
			
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
				oos.reset();
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
	
	
	
	/*public OutputStream getOutToClient() 
	{
		return outToClient;
	}
	
	public void setOutToClient(OutputStream outToClient) 
	{
		this.outToClient = outToClient;
		//the first thing we want to do is to send the names in the DB to the client
		compressObject(m.getNames(), outToClient);
	}*/
	
	

}