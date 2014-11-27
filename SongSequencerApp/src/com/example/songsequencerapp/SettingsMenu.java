package com.example.songsequencerapp;

import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SettingsMenu extends Activity{

	private static int instrument = 0;
	public static boolean playLoop1;
	public static boolean playLoop2;
	public static boolean playLoop3;
	public static boolean playLoop4;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings_menu);
		playLoop1 = false;
		playLoop2 = false;
		playLoop3 = false;
		playLoop4 = false;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings_menu, menu);
		return true;
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


	public void start_session(View view) {
		sendStartGameMessage();
		Intent intent = new Intent(this, GameActivity.class);
		startActivity(intent);
	}

	// onClick radiobutton group for choose tune
	public void setInstrument(View view) {
		Button b = (Button) view;
		String temp = b.getText().toString();

		Log.d("Button", "The Button " + temp);

		if (temp.compareTo("Bass") == 0)
			instrument = 2;
		else if (temp.compareTo("Drums") == 0)
			instrument = 3;
		else if (temp.compareTo("Xylo") == 0)
			instrument = 1;
		else if((temp.compareTo("Strings") == 0))
			instrument = 0;
		
		Log.d("instrument", "instrument " + instrument);
	}

	

	public static int getInstrument() {
		return instrument;
	}

	
	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		Intent intent = new Intent(this, MiddlemanConnection.class);
		startActivity(intent);
	}
	
	public void sendStartGameMessage() {
		MyApplication app = (MyApplication) getApplication();

		byte buf[] = new byte[1];
		buf[0] = GameActivity.MSG_TYPE_START_GAME;
		
		OutputStream out;
		try {
			out = app.sock.getOutputStream();
			try {
				out.write(buf, 0, 1);
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void debugLoop(View view){
		Intent intent = new Intent(this, LoopSettings.class);
		startActivity(intent);
	}
	// SETS the SOUND OUTPUT DEVICE
	public void setDeviceSoundOutput(View view) {
		Toast t = Toast.makeText(getApplicationContext(), "You're the host now!", Toast.LENGTH_LONG);
		t.show();
		
		MyApplication app = (MyApplication) getApplication();

		byte buf[] = new byte[1];
		buf[0] = GameActivity.MSG_TYPE_SET_SOUND_OUT;
		
		OutputStream out;
		try {
			out = app.sock.getOutputStream();
			try {
				out.write(buf, 0, 1);
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setLoop1(View view){
		playLoop1=!playLoop1;
		
	}
	public void setLoop2(View view){
		playLoop2=!playLoop2;
		
	}
	public void setLoop3(View view){
		playLoop3=!playLoop3;
		
	}
	public void setLoop4(View view){
		playLoop4=!playLoop4;
		
	}
	

}
