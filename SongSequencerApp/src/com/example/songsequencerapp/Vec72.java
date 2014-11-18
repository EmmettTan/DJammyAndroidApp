package com.example.songsequencerapp;

import android.R.string;
import android.content.Context;
import android.media.SoundPool;

public class Vec72 {
	// vec72 sounds
	public int note[] = new int[11];
	public int vec72_d4;
	public int vec72_e4;
	public int vec72_fsharp4;
	public int vec72_a4;
	public int vec72_b4;
	public int vec72_d5;
	public int vec72_e5;
	public int vec72_fsharp5;
	public int vec72_a5;
	public int vec72_b5;

	public void load(SoundPool soundpool, Context context, int key_index) {
		// Vec72 octave1
		
		
		note[0] = soundpool.load(context, R.raw.vec72_b3, 1);
		note[1] = soundpool.load(context, R.raw.vec72_d4, 1);
		note[2] = soundpool.load(context, R.raw.vec72_e4, 1);
		note[3] = soundpool.load(context, R.raw.vec72_fsharp4, 1);
		note[4] = soundpool.load(context, R.raw.vec72_a4, 1);
		note[5] = soundpool.load(context, R.raw.vec72_b4, 1);
		// Second Octave
		note[6] = soundpool.load(context, R.raw.vec72_d5, 1);
		note[7] = soundpool.load(context, R.raw.vec72_e5, 1);
		note[8] = soundpool.load(context, R.raw.vec72_fsharp5, 1);
		note[9] = soundpool.load(context, R.raw.vec72_a5, 1);
		note[10] = soundpool.load(context, R.raw.vec72_b5, 1);
	}
}
