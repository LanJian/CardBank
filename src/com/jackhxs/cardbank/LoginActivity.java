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

public class LoginActivity extends Activity implements
JSONResultReceiver.Receiver {
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

		if (App.accessToken != null && !App.accessToken.equals("")) {
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

		switch (resultCode) {

		case Constants.STATUS_FINISHED: {
			App.accessToken = resultData.getString("accessToken");
			Log.e("paul", App.accessToken);

			break;
		}
		case Constants.STATUS_ERROR: {
			Log.e("Network Error", "Error retrieving contacts");
			break;
		}
		}

		if (App.accessToken != null && !App.accessToken.equals("")) {
			Log.e("Success", "logged in");
			Intent intent = new Intent(this, MainActivity.class);
			intent.putExtra("mode", "contact");
			startActivity(intent);
			this.finish();
		} else {
			Log.e("Failed", "logged in");
		}
	}
}
