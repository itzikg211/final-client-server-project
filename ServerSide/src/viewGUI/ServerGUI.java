package viewGUI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import presenter.PropertiesServer;
/**
 * This the server's gui that implements from the BasicWindow generic class
 * 
 * @author  Sarusi Ran, Gershfeld Itzik 
 * @version 1.0
 * @since   2015-02-06
 */
public class ServerGUI extends BasicWindow implements View
{
	private PropertiesServer prop;
	private Table t;
	//the current index we will close the socket with
	private int val;
	/**
	 * Constructs and initializes the server's gui
	 * @param title the title of the window
	 * @param width the width of the window
	 * @param height the height of the window
	 */
	public ServerGUI(String title, int width, int height) 
	{
		super(title, width, height);
		val = 0;
	}
	/**
	 * sets the server window's widgets
	 */
	@Override
	void initWidgets() 
	{
		shell.setLocation(550, 300);
		//sets the layout to GridLayout
		shell.setLayout(new GridLayout(1,false));
		//creates a now button that starts the server and sets his LayoutData
		Button start = new Button(shell, SWT.PUSH);
		start.setText("Start the server!");
		start.setLayoutData(new GridData(SWT.CENTER,SWT.NONE, false,false,1,1));
		Button close = new Button(shell, SWT.PUSH);
		close.setText("Close the server");
		close.setLayoutData(new GridData(SWT.CENTER,SWT.NONE, false,false,1,1));
		close.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				closeServer();
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) 
			{
			}
		});
		//creates a new table that conatains the client's details
		t = new Table(shell, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		//Creates 3 new columns
		//c1 is the client ID column
		TableColumn c1 = new TableColumn(t, SWT.CENTER);
		//Creates 3 new columns
		//c2 is the client IP column
		TableColumn c2 = new TableColumn(t, SWT.CENTER);
		//Creates 3 new columns
		//c3 is the client status column
		TableColumn c3 = new TableColumn(t, SWT.CENTER);
		c1.setText("Serial Number");
		c2.setText("Client IP");
		c3.setText("Client Status");
		//sets the widths of the columns
		c1.setWidth(100);
		c2.setWidth(120);
		c3.setWidth(220);
		//sets the column's header to be visible
		t.setHeaderVisible(true);
		//sets the table's background color
		t.setBackground(new Color(display, 0, 120, 250));
		t.setLayoutData(new GridData(SWT.FILL,SWT.FILL, true,true,1,1));
		//creates a now button that removes the selected clients in the table
		Button removeClients = new Button(shell, SWT.PUSH);
		removeClients.setText("disconnect selected clients");
		removeClients.setLayoutData(new GridData(SWT.FILL,SWT.NONE, true,false,1,1));
		//add a selection listener that starts the server in a Thread
		start.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0)
			{
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						//call to function that starts the server
						startServer(getProperties());
						
					}
				}).start();
				//switches the start button to disabled, because the server can only be open once
				start.setEnabled(false);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) 
			{
			}
		});
		
		t.addListener(SWT.Selection, new Listener()
	    {
	        @Override
	        public void handleEvent(Event event)
	        {
	            if(event.detail == SWT.CHECK)
	            {
	                TableItem current = (TableItem)event.item;
	                for(int i=0;i<t.getItemCount();i++)
	                {
	                if(current.equals(t.getItem(i)))
	                {
	                    val = i;
	                }
	                }
	            }
	        }
	    });
		
		//add a selection listener that removed the selected clients
		removeClients.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent event) 
			{
				//checked if the user didn't select a client and notifies about it
				if(val==0)
				{
					MessageBox mg = new MessageBox(shell);
					mg.setMessage("this is currently not functioning / you still haven't selected a client");
					mg.open();
				}
				//checks if a specific row is checked
				else
				{
					String send = "disconnect " + val;
					//notify the presenter that a client is disconnected
					t.getItem(val).dispose();;
					setChanged();
					notifyObservers(send);
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) 
			{
			}
		});
	}

	
	/**
	 * Sets the properties
	 */
	public void setProperties(PropertiesServer ps)
	{
		this.prop = ps;
	}
	/**
	 * Starts the server *this function is called from a thread*
	 */
	@Override
	public void startServer(PropertiesServer pro) 
	{
		setChanged();
		notifyObservers("start server");
	}
	/**
	 * Closes the server
	 */
	@Override
	public void closeServer() 
	{
		setChanged();
		notifyObservers("close server");
	}
	/**
	 * @return returns the properties
	 */
	@Override
	public PropertiesServer getProperties() {
		return prop;
	}
	/**
	 * add a client to the clients table
	 */
	@Override
	public void addClient(int ID) {
		System.out.println("ADDING CLIENT NUMBER " + ID + " TO THE TABLE");
		String id = "" + ID;
		TableItem item1 = new TableItem(t, SWT.NONE);
	    item1.setText(new String[] { id, "127.0.0.1", "client connected" });
	 
	}
	/**
	 * Changes the status of a selected client
	 */
	@Override
	public void setStatus(int ID, String status) 
	{
			String num = "" + ID;
			TableItem item1 = new TableItem(t, SWT.NONE);
			item1.setText(new String[] { num, "127.0.0.1", status });
		
	}

}