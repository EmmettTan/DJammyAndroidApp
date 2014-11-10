package com.example.songsequencerapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class GameView extends View {
	public static final int DIVISIONS = 10;
	private int light_blue = Color.rgb(97, 198, 223);
	private int dark_blue = Color.rgb(0, 0, 153);
	
	public GameView(Context context, AttributeSet attribute_set) {
		super(context, attribute_set);
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawColor(light_blue);
		Paint paint = new Paint();
		int max_x = getWidth() - 1;
		int max_y = getHeight() - 1;
		
		// Draw the horizontal lines 
		paint.setColor(dark_blue);
		paint.setStrokeWidth(5);
		for (int y = max_y / DIVISIONS; y <= (DIVISIONS-1) * max_y / DIVISIONS; y += max_y / DIVISIONS) {
			canvas.drawLine(0, y, max_x, y, paint);
		}
	}
}
