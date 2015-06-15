package viewGUI;

import java.net.Socket;
import java.util.HashMap;

import presenter.PropertiesServer;
/**
 * an interface that defines a server GUI
 * @author  Sarusi Ran, Gershfeld Itzik 
 * @version 1.0
 * @since   2015-06-15
 */
public interface View 
{
	public void startServer(PropertiesServer pro);
	public void setProperties(PropertiesServer ps);
	public void closeServer();
	public PropertiesServer getProperties();
	public void addClient(int ID);
	public void setStatus(int ID,String status);
}