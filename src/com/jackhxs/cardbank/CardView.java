package com.jackhxs.cardbank;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.widget.TextView;

public class CardView extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_card_view, menu);
        return true;
    }


    @Override
    public void onResume() {
      System.out.println("=========== cardview resume ============");
      super.onResume();
      if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
        processIntent(getIntent());
      }
    }


    public void processIntent(Intent intent) {
      TextView textView = (TextView) findViewById(R.id.text_view);
      Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
      NdefMessage msg = (NdefMessage) rawMsgs[0];
      textView.setText(new String(msg.getRecords()[0].getPayload()));
    }
}
