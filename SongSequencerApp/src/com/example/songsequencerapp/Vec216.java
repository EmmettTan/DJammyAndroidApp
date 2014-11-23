package com.example.songsequencerapp;

public class Vec216 extends Instrument {
	public Vec216() {
		super();
		//Keys
		
		KEY_OF_C = 0;
		KEY_OF_CSHARP = 1;
		KEY_OF_D = 2;
		KEY_OF_DSHARP = 3;
		KEY_OF_E = 4;
		KEY_OF_F = 5;
		KEY_OF_FSHARP = 6;
		KEY_OF_G = 7;
		KEY_OF_GSHARP = 8;
		KEY_OF_A = 9;
		KEY_OF_ASHARP = 10;
		KEY_OF_B = 11;
	}

	@Override
	public void initialize_full_note_list() {
		full_note_list[0] = R.raw.vec216_c3;
		full_note_list[1] = R.raw.vec216_csharp3;
		full_note_list[2] = R.raw.vec216_d3;
		full_note_list[3] = R.raw.vec216_dsharp3;
		full_note_list[4] = R.raw.vec216_e3;
		full_note_list[5] = R.raw.vec216_f3;
		full_note_list[6] = R.raw.vec216_fsharp3;
		full_note_list[7] = R.raw.vec216_g3;
		full_note_list[8] = R.raw.vec216_gsharp3;
		full_note_list[9] = R.raw.vec216_a3;
		full_note_list[10] = R.raw.vec216_asharp3;
		full_note_list[11] = R.raw.vec216_b3;
		full_note_list[12] = R.raw.vec216_c4;
		full_note_list[13] = R.raw.vec216_csharp4;
		full_note_list[14] = R.raw.vec216_d4;
		full_note_list[15] = R.raw.vec216_dsharp4;
		full_note_list[16] = R.raw.vec216_e4;
		full_note_list[17] = R.raw.vec216_f4;
		full_note_list[18] = R.raw.vec216_fsharp4;
		full_note_list[19] = R.raw.vec216_g4;
		full_note_list[20] = R.raw.vec216_gsharp4;
		full_note_list[21] = R.raw.vec216_a4;
		full_note_list[22] = R.raw.vec216_asharp4;
		full_note_list[23] = R.raw.vec216_b4;
		full_note_list[24] = R.raw.vec216_c5;
		full_note_list[25] = R.raw.vec216_csharp5;
		full_note_list[26] = R.raw.vec216_d5;
		full_note_list[27] = R.raw.vec216_dsharp5;
		full_note_list[28] = R.raw.vec216_e5;
		full_note_list[29] = R.raw.vec216_f5;
		full_note_list[30] = R.raw.vec216_fsharp5;
		full_note_list[31] = R.raw.vec216_g5;
		full_note_list[32] = R.raw.vec216_gsharp5;
		full_note_list[33] = R.raw.vec216_a5;
		full_note_list[34] = R.raw.vec216_asharp5;
		full_note_list[35] = R.raw.vec216_b5;
	}
}
