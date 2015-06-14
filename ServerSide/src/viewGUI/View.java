package viewGUI;

import java.net.Socket;
import java.util.HashMap;

import presenter.PropertiesServer;

public interface View 
{
	public void startServer(PropertiesServer pro);
	public void setClients(HashMap<Integer, Socket> clients);
	public void setProperties(PropertiesServer ps);
	public void closeServer();
	public PropertiesServer getProperties();
	public void addClient(int ID);
}
