package com.jackhxs.cardbank;

import android.app.Application;
import android.content.res.Configuration;

import com.jackhxs.data.SimpleCard;

public class App extends Application {
	public String accessToken;
	public String username;
	
	public SimpleCard[] myCards;
	public SimpleCard[] myContacts;
	
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
