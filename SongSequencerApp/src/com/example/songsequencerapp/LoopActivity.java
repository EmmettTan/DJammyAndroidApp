package com.example.songsequencerapp;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
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
import android.widget.ProgressBar;

public class LoopActivity extends Activity {

	public int loopArray[];
	ProgressBar progress_bar;
	int progress_percentage = 0;
	int recordPosition = 0;
	int playPosition = 0;
	boolean startRecording = false;
	
	public static boolean onTouch = false;
	boolean keyPressed = false;

	public Vec72 vec72;
	public Vec216 vec216;
	public Bass bass;
	public Drums drums;

	public int bassdrum;
	public int bassdrum_timer = 0;
	SoundPool soundpool;
	float instrument_volume = 1;
	float bpm_volume = (float) 0.7;

	Timer bpm_timer;
	BPMTimerTask bpmTask;

	public int my_instrument = 3;
	public int my_key;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_loop);

		loopArray = new int[LoopSettings.beatNumber];
		progress_bar = (ProgressBar) findViewById(R.id.progressBar1);
		bpm_timer = new Timer();

		drums = new Drums();
		vec72 = new Vec72();
		vec72.init(vec72.KEY_OF_B);
		vec216 = new Vec216();
		vec216.init(vec216.KEY_OF_B);
		bass = new Bass();
		bass.init(bass.KEY_OF_B);

		soundpool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		vec72.load(soundpool, getApplicationContext(), 0);
		vec216.load(soundpool, getApplicationContext(), 0);
		bass.load(soundpool, getApplicationContext(), 0);
		drums.load(soundpool, getApplicationContext(), 0);
		bassdrum = soundpool.load(getApplicationContext(), R.raw.bassdrum, 1);
	}

	@Override
	public void onResume() {
		super.onResume();
		bpmTask = new BPMTimerTask();
		bpm_timer.schedule(bpmTask, 210, 210);
	}

	@Override
	protected void onPause() {
		super.onPause();
		bpmTask.cancel();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.loop, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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
		float key_size = size.y / LoopView.DIVISIONS;
		return (int) (y_pos / key_size);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float y = event.getY();
		View loop_view = findViewById(R.id.loopView1);
		int key_position = getKeyPosition(y);

		switch (event.getAction()) {
		case (MotionEvent.ACTION_DOWN):
			startRecording = true;
			onTouch = true;
			keyPressed = true;
			LoopView.touchPosition = key_position;
			loop_view.invalidate();
			return true;

		case (MotionEvent.ACTION_MOVE):
			LoopView.touchPosition = key_position;
			loop_view.invalidate();
			return true;

		case (MotionEvent.ACTION_UP):
			onTouch = false;
			loop_view.invalidate();
			return true;

		default:
			return super.onTouchEvent(event);
		}
	}

	// PLAY notes (timer)
	public class BPMTimerTask extends TimerTask {
		public void run() {
//			Log.d("BPMTimerTask", "Playing note");
			if (bassdrum_timer == 1) {
				soundpool.play(bassdrum, bpm_volume, bpm_volume, 0, 0, 1);
				bassdrum_timer = 0;
			} 
			else {
				bassdrum_timer = 1;
			}
			if (startRecording && recordPosition < LoopSettings.beatNumber){
				if (onTouch == true || keyPressed == true) {
					playSound(LoopView.touchPosition, my_instrument);
					keyPressed = false;
					loopArray[recordPosition] = LoopView.touchPosition;
				}
				else{
					loopArray[recordPosition] = -1;
				}
				recordPosition++;
				progress_percentage = progress_percentage + 100/LoopSettings.beatNumber;
				progress_bar.setProgress(progress_percentage);
			}
			else if (startRecording){
				if (playPosition < LoopSettings.beatNumber){
					playSound(loopArray[playPosition], my_instrument);
					playPosition++;
				}
				else{
					playPosition = 0;
					playSound(loopArray[playPosition], my_instrument);
					playPosition++;
				}
			}
		} 
	}

	// PLAY notes depending on the instrument and key received
	public void playSound(int touchPosition, int instrument) {
		//Log.d("PlaySound", "Key Pressed " + touchPosition);
		switch (instrument) {
		case Instrument.Vec72:
			pickVec72Note(touchPosition);
			break;
		case Instrument.Vec216:
			pickVec216Note(touchPosition);
			break;
		case Instrument.Bass:
			pickBassNote(touchPosition);
			break;
		case Instrument.Drums:
			pickDrumsNote(touchPosition);
			break;
		default:
			break;
		}
	}

	// PLAY Vec72 notes
	public void pickVec72Note(int touchPosition) {
		switch (touchPosition) {
		case 0:
			soundpool.play(vec72.note[10], instrument_volume,
					instrument_volume, 0, 0, 1);
			break;
		case 1:
			soundpool.play(vec72.note[9], instrument_volume, instrument_volume,
					0, 0, 1);
			break;
		case 2:
			soundpool.play(vec72.note[8], instrument_volume, instrument_volume,
					0, 0, 1);
			break;
		case 3:
			soundpool.play(vec72.note[7], instrument_volume, instrument_volume,
					0, 0, 1);
			break;
		case 4:
			soundpool.play(vec72.note[6], instrument_volume, instrument_volume,
					0, 0, 1);
			break;
		case 5:
			soundpool.play(vec72.note[5], instrument_volume, instrument_volume,
					0, 0, 1);
			break;
		case 6:
			soundpool.play(vec72.note[4], instrument_volume, instrument_volume,
					0, 0, 1);
			break;
		case 7:
			soundpool.play(vec72.note[3], instrument_volume, instrument_volume,
					0, 0, 1);
			break;
		case 8:
			soundpool.play(vec72.note[2], instrument_volume, instrument_volume,
					0, 0, 1);
			break;
		case 9:
			soundpool.play(vec72.note[1], instrument_volume, instrument_volume,
					0, 0, 1);
			break;
		case 10:
			soundpool.play(vec72.note[0], instrument_volume, instrument_volume,
					0, 0, 1);
			break;
		default:
			Log.d("PlaySound", "Redundant Key Pressed "
					+ LoopView.touchPosition);
			break;
		}
	}

	// PLAY Vec216 notes
	public void pickVec216Note(int touchPosition) {
		switch (touchPosition) {
		case 0:
			soundpool.play(vec216.note[10], instrument_volume,
					instrument_volume, 0, 0, 1);
			break;
		case 1:
			soundpool.play(vec216.note[9], instrument_volume,
					instrument_volume, 0, 0, 1);
			break;
		case 2:
			soundpool.play(vec216.note[8], instrument_volume,
					instrument_volume, 0, 0, 1);
			break;
		case 3:
			soundpool.play(vec216.note[7], instrument_volume,
					instrument_volume, 0, 0, 1);
			break;
		case 4:
			soundpool.play(vec216.note[6], instrument_volume,
					instrument_volume, 0, 0, 1);
			break;
		case 5:
			soundpool.play(vec216.note[5], instrument_volume,
					instrument_volume, 0, 0, 1);
			break;
		case 6:
			soundpool.play(vec216.note[4], instrument_volume,
					instrument_volume, 0, 0, 1);
			break;
		case 7:
			soundpool.play(vec216.note[3], instrument_volume,
					instrument_volume, 0, 0, 1);
			break;
		case 8:
			soundpool.play(vec216.note[2], instrument_volume,
					instrument_volume, 0, 0, 1);
			break;
		case 9:
			soundpool.play(vec216.note[1], instrument_volume,
					instrument_volume, 0, 0, 1);
			break;
		case 10:
			soundpool.play(vec216.note[0], instrument_volume,
					instrument_volume, 0, 0, 1);
			break;
		default:
			Log.d("PlaySound", "Redundant Key Pressed "
					+ LoopView.touchPosition);
			break;
		}
	}

	// PLAY bass notes
	public void pickBassNote(int touchPosition) {
		switch (touchPosition) {
		case 0:
			soundpool.play(bass.note[10], instrument_volume, instrument_volume,
					0, 0, 1);
			break;
		case 1:
			soundpool.play(bass.note[9], instrument_volume, instrument_volume,
					0, 0, 1);
			break;
		case 2:
			soundpool.play(bass.note[8], instrument_volume, instrument_volume,
					0, 0, 1);
			break;
		case 3:
			soundpool.play(bass.note[7], instrument_volume, instrument_volume,
					0, 0, 1);
			break;
		case 4:
			soundpool.play(bass.note[6], instrument_volume, instrument_volume,
					0, 0, 1);
			break;
		case 5:
			soundpool.play(bass.note[5], instrument_volume, instrument_volume,
					0, 0, 1);
			break;
		case 6:
			soundpool.play(bass.note[4], instrument_volume, instrument_volume,
					0, 0, 1);
			break;
		case 7:
			soundpool.play(bass.note[3], instrument_volume, instrument_volume,
					0, 0, 1);
			break;
		case 8:
			soundpool.play(bass.note[2], instrument_volume, instrument_volume,
					0, 0, 1);
			break;
		case 9:
			soundpool.play(bass.note[1], instrument_volume, instrument_volume,
					0, 0, 1);
			break;
		case 10:
			soundpool.play(bass.note[0], instrument_volume, instrument_volume,
					0, 0, 1);
			break;
		default:
			Log.d("PlaySound", "Redundant Key Pressed "
					+ LoopView.touchPosition);
			break;
		}
	}

	// Play drums sounds
	public void pickDrumsNote(int touchPosition) {
		switch (touchPosition) {
		case 0:
			soundpool.play(drums.note[10], instrument_volume,
					instrument_volume, 0, 0, 1);
			break;
		case 1:
			soundpool.play(drums.note[9], instrument_volume, instrument_volume,
					0, 0, 1);
			break;
		case 2:
			soundpool.play(drums.note[8], instrument_volume, instrument_volume,
					0, 0, 1);
			break;
		case 3:
			soundpool.play(drums.note[7], instrument_volume, instrument_volume,
					0, 0, 1);
			break;
		case 4:
			soundpool.play(drums.note[6], instrument_volume, instrument_volume,
					0, 0, 1);
			break;
		case 5:
			soundpool.play(drums.note[5], instrument_volume, instrument_volume,
					0, 0, 1);
			break;
		case 6:
			soundpool.play(drums.note[4], instrument_volume, instrument_volume,
					0, 0, 1);
			break;
		case 7:
			soundpool.play(drums.note[3], instrument_volume, instrument_volume,
					0, 0, 1);
			break;
		case 8:
			soundpool.play(drums.note[2], instrument_volume, instrument_volume,
					0, 0, 1);
			break;
		case 9:
			soundpool.play(drums.note[1], instrument_volume, instrument_volume,
					0, 0, 1);
			break;
		case 10:
			soundpool.play(drums.note[0], instrument_volume, instrument_volume,
					0, 0, 1);
			break;
		default:
			Log.d("PlaySound", "Redundant Key Pressed "
					+ LoopView.touchPosition);
			break;
		}
	}
}
