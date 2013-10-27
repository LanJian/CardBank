package com.jackhxs.cardbank;

import android.content.Context;
import android.content.res.Configuration;
import android.widget.Toast;

public class Util {
	private static String wifiError = "Network error. Please make sure you have wifi enabled.";
	
	public static boolean isTablet(Context context) {
	    return (context.getResources().getConfiguration().screenLayout
	            & Configuration.SCREENLAYOUT_SIZE_MASK)
	            >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}
	
	public static void showWifiErrorToast(Context context) {
		Toast toast = Toast.makeText(context, wifiError, Toast.LENGTH_LONG);
		toast.show();
	}
	
	public static void showErrorToast(Context context, String errorMsg) {
		Toast toast = Toast.makeText(context, errorMsg, Toast.LENGTH_LONG);
		toast.show();
	}
}
