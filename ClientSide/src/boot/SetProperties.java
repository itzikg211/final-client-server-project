package boot;

import java.beans.XMLEncoder;
import java.io.FileOutputStream;
import java.io.IOException;

import presenter.Properties;

public class SetProperties 
{

	public static void main(String[] args) 
	{
		XMLEncoder e;
		Properties pro=new Properties();
		try 
		{
			
			e = new XMLEncoder(new FileOutputStream("properties.xml"));
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
