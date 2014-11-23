package com.example.songsequencerapp;

import java.security.PublicKey;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

public class LoopActivity extends Activity {

	public int loopArray[];
	ProgressBar progress_bar;
	public static boolean onTouch = false;
	boolean keyPressed = false;
	int progress_percentage = 0;
	int beatPosition = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		loopArray = new int[8];
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_loop);
		progress_bar = (ProgressBar) findViewById(R.id.progressBar1);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.loop, menu);
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
		View loop_view = findViewById(R.id.loopView1);
		int key_position = getKeyPosition(y);

		switch (event.getAction()) {
		case (MotionEvent.ACTION_DOWN):
			LoopView.onTouch = true;
			keyPressed = true;
			LoopView.touchPosition = key_position;
			loop_view.invalidate();
			return true;

		case (MotionEvent.ACTION_MOVE):
			LoopView.touchPosition = key_position;
			loop_view.invalidate();
			return true;

		case (MotionEvent.ACTION_UP):
			LoopView.onTouch = false;
			loop_view.invalidate();
			
			if (beatPosition == 8) {
				progress_percentage = 0;
				beatPosition = 0;
				progress_bar.setProgress(0);
			} else {
				progress_percentage = progress_percentage + 13;
				progress_bar.setProgress(progress_percentage);
				loopArray[beatPosition] = LoopView.touchPosition;
				beatPosition++;
			}
			return true;

		default:
			return super.onTouchEvent(event);
		}
	}
}
