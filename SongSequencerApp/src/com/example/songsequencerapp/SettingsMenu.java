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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class SettingsMenu extends Activity implements OnSeekBarChangeListener, OnItemSelectedListener {
	public static final int tempo_start = 200;
	public static final int tempo_size = 40;
	private static int Tempo=tempo_start;
	private static String key;
	private static int instrument = 0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings_menu);

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

	public void getDropdownValue() {
		Spinner dropdown = (Spinner) findViewById(R.id.keys_spinner);

		key = String.valueOf(dropdown.getSelectedItem());

		Log.d("Key", "The Key " + key);

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

	public static int getInstrument() {
		return instrument;
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

}
