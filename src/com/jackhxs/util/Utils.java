package com.jackhxs.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.ViewGroup.LayoutParams;

public class Utils {

	public static float getDensity(Activity mActivity) {
		// TODO Auto-generated method stub
		float density  = mActivity.getResources().getDisplayMetrics().density;
	    
	    return density;
	}


	public static void ConfigurePagerSize(ViewPager mPager, Activity mActivity) {
		
		Display display = mActivity.getWindowManager().getDefaultDisplay();
	    DisplayMetrics outMetrics = new DisplayMetrics ();
	    display.getMetrics(outMetrics);

	    float density  = getDensity(mActivity);
	    float dpWidth  = outMetrics.widthPixels / density;
		
	    int width = (int) ((dpWidth - 32 ) * density);
	    int height = (int) (width * 0.541); // width is already in dp units
	    
	    LayoutParams mLayoutParams = mPager.getLayoutParams();
        
	    mLayoutParams.width = width;
        mLayoutParams.height = height;
        
	    mPager.setLayoutParams(mLayoutParams);
	}
	
	
	public static List<String> getEmails (Context mContext) {
		
		final Account[] accounts = AccountManager.get(mContext).getAccounts();
		final Set<String> emailSet = new HashSet<String>();
		
		for (Account account : accounts) {
			if (Patterns.EMAIL_ADDRESS.matcher(account.name).matches()) {
		    	emailSet.add(account.name);
		    }
		}
		return new ArrayList<String>(emailSet);
	}
}
