package com.jackhxs.cardbank;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.devspark.progressfragment.ProgressFragment;
import com.jackhxs.data.SimpleCard;
import com.jackhxs.remote.JSONResultReceiver;
import com.jackhxs.remote.RemoteService;
import com.jackhxs.remote.Constants.Operation;
import com.jackhxs.util.ImageUtil;

@SuppressLint("ValidFragment")
public class MyCardFragment extends ProgressFragment implements JSONResultReceiver.Receiver{
	private static final String TAG = "CardFragment";
	
	private View mContentView;
    
	private SimpleCard myCard;
	
	public JSONResultReceiver mReceiver;
    
	
    @SuppressLint("ValidFragment")
	public MyCardFragment(SimpleCard card) {
		myCard = card;
		//this.setHasOptionsMenu(true);
	}

    public MyCardFragment() {
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu items for use in the action bar
		inflater.inflate(R.menu.my_card_fragment_actions, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {

		mContentView = inflater.inflate(R.layout.my_card_fragment, container, false);
		
		mReceiver = new JSONResultReceiver(new Handler());
		mReceiver.setReceiver(this);
		
		final Intent intentCards = new Intent(Intent.ACTION_SYNC, null, getActivity(), RemoteService.class);
		
		intentCards.putExtra("receiver", mReceiver);
		intentCards.putExtra("operation",(Parcelable) Operation.GET_CARDS);
		
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
	
	private void updateUI(View rootView) {
		int index = 0;
		if (!myCard.imageUrl.equals("")) {
			index = Integer.parseInt(myCard.imageUrl);
		}
		
		String name, phone, email, companyName, address, jobTitle;
		
		// Inflate the layout for this fragment
		Log.e("paul", "create fragment");
		
		TextView txtView = (TextView) rootView
				.findViewById(R.id.card_flip_view_name);
		txtView.setText(myCard.firstName + " " + myCard.lastName);
		name = txtView.getText().toString();
		
		txtView = (TextView) rootView.findViewById(R.id.card_flip_view_phone);
		txtView.setText(myCard.phone);
		phone = txtView.getText().toString();

		txtView = (TextView) rootView.findViewById(R.id.card_flip_view_email);
		txtView.setText(myCard.email);
		email = txtView.getText().toString();
		
		txtView = (TextView) rootView.findViewById(R.id.card_flip_view_companyName);
        txtView.setText(myCard.companyName);
        companyName = txtView.getText().toString();

        txtView = (TextView) rootView.findViewById(R.id.card_flip_view_jobTitle);
        txtView.setText(myCard.jobTitle);
        jobTitle = txtView.getText().toString();

        txtView = (TextView) rootView.findViewById(R.id.card_flip_view_address);
        txtView.setText(myCard.address);
        address = txtView.getText().toString();
		
		ImageView imgView = (ImageView) rootView
				.findViewById(R.id.card_flip_view_image);
		
		Bitmap newCard = ImageUtil.GenerateCardImage(getActivity(), App.templateConfig[index], name, email, phone, companyName, address, jobTitle);
		imgView.setImageBitmap(newCard);
	}
	

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        
		// Handle action buttons
        switch(item.getItemId()) {
	        case R.id.action_edit:
	        	// create intent to perform web search for this planet
	            Intent intent = new Intent(
	    				getActivity(),
	    				CardEditActivity.class);
	    		startActivity(intent);
	    		return true;
	    			
	    		
	        default:
	            return super.onOptionsItemSelected(item);
        }
    }
	
	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		
		Log.d(TAG, "recieved Result in CardFragment");
		
		SimpleCard[] data = (SimpleCard[]) resultData.getParcelableArray("cards");
		
		App.myCards = data;
			
		myCard = App.myCards[0];
		
		updateUI(getView());
		
		setContentShown(true);
	}
}
