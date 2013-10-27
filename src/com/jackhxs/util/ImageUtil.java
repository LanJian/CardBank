package com.jackhxs.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.LruCache;

import com.jackhxs.data.TemplateConfig;

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
	
	static public Bitmap GenerateCardImage(Activity ref, TemplateConfig template, String name, String email, String phone) {
		String cacheKey = name + email + phone + template.url;
		Bitmap cached = getBitmapFromMemCache(cacheKey);
		
		if (cached != null) return cached;
		
		Bitmap bg = getBitmapFromAsset(ref, template.url);
		Bitmap res = combineImages(template, bg, name, email, phone);
		addBitmapToMemoryCache(cacheKey, res);
		
		return res;
	}
	
	static public Bitmap combineImages(TemplateConfig template, Bitmap background, String name, String email, String phone) { 
		int width = 768, height = 416;

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
		
		paint.setTextSize(50); 
		comboImage.drawText(name, template.name.left, template.name.top, paint);
		paint.setTextSize(25); 
		comboImage.drawText(email, template.email.left, template.email.top, paint);
		paint.setTextSize(25); 
		comboImage.drawText(phone, template.phone.left, template.phone.top, paint);
		
		return cs;
	}
}
