package com.example.socketserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import android.os.Handler;

import com.example.socketserver.constants.Constants;
import com.example.socketserver.utils.NetworkUtils;

/**
 * @author Vlad
 *
 */
public class ServerRunnable implements Runnable {
	
	private ServerSocket serverSocket;
	private Handler updateConversationHandler;
	private CommunicationListener communicationListener;
	private LogKeeper logKeeper;
	private boolean isRunning;
	
	public ServerRunnable(Handler updateConversationHandler, CommunicationListener communicationListener, LogKeeper logKeeper)
	{
		this.updateConversationHandler = updateConversationHandler;
		this.communicationListener = communicationListener;
		this.logKeeper = logKeeper;
	}

	public void run()
	{
		isRunning = true;
		Socket socket = null;
		try
		{
			serverSocket = new ServerSocket(Constants.SERVER_PORT); 
//			String hostname = NetworkUtils.getLocalIpAddress();
//			serverSocket.bind(new InetSocketAddress(hostname, Constants.SERVER_PORT));
		} catch (IOException e)
		{
			addLog("IOException!!!");
			e.printStackTrace();
		}
		addLog("Server started");
		while (!Thread.currentThread().isInterrupted() && isRunning)
		{
			try
			{
				socket = serverSocket.accept();
				addLog("Client accepted");
				
				CommunicationRunnable commThread = new CommunicationRunnable(socket, updateConversationHandler, communicationListener, logKeeper);
				new Thread(commThread).start();

			} catch (IOException e)
			{
				addLog("IOException!!!");
				e.printStackTrace();
			}
		}
		addLog("Server stoped");
	}
	
	private void addLog(final String log)
	{
		updateConversationHandler.post(new Runnable() {
			public void run()
			{
				logKeeper.addLog(log);
				communicationListener.update();
			}
		});
	}
	
	public void stopServerSocket()
	{
		try
		{
			if (serverSocket != null)
			{
				serverSocket.close();
			}
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		terminate();
	}
	
	public void terminate()
	{
		isRunning = false;
	}
}