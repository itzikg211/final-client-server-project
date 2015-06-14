package viewGUI;

import java.net.Socket;
import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import presenter.PropertiesServer;

public class ServerGUI extends BasicWindow implements View
{
	int numOfClients;
	PropertiesServer prop;
	Table t;
	public ServerGUI(String title, int width, int height) 
	{
		super(title, width, height);
	}

	@Override
	void initWidgets() 
	{
		shell.setLayout(new GridLayout(1,false));
		Button start = new Button(shell, SWT.PUSH);
		start.setText("Start the server!");
		start.setLayoutData(new GridData(SWT.CENTER,SWT.NONE, false,false,1,1));
		t = new Table(shell, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		TableColumn c1 = new TableColumn(t, SWT.CENTER);
		TableColumn c2 = new TableColumn(t, SWT.CENTER);
		TableColumn c3 = new TableColumn(t, SWT.CENTER);
		c1.setText("Client ID");
		c2.setText("Client IP");
		c3.setText("Client Status");
		c1.setWidth(70);
		c2.setWidth(70);
		c3.setWidth(150);
		t.setHeaderVisible(true);
		t.setLayoutData(new GridData(SWT.FILL,SWT.FILL, true,true,1,1));
		Button removeClients = new Button(shell, SWT.PUSH);
		removeClients.setText("disconnect selected clients");
		removeClients.setLayoutData(new GridData(SWT.FILL,SWT.NONE, true,false,1,1));
		start.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0)
			{
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						startServer(getProperties());
						
					}
				}).start();
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) 
			{
			}
		});
		removeClients.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int counter = 0;
				for(int i=0;i<t.getItemCount();i++)
				{
					if(t.isSelected(i))
					{
						counter++;
						String index = t.getItem(i).getText(0);
						String send = "disconnect " + index;
						setChanged();
						notifyObservers(send);
					}
				}
				if(counter==0)
				{
					MessageBox mg = new MessageBox(shell);
					mg.setMessage("you didnt select a client");
					mg.open();
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	
	
	public void setProperties(PropertiesServer ps)
	{
		this.prop = ps;
	}
	
	public void setClients(HashMap<Integer, Socket> clients)
	{
		//if(IdList.)
		//IdList.removeAll();
		//IdList.removeAll();
		if(clients != null)
		{
			for(Integer i : clients.keySet())
			{
				//IdList.add(i.toString());
				//IpList.add(clients.get(i).getInetAddress().toString());
			}
		}
	}

	@Override
	public void startServer(PropertiesServer pro) 
	{
		setChanged();
		notifyObservers("start server");
	}

	@Override
	public void closeServer() 
	{
		
	}

	@Override
	public PropertiesServer getProperties() {
		return prop;
	}

	@Override
	public void addClient(int ID) {
		System.out.println("ADDING CLIENT NUMBER " + ID + " TO THE TABLE");
		String id = "" + ID;
		TableItem item1 = new TableItem(t, SWT.NONE);
	    item1.setText(new String[] { id, "127.0.0.1", "client connected" });
	}

	@Override
	public void setStatus(int ID, String status) {
		for(int i=0;i<t.getItemCount();i++)
		{
			String num = "" + ID;
			if(t.getItem(i).getText(0).equals(num))
				t.getItem(i).setText(2, status);
		}
		
	}

	@Override
	public void removeClient(int ID)
	{
		for(int i=0;i<t.getItemCount();i++)
		{
			String num = "" + ID;
			if(t.getItem(i).getText(0).equals(num))
				t.getItem(i).dispose();
		}
	}
}