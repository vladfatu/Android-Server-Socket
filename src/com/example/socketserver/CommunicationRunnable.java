package com.example.socketserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import android.os.Handler;

/**
 * @author Vlad
 *
 */
public class CommunicationRunnable implements Runnable {

	private Socket clientSocket;
	private BufferedReader input;
	private Handler updateConversationHandler;
	private CommunicationListener communicationListener;
	private LogKeeper logKeeper;

	public CommunicationRunnable(Socket clientSocket, Handler updateConversationHandler, CommunicationListener communicationListener, LogKeeper logKeeper) {
		this.clientSocket = clientSocket;
		this.updateConversationHandler = updateConversationHandler;
		this.communicationListener = communicationListener;
		this.logKeeper = logKeeper;
		
		try
		{
			this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void run()
	{
		while (!Thread.currentThread().isInterrupted())
		{
			try
			{
				String read = input.readLine();
				addLog(read);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
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

}