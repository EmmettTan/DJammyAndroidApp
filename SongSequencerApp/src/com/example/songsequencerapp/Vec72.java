package com.example.songsequencerapp;

import android.content.Context;
import android.media.SoundPool;

public class Vec72 {
	// vec72 sounds
	public int note[] = new int[11];
	public int note_ID[] = new int[11];
	public int full_note_list[] = new int[36];
	public String string;  
	// The key of the instrument, ie, what pentatonic scale we are playing in
	public int instrument_key_index;
	
	
	public Vec72(int key) {
		instrument_key_index = key;
		initialize_full_note_list();
		initialize_scale_array(instrument_key_index);
	}
	
	private void initialize_full_note_list(){
		full_note_list[0] = R.raw.vec72_gsharp3;
		full_note_list[1] = R.raw.vec72_a3;
		full_note_list[2] = R.raw.vec72_asharp3;
		full_note_list[3] = R.raw.vec72_b3;
		full_note_list[4] = R.raw.vec72_c4;
		full_note_list[5] = R.raw.vec72_csharp4;
		full_note_list[6] = R.raw.vec72_d4;
		full_note_list[7] = R.raw.vec72_dsharp4;
		full_note_list[8] = R.raw.vec72_e4;
		full_note_list[9] = R.raw.vec72_f4;
		full_note_list[10] = R.raw.vec72_fsharp4;
		full_note_list[11] = R.raw.vec72_g4;
		full_note_list[12] = R.raw.vec72_gsharp4;
		full_note_list[13] = R.raw.vec72_a4;
		full_note_list[14] = R.raw.vec72_asharp4;
		full_note_list[15] = R.raw.vec72_b4;
		full_note_list[16] = R.raw.vec72_c5;
		full_note_list[17] = R.raw.vec72_csharp5;
		full_note_list[18] = R.raw.vec72_d5;
		full_note_list[19] = R.raw.vec72_dsharp5;
		full_note_list[20] = R.raw.vec72_e5;
		full_note_list[21] = R.raw.vec72_f5;
		full_note_list[22] = R.raw.vec72_fsharp5;
		full_note_list[23] = R.raw.vec72_g5;
		full_note_list[24] = R.raw.vec72_gsharp5;
		full_note_list[25] = R.raw.vec72_a5;
		full_note_list[26] = R.raw.vec72_asharp5;
		full_note_list[27] = R.raw.vec72_b5;
		full_note_list[28] = R.raw.vec72_c6;
		full_note_list[29] = R.raw.vec72_csharp6;
		full_note_list[30] = R.raw.vec72_d6;
		full_note_list[31] = R.raw.vec72_dsharp6;
		full_note_list[32] = R.raw.vec72_e6;
		full_note_list[33] = R.raw.vec72_f6;
		full_note_list[34] = R.raw.vec72_fsharp6;
		full_note_list[35] = R.raw.vec72_g6;		
	}
	
	private void initialize_scale_array(int i){
		note_ID[0] = full_note_list[i];
		note_ID[1] = full_note_list[i+3];
		note_ID[2] = full_note_list[i+5];
		note_ID[3] = full_note_list[i+7];
		note_ID[4] = full_note_list[i+10];
		note_ID[5] = full_note_list[i+12];
		note_ID[6] = full_note_list[i+15];
		note_ID[7] = full_note_list[i+17];
		note_ID[8] = full_note_list[i+19];
		note_ID[9] = full_note_list[i+22];
		note_ID[10] = full_note_list[i+24];							
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
