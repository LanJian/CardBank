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
import android.widget.EditText;

public class ImageUtil {
	
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
	
	static public Bitmap GenerateCardImage(Bitmap bg, EditText nameEditTxt, EditText emailEditTxt, EditText phoneNumberEditTxt) {
		Bitmap name = Bitmap.createBitmap(nameEditTxt.getDrawingCache());
		Bitmap email = Bitmap.createBitmap(emailEditTxt.getDrawingCache());
		Bitmap phone = Bitmap.createBitmap(phoneNumberEditTxt.getDrawingCache());
		return combineImages(bg, name, email, phone);
	}

	static public Bitmap GenerateCardImage(Bitmap bg, String name, String email, String phone) {
		return combineImages(bg, name, email, phone);
	}
	
	static public Bitmap combineImages(Bitmap background, String name, String email, String phone) { 
		int width = 768, height = 416;

		Paint paint = new Paint(); 
		paint.setColor(Color.WHITE); 
		paint.setStyle(Style.FILL); 
		 

		paint.setColor(Color.BLACK); 
		paint.setTextSize(20); 

		Bitmap cs;

		cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas comboImage = new Canvas(cs);
		
		background = Bitmap.createScaledBitmap(background, width, height, true);
		comboImage.drawPaint(paint);
		comboImage.drawBitmap(background, 0, 0, null);
				
		comboImage.drawText(name, 10, 50, paint);
		comboImage.drawText(email, 10, 100, paint);
		comboImage.drawText(phone, 10, 150, paint);
		
		return cs;
	}
	
	static public Bitmap combineImages(Bitmap background, Bitmap name, Bitmap email, Bitmap phone) { 
		int width = 768, height = 416;
		Bitmap cs;

		cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas comboImage = new Canvas(cs);
		
		background = Bitmap.createScaledBitmap(background, width, height, true);
		
		comboImage.drawBitmap(background, 0, 0, null);
		
		comboImage.drawBitmap(name, 10, 50, null);
		comboImage.drawBitmap(email, 10, 100, null);
		comboImage.drawBitmap(phone, 10, 150, null);

		return cs;
	}
}
