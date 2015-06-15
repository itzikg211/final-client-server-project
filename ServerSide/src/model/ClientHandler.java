package model;

import java.net.ServerSocket;
import java.net.Socket;
/**
 * a generic interface that handles the client
 * @author  Sarusi Ran, Gershfeld Itzik 
 * @version 1.0
 * @since   2015-02-06
 */

public interface ClientHandler 
{
	public void handleClient(Socket client);
	public void setServer(ServerSocket server);
}
