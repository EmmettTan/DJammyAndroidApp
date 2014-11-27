package com.example.songsequencerapp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MiddlemanConnection extends Activity implements OnItemSelectedListener, OnSeekBarChangeListener{
	protected boolean isHost;
	public static final int tempo_start = 200;
	public static final int tempo_size = 40;
	private static int Tempo=tempo_start;
	private static String key;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
		
		Spinner dropdown = (Spinner) findViewById(R.id.keys_spinner);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.planets_array,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		dropdown.setAdapter(adapter);
		dropdown.setOnItemSelectedListener(this);
		//getDropdownValue();
	
		
		TextView tv = (TextView) findViewById(R.id.seekBarLabel);
		tv.setVisibility(android.view.View.INVISIBLE);

		SeekBar sb = (SeekBar) findViewById(R.id.seekBar1);
		sb.setMax(tempo_size);

		sb.setOnSeekBarChangeListener(this);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
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
			GameActivity.groupSession = true;
			Intent intent = new Intent(MiddlemanConnection.this,
					SettingsMenu.class);
			startActivity(intent);
		}
		else{
			new SocketConnect().execute((Void) null);
		}
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
		GameActivity.groupSession = false;
		Intent intent = new Intent(MiddlemanConnection.this, SettingsMenu.class);
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
				
				GameActivity.groupSession = true;
				Intent intent = new Intent(MiddlemanConnection.this,
						SettingsMenu.class);
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

	public void getDropdownValue() {
		Spinner dropdown = (Spinner) findViewById(R.id.keys_spinner);

		key = String.valueOf(dropdown.getSelectedItem());

		Log.d("Key", "The Key " + key);

	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		TextView tv = (TextView) findViewById(R.id.seekBarLabel);
		tv.setText(Integer.toString(progress + tempo_start));
		tv.setX((seekBar.getX() + seekBar.getWidth()
				* ((float) progress / seekBar.getMax())));
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		TextView tv = (TextView) findViewById(R.id.seekBarLabel);
		tv.setVisibility(android.view.View.VISIBLE);
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		TextView tv = (TextView) findViewById(R.id.seekBarLabel);
		tv.setVisibility(android.view.View.INVISIBLE);
		Tempo = seekBar.getProgress() + tempo_start;
		Log.d("Tempo", "The Tempo" + Tempo);
	}

	public static int getTempo() {
		return Tempo;
	}

	public static String getKey() {
		return key;
	}
	public static int getTempoStart() {
		return tempo_start;
	}

	public static int getTempoSize() {
		return tempo_size;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
	key= parent.getItemAtPosition(position).toString();
	Log.d("key", "key is " + key);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		key="Gb/A#";
	}
}
