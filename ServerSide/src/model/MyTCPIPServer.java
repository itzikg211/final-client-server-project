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
							try 
							{
								ch.handleClient(someClient.getInputStream(),someClient.getOutputStream());			
								someClient.close();
							} 
							catch (IOException e) 
							{
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
		try 
		{
			myServer.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}
	
	
	 
}
