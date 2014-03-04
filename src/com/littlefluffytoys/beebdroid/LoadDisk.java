package com.littlefluffytoys.beebdroid;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
//import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONArray;
import org.json.JSONException;

import common.ImageCache;
import common.Network;
import common.Utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
//import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
//import android.view.ContextMenu;
//import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.ViewFlipper;



public class LoadDisk extends Activity implements OnTabChangeListener {
	//private static final String TAG="LoadDisk";
	
	private static final String SERVER_ROOT = "http://www.stairwaytohell.com/";
	//private static final String SERVER_ROOT = "http://www.littlefluffytoys.com/beebdroid/";
	//private static final int ID_DELETE = 1;
	public static final int ID_RESULT_LOADDISK = 101;
	public static final int ID_RESULT_SAVE = 102;
	public static final int ID_RESULT_RESTORE = 103;
	public static final int ID_RESULT_SDCARDLOAD = 104;
	public final static String EXTRA_BBC_KEY_CODE = "com.littlefluffytoys.beebdroid.EXTRA_BBC_KEY_CODE";
	public final static String EXTRA_GAME_FILENAME = "com.littlefluffytoys.beebdroid.EXTRA_GAME_FILENAME";
	
	protected TabHost mTabHost;
	protected ViewFlipper viewFlipper;
	
	/*
	 * ACTIVITY MANAGEMENT (i.e. functions generally called by the OS)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loaddisk);

        
        // Load in a list of the disks in a specific directory
                
        try
        {
            sdCardDisks = new ArrayList<SDcardDisk>();      
            String fullPath = Utils.SDrootPathSlash + "disks/";
            File root = new File(fullPath);
            File[] files = root.listFiles();
            //fileList.clear();
            for (File file : files)
            {
            	Log.i("Beebdroid", "LoadDisk found [" + file.getName() + "]");
            	sdCardDisks.add(new SDcardDisk(file.getName()));
            }
        }
    	catch (Exception e)
		{
			Log.e("Utils", e.toString());
		}
        
        
		try
		{
			configKeys = new ArrayList<ConfigKey>();
			Field[] fields = BeebKeys.class.getDeclaredFields();
			for(Field f : fields) 
			{
				ConfigKey ck = new ConfigKey();
				ck.label = f.getName();
				configKeys.add(ck);
			}
		} 
		catch (Exception e)
		{
			Log.e("Utils", e.toString());
		}
    	
    	
        
        
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();
        mTabHost.setOnTabChangedListener(this);
        setupTab("Installed", installedAdapter, R.layout.listview, onInstalledItemClickListener);
        setupTab("Online", onlineAdapter, R.layout.listview_online, onOnlineItemClickListener);
        setupTab("Saved", savedAdapter, R.layout.listview, onSaveItemClickListener);
        setupTab("SD Card", sdCardAdapter, R.layout.listview, onSdCardItemClickListener);
        setupTab("Setup Keys", configKeysAdapter, R.layout.listview, onConfigKeysItemClickListener);
        int tab = getIntent().getIntExtra("startTab", 0);
        mTabHost.setCurrentTab(tab);
        if (SavedGameInfo.savedGames == null) {
        	SavedGameInfo.init(this);
        }
     
	}

	
	
	private void setupTab(final String tag, final BaseAdapter adapter, final int layoutId, final OnItemClickListener onItemClickListener) {
	    View view = getLayoutInflater().inflate(R.layout.tabs_bg, null);
	    TextView tv = (TextView) view.findViewById(R.id.tabsText);
	    tv.setText(tag);
        TabSpec spec = mTabHost.newTabSpec(tag);
        spec.setIndicator(tag);
        spec.setContent(new TabHost.TabContentFactory() {
	        public View createTabContent(String tag) {
	        	View view = getLayoutInflater().inflate(layoutId, null);
	        	ListView list = (ListView)view.findViewById(R.id.list);
	        	list.setOnItemClickListener(onItemClickListener);
	        	list.setAdapter(adapter);
	        	View emptyView = view.findViewById(R.id.emptyView);
	        	if (emptyView != null) {
	        		list.setEmptyView(emptyView);
	        	}
	        	return view;
	        }
	    });
	    mTabHost.addTab(spec);
	}

	
	
	List<ConfigKey> configKeys = new ArrayList<ConfigKey>();
	BaseAdapter configKeysAdapter = new BaseAdapter()
	{
		@Override
		public int getCount()
		{
			return configKeys.size();
		}
		@Override
		public Object getItem(int position)
		{
			return null;
		}
		@Override
		public long getItemId(int position)
		{
			return position;
		}
		@Override
		public View getView(int position, View view, ViewGroup parent)
		{
			ConfigKey configKeyInfo = configKeys.get(position);
			if (view == null) {
				view = getLayoutInflater().inflate(R.layout.listitem_diskinfo, null);
			}
			view.setTag(configKeyInfo.label);
			Utils.setText(view, R.id.title, configKeyInfo.label);
			return view;
		}
	};
	
	
	
	
	List<SDcardDisk> sdCardDisks = new ArrayList<SDcardDisk>();
	BaseAdapter sdCardAdapter = new BaseAdapter()
	{
		@Override
		public int getCount()
		{
			return sdCardDisks.size();
		}
		@Override
		public Object getItem(int position)
		{
			return null;
		}
		@Override
		public long getItemId(int position)
		{
			return position;
		}
		@Override
		public View getView(int position, View view, ViewGroup parent)
		{
			SDcardDisk sdCardInfo = sdCardDisks.get(position);
			if (view == null) {
				view = getLayoutInflater().inflate(R.layout.listitem_diskinfo, null);
			}
			view.setTag(sdCardInfo.filename);
			Utils.setText(view, R.id.title, sdCardInfo.filename);
			return view;
		}
	};
	
	
	

	public static DiskInfo selectedDisk;
	//public static SDcardDisk selectedSDcardDisk;
	/*
	 * INSTALLED DISKS ADAPTER
	 */
	BaseAdapter installedAdapter = new BaseAdapter() {
		
		@Override
		public int getCount() {
			return InstalledDisks.getCount();
		}
	
		@Override
		public Object getItem(int position) {
			return null;
		}
	
		@Override
		public long getItemId(int position) {
			return position;
		}
	
		@Override
		public View getView(int position, View view, ViewGroup parent) {
			DiskInfo diskInfo = InstalledDisks.getByIndex(position);
			if (view == null) {
				view = getLayoutInflater().inflate(R.layout.listitem_diskinfo, null);
			}
			view.setTag(diskInfo);
			ImageCache.getImage(view, R.id.image, diskInfo.coverUrl);
			Utils.setText(view, R.id.title, diskInfo.title);
			Utils.setText(view, R.id.subtitle, diskInfo.publisher);			
			return view;
		}
		
	};
	
	
	
	

	/*
	 * ONLINE ADAPTER
	 */
	List<DiskInfo> onlineDisks = new ArrayList<DiskInfo>();
	BaseAdapter onlineAdapter = new BaseAdapter() {
		
		@Override
		public int getCount() {
			return onlineDisks.size();
		}
	
		@Override
		public Object getItem(int position) {
			return null;
		}
	
		@Override
		public long getItemId(int position) {
			return position;
		}
	
		@Override
		public View getView(int position, View view, ViewGroup parent) {
			DiskInfo diskInfo = onlineDisks.get(position);
			if (view == null) {
				view = getLayoutInflater().inflate(R.layout.listitem_diskinfo, null);
			}
			view.setTag(diskInfo);
			ImageCache.getImage(view, R.id.image, diskInfo.coverUrl);
			Utils.setText(view, R.id.title, diskInfo.title);
			Utils.setText(view, R.id.subtitle, diskInfo.publisher);
			Utils.setVisibility(view, R.id.installed, (InstalledDisks.getByKey(diskInfo.key)==null)?View.GONE:View.VISIBLE);
			return view;
		}
		
	};
	
	
	/*
	 * SAVED GAMES ADAPTER
	 */
	BaseAdapter savedAdapter = new BaseAdapter() {
		
		@Override
		public int getCount() {
			return SavedGameInfo.savedGames.size() + 1;
		}
		@Override
		public Object getItem(int position) {
			return null;
		}
	
		@Override
		public long getItemId(int position) {
			return position;
		}
	
		@Override
		public View getView(int position, View view, ViewGroup parent) {
			if (position == 0) {
				return getLayoutInflater().inflate(R.layout.listitem_add_new_saved_game, null);
			}
			position--;
			SavedGameInfo info = SavedGameInfo.savedGames.get(position);
			if (view == null || view.getTag()==null) {
				view = getLayoutInflater().inflate(R.layout.listitem_savedgame, null);
			}
			view.setTag(position);
			
			ImageView img = (ImageView)view.findViewById(R.id.image);
			img.setBackgroundDrawable(new BitmapDrawable(info.thumbnail));
			//img.setImageBitmap(info.thumbnail);
			
			Utils.setText(view, R.id.title, (info.diskInfo==null)? "No disk" : info.diskInfo.title);
			Utils.setText(view, R.id.age, Utils.age(info.timestamp) + " ");
	
			// Expand load/save buttons area
			View buttons = view.findViewById(R.id.buttons);
			if (info.equals(SavedGameInfo.current)) {
				buttons.setVisibility(View.VISIBLE);
				view.setBackgroundColor(0xffd0d0ff);
				buttons.findViewById(R.id.btnRestore).setTag(position);
				buttons.findViewById(R.id.btnOverwrite).setTag(position);
			}
			else {
				buttons.setVisibility(View.GONE);	
				view.setBackgroundDrawable(null);
			}
			return view;
		}
		
	};
	

	
	
	@Override
    public void onTabChanged(String tabId) {
		if (tabId.equalsIgnoreCase("Online")) {
			if (downloadTask == null) {
				refreshOnlineList();
			}
		}
    }

	
	OnItemClickListener onInstalledItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			setResult(ID_RESULT_LOADDISK);
			selectedDisk = (DiskInfo)view.getTag();
			finish();		
		}
	};
	
	
	OnItemClickListener onConfigKeysItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position, long id)
		{
			String bbcKeyCode = (String) view.getTag();
			Toast.makeText(LoadDisk.this, "Edit key [" + bbcKeyCode + "]", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(LoadDisk.this, EditGameKeyActivity.class);
			intent.putExtra(EXTRA_BBC_KEY_CODE, bbcKeyCode);
			intent.putExtra(EXTRA_GAME_FILENAME, Beebdroid.keyConfigFilename);
			startActivity(intent);
		}
	};
	
	
	
	public static String selectedSDcardDiskFilename = "";
	OnItemClickListener onSdCardItemClickListener = new OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position, long id)
		{
			// TODO Auto-generated method stub
			setResult(ID_RESULT_SDCARDLOAD);
			//selectedSDcardDisk = (SDcardDisk)view.getTag();
			selectedSDcardDiskFilename = (String) view.getTag();
			//Toast.makeText(LoadDisk.this, "Load SD [" + selectedSDcardDiskFilename + "]", Toast.LENGTH_SHORT).show();
			finish();			
		}
	};
	
	
	OnItemClickListener onOnlineItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			DiskInfo onlineDiskInfo = (DiskInfo)view.getTag();
			if (null != InstalledDisks.getByKey(onlineDiskInfo.key)) {
				Toast.makeText(LoadDisk.this, "Already installed", Toast.LENGTH_SHORT).show();
				return;
			}
			startDownload(onlineDiskInfo);
		}
	};
	OnItemClickListener onSaveItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (position == 0) {
				doSave(-1);
				return;
			}
			doRestore(position - 1);		
		}
	};
	
	private void startDownload(final DiskInfo onlineDiskInfo) {
		final File tmpFile = new File(getFilesDir(), "tmp.bin");
		Network.DownloadBinaryTask task = new Network.DownloadBinaryTask(tmpFile.getAbsolutePath(), false) {			
			@Override
			protected HttpUriRequest getHttpRequest() {
				return new HttpGet(onlineDiskInfo.diskUrl);
			}
			
			@Override
			protected void onDownloadComplete() {
				File targetFile = new File(getFilesDir(), onlineDiskInfo.key);
				if (contentType.equals("application/zip")) {
					try {
						Utils.unzip(tmpFile, targetFile, true);
					} catch (IOException e) {
						Toast.makeText(LoadDisk.this, "Error processing zipped disk image: " + e.toString(), Toast.LENGTH_SHORT).show();
						return;
					}
				}
				else {
					tmpFile.renameTo(targetFile);
				}
				Toast.makeText(LoadDisk.this, "Installed OK!", Toast.LENGTH_SHORT).show();
				setResult(ID_RESULT_LOADDISK);
				selectedDisk = InstalledDisks.add(onlineDiskInfo);
				finish();		
			}
			@Override
			protected void onError(String errorText) {
				Toast.makeText(LoadDisk.this, errorText, Toast.LENGTH_SHORT).show();
			}
			
		};
		task.execute();
	}
	
	//
	// Get the list of online disks
	//
	Network.DownloadJsonTask downloadTask;
	private void refreshOnlineList() {
		downloadTask = new Network.DownloadJsonTask() {
			@Override
			protected HttpUriRequest getHttpRequest() {
				return new HttpGet(SERVER_ROOT + "beebdroid.json");
			}
			
			@Override
			protected void onDownloadJsonComplete(Object object) throws JSONException {
				JSONArray a = (JSONArray)object;
				onlineDisks.clear();
				for (int i=0 ; i<a.length() ; i++) {
					DiskInfo diskInfo = new DiskInfo(a.getJSONObject(i));
					if (TextUtils.isEmpty(diskInfo.coverUrl)) {
						diskInfo.coverUrl = SERVER_ROOT + diskInfo.key + ".jpg";
					}
					if (TextUtils.isEmpty(diskInfo.diskUrl)) {
						diskInfo.diskUrl = SERVER_ROOT + diskInfo.key + ".zip";
					}
					onlineDisks.add(diskInfo);
				}
				onlineAdapter.notifyDataSetChanged();
			}
			@Override
			protected void onError(String errorText) {
				Toast.makeText(LoadDisk.this, errorText, Toast.LENGTH_SHORT).show();
			}
		};
		downloadTask.execute();
	}
	
	//
	// Saved states
	//
	/*@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		menu.add(0, ID_DELETE, 0, "Delete");
	}
	@Override
	public boolean onContextItemSelected (MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		SavedGameInfo.delete(this, info.position-1);
		adapter.notifyDataSetChanged();
		return true;
	}*/
	
	public void onOverwriteClicked(View view) {
		doSave((Integer)view.getTag());
	}
	public void onRestoreClicked(View view) {
		doRestore((Integer)view.getTag());
	}
	private void doSave(int index) {
		Intent data = new Intent();
		data.putExtra("index", index);
		setResult(ID_RESULT_SAVE, data);
		finish();
	}
	private void doRestore(int index) {
		Intent data = new Intent();
		data.putExtra("index", index);
		setResult(ID_RESULT_RESTORE, data);
		finish();
	}	
}