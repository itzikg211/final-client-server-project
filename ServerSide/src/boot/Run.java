package boot;

import java.beans.XMLDecoder;
import java.io.FileInputStream;
import java.io.IOException;

import presenter.Properties;
import model.MazeHandler;
import model.MyTCPIPServer;

public class Run 
{

	public static void main(String[] args) 
	{
		Properties pro = readProperties();
		MyTCPIPServer start = new MyTCPIPServer(pro.getPortNumber(),new MazeHandler());
		start.startServer(pro.getNumOfClients());
	}
	
	public static Properties readProperties()
	{
		XMLDecoder d;
		Properties p=null;
		try 
		{
			FileInputStream in=new FileInputStream("src/properties.xml");
			d=new XMLDecoder(in);
			p=(Properties)d.readObject();
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
