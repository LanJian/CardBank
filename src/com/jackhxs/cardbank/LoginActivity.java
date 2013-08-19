package com.jackhxs.cardbank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.jackhxs.remote.Constants;
import com.jackhxs.remote.Constants.Operation;
import com.jackhxs.remote.JSONResultReceiver;
import com.jackhxs.remote.RemoteService;

public class LoginActivity extends Activity implements JSONResultReceiver.Receiver {
	private EditText emailField, passwordField;
	public JSONResultReceiver mReceiver;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);

		mReceiver = new JSONResultReceiver(new Handler());
		mReceiver.setReceiver(this);
		
		emailField = (EditText) findViewById(R.id.login_email);
		passwordField = (EditText) findViewById(R.id.login_password);
	}

	@Override
    public void onResume() {
		super.onResume();

		App app = (App) getApplication();

		if (app.accessToken != null && !app.accessToken.equals("")) {
			Intent intent = new Intent(this, CardList.class);
			startActivity(intent);
			this.finish();
		}
    }
	
	public void login(View view) {
		String email = emailField.getText().toString();
		String password = passwordField.getText().toString();

		final Intent intent = new Intent(Intent.ACTION_SYNC, null, this,
				RemoteService.class);
		
		intent.putExtra("receiver", mReceiver);
		intent.putExtra("operation", (Parcelable) Operation.POST_LOGIN);
		intent.putExtra("email", email);
		intent.putExtra("password", password);
		
		startService(intent);		
	}

	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		
		App app = (App) getApplication();
		
		switch (resultCode) {
		
		case Constants.STATUS_FINISHED: {
//			app.accessToken = resultData.getString("accessToken");
			app.accessToken = "aaa";
			Log.e("paul", app.accessToken);
			
			break;
		}
		case Constants.STATUS_ERROR: {
			Log.e("Network Error", "Error retrieving contacts");
			break;
		}
		}
		
		if (app.accessToken != null && !app.accessToken.equals("")) {
			Log.e("Success", "logged in");
			Intent intent = new Intent(this, CardList.class);
			intent.putExtra("mode", "contact");
			startActivity(intent);
			this.finish();
		}
		else {
			Log.e("Failed", "logged in");
		}
	}
}