package boot;

import GUI.GUIStart;

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
		GUIStart gui = new GUIStart("Start",1020,775);
		gui.run();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Reads the properties from the XML file and sets the project's properties.
	 * @return the class properties with the selceted fields
	 */
	/*private static Properties readProperties()
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
	}*/
	

}
