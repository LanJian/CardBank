package com.jackhxs.cardbank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.jackhxs.data.SimpleCard;
import com.jackhxs.remote.Constants;
import com.jackhxs.remote.JSONResultReceiver;
import com.jackhxs.remote.RemoteService;

public class CardList extends Activity implements JSONResultReceiver.Receiver {
	private ListView myListView;
	private CardAdapter myAdapter;
	private SimpleCard[] myCards;

	public JSONResultReceiver mReceiver;

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		mReceiver = new JSONResultReceiver(new Handler());
		mReceiver.setReceiver(this);

		final Intent intent = new Intent(Intent.ACTION_SYNC, null, this,
				RemoteService.class);
		intent.putExtra("receiver", mReceiver);
		intent.putExtra("operation", 1);
		startService(intent);
	}

	public void onListItemClick(AdapterView<?> l, View v, int position, long id) {
		Intent intent = new Intent(this, CardFlipView.class);
		startActivity(intent);
	}

	public void onReceiveResult(int resultCode, Bundle resultData) {
		Log.e("paul", String.valueOf(resultCode));

		switch (resultCode) {
		case Constants.STATUS_FINISHED: {
			Log.e("paul", "--------*(&*(^&*%^^*($&---------|" + resultData.getParcelableArray("contacts") + "|");
			
			myCards = (SimpleCard[]) resultData.getParcelableArray("contacts");
			
			// setting a global reference
			((App) getApplication()).myCards = myCards;
			((App) getApplication()).ownCard = myCards[0];
			
			Log.e("paul", String.valueOf(myCards.length));

			setContentView(R.layout.activity_card_list);
			myListView = (ListView) findViewById(R.id.list_view);

			myAdapter = new CardAdapter(this, R.layout.list_view_row, myCards);

			myListView.setAdapter(myAdapter);

			myListView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> l, View v, int position,
						long id) {
					onListItemClick(l, v, position, id);
				}
			});

			break;
		}
		case Constants.STATUS_ERROR: {
			System.out.println("Error retrieving contacts");
		}
		}
	}
}
