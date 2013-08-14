package com.jackhxs.cardbank;

import java.nio.charset.Charset;

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
import android.widget.Toast;

import com.aphidmobile.flip.FlipViewController;
import com.google.gson.Gson;
import com.jackhxs.remote.Constants;
import com.jackhxs.remote.JSONResultReceiver;
import com.jackhxs.remote.RemoteService;

public class CardFlipView extends Activity implements CreateNdefMessageCallback,  JSONResultReceiver.Receiver {
	public JSONResultReceiver mReceiver;
	private NfcAdapter mNfcAdapter;
	protected FlipViewController myFlipView;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		CardAdapter adapter = new CardAdapter(this, R.layout.list_view_row,
				((App)getApplication()).myCards);

		myFlipView = new FlipViewController(this);
		myFlipView.setAdapter(adapter);
		setContentView(myFlipView);

		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

		if (mNfcAdapter == null) {
			Toast.makeText(this, "NFC not available", Toast.LENGTH_LONG).show();
			return;
		}

		mNfcAdapter.setNdefPushMessageCallback(this, this);
		
		mReceiver = new JSONResultReceiver(new Handler());
		mReceiver.setReceiver(this);
	}

	@Override
	protected void onResume() {
		Log.e("paul", "resume on cardflip");
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
        
        Log.e("NFC Data", simpleCardJSON);
        
        // send the result back to server
		final Intent serviceIntent = new Intent(Intent.ACTION_SYNC, null, this,
				RemoteService.class);
		
		intent.putExtra("receiver", mReceiver);
		intent.putExtra("operation", 3);
		intent.putExtra("newCardJSON", simpleCardJSON);
		
		startService(serviceIntent);	
    }
    
	/**
	 * {@inheritDoc}
	 * @see NfcAdapter.CreateNdefMessageCallback#createNdefMessage(NfcEvent)
	 */
	public NdefMessage createNdefMessage(NfcEvent event) {
		App app = ((App) getApplication());
		
		String serializedCard = (new Gson()).toJson(app.ownCard);
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
			break;
		}
		case Constants.STATUS_ERROR: {
			Log.e("Update Remote Server Failed", "Boo!");
			break;
		}
		}
		
	}
}
