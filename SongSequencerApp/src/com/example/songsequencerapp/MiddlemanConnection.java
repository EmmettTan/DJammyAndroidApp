package com.example.songsequencerapp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MiddlemanConnection extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// This call will result in better error messages if you
		// try to do things in the wrong thread.
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_middleman_connection);
		openSocket(getWindow().getDecorView().findViewById(R.layout.activity_middleman_connection));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.middleman_connection, menu);
		return true;
	}
	
	public void openSocket(View view) {
		MyApplication app = (MyApplication) getApplication();
		TextView msgbox = (TextView) findViewById(R.id.error_message_box);

		// Make sure the socket is not already opened 
		if (app.sock != null && app.sock.isConnected() && !app.sock.isClosed()) {
			msgbox.setText("Socket already open");
			Intent intent = new Intent(MiddlemanConnection.this, GameActivity.class);
			startActivity(intent);
		}
		
		// open the socket.  SocketConnect is a new subclass
	    // (defined below).  This creates an instance of the subclass
		// and executes the code in it.
		new SocketConnect().execute((Void) null);
	}
	
	public void closeSocket(View view) {
		MyApplication app = (MyApplication) getApplication();
		Socket s = app.sock;
		try {
			s.getOutputStream().close();
			s.close();
			
			Toast t = Toast.makeText(getApplicationContext(), "Connection closed", Toast.LENGTH_LONG);
			t.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getConnectToIP() {
		String addr = "";
		EditText text_ip;
		text_ip = (EditText) findViewById(R.id.ip1);
		addr += text_ip.getText().toString();
		text_ip = (EditText) findViewById(R.id.ip2);
		addr += "." + text_ip.getText().toString();
		text_ip = (EditText) findViewById(R.id.ip3);
		addr += "." + text_ip.getText().toString();
		text_ip = (EditText) findViewById(R.id.ip4);
		addr += "." + text_ip.getText().toString();
		return addr;
	}
	
	public Integer getConnectToPort() {
		Integer port;
		EditText text_port;

		text_port = (EditText) findViewById(R.id.port);
		port = Integer.parseInt(text_port.getText().toString());

		return port;
	}
	

	public class SocketConnect extends AsyncTask<Void, Void, Socket> {

		// The main parcel of work for this thread.  Opens a socket
		// to connect to the specified IP.
		protected Socket doInBackground(Void... voids) {
			Socket s = null;
			String ip = getConnectToIP();
			Integer port = getConnectToPort();

			try {
				s = new Socket();
				s.bind(null);
				s.connect((new InetSocketAddress(ip, port)), 1000);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return s;
		}
		

		// After executing the doInBackground method, this is 
		// automatically called, in the UI (main) thread to store
		// the socket in this app's persistent storage
		protected void onPostExecute(Socket s) {
			MyApplication myApp = (MyApplication) MiddlemanConnection.this
					.getApplication();
			myApp.sock = s;
			
			String msg;
			if (myApp.sock.isConnected()) {
				msg ="Connection opened successfully";
				Toast t = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
				t.show();
				Intent intent = new Intent(MiddlemanConnection.this, GameActivity.class);
				startActivity(intent);
				
			} else {
				msg = "Connection could not be opened";
				Toast t = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
				t.show();
			}
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
