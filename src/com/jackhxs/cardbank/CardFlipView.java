package com.jackhxs.cardbank;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.aphidmobile.flip.FlipViewController;
import com.jackhxs.data.SimpleCard;

public class CardFlipView extends Activity {

	protected FlipViewController myFlipView;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Bundle b = getIntent().getExtras();
//		Log.e("paul",
//				"--------*(&*(^&*%^^*($&---------|"
//						+ getIntent().getParcelableArrayExtra("contacts") + "|");
//		SimpleCard[] cards = (SimpleCard[]) getIntent()
//				.getParcelableArrayExtra("contacts");
		// SimpleCard[] cards = (SimpleCard[]) b.getParcelableArray("contacts");

		CardAdapter adapter = new CardAdapter(this, R.layout.card_flip_view,
				((App)getApplication()).myCards);

		myFlipView = new FlipViewController(this);

		myFlipView.setAdapter(adapter);

		setContentView(myFlipView);
	}

	@Override
	protected void onResume() {
		super.onResume();
		myFlipView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		myFlipView.onPause();
	}

}
