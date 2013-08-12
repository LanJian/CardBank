package com.jackhxs.cardbank;

import com.jackhxs.remote.RestInterface;

import retrofit.RestAdapter;
import android.app.Application;
import android.content.res.Configuration;

public class App extends Application {
	final private String BASE_URL = "http://localhost:8080";
	final private RestAdapter restAdapter = new RestAdapter.Builder()
	.setServer(BASE_URL)
	.build();
	
	final RestInterface simpleApi = restAdapter.create(RestInterface.class);
	String username = "";
	
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
