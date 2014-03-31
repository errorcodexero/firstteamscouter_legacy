package com.wilsonvillerobotics.firstteamscouter.utilities;

import java.io.File;
import java.util.Hashtable;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.widget.Button;

public class FTSUtilities {

	public static Boolean DEBUG = true;
	public static String alliancePositions[] = {"Red1","Red2","Red3","Blue1","Blue2","Blue3",};
	
	private static Hashtable<Integer, String> testTeamData = new Hashtable<Integer, String>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{ 
			put(1425, "Error Code Xero");
			put(1520, "Flaming Chickens");
			put(2929, "JagBots");
			put(1114, "Derp");
			put(500, "Spots");
			put(600, "Frozen Poultry");
			put(700, "Jumping Frogs");
			put(800, "Droids");
			put(900, "Sad Pandas");
			put(1000, "Grommets");
		}
	};
	
	public static void printToConsole(String message) {
		if(message.isEmpty() || !DEBUG) return;
		int len = 50 - message.length();
		String spacer = "";
		for(int i = 0; i < len/2;i++) {
			spacer += " ";
		}
		System.out.println("");
		System.out.println("**************************************************");
		System.out.println(spacer + message);
		System.out.println("**************************************************");
		System.out.println("");
	}
	
	public static Set<Integer> getTestTeamNumbers() {
		Set<Integer> teamNums = testTeamData.keySet();
		return teamNums;
	}
	
	public static String getTeamName(int tNum) {
		return testTeamData.get(tNum);
	}
	
	public static boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	/* Checks if external storage is available to at least read */
	public static boolean isExternalStorageReadable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	public static boolean initializeBluetooth(Activity activity) {
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
		    // Device does not support Bluetooth
			return false;
		}
		
		int REQUEST_ENABLE_BT = 1;
		
		if (!mBluetoothAdapter.isEnabled()) {
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
		
		
		return false;
	}
	
	public static Uri sendFileByBluetooth(Context context, String filePath, String btDeviceAddress) {
		//BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
		
		//BluetoothDevice device = btAdapter.getRemoteDevice(btDeviceAddress);
		//String filePath = Environment.getExternalStorageDirectory().toString() + "/file.jpg";

		ContentValues values = new ContentValues();
		values.put(BluetoothShare.URI, Uri.fromFile(new File(filePath)).toString());
		values.put(BluetoothShare.DESTINATION, btDeviceAddress); // device.getAddress());
		values.put(BluetoothShare.DIRECTION, BluetoothShare.DIRECTION_OUTBOUND);
		Long ts = System.currentTimeMillis();
		values.put(BluetoothShare.TIMESTAMP, ts);
		Uri contentUri = context.getContentResolver().insert(BluetoothShare.CONTENT_URI, values);
		return contentUri;
	}
	
	public static void setButtonStyles(Hashtable<Integer, Button> buttonHash, Boolean undo) {
		int color = (undo) ? Color.RED : Color.BLUE;
		
		for(Integer bID : buttonHash.keySet()) {
			buttonHash.get(bID).setTextColor(color);
		}
	}
}
