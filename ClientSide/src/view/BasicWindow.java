package view;

import java.util.Observable;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * <h1>class BasicWindow</h1>
* The BasicWindow class extends Observable and implements Runnable.
* This is the hurt of our GUI, it make the GUI to be event driven.
*<p> 
* This class does the run method and checks all the time if there is any event waiting. 
* If there is it sends it to the right event handler.
* @author  Gershfeld Itzik, Sarusi Ran
* @version 1.0
* @since   2015-06-02 
*/

public abstract class BasicWindow extends Observable implements Runnable{

	Display display;
	Shell shell;
	 /**
	   * This is the C'tor of BasicWindow. 
	   * <p>The thing it does is initializing the display and the shell.
	   * @param title <b>(String) </b>This is the first parameter to the BasicWindow method
	   * @param width <b>(int) </b>This is the second parameter to the BasicWindow method
	   * @param height <b>(int) </b>This is the third parameter to the BasicWindow method
	   * @return Nothing.
	   */
	public BasicWindow(String title, int width, int height) {
		display=new Display();
		shell=new Shell(display);
		shell.setText(title);
		shell.setSize(width,height);
	}
	
	
	 /**
	   * This is the function that sets up all the widgets. 
	   * <p>The thing it does is initializing widgets and setting up their event handlers.
	   * @param Nothing.
	   * @return Nothing.
	   */
	abstract void initWidgets();
	
	
	 /**
	   * This is the Run method that runs in the thread. 
	   * <p>The thing it does is checking if there where any kind of events and then insert then into a queue.
	   * @param Nothing.
	   * @return Nothing.
	   */
	@Override
	public void run() {
		initWidgets();
		shell.open();
		// main event loop
		 while(!shell.isDisposed()){ // while window isn't closed

		    // 1. read events, put then in a queue.
		    // 2. dispatch the assigned listener
		    if(!display.readAndDispatch()){ 	// if the queue is empty
		       display.sleep(); 			// sleep until an event occurs 
		    }

		 } // shell is disposed

		 display.dispose(); // dispose OS components
		 System.exit(0); // closing the whole program
	}

	
}
