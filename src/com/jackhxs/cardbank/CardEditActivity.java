package com.jackhxs.cardbank;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.jackhxs.data.SimpleCard;
import com.jackhxs.remote.Constants;
import com.jackhxs.remote.JSONResultReceiver;
import com.jackhxs.remote.RemoteService;
import com.jackhxs.util.ImageUtil;

public class CardEditActivity extends Activity implements JSONResultReceiver.Receiver {
	private EditText nameField, phoneField, emailField;
	public JSONResultReceiver mReceiver;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.card_edit_activity_actions, menu);
		return true;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setDisplayShowHomeEnabled(false);

		setContentView(R.layout.card_edit_activity);

		nameField = (EditText) findViewById(R.id.card_flip_view_nameEdit);
		phoneField = (EditText) findViewById(R.id.card_flip_view_phoneNoEdit);
		emailField = (EditText) findViewById(R.id.card_flip_view_emailEdit);
		
		App app = (App) getApplication();
		SimpleCard myCard = app.myCards[0];

		nameField.setText(myCard.firstName + " " + myCard.lastName);
		phoneField.setText(myCard.phoneNo);
		emailField.setText(myCard.email);

		// android.R.id.content as the container for each fragment
		mReceiver = new JSONResultReceiver(new Handler());
		mReceiver.setReceiver(this);
	}

	public void saveEdit() {
		String name = nameField.getText().toString();
		String phone = phoneField.getText().toString();
		String email = emailField.getText().toString();
		App app = (App) getApplication();

		final Intent intent = new Intent(Intent.ACTION_SYNC, null, this,
				RemoteService.class);


		app.myCards[0].email = email;
		app.myCards[0].phoneNo = phone;
		app.myCards[0].firstName = name.split(" ")[0];
		app.myCards[0].lastName = name.split(" ")[1];

		Bitmap bg = com.jackhxs.util.ImageUtil.getBitmapFromAsset(this, "template_gold");
		
		Bitmap newCard = ImageUtil.GenerateCardImage(bg, name, email, phone);
		
		ImageView imgView = (ImageView) findViewById(R.id.card_flip_view_image);
		imgView.setImageBitmap(newCard);
		
		/*
		intent.putExtra("receiver", mReceiver);
		intent.putExtra("operation", (Parcelable) Operation.POST_CARD);
		intent.putExtra("accessToken", app.accessToken);
		intent.putExtra("simpleCardJSON", new Gson().toJson(app.myCards[0]));

		startService(intent);
		*/
	}

	public void cancelEdit() {
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_save: {
			saveEdit();
			return true;
		}
		case R.id.action_cancel: {
			cancelEdit();
			this.finish();
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		switch (resultCode) {
		case Constants.STATUS_FINISHED: {
			this.finish();
			break;
		}
		case Constants.STATUS_ERROR: {
			Log.e("remote", "FAILED TO POST CARD");
		}
		}
	}
}
