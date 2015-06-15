package boot;

import java.beans.XMLDecoder;
import java.io.FileInputStream;
import java.io.IOException;

import model.MazeHandler;
import presenter.Presenter;
import presenter.PropertiesServer;
import viewGUI.ServerGUI;

public class Run 
{

	public static void main(String[] args) 
	{
		//reads the properties from the XML file
		PropertiesServer pro = readProperties();
		//opens the severr's window
		ServerGUI sgui = new ServerGUI("Server",500,500);
		//creates a new maze handler
		MazeHandler m = new MazeHandler();
		//creates a new presenter
		Presenter p = new Presenter(m, sgui);
		//adds a the presenter p as an observer to the mazehandler
		m.addObserver(p);
		//adds a the presenter p as an observer to the serverGUI
		sgui.addObserver(p);
		//sets the selected properties from the XML file
		sgui.setProperties(pro);
		//runs the window
		sgui.run();
	}
	
	public static PropertiesServer readProperties()
	{
		XMLDecoder d;
		PropertiesServer p=null;
		try 
		{
			FileInputStream in=new FileInputStream("src/properties.xml");
			d=new XMLDecoder(in);
			p=(PropertiesServer)d.readObject();
			d.close();
			in.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		return p;
	}

}
