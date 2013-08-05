package com.jackhxs.cardbank;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;

import com.jackhxs.data.GetJSONListener;
import com.jackhxs.data.JSONClient;

public class JSONActivity extends Activity {
	
	GetJSONListener l = new GetJSONListener(){
		public void onRemoteCallComplete(JSONObject jsonFromNet) {
			// add code to act on the JSON object that is returned			
		}
	};
	
	String url = "http://www.mocky.io/v2/51ef4285ed9a03d101e47139";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		JSONClient client = new JSONClient(this, l);
		String url = "url that will return JSON";
		
		client.execute(url);
	}	
}