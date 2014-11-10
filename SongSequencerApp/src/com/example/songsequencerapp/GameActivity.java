package com.example.songsequencerapp;

import android.app.Activity;
import android.content.Intent;
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

public class GameActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_game);
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
	
	private int getKeyPosition(float y_pos){
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		float key_size = size.y/GameView.DIVISIONS;
		return (int) (y_pos/key_size);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float y = event.getY();
		View game_view = findViewById (R.id.gameView1);
		
		switch (event.getAction()) {
			case (MotionEvent.ACTION_DOWN):
				GameView.onTouch = true;
				GameView.touchPosition = getKeyPosition(y);
				Log.d("MyApp", "Action was DOWN: " + GameView.touchPosition);
				game_view.invalidate();
				return true;
				
			case (MotionEvent.ACTION_MOVE):
				GameView.touchPosition = getKeyPosition(y);
				Log.d("MyApp", "Action was MOVE: " + getKeyPosition(y));
				game_view.invalidate();
				return true;
				
			case (MotionEvent.ACTION_UP):
				GameView.onTouch = false;
				Log.d("MyApp", "Action was UP");
				game_view.invalidate();
				return true;
				
			default:
				return super.onTouchEvent(event);
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
