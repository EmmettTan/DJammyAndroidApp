package com.example.songsequencerapp;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class EasterEgg extends Activity {

	public int sandstorm;
	SoundPool soundpool;
	MediaPlayer mediaplayer;

	public static final byte MSG_TYPE_BPM = 11;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_easter_egg);
		mediaplayer = MediaPlayer.create(this, R.raw.easter);
		mediaplayer.start();
		// soundpool = new SoundPool(8, AudioManager.STREAM_MUSIC, 0);
		// sandstorm = soundpool.load(getApplicationContext(), R.raw.easter, 1);
		// soundpool.play(sandstorm, 1, 1, 0, 0, 1);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.easter_egg, menu);
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

	@Override
	public void onResume() {
		super.onResume();
		sandstorm();
	}

	public void sandstorm() {
		long time1 = 0;
		long time2 = 0;
		while (true) {
			time1 = System.nanoTime();
			while (time2 - time1 < 434782608.6956522) {
				time2 = System.nanoTime();
			}
			sendBPMMessage();

		}

	}

	// SEND the BPM beats to the DE2
	public void sendBPMMessage() {
		MyApplication app = (MyApplication) getApplication();

		byte buf[] = new byte[1];
		buf[0] = MSG_TYPE_BPM;

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

	@Override
	public void onDestroy() {
		super.onDestroy();
		mediaplayer.stop();
		mediaplayer.release();
	}
}
