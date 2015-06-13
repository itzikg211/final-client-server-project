package model;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.GZIPInputStream;

import presenter.Presenter;
import presenter.Properties;
import presenter.PropertiesServer;
import viewGUI.ServerGUI;

public class MyTCPIPServer 
{
	private int port;
	private boolean isStopped;
	int id ;
	CommonClientHandler ch;
	ServerGUI server;
	HashMap<Integer, Socket> clients = new HashMap<Integer, Socket>();
	public MyTCPIPServer(int port,CommonClientHandler ch) 
	{
		id = 1;
		this.port = port;
		this.ch = ch;
	}
	public void startServer(int numOfClients)
	{
		System.out.println("START SERVER");
		ServerSocket myServer = null;
		
		try {
			myServer = new ServerSocket(port);
			myServer.setSoTimeout(300000);
			ExecutorService executor = Executors.newFixedThreadPool(numOfClients);
			System.out.println("waiting for client");
			try
			{
				while(!isStopped)
				{
					final Socket someClient = myServer.accept();
					System.out.println("Client number : " + id + " connected! ");
					id++;
					System.out.println("IP ADDRESS : " + someClient.getInetAddress());
					clients.put(id, someClient);
				//	server.setClients(clients);
					executor.execute(new Runnable() 
					{
						
						@Override
						public void run() 
						{
							// TODO Auto-generated method stub
							try 
							{
								//connectMVP(someClient);
								ch.handleClient(someClient.getInputStream(),someClient.getOutputStream());			
								//someClient.getOutputStream().close();
								//someClient.getInputStream().close();
								someClient.close();
							} 
							catch (IOException e) 
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
					});
				}
			}
			catch(SocketTimeoutException e){}
			executor.shutdown();
		}
		
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		try {
			myServer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/*private void connectMVP(Socket someClient) 
	{
		PropertiesServer pro = null;
		try 
		{
			pro = expandProperties(someClient.getInputStream());
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		MyModel m = new MyModel(pro);
		Presenter p = new Presenter(m,ch);
		m.addObserver(p);
		ch.addObserver(p);
		try 
		{
			p.setOutToClient(someClient.getOutputStream());
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}*/
	
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
