package com.jackhxs.cardbank;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.jackhxs.data.SimpleCard;
import com.jackhxs.remote.Constants;
import com.jackhxs.remote.JSONResultReceiver;

public class CardListFragment extends Fragment {
	private ListView myListView;
	private CardAdapter myAdapter;

	public JSONResultReceiver mReceiver;

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
	}


	public void onListItemClick(AdapterView<?> l, View v, int position, long id) {
		Intent intent = new Intent(getActivity(), CardFlipView.class);
		intent.putExtra("mode", "contact");
		startActivity(intent);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.activity_card_list, container, false);

		App app = (App) getActivity().getApplication();

		myListView = (ListView) view.findViewById(R.id.list_view);
		myAdapter = new CardAdapter(getActivity(), R.layout.list_view_row, app.myContacts);
		myListView.setAdapter(myAdapter);
		myListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> l, View v, int position,
					long id) {
				onListItemClick(l, v, position, id);
			}
		});

		return view;
	}

//	public void onReceiveResult(int resultCode, Bundle resultData) {
//		Log.e("paul", String.valueOf(resultCode));
//
//		switch (resultCode) {
//		case Constants.STATUS_FINISHED: {
//			Log.i("ContactList", "received card");
//
//			myContacts = (SimpleCard[]) resultData.getParcelableArray("contacts");
//			myCards = (SimpleCard[]) resultData.getParcelableArray("cards");
//
//			// setting a global reference in app
//			((App) getApplication()).myContacts = myContacts;
//			((App) getApplication()).myCards = myCards;
//
//			setContentView(R.layout.activity_card_list);
//
//			myListView = (ListView) findViewById(R.id.list_view);
//			myAdapter = new CardAdapter(this, R.layout.list_view_row, myContacts);
//			myListView.setAdapter(myAdapter);
//			myListView.setOnItemClickListener(new OnItemClickListener() {
//				public void onItemClick(AdapterView<?> l, View v, int position,
//						long id) {
//					onListItemClick(l, v, position, id);
//				}
//			});
//
//			break;
//		}
//		case Constants.STATUS_ERROR: {
//			System.out.println("Error retrieving contacts and cards");
//		}
//		}
//	}
}