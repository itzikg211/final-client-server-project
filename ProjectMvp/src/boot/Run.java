package boot;

import java.beans.XMLDecoder;
import java.io.FileInputStream;
import java.io.IOException;

import presenter.Properties;
import view.GUIStart;

///////Submitted by: Sarusi Ran 208631143, Gershfeld Itzik 208491886


/**
 * This is the Run class that contains the main function which connects the observer with the other observables.
 */

public class Run {

	 /**
	   * This is the main method which connects the observer with the other observables, in this function we are assembling the MVP parts.
	   * @return Nothing.
	   */
	public static void main(String[] args) 
	{
		
		///////////////////////////////////////////////////////////////////
		/*MyModel m = new MyModel(new Properties());
		Properties pro;
		if((pro=readProperties())!=null)
			m=new MyModel(pro);
		else
			m=new MyModel(new Properties());
		MyView v = new MyView();
		Presenter p = new Presenter(m,v);
		m.addObserver(p);
		v.addObserver(p);
		v.start();*/
		
		
		//////////////////////////////////////////////////////////////////
		/*MyModel m=new MyModel(new Properties());
		Properties pro;
		if((pro=readProperties())!=null)
			m=new MyModel(pro);
		else
			m=new MyModel(new Properties());
		StartWindow win=new StartWindow("Row Out Maze", 1200, 1000);
		Presenter p = new Presenter(m,win);
		m.addObserver(p);
		win.addObserver(p);
		win.run();*/
		////////////////////////////////////////////////////////////////////
		GUIStart gui = new GUIStart("Start",1000,800);
		gui.run();
		/*XMLEncoder encoder;
		try {
			encoder = new XMLEncoder(new FileOutputStream("src/properties.xml"));
			Properties properties = new Properties();
			properties.setDiagonal(true);
			properties.setDiagonalMovementCost(16);
			properties.setThreadNumber(3);
			properties.setView(WayOfDisplay.ECLIPSE_CONSOLE);
			encoder.writeObject(properties);
			encoder.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	*/
		
		
	}
	/**
	 * Reads the properties from the XML file and sets the project's properties.
	 * @return the class properties with the selceted fields
	 */
	private static Properties readProperties()
	{
		XMLDecoder d;
		Properties p=null;
		try {
			FileInputStream in=new FileInputStream("src/properties.xml");
			d=new XMLDecoder(in);
			p=(Properties)d.readObject();
			d.close();
			in.close();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		return p;
	}
	

}
