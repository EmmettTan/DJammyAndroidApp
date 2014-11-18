package com.example.songsequencerapp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MiddlemanConnection extends Activity {
	protected boolean isHost;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// This call will result in better error messages if you
		// try to do things in the wrong thread.

		TCPReadTimerTask tcp_task = new TCPReadTimerTask();
		Timer tcp_timer = new Timer();
		tcp_timer.schedule(tcp_task, 0, 100);

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_middleman_connection);
		openSocket(getWindow().getDecorView().findViewById(
				R.layout.activity_middleman_connection));

		EditText ip, port;
		SharedPreferences settings = getPreferences(MODE_PRIVATE);
		ip = (EditText) findViewById(R.id.ip1);
		ip.setText(settings.getString("ip1", "192"));
		ip = (EditText) findViewById(R.id.ip2);
		ip.setText(settings.getString("ip2", "168"));
		ip = (EditText) findViewById(R.id.ip3);
		ip.setText(settings.getString("ip3", "0"));
		ip = (EditText) findViewById(R.id.ip4);
		ip.setText(settings.getString("ip4", "100"));
		port = (EditText) findViewById(R.id.port);
		port.setText(settings.getString("port", "50002"));
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
			Intent intent = new Intent(MiddlemanConnection.this,
					GameActivity.class);
			startActivity(intent);
		}
//<<<<<<< HEAD

		// open the socket. SocketConnect is a new subclass
		// (defined below). This creates an instance of the subclass
		// and executes the code in it.
		//new SocketConnect().execute((Void) null);
//=======
		else{
			// open the socket.  SocketConnect is a new subclass
		    // (defined below).  This creates an instance of the subclass
			// and executes the code in it.
			new SocketConnect().execute((Void) null);
		}
		
//>>>>>>> 1eaf8e813e408253ad14065f6e4d10b0898e2007
	}

	public void closeSocket(View view) {
		MyApplication app = (MyApplication) getApplication();
		Socket s = app.sock;
		try {
			s.getOutputStream().close();
			s.close();

			Toast t = Toast.makeText(getApplicationContext(),
					"Connection closed", Toast.LENGTH_LONG);
			t.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void skipConnection(View view) {
		Toast t = Toast.makeText(getApplicationContext(),
				"Skipping... Not connected", Toast.LENGTH_LONG);
		t.show();
		Intent intent = new Intent(MiddlemanConnection.this, GameActivity.class);
		startActivity(intent);
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

		// The main parcel of work for this thread. Opens a socket
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
				e.printStackTrace();
			} catch (IOException e) {
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
			String msg2 = "You are the host of this Song Sequence Session!";
			String msg3 = "You have joined this Song Sequence Session!";

			if (myApp.sock.isConnected()) {
				msg = "Connection opened successfully";
				Toast t = Toast.makeText(getApplicationContext(), msg,
						Toast.LENGTH_LONG);
				t.show();
				if (isHost == true) {
					Toast a = Toast.makeText(getApplicationContext(), msg2,
							Toast.LENGTH_LONG);
					a.show();
				} else {
					Toast b = Toast.makeText(getApplicationContext(), msg3,
							Toast.LENGTH_LONG);
					b.show();
				}

				EditText connection_text;
				SharedPreferences settings = getPreferences(MODE_PRIVATE);
				SharedPreferences.Editor editor = settings.edit();

				connection_text = (EditText) findViewById(R.id.ip1);
				editor.putString("ip1", connection_text.getText().toString());
				connection_text = (EditText) findViewById(R.id.ip2);
				editor.putString("ip2", connection_text.getText().toString());
				connection_text = (EditText) findViewById(R.id.ip3);
				editor.putString("ip3", connection_text.getText().toString());
				connection_text = (EditText) findViewById(R.id.ip4);
				editor.putString("ip4", connection_text.getText().toString());
				connection_text = (EditText) findViewById(R.id.port);
				editor.putString("port", connection_text.getText().toString());
				editor.commit();
				sendMessage(new String("yo"));

				Intent intent = new Intent(MiddlemanConnection.this,
						GameActivity.class);
				startActivity(intent);

			} else {
				msg = "Connection could not be opened";
				Toast t = Toast.makeText(getApplicationContext(), msg,
						Toast.LENGTH_LONG);
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

	public void sendMessage(String msg) {
		MyApplication app = (MyApplication) getApplication();

		// Create an array of bytes. First byte will be the
		// message length, and the next ones will be the message
		byte buf[] = new byte[msg.length() + 1];
		buf[0] = (byte) msg.length();
		System.arraycopy(msg.getBytes(), 0, buf, 1, msg.length());

		// Now send through the output stream of the socket
		OutputStream out;
		try {
			out = app.sock.getOutputStream();
			try {
				out.write(buf, 0, msg.length() + 1);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Used to receive message from the middleman/DE2
	public class TCPReadTimerTask extends TimerTask {
		public void run() {
			MyApplication app = (MyApplication) getApplication();
			if (app.sock != null && app.sock.isConnected()
					&& !app.sock.isClosed()) {

				try {
					InputStream in = app.sock.getInputStream();

					// See if any bytes are available from the Middleman
					int bytes_avail = in.available();
					if (bytes_avail > 0) {

						// If so, read them in and create a sring
						byte buf[] = new byte[bytes_avail];
						in.read(buf);

						final String s = new String(buf, 0, bytes_avail,
								"US-ASCII");
						if (s == "host") {
							isHost = true;
						}
						Log.d("MyMessage", "Received: " + s);

						// PLAY SOUND HERE

					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
