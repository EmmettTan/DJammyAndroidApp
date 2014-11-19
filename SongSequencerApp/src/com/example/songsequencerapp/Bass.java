package com.example.songsequencerapp;

public class Bass extends Instrument {
	public Bass() {
		super();
		//Keys
		KEY_OF_FSHARP = 0;
		KEY_OF_G = 1;
		KEY_OF_GSHARP = 2;
		KEY_OF_A = 3;
		KEY_OF_ASHARP = 4;
		KEY_OF_B = 5;
		KEY_OF_C = 6;
		KEY_OF_CSHARP = 7;
		KEY_OF_D = 8;
		KEY_OF_DSHARP = 9;
		KEY_OF_E = 10;
		KEY_OF_F = 11;
	}

	
	@Override
	public void initialize_full_note_list() {
		full_note_list[0] = R.raw.bass_fsharp1;
		full_note_list[1] = R.raw.bass_g1;
		full_note_list[2] = R.raw.bass_gsharp1;
		full_note_list[3] = R.raw.bass_a1;
		full_note_list[4] = R.raw.bass_asharp1;
		full_note_list[5] = R.raw.bass_b1;
		full_note_list[6] = R.raw.bass_c2;
		full_note_list[7] = R.raw.bass_csharp2;
		full_note_list[8] = R.raw.bass_d2;
		full_note_list[9] = R.raw.bass_dsharp2;
		full_note_list[10] = R.raw.bass_e2;
		full_note_list[11] = R.raw.bass_f2;
		full_note_list[12] = R.raw.bass_fsharp2;
		full_note_list[13] = R.raw.bass_g2;
		full_note_list[14] = R.raw.bass_gsharp2;
		full_note_list[15] = R.raw.bass_a2;
		full_note_list[16] = R.raw.bass_asharp2;
		full_note_list[17] = R.raw.bass_b2;
		full_note_list[18] = R.raw.bass_c3;
		full_note_list[19] = R.raw.bass_csharp3;
		full_note_list[20] = R.raw.bass_d3;
		full_note_list[21] = R.raw.bass_dsharp3;
		full_note_list[22] = R.raw.bass_e3;
		full_note_list[23] = R.raw.bass_f3;
		full_note_list[24] = R.raw.bass_b2;
		full_note_list[25] = R.raw.bass_c3;
		full_note_list[26] = R.raw.bass_csharp3;
		full_note_list[27] = R.raw.bass_d3;
		full_note_list[28] = R.raw.bass_dsharp3;
		full_note_list[29] = R.raw.bass_e3;
		full_note_list[30] = R.raw.bass_f3;
		full_note_list[31] = R.raw.bass_b2;
		full_note_list[32] = R.raw.bass_c3;
		full_note_list[33] = R.raw.bass_csharp3;
		full_note_list[34] = R.raw.bass_d3;
		full_note_list[35] = R.raw.bass_dsharp3;
	}
	
	
	
}
