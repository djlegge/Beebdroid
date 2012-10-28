package com.littlefluffytoys.beebdroid;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import common.Utils;
//import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;

public class Controllers {
	// ===========================================================================
	// === 
	// === 
	// === 
	// ===========================================================================	
	static ControllerInfo currentControllerInfo = new ControllerInfo();
	static final String logTag = "Beebdroid.Controllers";
	static int controllerKeysLoaded = 0;
	static String currentDiskInfoKey = "";
	static String controlsConfigFilename = Utils.SDrootPathSlash + "/config/beebdroid.gamecontrols.txt";
	private static final String LOG_TAG = "Beebdroid.Controllers"; 
	private static final String VBCRLF = System.getProperty("line.separator");
	// ===========================================================================
	// === 
	// === 
	// === 
	// ===========================================================================
	public static ControllerInfo getControllerInfo(String diskInfoKey)
	{
		//Utils.writeLog("getControllerInfo[" + diskInfoKey + "]");
		currentDiskInfoKey = diskInfoKey;
		controllerKeysLoaded = 0;
		currentControllerInfo = new ControllerInfo();
		try
		{
			// Open the file
			FileInputStream fstream = new FileInputStream(controlsConfigFilename);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			//Read File Line By Line
			while ((strLine = br.readLine()) != null)   {
				// Parse the contents of the line
				parseRowAddKey(strLine);
			}
			//Close the input stream
			br.close();
			in.close();
			fstream.close();
		} 
		catch (Exception e)
		{
			//Log.e("Utils", e..toString());
			e.printStackTrace();
		}
		
		if (controllerKeysLoaded == 0)
		{
			Utils.writeLog("Return default controller");

			currentControllerInfo.addKey("Left",  "Z",   	0f,  1f, 1f, 1f, BeebKeys.BBCKEY_Z, KeyEvent.KEYCODE_DPAD_LEFT);
			currentControllerInfo.addKey("Right", "X",   	1f,  1f, 1f, 1f, BeebKeys.BBCKEY_X, KeyEvent.KEYCODE_DPAD_RIGHT);			
			currentControllerInfo.addKey("Up",    ":",  	-2f,  0f, 1f, 1f, BeebKeys.BBCKEY_COLON, KeyEvent.KEYCODE_DPAD_UP);
			currentControllerInfo.addKey("Down",  "/",  	-2f,  1f, 1f, 1f, BeebKeys.BBCKEY_SLASH, KeyEvent.KEYCODE_DPAD_DOWN);
			currentControllerInfo.addKey("Fire", "Return",  -1f,  0f, 1f, 2f, BeebKeys.BBCKEY_ENTER, KeyEvent.KEYCODE_BUTTON_1);
			
//			currentControllerInfo.addKey("Left",  "Z",   	0f,  1f, 1f, 1f, BeebKeys.BBCKEY_Z, KeyEvent.KEYCODE_DPAD_LEFT);
//			currentControllerInfo.addKey("Right", "X",   	1f,  1f, 1f, 1f, BeebKeys.BBCKEY_X, KeyEvent.KEYCODE_DPAD_RIGHT);			
//			currentControllerInfo.addKey("Up",    ":",  	-1f,  0f, 1f, 1f, BeebKeys.BBCKEY_COLON, KeyEvent.KEYCODE_DPAD_UP);
//			currentControllerInfo.addKey("Down",  "/",  	-1f,  1f, 1f, 1f, BeebKeys.BBCKEY_SLASH, KeyEvent.KEYCODE_DPAD_DOWN);
			
//			currentControllerInfo.addKey("A",  "A",		0f,  1f, 1f, 1f, BeebKeys.BBCKEY_A, KeyEvent.KEYCODE_A);
//			currentControllerInfo.addKey("B",  "B",		0f,  0f, 1f, 1f, BeebKeys.BBCKEY_B, KeyEvent.KEYCODE_B);
//			//currentControllerInfo.addKey("C",  "C",		0f,  -1f, 1f, 1f, BeebKeys.BBCKEY_C, KeyEvent.KEYCODE_C);
//			currentControllerInfo.addKey("D",  "D",		1f,  1f, 1f, 1f, BeebKeys.BBCKEY_D, KeyEvent.KEYCODE_D);
//			currentControllerInfo.addKey("E",  "E",		1f,  0f, 1f, 1f, BeebKeys.BBCKEY_E, KeyEvent.KEYCODE_E);
//			//currentControllerInfo.addKey("F",  "F",		1f,  -1f, 1f, 1f, BeebKeys.BBCKEY_F, KeyEvent.KEYCODE_F);
//			currentControllerInfo.addKey("G",  "G",		-2f,  1f, 1f, 1f, BeebKeys.BBCKEY_G, KeyEvent.KEYCODE_G);
//			currentControllerInfo.addKey("H",  "H",		-2f,  0f, 1f, 1f, BeebKeys.BBCKEY_H, KeyEvent.KEYCODE_H);
//			//currentControllerInfo.addKey("I",  "I",		-2f,  -1f, 1f, 1f, BeebKeys.BBCKEY_I, KeyEvent.KEYCODE_I);
//			currentControllerInfo.addKey("J",  "J",		-1f,  1f, 1f, 1f, BeebKeys.BBCKEY_J, KeyEvent.KEYCODE_J);
//			currentControllerInfo.addKey("K",  "K",		-1f,  0f, 1f, 1f, BeebKeys.BBCKEY_K, KeyEvent.KEYCODE_K);
//			//currentControllerInfo.addKey("L",  "L",		-1f,  -1f, 1f, 1f, BeebKeys.BBCKEY_L, KeyEvent.KEYCODE_L);
			

		}
		return currentControllerInfo;
	}
	// ===========================================================================
	// === 
	// === 
	// === 
	// ===========================================================================
	private static void parseRowAddKey(String configStr)
	{
		configStr = configStr.replace("\t", " ");
		configStr = configStr.replace("  ", " ");
		configStr = configStr.replace("  ", " ");
		configStr = configStr.replace("  ", " ");
		configStr = configStr.replace("  ", " ");
		configStr = configStr.replace("  ", " ");
		String[] separated = configStr.split(" ");		
		String lg = "Separated [" + separated.length + "] = ";
		for (int i = 0; i < separated.length; i++)
		{			
			lg += separated[i] + " ";
		}
		Log.i("beebdroid", lg);
		if (separated.length >= 6 && currentDiskInfoKey.compareToIgnoreCase(separated[0]) == 0)
		{
			String label = separated[1];
			String keyLabel = separated[2];			
			if (keyLabel.trim().length() == 0) keyLabel = "Space";			
			float xc = Float.parseFloat(separated[3]);
			float yc = Float.parseFloat(separated[4]);
			float width = Float.parseFloat(separated[5]);
			float height = Float.parseFloat(separated[6]);
			int scancode = BeebKeys.parseBeebKeyIntFromString(separated[7]);			
			if (separated.length == 8)
			{
				Log.i("Beebdroid", "parseRowAddKey [" + label + "] [" + keyLabel + "] xc[" + xc + "] yc[" + yc + "] w[" + width + "] h[" + height + "] sc[" + scancode + "]");
				currentControllerInfo.addKey(label, keyLabel, xc, yc, width, height, scancode);
				controllerKeysLoaded += 1;
			}			
			if (separated.length == 9)
			{
				int kc = BeebKeys.parseKeyEventIntFromString(separated[8]);
				Log.i("Beebdroid", "ControllerInfo2 [" + label + "] [" + keyLabel + "] [" + xc + "] [" + yc + "] [" + width + "] [" + height + "] sc[" + scancode + "] ak[" + kc + "]");
				currentControllerInfo.addKey(label, keyLabel, xc, yc, width, height, scancode, kc);
				controllerKeysLoaded += 1;
			}
			if (separated.length == 10)
			{
				int kc1 = BeebKeys.parseKeyEventIntFromString(separated[8]);
				int kc2 = BeebKeys.parseKeyEventIntFromString(separated[9]);
				Log.i("Beebdroid", "ControllerInfo3 [" + label + "] [" + keyLabel + "] [" + xc + "] [" + yc + "] [" + width + "] [" + height + "] sc[" + scancode + "] ak1[" + kc1 + "] ak2[" + kc2 + "]");
				currentControllerInfo.addKey(label, keyLabel, xc, yc, width, height, scancode, kc1, kc2);
				controllerKeysLoaded += 1;
			}
		}
	}
	// ===========================================================================
	// === 
	// === 
	// === 
	// ===========================================================================
	public static void writeKeyConfigRow(String fileName, ConfigKey ck)
	{
		String scancodeName = BeebKeys.getBbcKeyNamefromInt(ck.scancode);
		String newRow = "";
		newRow += fileName + " ";
		newRow += ck.label + " ";
		newRow += ck.keylabel + " ";
		newRow += ck.xc + " ";
		newRow += ck.yc + " ";
		newRow += ck.width + " ";
		newRow += ck.height + " ";
		newRow += scancodeName + " ";
		if (ck.androidKeyCode1 > 0) newRow += BeebKeys.getKeyEventNameFromInt(ck.androidKeyCode1) + " ";
		if (ck.androidKeyCode2 > 0) newRow += BeebKeys.getKeyEventNameFromInt(ck.androidKeyCode2) + " ";
		deleteOrReplaceKeyConfigRow(fileName, scancodeName, newRow);		
	}
	// ===========================================================================
	// === 
	// === 
	// === 
	// ===========================================================================	
	public static void deleteOrReplaceKeyConfigRow(String fileName, String bbcScanCodeName, String newRow) 
	{
		Log.i("Controllers.deleteOrReplaceKeyConfigRow", "File[" + fileName + "] Code[" + bbcScanCodeName + "] " + newRow);
		String remainingConfigFile = "";
		File file = new File(controlsConfigFilename);
		if(file.exists())
		{
			Log.i(LOG_TAG, "File exists");
			try
			{
				// Open the file
				FileInputStream fstream = new FileInputStream(controlsConfigFilename);
				// Get the object of DataInputStream
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String strLine;
				Boolean rowReplaced = false;
				//Read File Line By Line
				while ((strLine = br.readLine()) != null)   
				{
					Log.i(LOG_TAG, "Row [" + strLine + "]");
					if (strLine.contains(fileName + " ") == false || strLine.contains(bbcScanCodeName) == false)
					{
						Log.i(LOG_TAG, "Keep");
						remainingConfigFile += strLine + VBCRLF; 
					}
					else
					{
						Log.i(LOG_TAG, "Replace");
						if (newRow.trim().length() > 2) {
							remainingConfigFile += newRow + VBCRLF;
							rowReplaced = true;
						}
					}
				}
				if (rowReplaced == false)
				{
					Log.i(LOG_TAG, "Add");
					remainingConfigFile += newRow + VBCRLF;
				}
				//Close the input stream
				br.close();
				in.close();
				fstream.close();
			} 
			catch (Exception e)
			{
				//Log.e("Utils", e.toString());
				e.printStackTrace();
			}
		}
		else
		{
			Log.i(LOG_TAG, "File doesn't exist");
			File dir = new File(Utils.SDrootPathSlash + "/config");
			dir.mkdir();
			remainingConfigFile = newRow;
		}
		// === 		
		// === Write the entire file out to disk 		
		// === 		
		try
		{
			FileWriter out = new FileWriter(controlsConfigFilename);
			out.write(remainingConfigFile);
			out.close();
		}
		catch (Exception e)
		{
			//Log.e("Utils", e.toString());
			e.printStackTrace();
		}
	}
	// ===========================================================================
	// === 
	// === 
	// === 
	// ===========================================================================
}

//currentControllerInfo.addKey("Left",  "Z",   0f,  -1f, 1f, 1f, BeebKeys.BBCKEY_Z, KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_BUTTON_X);
//currentControllerInfo.addKey("Right", "X",   1f,  -1f, 1f, 1f, BeebKeys.BBCKEY_X, KeyEvent.KEYCODE_DPAD_RIGHT, KeyEvent.KEYCODE_BACK);
//currentControllerInfo.addKey("Up",    ":",  -2f,  0f, 1f, 1f, BeebKeys.BBCKEY_COLON, KeyEvent.KEYCODE_DPAD_UP, KeyEvent.KEYCODE_BUTTON_Y);
//currentControllerInfo.addKey("Down",  "/",  -2f,  1f, 1f, 1f, BeebKeys.BBCKEY_SLASH, KeyEvent.KEYCODE_DPAD_DOWN, KeyEvent.KEYCODE_DPAD_CENTER);
//currentControllerInfo.addKey("Fire", "Return",  -1f,  0f, 1f, 2f, BeebKeys.BBCKEY_ENTER, KeyEvent.KEYCODE_BUTTON_1, KeyEvent.KEYCODE_BUTTON_R1);
// the numbers mean ?? ?? width height
//currentControllerInfo.addKey("Left",  "Z",   	0f,  0f, 	1f, 1f, BeebKeys.BBCKEY_Z, KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_BUTTON_X);
//currentControllerInfo.addKey("Right", "X",   	1f,  0f, 	1f, 1f, BeebKeys.BBCKEY_X, KeyEvent.KEYCODE_DPAD_RIGHT, KeyEvent.KEYCODE_BACK);
//currentControllerInfo.addKey("Up",    ":",  	2f,  1f, 	1f, 1f, BeebKeys.BBCKEY_COLON, KeyEvent.KEYCODE_DPAD_UP, KeyEvent.KEYCODE_BUTTON_Y);
//currentControllerInfo.addKey("Down",  "/",  	3f,  1f, 	1f, 1f, BeebKeys.BBCKEY_SLASH, KeyEvent.KEYCODE_DPAD_DOWN, KeyEvent.KEYCODE_DPAD_CENTER);
//currentControllerInfo.addKey("Fire", "Return",	4f,  2f, 	1f, 2f, BeebKeys.BBCKEY_ENTER, KeyEvent.KEYCODE_BUTTON_1, KeyEvent.KEYCODE_BUTTON_R1);


//String state = Environment.getExternalStorageState();
//if (Environment.MEDIA_MOUNTED.equals(state)) {
//    Log.d("Controllers", "sdcard mounted and writable");
//}
//else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
//    Log.d("Controllers", "sdcard mounted readonly");
//}
//else {
//    Log.d("Controllers", "sdcard state: " + state);
//}

//File in = new FileReader(controlsConfigFilename);
//BufferedReader br = new BufferedReader(new FileReader(in));
//String line;
//while ((line = br.readLine()) != null) {
//   // process the line.
//}
//br.close();
//
//
//
//try
//{
//	FileReader in = new FileReader(controlsConfigFilename);
//	BufferedReader br = new BufferedReader(in); 
//while((controlsConfigLine = br.readLine()) != null) { 
//System.out.println(s); 
//} 
//fr.close(); 
//	
//	
//	//if (logStr == null) logStr = "?NULL?";
//	//Log.i("Utils", logStr);
//	// ===
//	//File sdCard = Environment.getExternalStorageDirectory();
//	File dir = new File(SDrootPathSlash + "logs");
//	dir.mkdirs();
//	// ===
//	String ymd = android.text.format.DateFormat.format("y-MM-d", new Date()).toString();
//	String hhmmss = android.text.format.DateFormat.format("kk:mm:ss", new Date()) + " ";
//	FileWriter out = new FileWriter(dir + "/beebdroid_" + ymd + ".txt", true);
//	out.write(hhmmss + logStr + "\r\n");
//	out.close();
//} 
//catch (Exception e)
//{
//	Log.e("Utils", e.toString());
//}

// beebdroid.gamecontrols.txt



//parseRowAddKey("imogen Left Z 0 -1 1f 1f BeebKeys.BBCKEY_Z KeyEvent.KEYCODE_DPAD_LEFT");		
//parseRowAddKey("imogen Right X 1 -1 1f 1f BeebKeys.BBCKEY_X KeyEvent.KEYCODE_DPAD_RIGHT");
//parseRowAddKey("imogen Up : 0 -1 1f 1f BeebKeys.BBCKEY_COLON KeyEvent.KEYCODE_DPAD_UP");
//parseRowAddKey("imogen Down / -2 1 1f 1f BeebKeys.BBCKEY_SLASH KeyEvent.KEYCODE_DPAD_DOWN");//		
//parseRowAddKey("imogen Fire Return -1 0 1f 2f BeebKeys.BBCKEY_ENTER KeyEvent.KEYCODE_BUTTON_1");
//parseRowAddKey("imogen [] [] 1 0 .5f .5f BeebKeys.BBCKEY_SPACE KeyEvent.KEYCODE_BUTTON_9");//		
//parseRowAddKey("imogen <- <- 0 0 .5f .5f BeebKeys.BBCKEY_ARROW_LEFT KeyEvent.KEYCODE_BUTTON_7");
//parseRowAddKey("imogen -> -> 0.5f -0 .5f .5f BeebKeys.BBCKEY_ARROW_RIGHT KeyEvent.KEYCODE_BUTTON_8");
//
//parseRowAddKey("zalaga Left Z 0 0 1f 1f BeebKeys.BBCKEY_CAPS KeyEvent.KEYCODE_DPAD_LEFT");
//parseRowAddKey("zalaga Right X 0 0 1f 1f BeebKeys.BBCKEY_CTRL KeyEvent.KEYCODE_DPAD_RIGHT");
//parseRowAddKey("zalaga Fire Return -1 0 1f 1f BeebKeys.BBCKEY_ENTER KeyEvent.KEYCODE_BUTTON_1");
//
//parseRowAddKey("cylonattack.ssd Left < 0 0 1f 1f BeebKeys.BBCKEY_LESS_THAN KeyEvent.KEYCODE_DPAD_LEFT");
//parseRowAddKey("cylonattack.ssd Right > 0 0 1f 1f BeebKeys.BBCKEY_MORE_THAN KeyEvent.KEYCODE_DPAD_RIGHT");
//parseRowAddKey("cylonattack.ssd Up A 0 0 1f 1f BeebKeys.BBCKEY_A KeyEvent.KEYCODE_DPAD_UP");
//parseRowAddKey("cylonattack.ssd Down Z 0 0 1f 1f BeebKeys.BBCKEY_Z KeyEvent.KEYCODE_DPAD_DOWN");
//parseRowAddKey("cylonattack.ssd Fire Space 0 0 1f 1f BeebKeys.BBCKEY_SPACE KeyEvent.KEYCODE_BUTTON_1");





//currentControllerInfo.addKey("Left",  "Z",   0,  -1, 1f, 1f, BeebKeys.BBCKEY_Z, KeyEvent.KEYCODE_DPAD_LEFT);
//currentControllerInfo.addKey("Right", "X",   1,  -1, 1f, 1f, BeebKeys.BBCKEY_X, KeyEvent.KEYCODE_DPAD_RIGHT);
//currentControllerInfo.addKey("Up",    ":",  -2,  0, 1f, 1f, BeebKeys.BBCKEY_COLON, KeyEvent.KEYCODE_DPAD_UP);
//currentControllerInfo.addKey("Down",  "/",  -2,  1, 1f, 1f, BeebKeys.BBCKEY_SLASH, KeyEvent.KEYCODE_DPAD_DOWN);		
//currentControllerInfo.addKey("Fire", "Return",  -1,  0, 1f, 2f, BeebKeys.BBCKEY_ENTER, KeyEvent.KEYCODE_BUTTON_1);
//currentControllerInfo.addKey("<-",  "<-",   0,  0, .5f, .5f, BeebKeys.BBCKEY_ARROW_LEFT, KeyEvent.KEYCODE_BUTTON_7);
//currentControllerInfo.addKey("->", "->",   0.5f,  -0, .5f, .5f, BeebKeys.BBCKEY_ARROW_RIGHT, KeyEvent.KEYCODE_BUTTON_8);
//currentControllerInfo.addKey("[]",    "[]",  1,  0, .5f, .5f, BeebKeys.BBCKEY_SPACE, KeyEvent.KEYCODE_BUTTON_9);	

//		controller_ZX_4way.addKey("Left",  "Z",   0, -1, 1f, 1f, BeebKeys.BBCKEY_Z, KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_BUTTON_X);
//		controller_ZX_4way.addKey("Right", "X",   1, -1, 1f, 1f, BeebKeys.BBCKEY_X, KeyEvent.KEYCODE_DPAD_RIGHT, KeyEvent.KEYCODE_BACK);
//		controller_ZX_4way.addKey("Up",    ":",  -1,  0, 1f, 1f, BeebKeys.BBCKEY_COLON, KeyEvent.KEYCODE_DPAD_UP, KeyEvent.KEYCODE_BUTTON_Y);
//		controller_ZX_4way.addKey("Down",  "/",  -1,  1, 1f, 1f, BeebKeys.BBCKEY_SLASH, KeyEvent.KEYCODE_DPAD_DOWN, KeyEvent.KEYCODE_DPAD_CENTER);
//		
//		controller_ZX_5way.addKey("Left",  "Z",   0,  -1, 1f, 1f, BeebKeys.BBCKEY_Z, KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_BUTTON_X);
//		controller_ZX_5way.addKey("Right", "X",   1,  -1, 1f, 1f, BeebKeys.BBCKEY_X, KeyEvent.KEYCODE_DPAD_RIGHT, KeyEvent.KEYCODE_BACK);
//		controller_ZX_5way.addKey("Up",    ":",  -2,  0, 1f, 1f, BeebKeys.BBCKEY_COLON, KeyEvent.KEYCODE_DPAD_UP, KeyEvent.KEYCODE_BUTTON_Y);
//		controller_ZX_5way.addKey("Down",  "/",  -2,  1, 1f, 1f, BeebKeys.BBCKEY_SLASH, KeyEvent.KEYCODE_DPAD_DOWN, KeyEvent.KEYCODE_DPAD_CENTER);
//		controller_ZX_5way.addKey("Fire", "Return",  -1,  0, 1f, 2f, BeebKeys.BBCKEY_ENTER, KeyEvent.KEYCODE_BUTTON_1, KeyEvent.KEYCODE_BUTTON_R1);
//		
//		controller_RocketRaid_Game.addKey("Back",  "SPACE",   0,  -1, 1f, 1f, BeebKeys.BBCKEY_SPACE, KeyEvent.KEYCODE_DPAD_LEFT);
//		controller_RocketRaid_Game.addKey("Forwards", "SHIFT",   1,  -1, 1f, 1f, BeebKeys.BBCKEY_SHIFT, KeyEvent.KEYCODE_DPAD_RIGHT);
//		controller_RocketRaid_Game.addKey("Up",    "A",  -2,  0, 1f, 1f, BeebKeys.BBCKEY_A, KeyEvent.KEYCODE_DPAD_UP);
//		controller_RocketRaid_Game.addKey("Down",  "Z",  -2,  1, 1f, 1f, BeebKeys.BBCKEY_Z, KeyEvent.KEYCODE_DPAD_DOWN);
//		controller_RocketRaid_Game.addKey("Fire", "Return",  -1,  0, 1f, 1f, BeebKeys.BBCKEY_ENTER, KeyEvent.KEYCODE_DPAD_CENTER);
//		controller_RocketRaid_Game.addKey("Bomb", "Tab",  -1,  1, 1f, 1f, BeebKeys.BBCKEY_TAB, KeyEvent.KEYCODE_BACK);
//		
//		controller_Qman_Game.addKey("Up-Left",  "A",   0, 0, 1f, 1f, BeebKeys.BBCKEY_A, KeyEvent.KEYCODE_BUTTON_X);
//		controller_Qman_Game.addKey("Down-Left", "Z",   0, -1, 1f, 1f, BeebKeys.BBCKEY_Z, KeyEvent.KEYCODE_DPAD_DOWN);
//		controller_Qman_Game.addKey("Up-Right",    ":",  -1,  0, 1f, 1f, BeebKeys.BBCKEY_COLON, KeyEvent.KEYCODE_DPAD_RIGHT);
//		controller_Qman_Game.addKey("Down-Right",  "/",  -1,  1, 1f, 1f, BeebKeys.BBCKEY_SLASH, KeyEvent.KEYCODE_DPAD_CENTER);
//		
//		// Arcadians has 2 controllers and uses PC triggers to switch between them
////		controller_Arcadians_Menu.addKey("1","1",    0f,  0, 1.5f, 1.5f,  BeebKeys.BBCKEY_1);
////		controller_Arcadians_Menu.addKey("2","2",    1.5f,  0, 1.5f, 1.5f,  BeebKeys.BBCKEY_2);
////		controller_Arcadians_Menu.addTrigger((short) 0x3203, controller_Arcadians_Game);
//		controller_Arcadians_Menu.addKey("Left","Caps",    0f,  0, 1.25f, 1.25f,  BeebKeys.BBCKEY_CAPS, KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_BUTTON_X);
//		controller_Arcadians_Menu.addKey("Right","Ctrl",   1.25f,  0, 1.25f, 1.25f,  BeebKeys.BBCKEY_CTRL, KeyEvent.KEYCODE_DPAD_RIGHT, KeyEvent.KEYCODE_BACK);
//		controller_Arcadians_Menu.addKey("Fire","Return",    -1f,  0,   1f,   1.25f,  BeebKeys.BBCKEY_ENTER, KeyEvent.KEYCODE_BUTTON_1, KeyEvent.KEYCODE_BUTTON_R1);
////		controller_Arcadians_Game.addTrigger((short) 0x4d95, controller_Arcadians_Menu);
//
//		controller_CastleQuest_Game.addKey("Left", "Z",   0,  -1, 1f, 1f, BeebKeys.BBCKEY_Z, KeyEvent.KEYCODE_DPAD_LEFT);
//		controller_CastleQuest_Game.addKey("Right", "X",   1,  -1, 1f, 1f, BeebKeys.BBCKEY_X, KeyEvent.KEYCODE_DPAD_RIGHT);
//		controller_CastleQuest_Game.addKey("Up", ":",  -1,  0, 1f, 1f, BeebKeys.BBCKEY_COLON, KeyEvent.KEYCODE_DPAD_UP);
//		controller_CastleQuest_Game.addKey("Down", "/",  -1,  1, 1f, 1f, BeebKeys.BBCKEY_SLASH, KeyEvent.KEYCODE_DPAD_DOWN);
//		controller_CastleQuest_Game.addKey("Pick Up", "P",   0,  1, 1f, 1f, BeebKeys.BBCKEY_P, KeyEvent.KEYCODE_BUTTON_Y);
//		controller_CastleQuest_Game.addKey("Drop", "D",   1,  1, 1f, 1f, BeebKeys.BBCKEY_D, KeyEvent.KEYCODE_DPAD_CENTER);
//		controller_CastleQuest_Game.addKey("Jump", "Return",   2,  1,  1f, 1f, BeebKeys.BBCKEY_ENTER, KeyEvent.KEYCODE_BUTTON_L1, KeyEvent.KEYCODE_BUTTON_R1);
//		
//		controller_ChuckieEgg_Game.addKey("Left",  ",",      0,   -1,  1f, 1f, BeebKeys.BBCKEY_COMMA, KeyEvent.KEYCODE_DPAD_LEFT);
//		controller_ChuckieEgg_Game.addKey("Right", ".",      1,   -1,  1f, 1f, BeebKeys.BBCKEY_PERIOD, KeyEvent.KEYCODE_DPAD_RIGHT);
//		controller_ChuckieEgg_Game.addKey("Up",    "A",     -2,    0,  1f, 1f, BeebKeys.BBCKEY_A, KeyEvent.KEYCODE_DPAD_UP, KeyEvent.KEYCODE_BUTTON_Y);
//		controller_ChuckieEgg_Game.addKey("Down",  "Z",     -2,    1,  1f, 1f, BeebKeys.BBCKEY_Z,  KeyEvent.KEYCODE_DPAD_CENTER);
//		controller_ChuckieEgg_Game.addKey("Jump",  " ",     -1,    0,  1f, 2f, BeebKeys.BBCKEY_SPACE, KeyEvent.KEYCODE_BACK);
//		controller_ChuckieEgg_Game.addKey("S",     "S", -1.75f,   1f, .5f, .5f, BeebKeys.BBCKEY_S);
//		controller_ChuckieEgg_Game.addKey("1",     "1", -1.75f, 1.5f, .5f, .5f, BeebKeys.BBCKEY_1);
//		controller_ChuckieEgg_Game.useDPad = true;
//		
//		controller_ChuckieEgg_Game_Alt.addKey("Left",  ",",      0,   -1,  1f, 1f, BeebKeys.BBCKEY_COMMA, KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_BUTTON_X);
//		controller_ChuckieEgg_Game_Alt.addKey("Right", ".",      1,   -1,  1f, 1f, BeebKeys.BBCKEY_PERIOD, KeyEvent.KEYCODE_DPAD_RIGHT, KeyEvent.KEYCODE_BACK);
//		controller_ChuckieEgg_Game_Alt.addKey("Up",    "A",     -2,    0,  1f, 1f, BeebKeys.BBCKEY_A, KeyEvent.KEYCODE_DPAD_UP, KeyEvent.KEYCODE_BUTTON_Y);
//		controller_ChuckieEgg_Game_Alt.addKey("Down",  "Z",     -2,    1,  1f, 1f, BeebKeys.BBCKEY_Z,  KeyEvent.KEYCODE_DPAD_DOWN, KeyEvent.KEYCODE_DPAD_CENTER);
//		controller_ChuckieEgg_Game_Alt.addKey("Jump",  " ",     -1,    0,  1f, 2f, BeebKeys.BBCKEY_SPACE, KeyEvent.KEYCODE_BUTTON_L1, KeyEvent.KEYCODE_BUTTON_R1);
//		controller_ChuckieEgg_Game_Alt.addKey("S",     "S", -1.75f,   1f, .5f, .5f, BeebKeys.BBCKEY_S);
//		controller_ChuckieEgg_Game_Alt.addKey("1",     "1", -1.75f, 1.5f, .5f, .5f, BeebKeys.BBCKEY_1);
//		controller_ChuckieEgg_Game_Alt.useDPad = true;
//		
//		controller_DareDevilDennis_Game.addKey("Accel", "Shift",   0,  0, 1f, 2f, BeebKeys.BBCKEY_SHIFT, KeyEvent.KEYCODE_DPAD_RIGHT, KeyEvent.KEYCODE_BACK);
//		controller_DareDevilDennis_Game.addKey("Stop", "Return",   1,  0, 1f, 2f, BeebKeys.BBCKEY_ENTER, KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_BUTTON_X);
//		controller_DareDevilDennis_Game.addKey("Jump", "",  -2,  0, 2f, 2f, BeebKeys.BBCKEY_SPACE, KeyEvent.KEYCODE_BUTTON_L1, KeyEvent.KEYCODE_BUTTON_R1);
//		
//		controller_Imogen_Game.addKey("Left",  "Z",   0,  -1, 1f, 1f, BeebKeys.BBCKEY_Z, KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_BUTTON_X);
//		controller_Imogen_Game.addKey("Right", "X",   1,  -1, 1f, 1f, BeebKeys.BBCKEY_X, KeyEvent.KEYCODE_DPAD_RIGHT, KeyEvent.KEYCODE_BACK);
//		controller_Imogen_Game.addKey("Up",    ":",  -2,  0, 1f, 1f, BeebKeys.BBCKEY_COLON, KeyEvent.KEYCODE_DPAD_UP, KeyEvent.KEYCODE_BUTTON_Y);
//		controller_Imogen_Game.addKey("Down",  "/",  -2,  1, 1f, 1f, BeebKeys.BBCKEY_SLASH, KeyEvent.KEYCODE_DPAD_DOWN, KeyEvent.KEYCODE_DPAD_CENTER);
//		controller_Imogen_Game.addKey("Fire", "Return",  -1,  0, 1f, 2f, BeebKeys.BBCKEY_ENTER, KeyEvent.KEYCODE_BUTTON_L1, KeyEvent.KEYCODE_BUTTON_R1);
//		controller_Imogen_Game.addKey("<-",  "<-",   0,  0, .5f, .5f, BeebKeys.BBCKEY_ARROW_LEFT, KeyEvent.KEYCODE_BUTTON_Y);
//		controller_Imogen_Game.addKey("->", "->",   0.5f,  -0, .5f, .5f, BeebKeys.BBCKEY_ARROW_RIGHT, KeyEvent.KEYCODE_BACK);
//		controller_Imogen_Game.addKey("[]",    "[]",  1,  0, .5f, .5f, BeebKeys.BBCKEY_SPACE, KeyEvent.KEYCODE_DPAD_CENTER);
//		
//		controller_Imogen_Game.addKey("Left",  "Z",   0,  -1, 1f, 1f, BeebKeys.BBCKEY_Z, KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_BUTTON_X);
//		controller_Imogen_Game.addKey("Right", "X",   1,  -1, 1f, 1f, BeebKeys.BBCKEY_X, KeyEvent.KEYCODE_DPAD_RIGHT, KeyEvent.KEYCODE_BACK);
//		controller_Imogen_Game.addKey("Up",    ":",  -2,  0, 1f, 1f, BeebKeys.BBCKEY_COLON, KeyEvent.KEYCODE_DPAD_UP, KeyEvent.KEYCODE_BUTTON_Y);
//		controller_Imogen_Game.addKey("Down",  "/",  -2,  1, 1f, 1f, BeebKeys.BBCKEY_SLASH, KeyEvent.KEYCODE_DPAD_DOWN, KeyEvent.KEYCODE_DPAD_CENTER);
//		controller_Imogen_Game.addKey("Fire", "Return",  -1,  0, 1f, 2f, BeebKeys.BBCKEY_ENTER, KeyEvent.KEYCODE_BUTTON_1);
//		controller_Imogen_Game.addKey("<-",  "<-",   0,  0, .5f, .5f, BeebKeys.BBCKEY_ARROW_LEFT, KeyEvent.KEYCODE_BUTTON_7);
//		controller_Imogen_Game.addKey("->", "->",   0.5f,  -0, .5f, .5f, BeebKeys.BBCKEY_ARROW_RIGHT, KeyEvent.KEYCODE_BUTTON_8);
//		controller_Imogen_Game.addKey("[]",    "[]",  1,  0, .5f, .5f, BeebKeys.BBCKEY_SPACE, KeyEvent.KEYCODE_BUTTON_2);
//		
//		controller_StrykersRun_Game.addKey("Left", "ArrowLeft", 0, 0, 1f, 1f, BeebKeys.BBCKEY_ARROW_LEFT, KeyEvent.KEYCODE_DPAD_LEFT);
//		controller_StrykersRun_Game.addKey("Right", "ArrowRight", 0, 0, 1f, 1f, BeebKeys.BBCKEY_ARROW_RIGHT, KeyEvent.KEYCODE_DPAD_RIGHT);
//		controller_StrykersRun_Game.addKey("Jump", "A",  -1,  0, 1f, 2f, BeebKeys.BBCKEY_A, KeyEvent.KEYCODE_BUTTON_1);
//		controller_StrykersRun_Game.addKey("Fire", "Shift",  -1,  0, 1f, 2f, BeebKeys.BBCKEY_SHIFT, KeyEvent.KEYCODE_BUTTON_2);
//		controller_StrykersRun_Game.addKey("Fly", "Ctrl",  -1,  0, 1f, 2f, BeebKeys.BBCKEY_CTRL, KeyEvent.KEYCODE_BUTTON_3);
//		controller_StrykersRun_Game.addKey("Duck", "Z",  -1,  0, 1f, 2f, BeebKeys.BBCKEY_Z, KeyEvent.KEYCODE_BUTTON_8);
//		controller_StrykersRun_Game.addKey("Nade", "Del",  -1,  0, 1f, 2f, BeebKeys.BBCKEY_DELETE, KeyEvent.KEYCODE_BUTTON_7);
//		
////		// 19, 20, 21, 22 are dpad
////		// 188, 191, 189, 192, 190, 193, 194, 195, 196 are buttons
////		  public static final int KEYCODE_DPAD_UP = 19;
////		  public static final int KEYCODE_DPAD_DOWN = 20;
////		  public static final int KEYCODE_DPAD_LEFT = 21;
////		  public static final int KEYCODE_DPAD_RIGHT = 22;
////		  public static final int KEYCODE_BUTTON_1 = 188;  A
////		  public static final int KEYCODE_BUTTON_2 = 189;  B
////		  public static final int KEYCODE_BUTTON_3 = 190;  C
////		  public static final int KEYCODE_BUTTON_4 = 191;  X
////		  public static final int KEYCODE_BUTTON_5 = 192;  Y
////		  public static final int KEYCODE_BUTTON_6 = 193;  Z
////		  public static final int KEYCODE_BUTTON_7 = 194;  LTOP
////		  public static final int KEYCODE_BUTTON_8 = 195;  RTOP
////		  public static final int KEYCODE_BUTTON_9 = 196;  START
//		
//		controller_Thrust_Game.addKey("Left","Caps",    0f,  -1, 1f, 1f,  BeebKeys.BBCKEY_CAPS, KeyEvent.KEYCODE_DPAD_LEFT);
//		controller_Thrust_Game.addKey("Right","Ctrl",   1f,  -1, 1f, 1f,  BeebKeys.BBCKEY_CTRL, KeyEvent.KEYCODE_DPAD_RIGHT);
//		controller_Thrust_Game.addKey("Thrust","Shift", -1f,  0, 1f, 2f,  BeebKeys.BBCKEY_SHIFT, KeyEvent.KEYCODE_BUTTON_1);
//		controller_Thrust_Game.addKey("Fire","Return", -2f,  0, 1f, 1f,  BeebKeys.BBCKEY_ENTER, KeyEvent.KEYCODE_BUTTON_7);
//		controller_Thrust_Game.addKey("Shield","", -2f,  1, 1f, 1f,  BeebKeys.BBCKEY_SPACE, KeyEvent.KEYCODE_BUTTON_8);
//		
//		controller_Planetoid_Game.addKey("Up",    "A",  -2,  0, 1f, 1f, BeebKeys.BBCKEY_A, KeyEvent.KEYCODE_DPAD_UP);
//		controller_Planetoid_Game.addKey("Down",  "Z",  -2,  1, 1f, 1f, BeebKeys.BBCKEY_Z, KeyEvent.KEYCODE_DPAD_DOWN);
//		controller_Planetoid_Game.addKey("Reverse",  "SPACE",   0,  -1, 1f, 1f, BeebKeys.BBCKEY_SPACE, KeyEvent.KEYCODE_DPAD_LEFT);
//		controller_Planetoid_Game.addKey("Forwards", "SHIFT",   1,  -1, 1f, 1f, BeebKeys.BBCKEY_SHIFT, KeyEvent.KEYCODE_DPAD_RIGHT);
//		controller_Planetoid_Game.addKey("Fire", "Return",  -1,  0, 1f, 1f, BeebKeys.BBCKEY_ENTER, KeyEvent.KEYCODE_DPAD_CENTER);
//		controller_Planetoid_Game.addKey("Bomb", "Tab",  -1,  1, 1f, 1f, BeebKeys.BBCKEY_TAB, KeyEvent.KEYCODE_BACK);
//
//		controller_Elite_Game.addKey("Left",  "<",   0,  0.8f, .75f, .75f, BeebKeys.BBCKEY_LESS_THAN, KeyEvent.KEYCODE_DPAD_LEFT);
//		controller_Elite_Game.addKey("Right", ">",   1.5f,  0.8f, .75f, .75f, BeebKeys.BBCKEY_MORE_THAN, KeyEvent.KEYCODE_DPAD_RIGHT);
//		controller_Elite_Game.addKey("Up",    "S",  .75f,  .5f, .75f, .75f, BeebKeys.BBCKEY_S, KeyEvent.KEYCODE_DPAD_UP);
//		controller_Elite_Game.addKey("Down",  "X",  .75f,  1.25f, .75f, .75f, BeebKeys.BBCKEY_X, KeyEvent.KEYCODE_DPAD_DOWN);
//		controller_Elite_Game.addKey("Fire",  "A",  -.75f,  0, .75f, 2f, BeebKeys.BBCKEY_A, KeyEvent.KEYCODE_DPAD_CENTER);
//		controller_Elite_Game.addKey("Acc.",  "SPC",-1.35f,  0, .5f, 1f, BeebKeys.BBCKEY_SPACE, KeyEvent.KEYCODE_DPAD_CENTER);
//		controller_Elite_Game.addKey("Dec.",  "?",  -1.35f,  1, .5f, 1f, BeebKeys.BBCKEY_QUESTIONMARK, KeyEvent.KEYCODE_DPAD_CENTER);
//		controller_Elite_Game.addKey("F0",    "Launch",    0f,  0, .5f, .5f, BeebKeys.BBCKEY_F0);
//		controller_Elite_Game.addKey("F1",    "Rear",     .5f,  0, .5f, .5f, BeebKeys.BBCKEY_F1);
//		controller_Elite_Game.addKey("F2",    "Left",      1f,  0, .5f, .5f, BeebKeys.BBCKEY_F2);
//		controller_Elite_Game.addKey("F3",    "Right",   1.5f,  0, .5f, .5f, BeebKeys.BBCKEY_F3);
//		
//		controller_Zalaga_Game.addKey("Left","Caps",    0f,  0, 1.25f, 1.25f,  BeebKeys.BBCKEY_CAPS, KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_BUTTON_X);
//		controller_Zalaga_Game.addKey("Right","Ctrl",   1.25f,  0, 1.25f, 1.25f,  BeebKeys.BBCKEY_CTRL, KeyEvent.KEYCODE_DPAD_RIGHT, KeyEvent.KEYCODE_BACK);
//		controller_Zalaga_Game.addKey("Fire","Return",    -1f,  0,   1f,   1.25f,  BeebKeys.BBCKEY_ENTER, KeyEvent.KEYCODE_BUTTON_1, KeyEvent.KEYCODE_BUTTON_R1);
//	}
//	
//	//
//	// This section maps disk keys to built-in ControllerInfos
//	//
//	static Map<String, ControllerInfo> controllersForKnownDisks = new HashMap<String, ControllerInfo>();
//	static {
//		controllersForKnownDisks.put("arcadians", 			controller_Arcadians_Menu);
//		controllersForKnownDisks.put("castle_quest", 		controller_CastleQuest_Game);
//		//controllersForKnownDisks.put("chuckie_egg", 		controller_ChuckieEgg_Game);
//		controllersForKnownDisks.put("chuckie_egg", 		controller_ChuckieEgg_Game_Alt);
//		controllersForKnownDisks.put("dare_devil_dennis", 	controller_DareDevilDennis_Game);
//		controllersForKnownDisks.put("firetrack",			controller_ZX_4way);		
//		controllersForKnownDisks.put("gimpo", 				controller_ZX_4way);
//		controllersForKnownDisks.put("hyper_viper", 		controller_ZX_4way);
//		controllersForKnownDisks.put("elite", 				controller_Elite_Game);
//		controllersForKnownDisks.put("imogen", 				controller_Imogen_Game);
//		controllersForKnownDisks.put("qman", 				controller_Qman_Game);
//		controllersForKnownDisks.put("planetoid", 			controller_Planetoid_Game);
//		controllersForKnownDisks.put("repton1", 			controller_ZX_4way);
//		controllersForKnownDisks.put("repton2", 			controller_ZX_4way);
//		controllersForKnownDisks.put("repton3", 			controller_ZX_4way);
//		controllersForKnownDisks.put("repton_infinity", 	controller_ZX_4way);
//		controllersForKnownDisks.put("repton_thru_time", 	controller_ZX_4way);
//		controllersForKnownDisks.put("ripton", 				controller_ZX_4way);
//		controllersForKnownDisks.put("rocket_raid", 		controller_RocketRaid_Game);
//		controllersForKnownDisks.put("snapper", 			controller_ZX_4way);
//		controllersForKnownDisks.put("thrust", 				controller_Thrust_Game);
//		controllersForKnownDisks.put("zalaga", 				controller_Zalaga_Game);
//		controllersForKnownDisks.put("strykers_run", 		controller_StrykersRun_Game);
//	}
//}
