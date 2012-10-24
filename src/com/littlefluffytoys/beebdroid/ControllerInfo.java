package com.littlefluffytoys.beebdroid;

import java.util.ArrayList;
//import java.util.List;
import common.Utils;

public class ControllerInfo {
	public String name;
	public ArrayList<KeyInfo> keyinfos = new ArrayList<KeyInfo>();
	//public List<TriggerAction> triggers =  new ArrayList<TriggerAction>();
	public boolean useDPad;
	public int androidKeycode;
	public class KeyInfo  {
		String label;
		int labelIconId;
		String keylabel;
		int scancode;
		int androidKeyCode1; // DCH added
		int androidKeyCode2; // DCH added		
		float xc,yc, width, height;
    }
	public static class TriggerAction {
		short pc_trigger;
		public TriggerAction(short pc_trigger) {
			this.pc_trigger = pc_trigger;
		}
	}
	public static class TriggerActionSetController extends TriggerAction {
		public TriggerActionSetController(short pc_trigger, ControllerInfo controllerInfo) {
			super(pc_trigger);
			this.controllerInfo = controllerInfo;
		}
		ControllerInfo controllerInfo;
	}
//	public void addTrigger(short pc, ControllerInfo switchToController) {
//		TriggerActionSetController trigger = new TriggerActionSetController(pc, switchToController);
//		triggers.add(trigger);
//	}
	public void addKey(String label, String keylabel, float xc, float yc, float width, float height, int scancode) {
		addKey(label, keylabel, xc, yc, width, height, scancode, 0);
	}
	public void addKey(String label, String keylabel, float xc, float yc, float width, float height, int scancode, int androidKeycode) {
		addKey(label, keylabel, xc, yc, width, height, scancode, androidKeycode, 0);
	}
	public void addKey(String label, String keyLabel, float xc, float yc, float width, float height, int scancode, int androidKeycode, int androidKeycode2) {
		Utils.writeLog("addControllerKey [" + label + "] [" + keyLabel + "] [" + xc + "] [" + yc + "] [" + width + "] [" + height + "] [" + scancode + "] [" + androidKeycode + "] [" + androidKeycode2 + "]");
		KeyInfo key = new KeyInfo();
		key.label = label;
		key.keylabel = keyLabel;
		key.xc = xc;
		key.yc = yc;
		key.width = width;
		key.height = height;
		key.scancode = scancode;
		key.androidKeyCode1 = androidKeycode;
		key.androidKeyCode2 = androidKeycode2;		
		keyinfos.add(key);
	}
}
