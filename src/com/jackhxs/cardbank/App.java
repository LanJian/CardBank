package com.jackhxs.cardbank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

import android.app.Application;
import android.content.res.AssetManager;
import android.content.res.Configuration;

import com.google.gson.Gson;
import com.jackhxs.data.SimpleCard;
import com.jackhxs.data.TemplateConfig;

public class App extends Application {
	public static String sessionId;
	public static String userId;

	public static SimpleCard[] myCards;
	public static SimpleCard[] myContacts;
	public static SimpleCard[] myReferrals;
	
	public static TemplateConfig[] templateConfig;
	public static Calendar lastUpdated;
	
	public Boolean addContact() {
		return null;
	}

	public Boolean addCard() {
		return null;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		lastUpdated = null;
		myContacts = null;
		myCards = null;
		userId = null;
		sessionId = null;

		AssetManager assetManager = getResources().getAssets();
		InputStream inputStream = null;

		try {
			inputStream = assetManager.open("template.json");
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
		    StringBuilder sb = new StringBuilder();

		    String line = null;
		    
		    while ((line = reader.readLine()) != null) {
		        sb.append(line + "\n");
		    }
		    
		    templateConfig = new Gson().fromJson(sb.toString(), TemplateConfig[].class);
		    
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}
}
