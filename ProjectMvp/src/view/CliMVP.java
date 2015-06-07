package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
/**
 * This is the cli we use in the MVP pattern. a generic cli that works with all the different types of commands.
 * @author Ran Saroussi
 *
 */
public class CliMVP extends CLI implements Runnable{
	MyView v;
	StartWindow sw;
	/**
	 * Constructs the cliMvp
	 * @param in a BufferedReader we put on the super class
	 * @param out a PrintWriter we put on the super class
	 */
	public CliMVP(BufferedReader in, PrintWriter out) 
	{
		super(in, out);
		
		// TODO Auto-generated constructor stub
	}
	/**
	 * Setting the hash map in the class to the injected hashmap
	 * @param h the injected hashmap
	 */
	public void setHashMap(HashMap<String, Command> h)
	{
		for (String s : h.keySet()) 
		{
			addCommand(h.get(s), s);
		}
	}
	/**
	 * Srtting the view to the injected view
	 * @param v2 the injected MyView
	 */
	public void setView(MyView v2)
	{
		this.v=v2;
	}
	public void setView(StartWindow sw)
	{
		this.sw=sw;
	}
	@Override
	/**
	 * Overrides the start of the CLI (the super class) to work in the MVP pattern
	 */
	public void start()
	{
		String arg = "";
		boolean flag=false;
		String commandName="";
		System.out.print("Enter command: ");
		try {
			String line = in.readLine();
			while (!line.equals("exit"))
			{
				String[] sp = line.split(" ");
				if (sp.length > 2)
				{
					commandName = sp[0] + " " + sp[1];
					flag=true;
				}
				else if(sp.length == 2)
				{
					commandName = sp[0];
					flag=true;
				}
				if(flag==true)
				{
					if(sp.length==2)
					{
						arg=sp[1];
						Command command = this.userCommands.selectCommand(commandName);
						command.doCommand(arg);
					}
					else if (sp.length > 2)
					{
						for(int i=2;i<sp.length;i++)
						{
							arg += sp[i];
							arg += " ";
						}
						arg = arg.substring(0, arg.length()-1);
						if(userCommands.commands.containsKey(commandName))
						{
							Command command = this.userCommands.selectCommand(commandName);
							v.command = command;
							v.setChangedFunc();
							v.notifyObservers(arg);
						}
					}
					else
						System.out.println("Wrong amount of words, sould be at least 2");
					
				}
				else
					System.out.println("Wrong command name,enter another one : ");
				System.out.print("Enter command: ");
				line = in.readLine();
				arg="";
			}
			if(line.equals("exit"))
			{
				System.out.println("exiting");
				v.setChangedFunc();
				v.notifyObservers("finish");
			}
						
		} catch (IOException e) {			
			e.printStackTrace();
		} finally {
			try {
				in.close();
				out.close();
			} catch (IOException e) {				
				e.printStackTrace();
			}			
		}	
	}
	@Override
	/**
	 * The runnable method run.
	 */
	public void run() {
		// TODO Auto-generated method stub
		start();
	}

}