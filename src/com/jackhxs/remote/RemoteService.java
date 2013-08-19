package com.jackhxs.remote;

import retrofit.RestAdapter;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.google.gson.Gson;
import com.jackhxs.cardbank.App;
import com.jackhxs.data.ContactAndCards;
import com.jackhxs.data.SimpleCard;
import com.jackhxs.remote.Constants.Operation;

public class RemoteService extends IntentService {
	private final RestAdapter restAdapter = new RestAdapter.Builder()
	.setServer("http://www.mocky.io/v2")
	.build();
	private final RestInterface service;
	
	public RemoteService() {
		this("remoteService");
	}

	public RemoteService(String name) {
		super(name);
		service = restAdapter.create(RestInterface.class);
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		final ResultReceiver receiver = intent.getParcelableExtra("receiver");
		Bundle b = new Bundle();

		Operation command = intent.getParcelableExtra("operation");
		String accessToken = ((App) getApplication()).accessToken;

		Log.i("paul", command.toString());
		receiver.send(Constants.STATUS_RUNNING, Bundle.EMPTY);
		
		switch (command) { 
		case POST_LOGIN: {
			String username = intent.getStringExtra("username");
			String password = intent.getStringExtra("password");
			String newAccessToken = service.login(username, password);
			
			b.putString("accessToken", newAccessToken);

			receiver.send(Constants.STATUS_FINISHED, b);
			Log.i("remoteService", newAccessToken);
			break;
		}
		case GET_CARDS: {
			SimpleCard[] contacts = service.listOwnCards(accessToken);
			b.putParcelableArray("cards", contacts);
            receiver.send(Constants.STATUS_FINISHED, b);
			break;
		}
		case GET_CONTACTS: {
			SimpleCard[] contacts = service.listContacts(accessToken);
			b.putParcelableArray("contacts", contacts);
            receiver.send(Constants.STATUS_FINISHED, b);
			break;
		}
		case GET_BOTH_CONTACT_AND_CARD: {
			ContactAndCards contactAndCards = service.listContactAndCards(accessToken);
			b.putParcelableArray("contacts", contactAndCards.contacts);
			b.putParcelableArray("cards", contactAndCards.cards);
            receiver.send(Constants.STATUS_FINISHED, b);
			break;
		}
		case POST_CARD: {
			String simpleCardJSON = intent.getStringExtra("simpleCardJSON");
			
			SimpleCard simpleCard = new Gson().fromJson(simpleCardJSON, SimpleCard.class);
			Boolean result = service.addCard(accessToken, simpleCard);
			
			b.putBoolean("result", result);
			receiver.send(Constants.STATUS_FINISHED, b);
			break;
		}
		case POST_CONTACT: {
			String simpleCardJSON = intent.getStringExtra("newContactJSON");
			SimpleCard simpleCard = new Gson().fromJson(simpleCardJSON, SimpleCard.class);
			Boolean result = service.addContact(accessToken, simpleCard);
			
			b.putBoolean("result", result);
			receiver.send(Constants.STATUS_FINISHED, b);
			break;
		}
		case DEL_CARD:
			break;
		case DEL_CONTACT:
			break;
		case PUT_CARD:
			break;
		default:
			break;
		}
		this.stopSelf();
	}
}