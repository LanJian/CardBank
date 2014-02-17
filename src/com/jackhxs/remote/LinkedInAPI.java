package com.jackhxs.remote;

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
	private static final String PROTECTED_RESOURCE_URL = "http://api.linkedin.com/v1/people/~/connections:(id,last-name)";
	public static final String CALL_BACK_URL = "oauth://cardbeam";

	private static Token requestToken = null;
	private static Token accessToken = null;
	private static String authUrl = null;
	private static OAuthService service = null;

	private static LinkedInAPI instance = null;

	private class RequestTokenInit extends AsyncTask<String, Integer, Token> {
		protected Token doInBackground(String... urls) {
			return service.getRequestToken();
		}

		protected void onPostExecute(Token result) {
			requestToken = result;
			authUrl = service.getAuthorizationUrl(requestToken);
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
		service = new ServiceBuilder().provider(LinkedInApi.class)
				.apiKey("7512rfpk70myub").apiSecret("IM122fSZlrbvBwNj")
				.callback(CALL_BACK_URL).build();
		
		new RequestTokenInit().execute();
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
