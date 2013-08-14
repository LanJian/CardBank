package com.jackhxs.cardbank;

import android.app.Application;
import android.content.res.Configuration;

import com.jackhxs.data.SimpleCard;

public class App extends Application {
	public SimpleCard[] myCards;
	
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
