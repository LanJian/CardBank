package com.jackhxs.cardbank;

import java.nio.charset.Charset;
import java.text.ParseException;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
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
	private Boolean editCardImmediately;
	private Boolean networkFinished;
	private Boolean startedPolling;
	private Boolean pollingInProgress;
	private Integer longPollCount;

	public JSONResultReceiver mReceiver;

	private void startLongPollingGetContact() {
		if (!networkFinished) { 
			return;
		}

		pollingInProgress = startedPolling = true;
		networkFinished = false;

		final Intent serviceIntent = new Intent(Intent.ACTION_SYNC, null, this,
				RemoteService.class);

		serviceIntent.putExtra("receiver", mReceiver);
		serviceIntent.putExtra("longPoll", true);
		serviceIntent.putExtra("operation", (Parcelable) Operation.GET_CONTACTS);
		startService(serviceIntent);
	}

	private void startService(Operation op) {
		final Intent serviceIntent = new Intent(Intent.ACTION_SYNC, null, this,
				RemoteService.class);

		serviceIntent.putExtra("receiver", mReceiver);
		serviceIntent.putExtra("operation", (Parcelable) op);
		startService(serviceIntent);
	}

	// ----- Methods -----
	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		editCardImmediately = getIntent().getExtras().getString("mode", "oldAccount").equals("newAccount");
		startedPolling = pollingInProgress = networkFinished = false;
		longPollCount = 0;

		if (!Util.isTablet(getApplicationContext())) {
			getActionBar().setDisplayShowTitleEnabled(false);
			getActionBar().setDisplayShowHomeEnabled(false);
		}

		// Notice that setContentView() is not used, because we use the root
		// android.R.id.content as the container for each fragment
		try {
			mReceiver = new JSONResultReceiver(new Handler());
			mReceiver.setReceiver(this);

			final Intent intentCards = new Intent(Intent.ACTION_SYNC, null, this,
					RemoteService.class);

			intentCards.putExtra("receiver", mReceiver);
			intentCards.putExtra("operation",(Parcelable) Operation.GET_CARDS);

			startService(intentCards);
			dataInitialization();

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
		if (startedPolling && !pollingInProgress) {
			startLongPollingGetContact();
		}
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
			Intent intent = new Intent(
					getApplicationContext(),
					CardEditActivity.class);
			startActivity(intent);
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void dataInitialization() {
		Log.i("info", "starting...");

		// setup action bar for tabs
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(false);
	}

	private void addTab(String tabName, Fragment frag, String name) {
		ActionBar actionBar = getActionBar();
		Tab tab = actionBar
				.newTab()
				.setText(tabName)
				.setTabListener(
						new TabListener<CardFragment>(this, name,
								frag));
		actionBar.addTab(tab);
	}

	private void dataUpdated(Bundle resultData) throws ParseException {
		String dataType = resultData.getString("dataType");
		SimpleCard[] data = (SimpleCard[]) resultData.getParcelableArray(dataType);

		if (dataType.equals("cards")) {
			App.myCards = data;

			Fragment cardFragment = new CardFragment(App.myCards[0]);
			addTab("My Card", cardFragment, "myCard");

			startService(Operation.GET_CONTACTS);
		}
		else if (dataType.equals("contacts")) {
			App.myContacts = data;
			networkFinished = true;

			if (resultData.getBoolean("longPoll", false) && App.lastUpdated != null && 
					Util.ISO8601.toCalendar(resultData.getString("updatedAt")).compareTo(App.lastUpdated) != 1) {

				Handler handler = new Handler(); 
				handler.postDelayed(new Runnable() { 
					public void run() { 
						Log.e("LONG POLL", "LONG POLL");
						longPollCount += 1;

						if (longPollCount <= 5) { // stop long polling after 5 times
							startLongPollingGetContact();
						}
						else {
							longPollCount = 0;
						}
					} 
				}, 2000); 

				return;
			}
			else {
				pollingInProgress = false;
				startedPolling = false;
			}

			App.lastUpdated = Util.ISO8601.toCalendar(resultData.getString("updatedAt"));

			// This is needed to prevent generating repeat tabs on home screen
			if (resultData.getBoolean("longPoll", false)) {
				return;
			}

			Fragment listFragment = new CardListFragment();
			addTab("Contact", listFragment, "myContact");

			final Intent intentReferrals = new Intent(Intent.ACTION_SYNC, null, this,
					RemoteService.class);

			intentReferrals.putExtra("receiver", mReceiver);
			intentReferrals.putExtra("operation",(Parcelable) Operation.LIST_REFERRALS);
			startService(intentReferrals);
		}
		else if (dataType.equals("referrals")){ //referalls
			App.myReferrals = data;

			Fragment fragment = new ReferralsListFragment();
			addTab("Referrals", fragment, "referrals");

			// this is always the last call
			if (editCardImmediately) {
				editCardImmediately = !editCardImmediately;
				Intent intent = new Intent(
						getApplicationContext(),
						CardEditActivity.class);
				startActivity(intent);
			}
		}
	}

	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		Log.e("paul", String.valueOf(resultCode));

		switch (resultCode) {
		case Constants.STATUS_FINISHED: {
			try {
				dataUpdated(resultData);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
		case Constants.STATUS_ERROR: {
			Context context = getApplicationContext();
			Util.showWifiErrorToast(context);
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
		Integer myContactLength = App.myContacts.length;

		App.myContacts[myContactLength] = 
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
		String serializedCard = (new Gson()).toJson(App.myCards[0]);

		Log.e("Serialized JSON", serializedCard);
		NdefMessage msg = new NdefMessage(
				new NdefRecord[] {
						createMimeRecord("application/com.jackhxs.cardbank", serializedCard.getBytes()),
				});

		startLongPollingGetContact();
		return msg;
	}
}
