package com.jackhxs.cardbank;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.jackhxs.data.SimpleCard;
import com.jackhxs.remote.Constants;
import com.jackhxs.remote.Constants.Operation;
import com.jackhxs.remote.JSONResultReceiver;
import com.jackhxs.remote.RemoteService;
import com.jackhxs.util.ImageUtil;

public class CardEditActivity extends Activity implements JSONResultReceiver.Receiver, OnClickListener {

	class FlingDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			try {
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
					return false;
				
				int templateLength = App.templateConfig.length;

				// right to left swipe
				if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					if (templateIndex < templateLength  - 1) {
						templateIndex += 1;
					}
					updateCardImage(templateIndex);
					Log.e("SWIPE", "r to l");

				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					if (templateIndex > 0) {
						templateIndex -= 1;
					}
					updateCardImage(templateIndex);
					Log.e("SWIPE", "l to r");  
				}

			} catch (Exception e) {
				// nothing
			}
			return false;
		}
	}

	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;

	private EditText nameField, phoneField, emailField;
	private ImageView cardImage;

	private int templateIndex;
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
		cardImage = (ImageView) findViewById(R.id.card_flip_view_image);

		SimpleCard myCard = App.myCards[0];

		nameField.setText(myCard.firstName + " " + myCard.lastName);
		phoneField.setText(myCard.phoneNo);
		emailField.setText(myCard.email);

		// android.R.id.content as the container for each fragment
		mReceiver = new JSONResultReceiver(new Handler());
		mReceiver.setReceiver(this);

		// Gesture detection
		gestureDetector = new GestureDetector(this, new FlingDetector());
		gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		};

		cardImage.setOnClickListener(this);
		cardImage.setOnTouchListener(gestureListener);
		templateIndex = 0;
		updateCardImage(templateIndex);
	}

	private void updateCardImage(int index) {
		String name = nameField.getText().toString();
		String phone = phoneField.getText().toString();
		String email = emailField.getText().toString();

		Bitmap newCard = ImageUtil.GenerateCardImage(this, App.templateConfig[index], name, email, phone);
		
		ImageView imgView = (ImageView) findViewById(R.id.card_flip_view_image);
		imgView.setImageBitmap(newCard);
	}

	public void saveEdit() {
		String name = nameField.getText().toString();
		String phone = phoneField.getText().toString();
		String email = emailField.getText().toString();

		final Intent intent = new Intent(Intent.ACTION_SYNC, null, this,
				RemoteService.class);

		App.myCards[0].email = email;
		App.myCards[0].phoneNo = phone;
		App.myCards[0].firstName = name.split(" ")[0];
		App.myCards[0].lastName = name.split(" ")[1];
		App.myCards[0].imageUrl = String.valueOf(templateIndex); 

		intent.putExtra("receiver", mReceiver);
		intent.putExtra("operation", (Parcelable) Operation.POST_CARD);
		intent.putExtra("accessToken", App.accessToken);
		intent.putExtra("simpleCardJSON", new Gson().toJson(App.myCards[0]));

		startService(intent);
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

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}
