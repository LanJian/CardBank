package com.jackhxs.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.jackhxs.cardbank.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LruCache;
import android.util.TypedValue;
import android.view.Display;

import com.jackhxs.data.template.TemplateConfig_old;

public class ImageUtil {
	final static int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
	final static int cacheSize = maxMemory / 8;
	
	private static LruCache<String, Bitmap> mMemoryCache =
		new LruCache<String, Bitmap>(cacheSize) {
        @Override
        protected int sizeOf(String key, Bitmap bitmap) {
            // The cache size will be measured in kilobytes rather than
            // number of items.
            return bitmap.getByteCount() / 1024;
        }
    }; 

	public static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null) {
	        mMemoryCache.put(key, bitmap);
	    }
	}

	public static Bitmap getBitmapFromMemCache(String key) {
	    return mMemoryCache.get(key);
	}
	
	public static Bitmap getBitmapFromAsset(Activity context, String name) {
		return BitmapFactory.decodeResource(context.getResources(), context.getResources().getIdentifier(name, "drawable", context.getPackageName()));
    }
	
	
	public static Bitmap getBitmapFromURL(String src) {
	    try {
	        URL url = new URL(src);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setDoInput(true);
	        connection.connect();
	        InputStream input = connection.getInputStream();
	        Bitmap myBitmap = BitmapFactory.decodeStream(input);
	        return myBitmap;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	static public Bitmap GenerateCardImage(Activity ref, 
			TemplateConfig_old template, 
			String name, 
			String email, 
			String phone,
			String company,
			String address,
			String jobTitle) {
		
		String cacheKey = name + email + phone + company + address + jobTitle + template.url;
		Bitmap cached = getBitmapFromMemCache(cacheKey);
		
		if (cached != null) return cached;
		
		Bitmap bg = getBitmapFromAsset(ref, template.url);
		Bitmap res = combineImages(template, bg, name, email, phone, company, address, jobTitle, ref);
		addBitmapToMemoryCache(cacheKey, res);
		
		return res;
	}
	
	static public Bitmap combineImages(TemplateConfig_old template, 
			Bitmap background, 
			String name, 
			String email, 
			String phone,
			String company,
			String address,
			String jobTitle,
			Activity mActivity) { 
		
		int width, height;

		float whScale = 0.541667f; // width to height ratio
		
		TypedValue typedValue = new TypedValue();
		mActivity.getResources().getValue(R.dimen.font_scale, typedValue, true);
		float fontScale = typedValue.getFloat();
		
		Display display = mActivity.getWindowManager().getDefaultDisplay();
	    DisplayMetrics outMetrics = new DisplayMetrics ();
	    display.getMetrics(outMetrics);

	    float density  = mActivity.getResources().getDisplayMetrics().density;
	    float dpWidth  = outMetrics.widthPixels / density;
		
	    dpWidth = dpWidth - 20; // there is a 10dp padding on either side
	    float dpHeight = dpWidth * whScale;
	    
	    width = (int) (dpWidth * density);
	    height = (int) (dpHeight * density);
	    
	    Log.i("density", Float.toString(density));
	    Log.i("height", Float.toString(height));
	    Log.i("width", Float.toString(width));
	    Log.i("fontScale", Float.toString(fontScale));
		
	    
	    
	    
		Paint paint = new Paint(); 
		paint.setStyle(Style.FILL);
		
		if (template.name.color.equals("white")) {
			paint.setColor(Color.WHITE);	
		}
		else {
			paint.setColor(Color.BLACK);
		}
		
		Bitmap cs;
		cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas comboImage = new Canvas(cs);
		
		background = Bitmap.createScaledBitmap(background, width, height, true);
		
		comboImage.drawPaint(paint);
		comboImage.drawBitmap(background, 0, 0, null);
		
		paint.setTextSize(25 * fontScale * density); 
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		comboImage.drawText(name, template.name.left * width, template.name.top * height, paint);
		paint.setTextSize(12 * fontScale * density); 
		comboImage.drawText(email, template.email.left * width, template.email.top * height, paint);
		paint.setTextSize(12 * fontScale * density); 
		comboImage.drawText(phone, template.phone.left * width, template.phone.top * height, paint);
		paint.setTextSize(12 * fontScale * density); 
		comboImage.drawText(company, template.company.left * width, template.company.top * height, paint);
		paint.setTextSize(12 * fontScale * density); 
		comboImage.drawText(address, template.address.left * width, template.address.top * height, paint);
		paint.setTextSize(12 * fontScale * density); 
		comboImage.drawText(jobTitle, template.jobTitle.left * width, template.jobTitle.top * height, paint);
		
		return cs;
	}
}
