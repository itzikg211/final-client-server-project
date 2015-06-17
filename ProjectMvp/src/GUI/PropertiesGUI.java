package GUI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Text;

import presenter.Properties;
import presenter.Properties.MazeGenerator;
import presenter.Properties.MazeSolver;
import presenter.Properties.WayOfDisplay;

public class PropertiesGUI extends BasicWindow
{
	private Properties pro;
	Text t1;
	Text t2;
	Text t3;
	
	public PropertiesGUI(String title, int width, int height) 
	{
		super(title, width, height);
		pro = new Properties();
	}
	@Override
	void initWidgets() 
	{
		shell.setLayout(new GridLayout(2,false));
		//the Maze solver algorithm
		Label MazeSolve = new Label(shell,SWT.NONE);
		MazeSolve.setText("Choose The Maze Solver Algorithm:");
		MazeSolve.setLayoutData(new GridData(SWT.FILL,SWT.FILL, false,false,1,1));
		//here we create the combo for the maze solver algorithm.
		Combo MazeSolver1 = new Combo(shell, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
		MazeSolver1.setLayoutData(new GridData(SWT.FILL,SWT.FILL, true ,false,1,1));
		MazeSolver1.add("A* with diagonals");
		MazeSolver1.add("A* without diagonals");
		MazeSolver1.add("BFS with diagonals");
		MazeSolver1.add("BFS without diagonals");
		MazeSolver1.setText("A* without diagonals");
		//the maze generator algorithm
		Label MazeGenerate = new Label(shell,SWT.NONE);
		MazeGenerate.setText("Choose The Algorithm To Generate The Maze:");
		MazeGenerate.setLayoutData(new GridData(SWT.FILL,SWT.FILL, false,false,1,1));
		//here we create the combo for the maze generation algorithm.
		Combo MazeGenerator1 = new Combo(shell, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
		MazeGenerator1.setLayoutData(new GridData(SWT.FILL,SWT.FILL, true ,false,1,1));
		MazeGenerator1.add("DFS Algorithm");
		MazeGenerator1.add("Random Algorithm");
		MazeGenerator1.setText("DFS Algorithm");
		//the cost of solving without diagonal - regular movement
		Label nodiagonal = new Label(shell,SWT.NONE);
		nodiagonal.setText("Choose The Cost Of Movment Without Diagonal:");
		nodiagonal.setLayoutData(new GridData(SWT.FILL,SWT.FILL, false,false,1,1));
		//here we create the scale that we move in order to change the regular movement cost
	    Scale regCost = new Scale (shell, SWT.BORDER);
	    regCost.setSize (60, 20);
	    regCost.setMaximum (40);
	    regCost.setMinimum(5);
	    regCost.setPageIncrement (5);
	    regCost.setLayoutData(new GridData(SWT.FILL,SWT.FILL, true ,false,1,1));
		Label lblChooseReg = new Label(shell,SWT.NONE);
		lblChooseReg.setText("The Cost You Chose For Regular Movement:");
		lblChooseReg.setLayoutData(new GridData(SWT.FILL,SWT.FILL, false,false,1,1));
		t1 = new Text(shell, SWT.BORDER | SWT.READ_ONLY);
		t1.setLayoutData(new GridData(SWT.FILL,SWT.TOP, false,false,1,1));
		t1.setText("10");
	    //the cost of solving in diagonal
	    Label diagonal = new Label(shell,SWT.NONE);
	    diagonal.setText("Choose The Cost Of Movment In Diagonal:");
	    diagonal.setLayoutData(new GridData(SWT.FILL,SWT.FILL, false,false,1,1));
	    //here we create the scale that we move in order to change the regular movement cost
	    Scale DiagonalCost = new Scale (shell, SWT.BORDER);
	    DiagonalCost.setSize (60, 20);
	    DiagonalCost.setMaximum ((regCost.getSelection()*2)-1);
	    DiagonalCost.setMinimum(regCost.getSelection()+1);
	    DiagonalCost.setPageIncrement(5);
	    DiagonalCost.setLayoutData(new GridData(SWT.FILL,SWT.FILL, true ,false,1,1));
		Label lblChooseDig = new Label(shell,SWT.NONE);
		lblChooseDig.setText("The Cost You Chose For Diagonal Movement:");
		lblChooseDig.setLayoutData(new GridData(SWT.FILL,SWT.FILL, false,false,1,1));
		t2 = new Text(shell, SWT.BORDER | SWT.READ_ONLY);
		t2.setLayoutData(new GridData(SWT.FILL,SWT.TOP, false,false,1,1));
		t2.setText("15");
		//the way of view
		Label way = new Label(shell,SWT.NONE);
		way.setText("Choose The Way Of Diaplay:");
		way.setLayoutData(new GridData(SWT.FILL,SWT.FILL, false,false,1,1));
		Combo wayOfDisplay1 = new Combo(shell, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
		wayOfDisplay1.setLayoutData(new GridData(SWT.FILL,SWT.FILL, true ,false,1,1));
		wayOfDisplay1.add("GUI");
		wayOfDisplay1.add("Eclipse Console");
		wayOfDisplay1.setText("GUI");
	    //the port setting
	    Label port = new Label(shell,SWT.NONE);
	    port.setText("Choose The Cost Of Movment In Diagonal:");
	    port.setLayoutData(new GridData(SWT.FILL,SWT.FILL, false,false,1,1));
	    Scale portNum = new Scale (shell, SWT.BORDER);
	    portNum.setSize (60, 30);
	    portNum.setMaximum (65000);
	    portNum.setMinimum(1024);
	    portNum.setPageIncrement(5000);
	    portNum.setLayoutData(new GridData(SWT.FILL,SWT.FILL, true ,false,1,1));
		Label lblChoosePort = new Label(shell,SWT.NONE);
		lblChoosePort.setText("The Port You Chose:");
		lblChoosePort.setLayoutData(new GridData(SWT.FILL,SWT.FILL, false,false,1,1));
		t3 = new Text(shell, SWT.BORDER);
		t3.setLayoutData(new GridData(SWT.FILL,SWT.TOP, false,false,1,1));
		t3.setText("5040");
		//the buttons at the botton 
		Button save = new Button(shell, SWT.PUSH);
		save.setText("save");
		save.setLayoutData(new GridData(SWT.FILL,SWT.FILL, false,false,1,1));
		Button cancel = new Button(shell, SWT.PUSH);
		cancel.setText("cancel");
		cancel.setLayoutData(new GridData(SWT.FILL,SWT.FILL, false,false,1,1));
		
		//here we define all the listeners
		MazeSolver1.addSelectionListener(new SelectionListener() 
		{
			
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				if(MazeSolver1.getText().equals("A* without diagonals"))
				{
					pro.setMazeSolver(MazeSolver.ASTAR_MANHATTAN_DISTANCE);
					pro.setDiagonal(false);
				}
				if(MazeSolver1.getText().equals("A* with diagonals"))
				{
					pro.setMazeSolver(MazeSolver.ASTAR_AIR_DISTANCE);
					pro.setDiagonal(true);
				}
				if(MazeSolver1.getText().equals("BFS with diagonals"))
				{
					pro.setMazeSolver(MazeSolver.BFS_DIAGONAL);
					pro.setDiagonal(true);
				}
				if(MazeSolver1.getText().equals("BFS without diagonals"))
				{
					pro.setMazeSolver(MazeSolver.BFS_NO_DIAGONAL);
					pro.setDiagonal(false);
				}
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) 
			{				
			}
		});
		MazeGenerator1.addSelectionListener(new SelectionListener() 
		{
			
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				if(MazeGenerator1.getText().equals("DFS Algorithm"))
					pro.setMazeGenerator(MazeGenerator.DFS_ALGO);
				if(MazeGenerator1.getText().equals("Random Algorithm"))
					pro.setMazeGenerator(MazeGenerator.RANDOM_ALGO);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) 
			{				
			}
		});
		
		wayOfDisplay1.addSelectionListener(new SelectionListener() 
		{
			
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				if(wayOfDisplay1.getText().equals("GUI"))
					pro.setView(WayOfDisplay.GUI);
				if(wayOfDisplay1.getText().equals("Eclipse Console"))
					pro.setView(WayOfDisplay.ECLIPSE_CONSOLE);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) 
			{				
			}
		});
		
		regCost.addSelectionListener(new SelectionListener() 
		{
			
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				t1.setText(""+ regCost.getSelection());
				DiagonalCost.setMaximum ((regCost.getSelection()*2)-1);
				DiagonalCost.setMinimum(regCost.getSelection()+1);
				t2.setText(""+(regCost.getSelection()+1));
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) 
			{
			}
		});
		
		DiagonalCost.addSelectionListener(new SelectionListener() 
		{
			
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				t2.setText(""+ DiagonalCost.getSelection());
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) 
			{
			}
		});
		portNum.addSelectionListener(new SelectionListener() 
		{
			
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				t3.setText(""+portNum.getSelection());
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) 
			{
			}
		});
		cancel.addSelectionListener(new SelectionListener() 
		{
			
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				display.dispose();
				GUIStart guis = new GUIStart("Start",1000,800);
				guis.run();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) 
			{
			}
		});
		
		save.addSelectionListener(new SelectionListener() 
		{
			
			@Override
			public void widgetSelected(SelectionEvent arg0) 
			{
				display.dispose();
				GUIStart guis = new GUIStart("Start",1000,800);
				pro.setDiagonal(true);
				guis.setProperties(pro);
				guis.run();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) 
			{
			}
		});
		
	}
	

	public Properties getPro() 
	{
		pro.setDiagonalMovementCost(Integer.parseInt(t1.getText()));
		pro.setMovementCost(Integer.parseInt(t2.getText()));
		pro.setPortNumber(Integer.parseInt(t3.getText()));
		return pro;
	}

	public void setPro(Properties pro) 
	{
		this.pro = pro;
	}

}
