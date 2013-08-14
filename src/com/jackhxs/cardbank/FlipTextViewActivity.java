package com.jackhxs.cardbank;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.aphidmobile.flip.FlipViewController;
import com.jackhxs.data.Card;

public class FlipTextViewActivity extends Activity {

	protected FlipViewController flipView;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle("test");

//		App app = (App) getApplication();
//		List<Card> cards = app.simpleApi.listCards(app.username);

//		Card[] cardData = new Card[cards.size()];
		Card[] cardData = new Card[0];
//		cards.toArray(cardData);

		flipView = new FlipViewController(this);

//		flipView.setAdapter(new CardAdapter(this, R.layout.list_view_row,
//				cardData));

		setContentView(flipView);
	}

	@Override
	protected void onResume() {
		super.onResume();
		flipView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		flipView.onPause();
	}
}
