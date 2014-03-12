package com.jackhxs.util;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
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
}
