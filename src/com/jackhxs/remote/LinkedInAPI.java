package com.jackhxs.remote;

import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import android.os.AsyncTask;

public class LinkedInAPI {
	public static final String CALL_BACK_URL = "oauth://cardbeam";
	public static final String API_KEY 		= "7512rfpk70myub";
	public static final String API_SECRETE 	= "IM122fSZlrbvBwNj";
			
	private static final String PERSONAL_INFO_URL = 
			"http://api.linkedin.com/v1/people/~:(id,first-name,last-name,positions,email-address,phone-numbers)?format=json";
	
	private static final String CONNECTION_INFO_URL = 
			"http://api.linkedin.com/v1/people/~/connections:(id,last-name)?format=json";

	private static Token requestToken = null;
	private static Token accessToken = null;
	private static String authUrl = null;
	private static OAuthService service = null;
	private static LinkedInAPI instance = null;
	
	private static String UserContactsCache = null;
	private static String UserInfoCache = null;
	
	private class RequestTokenInit extends AsyncTask<String, Integer, Token> {
		protected Token doInBackground(String... urls) {
			return service.getRequestToken();
		}

		protected void onPostExecute(Token result) {
			requestToken = result;
			authUrl = service.getAuthorizationUrl(requestToken);
		}
	}

	private class ApiResult extends AsyncTask<String, Integer, String> {
		private OnTaskCompleted listener;
		private String cache;
		
		public ApiResult(OnTaskCompleted listener, String cache){
			this.listener = listener;
		}

		@Override
		protected String doInBackground(String... urls) {
			if (cache == null) {
				OAuthRequest request = new OAuthRequest(Verb.GET, urls[0]);
				service.signRequest(accessToken, request);
				Response response = request.send();
				cache = response.getBody();
			}
			
			return cache;
		}

		protected void onPostExecute(String result) {
			this.listener.onTaskCompleted(result);
		}
	}

	private class AccessTokenInit extends AsyncTask<String, Integer, Token> {
		protected Token doInBackground(String... urls) {
			Verifier verifier = new Verifier(urls[0]);
			return service.getAccessToken(requestToken, verifier);
		}

		protected void onPostExecute(Token result) {
			accessToken = result;
		}
	}

	private LinkedInAPI() {
		service = new ServiceBuilder().provider(LinkedInApi.withScopes("r_contactinfo", 
				"r_emailaddress", "r_fullprofile"))
				.apiKey(API_KEY).apiSecret(API_SECRETE)
				.callback(CALL_BACK_URL).build();

		new RequestTokenInit().execute();
	}

	public void getUserinfo(OnTaskCompleted cb) {
		ApiResult apiResult = new ApiResult(cb, UserInfoCache);
		apiResult.execute(PERSONAL_INFO_URL);
	}

	public void getUserConnection(OnTaskCompleted cb) {
		ApiResult apiResult = new ApiResult(cb, UserContactsCache);
		apiResult.execute(CONNECTION_INFO_URL);
	}

	public void initAccessToken(String _verifier) {
		new AccessTokenInit().execute(_verifier);
	}

	public String getAuthUrl() {
		while (authUrl == null) {
		}
		return authUrl;
	}

	public static LinkedInAPI getInstance() {
		if (instance == null) {
			instance = new LinkedInAPI();
		}

		return instance;
	}
}
