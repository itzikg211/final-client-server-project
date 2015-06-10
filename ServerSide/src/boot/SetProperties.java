package boot;

import java.beans.XMLEncoder;
import java.io.FileOutputStream;
import java.io.IOException;

import presenter.PropertiesServer;

public class SetProperties 
{

	public static void main(String[] args) 
	{
			XMLEncoder e;
			PropertiesServer pro=new PropertiesServer();
			try 
			{
				
				e = new XMLEncoder(new FileOutputStream("src/properties.xml"));
				e.writeObject(pro);
				e.flush();
				e.close();
			} 
			catch (IOException e1) 
			{
				e1.printStackTrace();
			}
	}

}
