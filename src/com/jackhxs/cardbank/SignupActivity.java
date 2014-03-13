package com.jackhxs.cardbank;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.jackhxs.remote.Constants;
import com.jackhxs.remote.Constants.Operation;
import com.jackhxs.remote.JSONResultReceiver;
import com.jackhxs.remote.RemoteService;
import com.jackhxs.util.Utils;

public class SignupActivity extends Activity implements
JSONResultReceiver.Receiver {
	private EditText passwordField, confirmField;
	private AutoCompleteTextView emailField;
	public JSONResultReceiver mReceiver;
	private ProgressDialog progress;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_signup);

		mReceiver = new JSONResultReceiver(new Handler());
		mReceiver.setReceiver(this);

		emailField = (AutoCompleteTextView) findViewById(R.id.login_email);
		passwordField = (EditText) findViewById(R.id.login_password);
		confirmField = (EditText) findViewById(R.id.login_password2);
		
		List<String> emails = Utils.getEmails(this);
		
		emailField.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, emails));

		emailField.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					Log.d("getting focus", "now");
					emailField.showDropDown();
				}
			}
			
		});
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	public void signup(View view) {
		
		String email = emailField.getText().toString();
		String password = passwordField.getText().toString();
		String password2 = confirmField.getText().toString();
		Boolean valid = true;

		if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
			emailField.setError("Email is invalid");
			valid = false;
		}

		if (!password.equals(password2)) {
			confirmField.setError( "Password does not match." );
			valid = false;
		}

		if (!valid) {
			return;
		}

		try {
			progress = new ProgressDialog(this);
			progress.setCancelable(false);
			progress.setMessage("Creating your account...");
	        progress.show();
	        
			final Intent intent = new Intent(Intent.ACTION_SYNC, null, this,
					RemoteService.class);

			intent.putExtra("receiver", mReceiver);
			intent.putExtra("operation", (Parcelable) Operation.POST_SIGNUP);
			intent.putExtra("email", email);
			intent.putExtra("password", password);

			startService(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		Log.e("paul", "result");
		
		progress.dismiss();

		switch (resultCode) {

		case Constants.STATUS_FINISHED: {
			String errorMsg = resultData.getString("error", null);
			
			if (errorMsg != null) {
				Util.showErrorToast(getApplicationContext(), errorMsg);
				return;
			}
			
			App.userId = resultData.getString("userId");
			App.sessionId = resultData.getString("sessionId");
			break;
		}
		case Constants.STATUS_ERROR: {
			Context context = getApplicationContext();
			Util.showWifiErrorToast(context);
			Log.e("Network Error", "Error retrieving contacts");
			break;
		}
		}

		if (App.userId != null && !App.userId.equals("") &&
			App.sessionId != null && !App.sessionId.equals("")) {
			
			Log.e("Success", "logged in");
			
			Intent intent = new Intent(this, HomeActivity.class);
			intent.putExtra("mode", "newAccount");
			startActivity(intent);
			finishActivity(0);
			this.finish();
			
		} else {
			Log.e("Failed", "logged in");
		}
	}
}