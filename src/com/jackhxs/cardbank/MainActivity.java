package com.jackhxs.cardbank;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;

import com.jackhxs.data.SimpleCard;
import com.jackhxs.remote.Constants;
import com.jackhxs.remote.Constants.Operation;
import com.jackhxs.remote.JSONResultReceiver;
import com.jackhxs.remote.RemoteService;

public class MainActivity extends Activity implements
JSONResultReceiver.Receiver {

	public JSONResultReceiver mReceiver;

	// ----- Methods -----
	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getActionBar().setDisplayShowTitleEnabled(false);
		getActionBar().setDisplayShowHomeEnabled(false);

		// Notice that setContentView() is not used, because we use the root
		// android.R.id.content as the container for each fragment
		try {
			mReceiver = new JSONResultReceiver(new Handler());
			mReceiver.setReceiver(this);

			final Intent intent = new Intent(Intent.ACTION_SYNC, null, this,
					RemoteService.class);
			Log.e("paul", "after intent");
			intent.putExtra("receiver", mReceiver);
			intent.putExtra("operation",
					(Parcelable) Operation.GET_BOTH_CONTACT_AND_CARD);
			startService(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_edit: {
			Intent intent = new Intent(getApplicationContext(), CardEditActivity.class);
			startActivity(intent);
			return true;
		}
		case R.id.action_delete: {
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		Log.e("paul", String.valueOf(resultCode));

		switch (resultCode) {
		case Constants.STATUS_FINISHED: {
			Log.i("ContactList", "received card");

			App app = (App) getApplication();

			SimpleCard[] contacts = (SimpleCard[]) resultData.getParcelableArray("contacts");
			SimpleCard[] cards = (SimpleCard[]) resultData.getParcelableArray("cards");

			// setting a global reference in app
			app.myContacts = contacts;
			app.myCards = cards;

			// temp mock images
			contacts[0].image = R.drawable.mock_card;
			contacts[1].image = R.drawable.mock_card2;

			cards[0].image = R.drawable.mock_my_card;

			// setup action bar for tabs
			ActionBar actionBar = getActionBar();
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			actionBar.setDisplayShowTitleEnabled(false);

			Fragment cardFragment = new CardFragment((app.myCards)[0]);

			Tab tab = actionBar
					.newTab()
					.setText("My Card")
					.setTabListener(
							new TabListener<CardFragment>(this, "myCard",
									cardFragment));
			actionBar.addTab(tab);

			Fragment listFragment = new CardListFragment();
			tab = actionBar
					.newTab()
					.setText("My Contacts")
					.setTabListener(
							new TabListener<CardListFragment>(this, "myContacts",
									listFragment));
			actionBar.addTab(tab);

			break;
		}
		case Constants.STATUS_ERROR: {
			System.out.println("Error retrieving contacts and cards");
		}
		}
	}
}
