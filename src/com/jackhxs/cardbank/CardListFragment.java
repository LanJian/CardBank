package com.jackhxs.cardbank;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.devspark.progressfragment.ProgressFragment;
import com.jackhxs.data.APIResult;
import com.jackhxs.data.BusinessCard;
import com.jackhxs.remote.Constants.Operation;
import com.jackhxs.remote.JSONResultReceiver;
import com.jackhxs.remote.RemoteService;
import com.xtremelabs.imageutils.ImageLoader;

// Fragment with a list view of the contacts
public class CardListFragment extends ProgressFragment implements JSONResultReceiver.Receiver{
	private static final String TAG = "CardListFragment";
	
	public JSONResultReceiver mReceiver;
	private View mContentView;
    
	private TextView emptyMsg;
	private ListView myListView;
	private TEMPCardAdapter myAdapter;
	private ImageLoader mImageLoader;
	private boolean mIsRefer;
	private Integer toRefer;

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		
		mIsRefer = false;
		Bundle args = getArguments();
		if (args != null) {
			mIsRefer = args.getBoolean("isRefer", false);
			toRefer = args.getInt("toRefer", 0);
		}
	}

	public void onListItemClick(AdapterView<?> l, View v, int position, long id) {
		if (mIsRefer) {
			// refer the card

			final Intent serviceIntent = new Intent(Intent.ACTION_SYNC, null, getActivity(),
					RemoteService.class);

			serviceIntent.putExtra("operation", (Parcelable) Operation.REFER);
			serviceIntent.putExtra("referredTo", App.myContacts[position].getUserId()); 
			serviceIntent.putExtra("cardId", App.myContacts[toRefer].get_id());
			
			getActivity().startService(serviceIntent);

		} else {
			Intent intent = new Intent(getActivity(), CardFlipView.class);
			intent.putExtra("mode", "contact");
			intent.putExtra("position", position);
			startActivity(intent);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		mContentView = inflater.inflate(R.layout.activity_card_list, container, false);
		
		mReceiver = new JSONResultReceiver(new Handler());
		mReceiver.setReceiver(this);
		
		final Intent intentCards = new Intent(Intent.ACTION_SYNC, null, getActivity(), RemoteService.class);
		
		intentCards.putExtra("receiver", mReceiver);
		intentCards.putExtra("operation",(Parcelable) Operation.GET_CONTACTS);
		
		getActivity().startService(intentCards);
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Setup content view
        setContentView(mContentView);
        setContentShown(false);

        // Setup text for empty content
        setEmptyText("no data");
    }
	
	@Override
	public void onDestroyView() {
		super.onDestroy();
		if (mImageLoader != null) mImageLoader.destroy();
	}

	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		// TODO Auto-generated method stub
	
		Log.d(TAG, "recieved Result in CardListFragment");
		
		BusinessCard[] data = (BusinessCard[]) resultData.getParcelableArray("contacts");
		

		App.myContacts = data;
		
		try {
			App.lastUpdated = Util.ISO8601.toCalendar(resultData.getString("updatedAt"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		// This is needed to prevent generating repeat tabs on home screen
		if (resultData.getBoolean("longPoll", false)) {
			return;
		}
		
		Fragment listFragment = new CardListFragment();
		//addTab("Contact", listFragment, "myContact");
		*/
			
		mImageLoader = ImageLoader.buildImageLoaderForSupportFragment(this);
		//mImageLoader = ImageLoader.buildImageLoaderForFragment(this);

		myListView = (ListView) getView().findViewById(R.id.list_view);
		emptyMsg = (TextView) getView().findViewById(R.id.empty_message);
		
		// should use the ListView emptyView
		//myListView.setEmptyView(R.id.empty_message);
		if (App.myContacts.length > 0) {
			emptyMsg.setVisibility(View.GONE);
		}
		
		ArrayList<BusinessCard> list = new ArrayList<BusinessCard>();
		list.addAll(Arrays.asList(App.myContacts));
		
		myAdapter = new TEMPCardAdapter(getActivity(), R.layout.list_view_row, list, mImageLoader);
		myListView.setAdapter(myAdapter);

		myListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				onListItemClick(parent, v, position, id);

				if (mIsRefer) {
					ImageView referredIcon = (ImageView) v.findViewById(R.id.referredIcon);
					referredIcon.setVisibility(View.VISIBLE);
				}
			}
		});
		
		setContentShown(true);
	}
}
