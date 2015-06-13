package viewGUI;

import java.net.Socket;
import java.util.HashMap;

import model.MazeHandler;
import model.Model;
import model.MyModel;
import model.MyTCPIPServer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.List;

import presenter.Presenter;
import presenter.PropertiesServer;

public class ServerGUI extends BasicWindow implements View
{
	
	PropertiesServer prop;
	List IdList;
	List IpList;
	public ServerGUI(String title, int width, int height) {
		super(title, width, height);
		// TODO Auto-generated constructor stub
	}

	@Override
	void initWidgets() 
	{
		/*shell.setLayout(new GridLayout(2,true));
		Label IDs = new Label(shell, SWT.NONE);
		IDs.setText("client's ID");
		IDs.setLayoutData(new GridData(SWT.CENTER,SWT.CENTER, false,false,1,1));
		Label IPs = new Label(shell, SWT.NONE);
		IPs.setText("client's IP");
		IPs.setLayoutData(new GridData(SWT.CENTER,SWT.CENTER, false,false,1,1));
		IdList = new List(shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		IdList.setLayoutData(new GridData(SWT.CENTER,SWT.TOP, true,true,1,1));
		IpList = new List(shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		IpList.setLayoutData(new GridData(SWT.CENTER,SWT.TOP, true,true,1,1));*/
		shell.setLayout(new GridLayout(2,true));
		Button start = new Button(shell, SWT.PUSH);
		start.setText("Start the server!");
		start.setLayoutData(new GridData(SWT.CENTER,SWT.CENTER, false,false,1,1));
		
		start.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0)
			{
				startServer(getProperties());
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
				IdList.add(i.toString());
				IpList.add(clients.get(i).getInetAddress().toString());
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
}
