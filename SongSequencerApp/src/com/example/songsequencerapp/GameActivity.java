package com.example.songsequencerapp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;



public class GameActivity extends Activity {

	public static final String KEY_JSON = "KEY";
	public static final String INSTRUMENT_JSON = "INS";
	public final byte MSG_TYPE_BROADCAST_KEYS = 1;
	
	public static final int KEY_OF_GSHARP = 0;
	public static final int KEY_OF_A = 1;
	public static final int KEY_OF_ASHARP = 2;
	public static final int KEY_OF_B = 3;
	public static final int KEY_OF_C = 4;
	public static final int KEY_OF_CSHARP = 5;
	public static final int KEY_OF_D = 6;
	public static final int KEY_OF_DSHARP = 7;
	public static final int KEY_OF_E = 8;
	public static final int KEY_OF_F = 9;
	public static final int KEY_OF_FSHARP = 10;
	public static final int KEY_OF_G = 11;
	
	public static boolean onTouch = false;
	boolean keyPressed = false;
	
	public Vec72 vec72;
	//vec216 sounds
	public int vec216_b3;
	public int vec216_d4;
	public int vec216_e4;
	public int vec216_fsharp4;
	public int vec216_a4;
	public int vec216_b4;
	public int vec216_d5;
	public int vec216_e5;
	public int vec216_fsharp5;
	public int vec216_a5;
	public int vec216_b5;
	
	public int bassdrum;
	public int bassdrum_timer=0;
	SoundPool soundpool;
	Timer bpm_timer;
	Timer tcp_timer;
	BPMTimerTask bpmTask;
	TCPReadTimerTask tcp_task;
	
	public int player0_instrument;
	public int player0_key;
	public boolean tcp_updated = false;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		vec72 = new Vec72(GameActivity.KEY_OF_CSHARP);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_game);

		tcp_task = new TCPReadTimerTask();
		tcp_timer = new Timer();
		bpmTask = new BPMTimerTask();
		bpm_timer = new Timer();
		
		soundpool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		//First Octave
		vec216_b3 = soundpool.load(getApplicationContext(), R.raw.vec216_b3, 1); // in 2nd param u have to pass your desire ringtone
		vec216_d4 = soundpool.load(getApplicationContext(), R.raw.vec216_d4, 1); // in 2nd param u have to pass your desire ringtone
		vec216_e4 = soundpool.load(getApplicationContext(), R.raw.vec216_e4, 1); // in 2nd param u have to pass your desire ringtone
		vec216_fsharp4 = soundpool.load(getApplicationContext(), R.raw.vec216_fsharp4, 1); // in 2nd param u have to pass your desire ringtone
		vec216_a4 = soundpool.load(getApplicationContext(), R.raw.vec216_a4, 1); // in 2nd param u have to pass your desire ringtone
		vec216_b4 = soundpool.load(getApplicationContext(), R.raw.vec216_b4, 1); // in 2nd param u have to pass your desire ringtone
		//Second Octave
		vec216_d5 = soundpool.load(getApplicationContext(), R.raw.vec216_d5, 1); // in 2nd param u have to pass your desire ringtone
		vec216_e5 = soundpool.load(getApplicationContext(), R.raw.vec216_e5, 1); // in 2nd param u have to pass your desire ringtone
		vec216_fsharp5 = soundpool.load(getApplicationContext(), R.raw.vec216_fsharp5, 1); // in 2nd param u have to pass your desire ringtone
		vec216_a5 = soundpool.load(getApplicationContext(), R.raw.vec216_a5, 1); // in 2nd param u have to pass your desire ringtone
		vec216_b5 = soundpool.load(getApplicationContext(), R.raw.vec216_b5, 1); // in 2nd param u have to pass your desire ringtone
	
		
		vec72.load(soundpool, getApplicationContext(), 0);
		
		
		bassdrum = soundpool.load(getApplicationContext(), R.raw.bassdrum, 1); // in 2nd param u have to pass your desire ringtone
	}

	@Override
	public void onResume() {
		super.onResume();
		bpm_timer.schedule(bpmTask, 210, 210);
		tcp_timer.schedule(tcp_task, 100, 100);
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		bpmTask.cancel();
		tcp_task.cancel();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	private int getKeyPosition(float y_pos) {
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		float key_size = size.y / GameView.DIVISIONS;
		return (int) (y_pos / key_size);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float y = event.getY();
		View game_view = findViewById(R.id.gameView1);
		int key_position = getKeyPosition(y);

		switch (event.getAction()) {
		case (MotionEvent.ACTION_DOWN):
			onTouch = true;
			keyPressed = true;
			GameView.touchPosition = key_position;
			game_view.invalidate();
			sendMessage(composeMessage(0, key_position));
			Log.d("MyApp", "Action was DOWN: " + GameView.touchPosition);
			return true;

		case (MotionEvent.ACTION_MOVE):
			GameView.touchPosition = key_position;
			game_view.invalidate();
			sendMessage(composeMessage(0, key_position));
			Log.d("MyApp", "Action was MOVE: " + getKeyPosition(y));
			return true;

		case (MotionEvent.ACTION_UP):
			onTouch = false;
			game_view.invalidate();
			Log.d("MyApp", "Action was UP");
			return true;

		default:
			return super.onTouchEvent(event);
		}
	}

	// Compose message before sending
	private String composeMessage(int instrument, int key) {
		JSONObject json = new JSONObject();

		try {
			json.put(INSTRUMENT_JSON, instrument);
			json.put(KEY_JSON, key);
		} catch (JSONException e) {
			Log.d("MyError", "Error putting in JSON!");
			e.printStackTrace();
		}

		return json.toString();
	}

	public void sendMessage(String msg) {
		MyApplication app = (MyApplication) getApplication();
	
		byte buf[] = new byte[msg.length() + 1];
		buf[0] = MSG_TYPE_BROADCAST_KEYS;
		
		System.arraycopy(msg.getBytes(), 0, buf, 1, msg.length());

		OutputStream out;
		try {
			out = app.sock.getOutputStream();
			try {
				out.write(buf, 0, msg.length() + 1);
				out.flush();
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

						final String s = new String(buf, 1, bytes_avail-1, "US-ASCII");
						Log.d("MyRcvdMessage", "Received: " + s);
						JSONObject json = new JSONObject(s);

						Log.d("MyRcvdMessage", "Instrument: " + json.getString(INSTRUMENT_JSON) + " Key: " + json.getString(KEY_JSON));
						
						player0_instrument = json.getInt(INSTRUMENT_JSON);
						player0_key = json.getInt(KEY_JSON);
						tcp_updated = true;
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					Log.d("MyRcvdError", "String to JSON conversion error!");
					e.printStackTrace();
				}
			}
		}
	}

	// Used to receive message from the middleman/DE2
	public class BPMTimerTask extends TimerTask {
		public void run() {
			Log.d("BPMTimerTask", "Playing note");
			if(bassdrum_timer == 1){
				soundpool.play(bassdrum, (float)0.7, (float)0.7, 0, 0, 1);	
				bassdrum_timer=0;
			}
			else{
				bassdrum_timer=1;
			}
			if(tcp_updated == true){
				playSound(player0_key);
				tcp_updated = false;
			}
//			if (onTouch == true) {
//				playSound(GameView.touchPosition);
//				keyPressed = false;
//			}
//			else if (keyPressed == true){
//				playSound(GameView.touchPosition);
//				keyPressed = false;
//			}
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

	public void playSound(int touchPosition) {
		Log.d("PlaySound", "Key Pressed " + touchPosition);
		switch (touchPosition) {
		case 0:
			soundpool.play(vec72.note[10], 1, 1, 0, 0, 1);
			break;
		case 1:
			soundpool.play(vec72.note[9], 1, 1, 0, 0, 1);
			break;
		case 2:
			soundpool.play(vec72.note[8], 1, 1, 0, 0, 1);
			break;
		case 3:
			soundpool.play(vec72.note[7], 1, 1, 0, 0, 1);
			break;
		case 4:
			soundpool.play(vec72.note[6], 1, 1, 0, 0, 1);
			break;
		case 5:
			soundpool.play(vec72.note[5], 1, 1, 0, 0, 1);
			break;
		case 6:
			soundpool.play(vec72.note[4], 1, 1, 0, 0, 1);
			break;
		case 7:
			soundpool.play(vec72.note[3], 1, 1, 0, 0, 1);
			break;
		case 8:
			soundpool.play(vec72.note[2], 1, 1, 0, 0, 1);
			break;
		case 9:
			soundpool.play(vec72.note[1], 1, 1, 0, 0, 1);
			break;
		case 10:
			soundpool.play(vec72.note[0], 1, 1, 0, 0, 1);
			break;
		default:
			Log.d("PlaySound", "Redundant Key Pressed "
					+ GameView.touchPosition);
			break;
		}
	}

}
