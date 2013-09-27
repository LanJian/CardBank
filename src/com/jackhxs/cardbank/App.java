package com.jackhxs.cardbank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.content.res.AssetManager;
import android.content.res.Configuration;

import com.jackhxs.data.SimpleCard;

public class App extends Application {
	public String accessToken;
	public String username;

	public SimpleCard[] myCards;
	public SimpleCard[] myContacts;

	public static JSONObject templateConfig;

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
		myContacts = null;
		myCards = null;
		username = null;
		accessToken = null;

		AssetManager assetManager = getResources().getAssets();
		InputStream inputStream = null;

		try {
			inputStream = assetManager.open("template.json");
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
		    StringBuilder sb = new StringBuilder();

		    String line = null;
		    while ((line = reader.readLine()) != null)
		    {
		        sb.append(line + "\n");
		    }
		    templateConfig = new JSONObject(sb.toString());
		    
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
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
