package model;

import java.net.Socket;

public interface ClientHandler 
{
	public void handleClient(Socket client);
}
