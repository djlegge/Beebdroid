package com.littlefluffytoys.beebdroid;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import common.Packageable;
import common.Utils;

public class DiskInfo extends Packageable {
	
	String key;
	String title;
	String publisher;
	String coverUrl;
	String diskUrl;
	String bootCmd;

	public DiskInfo() {
	}
	
	public DiskInfo(JSONObject obj) throws JSONException {
		key = obj.getString("k");
		title = obj.getString("t");
		publisher = obj.getString("pub");
		diskUrl = Utils.safeGetJsonString(obj, "disk");
		coverUrl = Utils.safeGetJsonString(obj, "cover");
		bootCmd = Utils.safeGetJsonString(obj, "boot");
	}
	
	@Override
	public void readFromPackage(PackageInputStream in) throws IOException {
		key = in.readString();
		title = in.readString();
		publisher = in.readString();
		coverUrl = in.readString();
		diskUrl = in.readString();
		bootCmd = in.readString();
	}

	@Override
	public void writeToPackage(PackageOutputStream out) throws IOException {
		out.writeString(key);
		out.writeString(title);
		out.writeString(publisher);
		out.writeString(coverUrl);
		out.writeString(diskUrl);
		out.writeString(bootCmd);
	}	
		
}
