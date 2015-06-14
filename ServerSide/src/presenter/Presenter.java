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

import org.eclipse.swt.widgets.Display;

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
			String str2 = (String)arg1;
			if(str2.equals("start server"))
			{
				System.out.println("STARTING THE SERVER.....");
				MyTCPIPServer start = new MyTCPIPServer(v.getProperties().getPortNumber(),m);
				start.startServer(v.getProperties().getNumOfClients());
			}
			//if(str2.startsWith(""))
		}
		if(arg0==m)
		{
			System.out.println("Getting a message from client handler");
			String str = (String)arg1;
			if(str.startsWith("client added"))
			{
				String[] temp = str.split(" ");
				int ID = Integer.parseInt(temp[2]);
				System.out.println("PRESENTER : THE ID IS : " + ID);
				new Thread(new Runnable() {
				      public void run() {
				             Display.getDefault().asyncExec(new Runnable() {
				               public void run() {
				            	   v.addClient(ID);
				               }
				            });
				         }
				      }
				   ).start();
				
			}
			else if(str.startsWith("remove client"))
			{
				String[] temp = str.split(" ");
				int ID = Integer.parseInt(temp[2]);
				new Thread(new Runnable() {
				      public void run() {
				             Display.getDefault().asyncExec(new Runnable() {
				               public void run() {
				            	   v.removeClient(ID);
				               }
				            });
				         }
				      }
				   ).start();
			}
			else
			{
				String status;
				String[] temp = str.split(" ");
				status = temp[0] + " " + temp[1];
				int clientID = Integer.parseInt(temp[2]);
				//System.out.println("PRESENTER : GENERATING MAZE");
				new Thread(new Runnable() {
				      public void run() {
				             Display.getDefault().asyncExec(new Runnable() {
				               public void run() {
				            	   v.setStatus(clientID, status);
				               }
				            });
				         }
				      }
				   ).start();
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