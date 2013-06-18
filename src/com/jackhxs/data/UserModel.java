package com.jackhxs.data;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/**
 * Main Entry for Getting Card Array;
 * 
 * Once authenticated, initiate this object
 * 
 * @author MooMou
 *
 */
public class UserModel {
	private final String ContactURL = "http://dummy.com/user/:username/contacts";
	private final String CardURL = "dummy://";

	private final Context mCtx;
	private final String username;
	private final CardDBAdapter cardDBHelper;
	private final ContactAdapter contactDBHelper;

	private class DownloadJSON extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {
			String response = "";
			for (String url : urls) {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(url);
				try {
					HttpResponse execute = client.execute(httpGet);
					InputStream content = execute.getEntity().getContent();

					BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
					String s = "";
					while ((s = buffer.readLine()) != null) {
						response += s;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			
		}
	}

	/**
	 * Constructor - takes the context to allow the database to be
	 * opened/created
	 * 
	 * @param ctx
	 *            the Context within which to work
	 */
	public UserModel(Context ctx, String _username) {
		this.mCtx = ctx;
		username = _username;

		//initiating specific table helper
		cardDBHelper = new CardDBAdapter(ctx);
		contactDBHelper = new ContactAdapter(ctx);
	}

	/**
	 * Open the database. If it cannot be opened, try to create a new
	 * instance of the database. If it cannot be created, throw an exception to
	 * signal the failure
	 * 
	 * @return this (self reference, allowing this to be chained in an
	 *         initialization call)
	 * @throws SQLException
	 *             if the database could be neither opened or created
	 */
	public UserModel open() throws SQLException {
		cardDBHelper.open();
		contactDBHelper.open();

		return this;
	}

	/**
	 * close return type: void
	 */
	public void close() {
		cardDBHelper.close();
		contactDBHelper.close();
	}

	public List<Card> getContactCards() {
		Map<String, String> remoteIds = contactDBHelper.getAllContactIds(username);
		List<Card> cards = new ArrayList<Card>();

		for (Map.Entry<String, String> entry : remoteIds.entrySet()) {
			String remoteId = entry.getKey();
			Card contactCard = cardDBHelper.getCard(remoteId);

			if (contactCard != null) {
				cards.add(contactCard);
			}
			else { //not in the database
				//if online, retrieve it,
				//else add a blank card
			}
		}

		return cards;
	}

	public Boolean addContact(Card newContact) {
		/*
		 * Add card to table if not already in
		 * Add an entry to contact table if not already there
		 */

		if (newContact != null) {
			if (cardDBHelper.getCard(newContact.getId()) == null) {
				cardDBHelper.createCard(newContact);
			}

			return contactDBHelper.createContact(new Contact(username, newContact.getId())) > 0;
		}

		return false;
	}

	public Boolean removeContact(Card contact) {
		return null;
	}
	
	public void startSyncProcess() {
		
		DownloadJSON task = new DownloadJSON();
		task.execute(ContactURL.replace("username", username));
	}
	
	//Override Local Database with Remote State
	public Boolean syncWithRemote(String inputJSON) throws JsonIOException, JsonSyntaxException, MalformedURLException {
		List<Card> remoteContactCards = new Gson().fromJson(inputJSON,
					new TypeToken<List<Contact>>(){}.getType());
		Map<String, String> localContactIds = contactDBHelper.getAllContactIds(username);
		
		for (Card remoteContact: remoteContactCards) {
			String id = remoteContact.getId();
			cardDBHelper.updateCard(remoteContact); //update if exists else create
			localContactIds.remove(remoteContact.getId());
		}
		
		for (Map.Entry<String, String> entry : localContactIds.entrySet()) {
			contactDBHelper.deleteContact(Long.getLong(entry.getValue()));
		}
		
		return true;
	}
}
