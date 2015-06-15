package model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import viewGUI.ServerGUI;
/**
 * this class is responsible for the connections with the clients
 * @author  Sarusi Ran, Gershfeld Itzik 
 * @version 1.0
 * @since   2015-15-06
 *
 */
public class MyTCPIPServer 
{
	private int port;
	private boolean isStopped;
	int id ;
	CommonClientHandler ch;
	ServerGUI server;
	HashMap<Integer, Socket> clients = new HashMap<Integer, Socket>();
	/**
	 * Constructs and initializes the MyTCPIPServer class
	 * @param port port of the server to run on
	 * @param ch the CommonClientHandler that handles each client
	 */
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
			//opens the serverSocket at the implemented port 
			myServer = new ServerSocket(port);
			//setting the timeout of the server
			myServer.setSoTimeout(300000);
			//sets the server at the client handler
			ch.setServer(myServer);
			ExecutorService executor = Executors.newFixedThreadPool(numOfClients);
			System.out.println("waiting for client");
			try
			{
				while(!isStopped)
				{
					//waiting for accept and sets someClient
					final Socket someClient = myServer.accept();
					System.out.println("Client number : " + id + " connected! ");
					id++;
					System.out.println("IP ADDRESS : " + someClient.getInetAddress());
					//Enters the socket and his serial number to the 
					clients.put(id, someClient);					
					executor.execute(new Runnable() 
					{
						@Override
						public void run() 
						{
							try 
							{			
								//handles the client in a threadPool
								ch.handleClient(someClient);			
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
