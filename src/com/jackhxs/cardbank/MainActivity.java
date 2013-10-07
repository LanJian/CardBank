package com.jackhxs.cardbank;

import java.nio.charset.Charset;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
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
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jackhxs.data.SimpleCard;
import com.jackhxs.remote.Constants;
import com.jackhxs.remote.Constants.Operation;
import com.jackhxs.remote.JSONResultReceiver;
import com.jackhxs.remote.RemoteService;

public class MainActivity extends Activity implements CreateNdefMessageCallback, JSONResultReceiver.Receiver {
	private NfcAdapter mNfcAdapter;
	public JSONResultReceiver mReceiver;

	// ----- Methods -----
	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (!Util.isTablet(getApplicationContext())) {
			getActionBar().setDisplayShowTitleEnabled(false);
			getActionBar().setDisplayShowHomeEnabled(false);
		}

		// Notice that setContentView() is not used, because we use the root
		// android.R.id.content as the container for each fragment
		try {
			mReceiver = new JSONResultReceiver(new Handler());
			mReceiver.setReceiver(this);

			final Intent intent = new Intent(Intent.ACTION_SYNC, null, this,
					RemoteService.class);
			Log.e("paul", "after intent");
			intent.putExtra("receiver", mReceiver);
			intent.putExtra("operation",
					(Parcelable) Operation.GET_BOTH_CONTACT_AND_CARD);
			startService(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// update NFC related configs
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

		if (mNfcAdapter == null) {
			Toast.makeText(this, "NFC not available", Toast.LENGTH_LONG).show();
			return;
		}
		mNfcAdapter.setNdefPushMessageCallback(this, this);
	}

	@Override
	public void onResume() {
		super.onResume();

		// Check to see that the Activity started due to an Android Beam
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
			processIntent(getIntent());
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_edit: {
			Intent intent = new Intent(getApplicationContext(),
					TemplateGallery.class); // CardEdit
			startActivity(intent);
			return true;
		}
		case R.id.action_delete: {
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void dataInitialization(Bundle resultData) {
		Log.i("ContactList", "received card");

		App app = (App) getApplication();

		SimpleCard[] contacts = (SimpleCard[]) resultData.getParcelableArray("contacts");
		SimpleCard[] cards = (SimpleCard[]) resultData.getParcelableArray("cards");

		// setting a global reference in app
		app.myContacts = contacts;
		app.myCards = cards;

		// setup action bar for tabs
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(false);

		Fragment cardFragment = new CardFragment((app.myCards)[0]);

		Tab tab = actionBar
				.newTab()
				.setText("My Card")
				.setTabListener(
						new TabListener<CardFragment>(this, "myCard",
								cardFragment));
		actionBar.addTab(tab);

		Fragment listFragment = new CardListFragment();
		tab = actionBar
				.newTab()
				.setText("My Contacts")
				.setTabListener(
						new TabListener<CardListFragment>(this, "myContacts",
								listFragment));
		actionBar.addTab(tab);

		Fragment fragment = new ReferralsListFragment();
		tab = actionBar
				.newTab()
				.setText("Referrals")
				.setTabListener(
						new TabListener<CardListFragment>(this, "referrals",
								fragment));
		actionBar.addTab(tab);
	}

	private void dataUpdated(Bundle resultData) {
	}

	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		Log.e("paul", String.valueOf(resultCode));

		switch (resultCode) {
		case Constants.STATUS_FINISHED: {
			if (resultData.getString("action").equals("initialization")) {
				dataInitialization(resultData);
			} else if (resultData.getString("action").equals("update")) {
				dataUpdated(resultData);
			}

			break;
		}
		case Constants.STATUS_ERROR: {
			System.out.println("Error retrieving contacts and cards");
		}
		}
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
		Integer myContactLength = app.myContacts.length;

		((App) getApplication()).myContacts[myContactLength] = 
				(new Gson()).fromJson(simpleCardJSON, SimpleCard.class);
		
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
	 * Creates a custom MIME type encapsulated in an NDEF record
	 */
	public NdefRecord createMimeRecord(String mimeType, byte[] payload) {
		byte[] mimeBytes = mimeType.getBytes(Charset.forName("US-ASCII"));

		NdefRecord mimeRecord = new NdefRecord(
				NdefRecord.TNF_MIME_MEDIA, mimeBytes, new byte[0], payload);

		return mimeRecord;
	}

	@Override
	public NdefMessage createNdefMessage(NfcEvent event) {
		App app = ((App) getApplication());

		String serializedCard = (new Gson()).toJson(app.myCards[0]);

		Log.e("Serialized JSON", serializedCard);
		NdefMessage msg = new NdefMessage(
				new NdefRecord[] {
						createMimeRecord("application/com.jackhxs.cardbank", serializedCard.getBytes()),
						//NdefRecord.createApplicationRecord("com.jackhxs.cardbank")
				});

		return msg;
	}
}
