package com.littlefluffytoys.beebdroid;


//import com.littlefluffytoys.beebdroid.Beebdroid.BeebView;

import android.content.Context;
import android.graphics.Canvas;
//import android.graphics.Point;
import android.graphics.RectF;
//import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
//import android.view.Display;
import android.view.MotionEvent;
//import android.view.WindowManager;
//import android.view.View;

public class ControllerView extends TouchpadsView 
//implements DPad.Listener 
{

	public ControllerView(Context context, AttributeSet attrs) {
		super(context, attrs);		
     }
	
	ControllerInfo controllerInfo;
	//DPad dpad;
	//int dpadSize;
	//int scancodeLeft;
	//int scancodeRight;
	//int scancodeUp;
	//int scancodeDown;
	
	public void setController(ControllerInfo info) {
		controllerInfo = info;

		// Don't create visible keys on a Play
//		if (beebdroid.isXperiaPlay) {
//			return;
//		}
		
		//dpadSize = (int)Beebdroid.dp(160);
//		dpad = null;
//		if (info.useDPad) {
//			dpad = new DPad(getContext());
//			dpad.listener = this;
//			requestLayout();
//		}
		recreateKeys();
	}
	
	private void recreateKeys() {
		allkeys.clear();
		if (controllerInfo == null) {
			return;
		}

		int div = 5;
//		if (Beebdroid.DP_SCREEN_WIDTH >= 500) { // big bastard screen
//			div = 6;
//		}

		float padwidth = Beebdroid.DP_SCREEN_WIDTH / div;
		
		float padheight = (Beebdroid.DP_SCREEN_HEIGHT / 2) / 3;
		
		//float padheight = padwidth;

		// getWidth() or getHeight() can return zero
		// http://stackoverflow.com/questions/6145001/getwidth-and-getheight-are-returning-a-zero
		DisplayMetrics metrics = Beebdroid.myContext.getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;
		
		Log.i("recreateKeys", "width[" + width + "] height[" + height + "] padwidth[" + padwidth + "] padheight[" + padheight + "]");
		
		for (ControllerInfo.KeyInfo keyinfo : controllerInfo.keyinfos) {
			Key key = new Key();
			key.scancode = keyinfo.scancode;
			key.label = keyinfo.label;
			key.androidKeyCode1 = keyinfo.androidKeyCode1;
			key.androidKeyCode2 = keyinfo.androidKeyCode2;
			
//    		float rBottom = (keyinfo.xc < 0) ? (width + keyinfo.xc*padwidth) : (keyinfo.xc*padwidth);
//    		float rTop = (rBottom + (padwidth * keyinfo.height));
//    		float rLeft = (keyinfo.yc < 0) ? (height + keyinfo.yc*padheight) : (keyinfo.yc*padheight);
//    		float rRight = (rLeft + (padwidth * keyinfo.width));
//    		key.bounds = new RectF(rBottom, rLeft, rRight, rTop);

			float l = (keyinfo.xc < 0) ? (width + keyinfo.xc*padwidth) : (keyinfo.xc*padwidth);
			float t = (keyinfo.yc < 0) ? (height + keyinfo.yc*padheight) : (keyinfo.yc*padheight);
			key.bounds = new RectF(l, t, (l+padwidth*keyinfo.width), (t+padheight*keyinfo.height));
			
    		Log.i("recreateKeys", "Label[" + key.label + "] Key Rect is LR " + (key.bounds.left <= key.bounds.right ? "Valid" : "Invalid"));
    		Log.i("recreateKeys", "Label[" + key.label + "] Key Rect is TB " + (key.bounds.top <= key.bounds.bottom ? "Valid" : "Invalid"));
			Log.i("recreateKeys", "Label[" + key.label + "] Rect[" + (int)key.bounds.left + "][" + (int)key.bounds.top + "][" + (int)key.bounds.right + "][" + (int)key.bounds.bottom + "]");
    		
    		allkeys.add(key);
		}
		invalidate();
	}
	
	
	//Log.i("recreateKeys", "Label[" + key.label + "] xc[" + keyinfo.xc + "] l[" + l + "]");
	//Log.i("recreateKeys", "Label[" + key.label + "] yc[" + keyinfo.xc + "] l[" + t + "]");
	// Create a new rectangle with the specified coordinates. 
	// Note: no range checking is performed, so the caller must ensure that left <= right and top <= bottom.
//	if (dpad != null) {
//		if (keyinfo.label.equals("Left")) {scancodeLeft = keyinfo.scancode; continue;}
//  		if (keyinfo.label.equals("Right")) {scancodeRight = keyinfo.scancode; continue;}
//  		if (keyinfo.label.equals("Up")) {scancodeUp = keyinfo.scancode; continue;}
//  		if (keyinfo.label.equals("Down")) {scancodeDown = keyinfo.scancode; continue;}
//	}
	//float l = (keyinfo.xc < 0) ? (width + keyinfo.xc*padwidth) : (keyinfo.xc*padwidth);
	//float t = (keyinfo.yc < 0) ? (height + keyinfo.yc*padheight) : (keyinfo.yc*padheight);
	//key.bounds = new RectF(l, t, (l+padwidth*keyinfo.width), (t+padwidth*keyinfo.height));

	//float rBottom = 0, rLeft = 0, rRight = 0, rTop = 0;
	// Where xc is negative this indicates that the pad is at the left side of the display.
	// xc can be 0, 1 (for left side keys) or 
/*	if (keyinfo.xc < 0)
	{
		rBottom = (width + keyinfo.xc*padwidth);
		rTop = (l+padwidth*keyinfo.width);
	}
	else
	{
		rBottom = (keyinfo.xc*padwidth);
		rTop = rBottom + padwidth;
	}
	
	// Where xc is zero or positive the pad is on the right of the display.
	if (keyinfo.yc < 0)
	{
		rLeft = (height + keyinfo.yc*padheight);
		rRight = (l+padwidth*keyinfo.width);
	}
	else
	{
		rLeft = (keyinfo.yc*padheight);
		rRight = rLeft + padwidth;
	}*/
	// Where xc is negative this indicates that the pad is at the left side of the display.
	// Where xc is zero or positive the pad is on the right of the display.
//	float l = (keyinfo.xc < 0) ? (keyinfo.xc*padwidth) : (keyinfo.xc*padwidth);
//	float t = (keyinfo.yc < 0) ? (keyinfo.yc*padheight) : (keyinfo.yc*padheight);
//	key.bounds = new RectF(l, t, (l+padwidth*keyinfo.width), (t+padwidth*keyinfo.height));
//	float rBottom = (keyinfo.xc < 0) ? (width + (keyinfo.xc * padwidth)) : (keyinfo.xc * padwidth);
//	float rLeft = (keyinfo.yc < 0) ? (height + ((keyinfo.yc * -1) * padheight)) : (keyinfo.yc * padheight);
//	key.bounds = new RectF(rBottom, rLeft, rBottom + padwidth, rLeft + padheight);
	//float rBottom = (keyinfo.xc < 0) ? (width + (keyinfo.xc * padwidth)) : (keyinfo.xc * padwidth);
	//float rLeft = (keyinfo.yc < 0) ? (height + ((keyinfo.yc * -1) * padheight)) : (keyinfo.yc * padheight);
	//float rRight = 0;
	//float rTop = 0;	
	
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b); 
//		if (dpad != null) {
//			dpad.onLayout(l, b-dpadSize, l+dpadSize, b);
//			//dpad.onLayout(r-dpadSize, b-dpadSize, r, b);
//		}
		//recreateKeys();
	}
	
	
	@Override
	public void draw(Canvas canvas) {
//		if (beebdroid.isXperiaPlay) {
//			return;
//		}
		super.draw(canvas);
//		if(dpad != null) {
//			dpad.draw(canvas);
//		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		// Dpad
//		if (dpad != null) {
//			if (dpad.onTouchEvent(event)) {
//				//invalidate();
//				return true;
//			}
//		}
		
		return super.onTouchEvent(event);
	}
	
	
	
	//
	// DPadView.Listener
	//
//	@Override
//	public void onLeft(boolean pressed) {
//		beebdroid.bbcKeyEvent(scancodeLeft, 0, pressed?1:0);
//		invalidate();
//	}
//	@Override
//	public void onUp(boolean pressed) {
//		beebdroid.bbcKeyEvent(scancodeUp, 0, pressed?1:0);
//		invalidate();
//	}
//	@Override
//	public void onRight(boolean pressed) {
//		beebdroid.bbcKeyEvent(scancodeRight, 0, pressed?1:0);
//		invalidate();
//	}
//	@Override
//	public void onDown(boolean pressed) {
//		beebdroid.bbcKeyEvent(scancodeDown, 0, pressed?1:0);
//		invalidate();
//	}	
}
