package model;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyTCPIPServer 
{
	private int port;
	private boolean isStopped;
	int id ;
	ClientHandler ch;
	HashMap<Integer, Socket> clients = new HashMap<Integer, Socket>();
	public MyTCPIPServer(int port,ClientHandler ch) 
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
			myServer.setSoTimeout(50000);
			ExecutorService executor = Executors.newFixedThreadPool(numOfClients);
			System.out.println("waiting for client");
			try
			{
				while(!isStopped)
				{
					final Socket someClient = myServer.accept();
					System.out.println("Client number : " + id + " connected! ");
					id++;
					clients.put(id, someClient);
					executor.execute(new Runnable() 
					{
						
						@Override
						public void run() 
						{
							// TODO Auto-generated method stub
							try 
							{
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			myServer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	 
}
