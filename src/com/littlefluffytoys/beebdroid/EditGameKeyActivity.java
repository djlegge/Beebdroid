package com.littlefluffytoys.beebdroid;

import common.Utils;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class EditGameKeyActivity extends Activity
{
	// ===========================================================================
	private TextView LBbbcKey;
	private TextView LBandroidKey;
	private Button BTsave;
	private Button BTclose;
	private Button BTreadKey;
	private Button BTdelete;
	private String bbcKey = "";
	private String gameFilename = "";
	private int androidKey = -1;
	private Boolean readKeyStateNow = false;
	// ===========================================================================
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_game_key);
		LBbbcKey = (TextView) findViewById(R.id.LBbbcKey);
		LBandroidKey = (TextView) findViewById(R.id.LBandroidKey);
		BTsave = (Button) findViewById(R.id.BTsave);
		BTclose = (Button) findViewById(R.id.BTclose);
		BTreadKey = (Button) findViewById(R.id.BTreadKey);
		BTdelete = (Button) findViewById(R.id.BTdelete);
		// === 
		Intent intent = getIntent();
		bbcKey = intent.getStringExtra(LoadDisk.EXTRA_BBC_KEY_CODE);
		gameFilename = intent.getStringExtra(LoadDisk.EXTRA_GAME_FILENAME);
		int i = BeebKeys.parseBeebKeyIntFromString(bbcKey);
		String s = BeebKeys.getBbcKeyNamefromInt(i);
		Utils.writeLog("EditGameKeyActivity Filename[" + gameFilename + "] Key [" + bbcKey + "] Int [" + i + "] Key [" + s + "]");
		// === 
		LBbbcKey.setText(bbcKey);
		readKeyStateNow = false;
		// ===========================================================================
		BTsave.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				
				if (androidKey < 0) androidKey = 42; // TODO remove
				
				ConfigKey ck = new ConfigKey();
				ck.label = "label";
				ck.keylabel = "keylabel";
				ck.xc = 1f;
				ck.yc = 1f;
				ck.width = 1f;
				ck.height = 1f;
				ck.scancode = BeebKeys.parseBeebKeyIntFromString(bbcKey);
				ck.androidKeyCode1 = androidKey;
				ck.androidKeyCode2 = -1;
				Controllers.writeKeyConfigRow(gameFilename, ck);
				finish();
			}
		});
		// ===========================================================================
		BTclose.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
		// ===========================================================================
		BTreadKey.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				readKeyStateNow = true;
				Toast.makeText(EditGameKeyActivity.this, "Press your key now", Toast.LENGTH_SHORT).show();
			}
		});
		// ===========================================================================
		BTdelete.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Controllers.deleteOrReplaceKeyConfigRow(gameFilename, bbcKey, "");
				finish();
			}
		});
	}
	// ===========================================================================
	@Override
	public boolean onKeyDown(int keycode, KeyEvent event)
	{
		if (readKeyStateNow)
		{
			Toast.makeText(EditGameKeyActivity.this, "Got [" + keycode + "]", Toast.LENGTH_SHORT).show();
			androidKey = keycode;
			String s = BeebKeys.getKeyEventNameFromInt(keycode);
			int i = BeebKeys.parseKeyEventIntFromString(s);			
			Utils.writeLog("onKeyDown KeyCode [" + keycode + "] Int [" + i + "] Key [" + s + "]");			
			LBandroidKey.setText("Set [" + androidKey + "]");
			return true; // stop this event here, don't process this keypress any further
		}
		return super.onKeyDown(keycode, event);
	}
	// ===========================================================================
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.activity_edit_game_key, menu);
		return true;
	}
	// ===========================================================================
}




