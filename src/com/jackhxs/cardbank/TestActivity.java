package com.jackhxs.cardbank;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.jackhxs.cardbank.FlipAdapter.Callback;
import com.jackhxs.data.BusinessCard;
import com.jackhxs.remote.Constants;
import com.jackhxs.remote.JSONResultReceiver;
import com.jackhxs.remote.RemoteService;
import com.jackhxs.remote.Constants.Operation;
import com.xtremelabs.imageutils.ImageLoader;

import se.emilsjolander.flipview.FlipView;
import se.emilsjolander.flipview.FlipView.OnFlipListener;
import se.emilsjolander.flipview.FlipView.OnOverFlipListener;
import se.emilsjolander.flipview.OverFlipMode;
import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class TestActivity extends Activity implements Callback, OnFlipListener, OnOverFlipListener, CreateNdefMessageCallback,  JSONResultReceiver.Receiver {
	
	private FlipView mFlipView;
	private CardAdapter mAdapter;

	private NfcAdapter mNfcAdapter;

	public JSONResultReceiver mReceiver;
	public String cardViewMode = null;
	
	private String simpleCardJSON;
	
	private ArrayList<BusinessCard> list = new ArrayList<BusinessCard>();
    
	private void updateCardFlipView() {
		Log.e("TestActivity", "updateCardFlipView()");
		updateCardFlipView(0);
	}

	private void updateCardFlipView(Integer position) {
		Log.e("TestActivity", "updateCardFlipView(" + position + ")");
		
		mFlipView = (FlipView) findViewById(R.id.flip_view);
		
		/*
         * TODO IS THERE ANOTHER MODE? I can't find any.
         * TODO Is a user allowed to have more than 1 card?
         */
        
        if (cardViewMode == null || cardViewMode.equals("contact")) {
            if (App.myContacts != null)
            	list.addAll(Arrays.asList(App.myContacts));
            
            mAdapter = new CardAdapter(this, R.layout.card_flip_view, list, null);
    		
		}
		else {
            list.addAll(Arrays.asList(App.myCards));
           
            mAdapter = new CardAdapter(this, R.layout.card_flip_view, list, null);
		}

        mFlipView.setAdapter(mAdapter);
		mFlipView.setOnFlipListener(this);
		mFlipView.setOverFlipMode(OverFlipMode.RUBBER_BAND);
		mFlipView.setEmptyView(findViewById(R.id.empty_view));
		mFlipView.setOnOverFlipListener(this);
		
		Log.i("position", "" + position);
		
		mFlipView.flipTo(position);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater=getMenuInflater();
		inflater.inflate(R.menu.card_flip_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_refer:
                Intent intent = new Intent(this, ReferToListView.class);
                //intent.putExtra("toRefer", myFlipView.getSelectedItemPosition());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		
		
		cardViewMode = getIntent().getStringExtra("mode");
		
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
	public void onPageRequested(int page) {
		mFlipView.smoothFlipTo(page);
	}

	@Override
	public void onFlippedToPage(FlipView v, int position, long id) {
		Log.i("pageflip", "Page: "+position);
		if(position > mFlipView.getPageCount()-3 && mFlipView.getPageCount()<30){
			//mAdapter.addItems(5);
		}
	}

	@Override
	public void onOverFlip(FlipView v, OverFlipMode mode,
			boolean overFlippingPrevious, float overFlipDistance,
			float flipDistancePerPage) {
		Log.i("overflip", "overFlipDistance = "+overFlipDistance);
	}

	

	@Override
	protected void onResume() {
		super.onResume();
		//myFlipView.onResume();

		
		
		// Check to see that the Activity started due to an Android Beam
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
			processIntent(getIntent());
		}
		
		if (App.myContacts != null) {
			
			Integer initialPosition = getIntent().getIntExtra("position", 0);
			
			updateCardFlipView(initialPosition);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		//myFlipView.onPause();
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
		simpleCardJSON = (new String(msg.getRecords()[0].getPayload()));

		final Intent intentCards = new Intent(Intent.ACTION_SYNC, null, this, RemoteService.class);
		
		intentCards.putExtra("receiver", mReceiver);
		intentCards.putExtra("operation",(Parcelable) Operation.GET_CONTACTS);
		
		startService(intentCards);
	}

	/**
	 * {@inheritDoc}
	 * @see NfcAdapter.CreateNdefMessageCallback#createNdefMessage(NfcEvent)
	 */
	public NdefMessage createNdefMessage(NfcEvent event) {
		Integer positionIndex = 0;//myFlipView.getSelectedItemPosition();
		String serializedCard = (new Gson()).toJson(App.myContacts[positionIndex]);

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
		Log.e("TestActivity", "onReceiveResult");
		
		switch (resultCode) {
		case Constants.STATUS_FINISHED: {
			
			Operation command = resultData.getParcelable("operation");
			if (command != null){
				switch (command) {
				case GET_CONTACTS:
					BusinessCard[] data = (BusinessCard[]) resultData.getParcelableArray("contacts");
					
					App.myContacts = data;
					
					//List<BusinessCard> cardList = new ArrayList<BusinessCard>(Arrays.asList(App.myContacts));
					//BusinessCard tmpCard = (new Gson()).fromJson(simpleCardJSON, BusinessCard.class);
					//cardList.add(tmpCard);
					//list.add(tmpCard);
					//App.myContacts = cardList.toArray(new BusinessCard[cardList.size()]);

					Log.e("NFC Data", simpleCardJSON);

					// send the result back to server
					final Intent serviceIntent = new Intent(Intent.ACTION_SYNC, null, this,
							RemoteService.class);

					serviceIntent.putExtra("receiver", mReceiver);
					serviceIntent.putExtra("operation", (Parcelable) Operation.POST_CONTACT);
					serviceIntent.putExtra("newContactJSON", simpleCardJSON);

					startService(serviceIntent);
					
					//mAdapter.notifyDataSetChanged();
					
					Log.i("cardList.size()", "" + App.myContacts.length);
					//Log.i("mAdapter.getCount()", "" + mAdapter.getCount());
					
					updateCardFlipView(App.myContacts.length - 1);
					break;
				}
			}
			break;
		}
		case Constants.STATUS_ERROR: {
			Log.e("Update Remote Server Failed", "Boo!");
			break;
		}
		}

	}
}
