package boot;

import java.beans.XMLDecoder;
import java.io.FileInputStream;
import java.io.IOException;

import presenter.PropertiesServer;
import viewGUI.ServerGUI;

public class Run 
{

	public static void main(String[] args) 
	{
		
		//System.out.println("GOOD");
		PropertiesServer pro = readProperties();
		ServerGUI sgui = new ServerGUI("Server",1000,800);
		sgui.startServer(pro);
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
