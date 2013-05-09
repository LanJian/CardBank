package com.jackhxs.cardbank;

import java.nio.charset.Charset;

import android.app.Activity;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements CreateNdefMessageCallback {
  
  // ----- Members -----
  private NfcAdapter mNfcAdapter;


  // ----- Methods -----
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
    if (mNfcAdapter == null) {
      Toast.makeText(this, "NFC not available", Toast.LENGTH_LONG).show();
      return;
    }

    mNfcAdapter.setNdefPushMessageCallback(this, this);
  }


  /**
   * {@inheritDoc}
   * @see NfcAdapter.CreateNdefMessageCallback#createNdefMessage(NfcEvent)
   */
  public NdefMessage createNdefMessage(NfcEvent event) {
    EditText editText = (EditText) findViewById(R.id.edit_text);
    String text = editText.getText().toString();

    NdefMessage msg = new NdefMessage(
        new NdefRecord[] {
          createMimeRecord("application/com.jackhxs.cardbank", text.getBytes()),
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

}
