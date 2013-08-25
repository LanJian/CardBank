package com.jackhxs.cardbank;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.aphidmobile.flip.FlipViewController;
import com.google.gson.Gson;
import com.jackhxs.data.SimpleCard;
import com.jackhxs.remote.Constants;
import com.jackhxs.remote.Constants.Operation;
import com.jackhxs.remote.JSONResultReceiver;
import com.jackhxs.remote.RemoteService;

public class CardFlipView extends Activity implements CreateNdefMessageCallback,  JSONResultReceiver.Receiver {
	private NfcAdapter mNfcAdapter;

	public JSONResultReceiver mReceiver;
	public String cardViewMode = null;

	protected FlipViewController myFlipView;

	private void updateCardFlipView() {
		updateCardFlipView(0);
	}

	private void updateCardFlipView(Integer position) {
		CardAdapter adapter;

		if (cardViewMode == null || cardViewMode.equals("contact")) {
			adapter = new CardAdapter(this, R.layout.card_flip_view, ((App)getApplication()).myContacts);
		}
		else {
			adapter = new CardAdapter(this, R.layout.card_flip_view, ((App)getApplication()).myCards);
		}

		myFlipView = new FlipViewController(this);
		myFlipView.setAdapter(adapter, position);
		setContentView(myFlipView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater=getMenuInflater();
		inflater.inflate(R.menu.card_flip_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		cardViewMode = getIntent().getStringExtra("mode");
		Integer initialPosition = getIntent().getIntExtra("position", 0);

		updateCardFlipView(initialPosition);

		// update NFC related configs
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

		if (mNfcAdapter == null) {
			Toast.makeText(this, "NFC not available", Toast.LENGTH_LONG).show();
			return;
		}
		mNfcAdapter.setNdefPushMessageCallback(this, this);

		// configure remote service callback
		mReceiver = new JSONResultReceiver(new Handler());
		mReceiver.setReceiver(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		myFlipView.onResume();

		// Check to see that the Activity started due to an Android Beam
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
			processIntent(getIntent());
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		myFlipView.onPause();
	}

	@Override
	public void onNewIntent(Intent intent) {
		// onResume gets called after this to handle the intent
		setIntent(intent);
	}

	/**
	 * Parses the NDEF Message from the intent and prints to the TextView
	 */
	void processIntent(Intent intent) {
		Parcelable[] rawMsgs = intent.getParcelableArrayExtra(
				NfcAdapter.EXTRA_NDEF_MESSAGES);

		// only one message sent during the beam
		NdefMessage msg = (NdefMessage) rawMsgs[0];

		// record 0 contains the MIME type, record 1 is the AAR, if present
		String simpleCardJSON = (new String(msg.getRecords()[0].getPayload()));

		App app = (App) getApplication();

		List<SimpleCard> cardList = new ArrayList<SimpleCard>(Arrays.asList(app.myContacts));
		SimpleCard tmpCard = (new Gson()).fromJson(simpleCardJSON, SimpleCard.class);
		cardList.add(tmpCard);
		app.myContacts = cardList.toArray(new SimpleCard[cardList.size()]);

		Log.e("NFC Data", simpleCardJSON);

		// send the result back to server
		final Intent serviceIntent = new Intent(Intent.ACTION_SYNC, null, this,
				RemoteService.class);

		serviceIntent.putExtra("receiver", mReceiver);
		serviceIntent.putExtra("operation", (Parcelable) Operation.POST_CONTACT);
		serviceIntent.putExtra("newContactJSON", simpleCardJSON);

		startService(serviceIntent);	
	}

	/**
	 * {@inheritDoc}
	 * @see NfcAdapter.CreateNdefMessageCallback#createNdefMessage(NfcEvent)
	 */
	public NdefMessage createNdefMessage(NfcEvent event) {
		App app = ((App) getApplication());

		Integer positionIndex = myFlipView.getSelectedItemPosition();
		String serializedCard = (new Gson()).toJson(app.myContacts[positionIndex]);

		Log.e("Serialized JSON", serializedCard);
		NdefMessage msg = new NdefMessage(
				new NdefRecord[] {
						createMimeRecord("application/com.jackhxs.cardbank", serializedCard.getBytes()),
						//NdefRecord.createApplicationRecord("com.jackhxs.cardbank")
				});

		return msg;
	}

	/**
	 * Creates a custom MIME type encapsulated in an NDEF record
	 */
	public NdefRecord createMimeRecord(String mimeType, byte[] payload) {
		byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));

		NdefRecord mimeRecord = new NdefRecord(
				NdefRecord.TNF_MIME_MEDIA, mimeBytes, new byte[0], payload);

		return mimeRecord;
	}

	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		switch (resultCode) {
		case Constants.STATUS_FINISHED: {
			Log.e("Update Remote Server Worked", "yay!");
			updateCardFlipView();
			break;
		}
		case Constants.STATUS_ERROR: {
			Log.e("Update Remote Server Failed", "Boo!");
			break;
		}
		}

	}
}
