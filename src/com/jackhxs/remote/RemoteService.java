package com.jackhxs.remote;

import java.util.List;

import retrofit.RestAdapter;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import android.os.ResultReceiver;
import android.util.Log;

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

		Operation command = Constants.OperationVals[intent.getIntExtra("operation", 0)];
		Log.e("paul", command.toString());
		String userId = intent.getStringExtra("userId");
		
		receiver.send(Constants.STATUS_RUNNING, Bundle.EMPTY);
		
		switch (command) { 
		case GET_CARDS: {
			break;
		}
		case GET_CONTACTS:
			SimpleCard[] contacts = service.listCards();
			b.putParcelableArray("contacts", contacts);
            receiver.send(Constants.STATUS_FINISHED, b);
			break;
		}

		this.stopSelf();
	}
}
