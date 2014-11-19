package com.example.songsequencerapp;

import android.content.Context;
import android.media.SoundPool;

public abstract class Instrument {
	// vec72 sounds
	public int note[] = new int[11];
	public int note_ID[] = new int[11];
	public int full_note_list[] = new int[36];
	public String string;
	// The key of the instrument, ie, what pentatonic scale we are playing in
	public int instrument_key_index;

	public Instrument(int key) {
		this.instrument_key_index = key;
		initialize_full_note_list();
		initialize_scale_array(instrument_key_index);
	}

	public void initialize_full_note_list() {
		
	}

	private void initialize_scale_array(int i) {
		note_ID[0] = full_note_list[i];
		note_ID[1] = full_note_list[i + 3];
		note_ID[2] = full_note_list[i + 5];
		note_ID[3] = full_note_list[i + 7];
		note_ID[4] = full_note_list[i + 10];
		note_ID[5] = full_note_list[i + 12];
		note_ID[6] = full_note_list[i + 15];
		note_ID[7] = full_note_list[i + 17];
		note_ID[8] = full_note_list[i + 19];
		note_ID[9] = full_note_list[i + 22];
		note_ID[10] = full_note_list[i + 24];
	}

	public void load(SoundPool soundpool, Context context, int key_index) {
		note[0] = soundpool.load(context, note_ID[0], 1);
		note[1] = soundpool.load(context, note_ID[1], 1);
		note[2] = soundpool.load(context, note_ID[2], 1);
		note[3] = soundpool.load(context, note_ID[3], 1);
		note[4] = soundpool.load(context, note_ID[4], 1);
		note[5] = soundpool.load(context, note_ID[5], 1);
		note[6] = soundpool.load(context, note_ID[6], 1);
		note[7] = soundpool.load(context, note_ID[7], 1);
		note[8] = soundpool.load(context, note_ID[8], 1);
		note[9] = soundpool.load(context, note_ID[9], 1);
		note[10] = soundpool.load(context, note_ID[10], 1);
	}
}
