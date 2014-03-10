package com.jackhxs.cardbank;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.EditText;

import com.jackhxs.data.AccountType;
import com.jackhxs.remote.Constants;
import com.jackhxs.remote.Constants.Operation;
import com.jackhxs.remote.JSONResultReceiver;
import com.jackhxs.remote.LinkedInAPI;
import com.jackhxs.remote.RemoteAPICompleted;
import com.jackhxs.remote.RemoteService;

public class LoginActivity extends Activity implements 
JSONResultReceiver.Receiver,
RemoteAPICompleted {
	public static final String PREFS_NAME = "MyPrefsFile";

	private String email, password;
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

			if (!App.sessionId.equals("none") && !App.userId.equals("none")) {
				onReceiveResult(-1, null);	
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		if (App.sessionId != null && !App.sessionId.equals("")) {
			Intent intent = new Intent(this, HomeActivity.class);
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

	public void signup(View view) {
		Intent intent = new Intent(this, SignupActivity.class);
		intent.putExtra("mode", "contact");
		startActivityForResult(intent, 1);
	}

	public void signInWithLNKD(View view) {
		final RemoteAPICompleted that = this;
		String authUrl = LinkedInAPI.getInstance().getAuthUrl();
		final WebView webview = (WebView) findViewById(R.id.webview);
		webview.setVisibility(View.VISIBLE);

		webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView webView, String url) {
				if (url != null && url.startsWith(LinkedInAPI.CALL_BACK_URL)) {
					Uri uri = Uri.parse(url);
					LinkedInAPI.getInstance().initAccessToken(uri.getQueryParameter("oauth_verifier")); // added this
					LinkedInAPI.getInstance().getUserinfo(that);
					webview.setVisibility(View.INVISIBLE);

					//LinkedInAPI.getInstance().getUserConnection(that);
					return true;
				}
				else {
					return false;
				}
			}
		});

		webview.loadUrl(authUrl);
	}


	@Override
	public void onBackPressed() {
		WebView webview = (WebView) findViewById(R.id.webview);
		if (webview.getVisibility() == View.VISIBLE) {
			webview.setVisibility(View.GONE);
		} else {
			super.onBackPressed();
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

	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		Log.e("paul", "result");

		Integer errorCode = null;
		String errorMsg = null;
		
		if (resultData != null) {
			errorCode = resultData.getInt("errorCode", 0);
			errorMsg = resultData.getString("errorMsg", null);
		}
		
		if (progress != null) {
			progress.dismiss();
		}

		switch (resultCode) {

		case Constants.STATUS_FINISHED: {
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
			if (App.accounType == AccountType.LINKED_ACCOUNT &&
					errorCode == 403) { 

				final Intent intent = new Intent(Intent.ACTION_SYNC, null, this,
						RemoteService.class);

				App.accounType = AccountType.NEW_ACCOUNT;

				intent.putExtra("receiver", mReceiver);
				intent.putExtra("operation", (Parcelable) Operation.POST_SIGNUP);

				intent.putExtra("email", email);
				intent.putExtra("password", password);
				startService(intent);
				
				return;
			}

			if (!errorMsg.equals("")) {
				Util.showErrorToast(getApplicationContext(), errorMsg);
				return;
			}

			Context context = getApplicationContext();
			Util.showWifiErrorToast(context);
			Log.e("Network Error", "Error logging in");
			break;
		}
		}

		// Why even check this if there is a network or login issue? it will always fail
		if (App.userId != null && !App.userId.equals("") &&
				App.sessionId != null && !App.sessionId.equals("")) {

			Log.e("Success", "logged in");

			Intent intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
			
			this.finish();
		} else {
			Log.e("Failed", "logged in");
		}
	}

	@Override
	public void remoteCompleted(String responseJSON) {
		// Received the user info
		try {
			JSONObject jObject = new JSONObject(responseJSON);

			App.accounType = AccountType.LINKED_ACCOUNT;

			email = jObject.getString("emailAddress");
			password = LinkedInAPI.API_SECRETE + jObject.getString("id");

			emailField.setText(email);
			passwordField.setText(password);

			this.login(getWindow().getDecorView().findViewById(android.R.id.content));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
