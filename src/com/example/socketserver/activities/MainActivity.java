package com.example.socketserver.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.socketserver.CommunicationListener;
import com.example.socketserver.LogEntry;
import com.example.socketserver.LogKeeper;
import com.example.socketserver.R;
import com.example.socketserver.ServerRunnable;

/**
 * @author Vlad
 *
 */
public class MainActivity extends Activity implements CommunicationListener{

	private LogKeeper logKeeper;
	private Button startServerButton;
	private Button stopServerButton;
	private boolean serverStarted;
	private Handler updateConversationHandler;
	private ServerRunnable serverRunnable;
	private TextView logTextView;

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v)
		{
			switch (v.getId())
			{
				case R.id.startServerButton:
				{
					startServer();
					break;
				}
				case R.id.stopServerButton:
				{
					stopServer();
					break;
				}
				default:
					break;
			}

		}
	};
	
	private void startServer()
	{
		if (!serverStarted)
		{
			this.serverRunnable = new ServerRunnable(updateConversationHandler, this, logKeeper);
			Thread serverThread = new Thread(serverRunnable);
			serverThread.start();
			serverStarted = true;
			initModel();
		}
	}
	
	private void stopServer()
	{
		if (serverStarted)
		{
			serverRunnable.stopServerSocket();
			serverRunnable.terminate();
			serverStarted = false;
			logKeeper.clear();
			initModel();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.logTextView = (TextView) findViewById(R.id.logTextView);
		this.startServerButton = (Button) findViewById(R.id.startServerButton);
		startServerButton.setOnClickListener(clickListener);
		this.stopServerButton = (Button) findViewById(R.id.stopServerButton);
		stopServerButton.setOnClickListener(clickListener);
		updateConversationHandler = new Handler();
		this.logKeeper = new LogKeeper();
		initModel();
	}

	private void initModel()
	{
		if (serverStarted)
		{
			this.startServerButton.setVisibility(View.GONE);
			this.stopServerButton.setVisibility(View.VISIBLE);
			this.logTextView.setVisibility(View.VISIBLE);
		} else
		{
			this.startServerButton.setVisibility(View.VISIBLE);
			this.stopServerButton.setVisibility(View.GONE);
			this.logTextView.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void update()
	{
		StringBuilder log = new StringBuilder();
		for (LogEntry logEntry : logKeeper.getLogs())
		{
			log.append(logEntry.getStringEntry()).append("\n");
		}
		this.logTextView.setText(log.toString());
		
	}
	
	@Override
	protected void onStop()
	{
		stopServer();
		super.onStop();
	}

}
