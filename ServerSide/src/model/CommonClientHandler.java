package model;

import java.net.ServerSocket;
import java.util.Observable;
/**
 * an abstractic class that implements from clientHandler
 * @author saroussira
 *
 */
public abstract class CommonClientHandler extends Observable implements ClientHandler
{
	ServerSocket server;
}
