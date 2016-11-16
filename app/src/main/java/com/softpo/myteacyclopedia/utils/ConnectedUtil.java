package com.softpo.myteacyclopedia.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectedUtil {
	public  static boolean isConnected(Context context){
		
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
		
		if(activeNetworkInfo==null){
			return false;
		}
		
		switch (activeNetworkInfo.getType()) {
		
		case ConnectivityManager.TYPE_WIFI:
			
			return true;
		case ConnectivityManager.TYPE_MOBILE:
			
			return true;

		default:
			break;
		}
		
		return false;
		
	}
}
