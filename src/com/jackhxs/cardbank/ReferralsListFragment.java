package com.jackhxs.cardbank;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.devspark.progressfragment.ProgressFragment;
import com.jackhxs.data.BusinessCard;
import com.jackhxs.remote.JSONResultReceiver;
import com.jackhxs.remote.RemoteService;
import com.jackhxs.remote.Constants.Operation;
import com.xtremelabs.imageutils.ImageLoader;


public class ReferralsListFragment extends ProgressFragment implements JSONResultReceiver.Receiver{
	private static final String TAG = "CardListFragment";
	
	public JSONResultReceiver mReceiver;
	private View mContentView;
    
	private TextView emptyMsg;

	private ListView myListView;
	private TEMPCardAdapter myAdapter;
    private ImageLoader mImageLoader;

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		
		mContentView = inflater.inflate(R.layout.list_view, container, false);
		
		mReceiver = new JSONResultReceiver(new Handler());
		mReceiver.setReceiver(this);
		
		final Intent intentCards = new Intent(Intent.ACTION_SYNC, null, getActivity(), RemoteService.class);
		
		intentCards.putExtra("receiver", mReceiver);
		intentCards.putExtra("operation",(Parcelable) Operation.LIST_REFERRALS);
		
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
	public void onReceiveResult(int resultCode, Bundle resultData) {
		// TODO Auto-generated method stub
		
		String dataType = resultData.getString("dataType");
		BusinessCard[] data = (BusinessCard[]) resultData.getParcelableArray(dataType);
		
		
		App.myReferrals = data;
		
		mImageLoader = ImageLoader.buildImageLoaderForSupportFragment(this);

		myListView = (ListView) mContentView.findViewById(R.id.list_view);
		
		emptyMsg = (TextView) mContentView.findViewById(R.id.empty_message);
		
		if (App.myReferrals.length > 0) {
			emptyMsg.setVisibility(View.GONE);
		}
		
		ArrayList<BusinessCard> list = new ArrayList<BusinessCard>();
        list.addAll(Arrays.asList(App.myReferrals));
		myAdapter = new TEMPCardAdapter(getActivity(), R.layout.refer_list_row, list, mImageLoader);
		myListView.setAdapter(myAdapter);
        myListView.setDivider(null);
        myListView.setDividerHeight(5);

        setContentShown(true);
	}
		
}
