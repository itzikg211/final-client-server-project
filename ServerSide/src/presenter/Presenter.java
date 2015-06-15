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
/**
 * an observer that connects the view and the clientHandler
 * 
 * @author  Sarusi Ran, Gershfeld Itzik 
 * @version 1.0
 * @since   2015-15-06
 *
 */
public class Presenter implements Observer
{
	View v;
	MazeHandler m;
	OutputStream outToClient;
	MyTCPIPServer start;
	HashMap<String, Command> commands = new HashMap<String, Command>();
	/**
	 * Constructs and initializes the Presenter
	 * @param m the mazeHandler
	 * @param v the injected view
	 */
	public Presenter(MazeHandler m, View v) 
	{
		this.m = m;
		this.v = v;
	}
	/**
	 * This function is triggered from the observable that sends an object 
	 */
	@Override
	public void update(Observable arg0, Object arg1) 
	{
		if(arg0==v)
		{
			String str2 = (String)arg1;
			//the view sends this string when the "start server" button is pressed
			if(str2.equals("start server"))
			{
				System.out.println("STARTING THE SERVER.....");
				//creating new MyTCPIPServer with the properties port number
				start = new MyTCPIPServer(v.getProperties().getPortNumber(),m);
				start.startServer(v.getProperties().getNumOfClients());
				
			}
			//the view sends this string "close server" button is pressed
			if(str2.equals("close server"))
			{
				System.out.println("CLOSING THE SERVER.....");
				start.setStopped(true);
				start.startServer(0);
				
			}
			if(str2.startsWith("disconnect "))
			{
				m.removeClient(Integer.parseInt(str2.split(" ")[1]));
			}
			
		}
		if(arg0==m)
		{
			System.out.println("Getting a message from client handler");
			String str = (String)arg1;
			//checks if the clientHandler want to change the client's list
			if(str.startsWith("client added"))
			{
				String[] temp = str.split(" ");
				int ID = Integer.parseInt(temp[2]);
				System.out.println("PRESENTER : THE ID IS : " + ID);
				//runs the add client function in the View that adds a new item in the clients table
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
			//checks if the clientHandler want to remove a client
			else
			{
				//the status we want to inject
				String status;
				String[] temp = str.split(" ");
				status = temp[0] + " " + temp[1];
				//the ID of the client
				int clientID = Integer.parseInt(temp[2]);
				//runs the setStatus function in the View that sets the selected status to the client
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
	
	/**
	 * Compresses an object to GZIP format and sends it to the OutputStream
	 * @param objectToCompress the object we compress and send
	 * @param outstream OutputStream the compressed object is sent to
	 */
	public void compressObject(Object objectToCompress, OutputStream outstream)
	{
		GZIPOutputStream gz = null;
		try 
		{
			//creates new GZIPOutputStream that we want to send
			gz = new GZIPOutputStream(outstream);
		} catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		ObjectOutputStream oos = null;
		try 
		{
			//an ObjectOutputStream that writes to the OutputStream
			oos = new ObjectOutputStream(gz);
		} catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		try 
		{
			try 
			{
				//writes the object to the OutputStream
				oos.writeObject(objectToCompress);
			} 
			catch (IOException e) 
			{

				e.printStackTrace();
			}
			try
			{
				oos.flush();
				oos.reset();
			} 
			catch (IOException e) 
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

}