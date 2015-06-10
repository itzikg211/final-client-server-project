package boot;

import java.beans.XMLDecoder;
import java.io.FileInputStream;
import java.io.IOException;

import presenter.PropertiesServer;
import model.MazeHandler;
import model.MyTCPIPServer;
import model.ServerGUI;

public class Run 
{

	public static void main(String[] args) 
	{
		
		System.out.println("GOOD");
		PropertiesServer pro = readProperties();
		MyTCPIPServer start = new MyTCPIPServer(pro.getPortNumber(),new MazeHandler());
		start.startServer(pro.getNumOfClients() + 1);
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
