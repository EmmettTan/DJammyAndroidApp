package com.eece381.soundtest;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController.MediaPlayerControl;

public class MainActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

	
	
	
	/*
	 * Here are the functions to play sound. Clicking B3 will call this function and play B3.
	 * However, there are a few issues with this function that I was not able to fix.
	 * 
	 * 1) The sound doesn't come out right away and is kind of delayed
	 * 2) The sound doesn't always sound the same when it comes out, try pressing random buttons
	 * quickly and you'll see what I mean
	 * 3) If you repeatedly press a button, eventually, the buttons won't work anymore. Logcat
	 * mentions a mediaplayer error (1, 0)
	 */
	public void play_sound_0(View view) {
		MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.synth_b5);
		mediaPlayer.start();
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
	        public void onCompletion(MediaPlayer mp) {
	            mp.release();
	        };
	    });
	}

	public void play_sound_1(View view) {
		MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.synth_d4);
		mediaPlayer.start(); 
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
	        public void onCompletion(MediaPlayer mp) {
	            mp.release();
	        };
	    });
	}

	public void play_sound_2(View view) {
		MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.synth_e4);
		mediaPlayer.start(); 
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
	        public void onCompletion(MediaPlayer mp) {
	            mp.release();
	        };
	    });
	}

	public void play_sound_3(View view) {
		MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.synth_fsharp4);
		mediaPlayer.start(); 
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
	        public void onCompletion(MediaPlayer mp) {
	            mp.release();
	        };
	    });
	}

	public void play_sound_4(View view) {
		MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.synth_a4);
		mediaPlayer.start();
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
	        public void onCompletion(MediaPlayer mp) {
	            mp.release();
	        };
	    });
	}

	public void play_sound_5(View view) {
		MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.synth_b4);
		mediaPlayer.start(); 
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
	        public void onCompletion(MediaPlayer mp) {
	            mp.release();
	        };
	    });
	}
}
