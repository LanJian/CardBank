package com.jackhxs.cardbank;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;

import com.jackhxs.remote.Constants;
import com.jackhxs.remote.Constants.Operation;
import com.jackhxs.remote.JSONResultReceiver;
import com.jackhxs.remote.RemoteService;

public class LoginActivity extends Activity implements JSONResultReceiver.Receiver {
    public static final String PREFS_NAME = "MyPrefsFile";

    private EditText emailField, passwordField;
    private CheckBox rememberLogin;
    
	public JSONResultReceiver mReceiver;
	private ProgressDialog progress;
	private SharedPreferences settings;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_login);

		mReceiver = new JSONResultReceiver(new Handler());
		mReceiver.setReceiver(this);
		
		settings = getSharedPreferences(PREFS_NAME, 0);
		
		emailField = (EditText) findViewById(R.id.login_email);
		passwordField = (EditText) findViewById(R.id.login_password);
		rememberLogin = (CheckBox) findViewById(R.id.rememberMeCheckbox);
		
		if (settings.getBoolean("authenticated", false)) {
			App.sessionId = settings.getString("sessionId", "none");
			App.userId = settings.getString("userId", "none");
			onReceiveResult(-1, null);
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		if (App.sessionId != null && !App.sessionId.equals("")) {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			this.finish();
		}
	}

	public void login(View view) {
		String email = emailField.getText().toString();
		String password = passwordField.getText().toString();
		Boolean valid = true;
		
		if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
			emailField.setError("Email is invalid");
			valid = false;
		}
		
		if (!valid) {
			return;
		}
		
		try {
			Log.e("paul", "before intent");
			progress = new ProgressDialog(this);
			progress.setCancelable(false);
			progress.setMessage("Logging in...");
	        progress.show();

	        final Intent intent = new Intent(Intent.ACTION_SYNC, null, this,
					RemoteService.class);

			intent.putExtra("receiver", mReceiver);
			intent.putExtra("operation", (Parcelable) Operation.POST_LOGIN);
			intent.putExtra("email", email);
			intent.putExtra("password", password);

			startService(intent);
			Log.e("paul", "after intent");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent)
	{
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == 0) {
			finish();	
		}
	}

	public void signup(View view) {
		Intent intent = new Intent(this, SignupActivity.class);
		intent.putExtra("mode", "contact");
		startActivityForResult(intent, 1);
	}

	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		Log.e("paul", "result");

		if (progress != null) {
			progress.dismiss();
		}
		
		switch (resultCode) {

		case Constants.STATUS_FINISHED: {
			String errorMsg = resultData.getString("error", null);
			
			if (errorMsg != null) {
				Util.showErrorToast(getApplicationContext(), errorMsg);
				return;
			}
			
			App.sessionId = resultData.getString("sessionId");
			App.userId = resultData.getString("userId");
			
			if (rememberLogin.isChecked()) {
				settings.edit().putBoolean("authenticated", true).commit();
				settings.edit().putString("sessionId", App.sessionId).commit();
				settings.edit().putString("userId", App.userId).commit();
			}
			
			break;
		}
		case Constants.STATUS_ERROR: {
			Context context = getApplicationContext();
			Util.showWifiErrorToast(context);
			Log.e("Network Error", "Error logging in");
			break;
		}
		}

		if (App.userId != null && !App.userId.equals("") &&
				App.sessionId != null && !App.sessionId.equals("")) {
			
			Log.e("Success", "logged in");
			
			Intent intent = new Intent(this, MainActivity.class);
			intent.putExtra("mode", "oldAccount");
			
			startActivity(intent);
			
			this.finish();
		} else {
			Log.e("Failed", "logged in");
		}
	}
}
