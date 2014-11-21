package com.example.songsequencerapp;

import android.content.Context;
import android.media.SoundPool;

public class Drums extends Instrument {
	
	public Drums() {
		super();
	}
	
	public void load(SoundPool soundpool, Context context, int key_index) {
		note[0] = soundpool.load(context, R.raw.kick0, 1);
		note[1] = soundpool.load(context, R.raw.hat0, 1);
		note[2] = soundpool.load(context, R.raw.hat1, 1);
		note[3] = soundpool.load(context, R.raw.snare0, 1);
		note[4] = soundpool.load(context, R.raw.snare1, 1);
		note[5] = soundpool.load(context, R.raw.snare2, 1);
		note[6] = soundpool.load(context, R.raw.snare3, 1);
		note[7] = soundpool.load(context, R.raw.snare4, 1);
		note[8] = soundpool.load(context, R.raw.tincan, 1);
		note[9] = soundpool.load(context, R.raw.clap0, 1);
		note[10] = soundpool.load(context, R.raw.crash0, 1);
	}

	
	
	
	
}
