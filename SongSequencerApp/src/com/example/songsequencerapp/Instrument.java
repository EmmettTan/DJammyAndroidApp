package com.example.songsequencerapp;

import android.content.Context;
import android.media.SoundPool;

public abstract class Instrument {
	
	public static final int Vec72 = 0;
	public static final int Vec216 = 1;
	public static final int Bass = 2;
	public static final int Drums = 3;
	
	//Keys
	public int KEY_OF_GSHARP;
	public int KEY_OF_A;
	public int KEY_OF_ASHARP;
	public int KEY_OF_B;
	public int KEY_OF_C;
	public int KEY_OF_CSHARP;
	public int KEY_OF_D;
	public int KEY_OF_DSHARP;
	public int KEY_OF_E;
	public int KEY_OF_F;
	public int KEY_OF_FSHARP;
	public int KEY_OF_G;
	
	// vec72 sounds
	public int note[] = new int[11];
	public int note_ID[] = new int[11];
	public int full_note_list[] = new int[36];
	public String string;
	// The key of the instrument, ie, what pentatonic scale we are playing in
	public int instrument_key_index;

	public Instrument() {
	}
	
	public void init(int key){
		initialize_full_note_list();
		initialize_scale_array(key);
	}

	public void initialize_full_note_list() {
		
	}

	public void initialize_scale_array(int i) {
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
	
	public void unload(SoundPool soundpool) {
		for (int i=0; i<11; i++){
			soundpool.unload(note[i]);
		}
	}
}
