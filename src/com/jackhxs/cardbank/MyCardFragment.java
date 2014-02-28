package com.jackhxs.cardbank;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.devspark.progressfragment.ProgressFragment;
import com.google.gson.Gson;
import com.jackhxs.data.SimpleCard;
import com.jackhxs.data.Template;
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
    
	private enum FieldName {
		NAME,
		COMPANY,
		TITLE,
		EMAIL,
		PHONE,
		ADDRESS
	}; 
	
	private EditText editTextName;
	private EditText editCompanyName;
	private EditText editJobTitle;
	private EditText editEmail;
	private EditText editPhone;
	private EditText editAddress;
	
	private Timer timer=new Timer();
    private boolean updatePending = false;
    
    private ViewPager mPager;

    private PagerAdapter mPagerAdapter;
    private PagerContainer mContainer;

    private ArrayList<Template> templates;
    
	/*
	 * TODO This isn't needed. needs to be removed in future
	 */
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
		
		// Create an empty list to store templates
		templates = new ArrayList<Template>();
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
		
		
		final Intent intentTemplates = new Intent(Intent.ACTION_SYNC, null, getActivity(), RemoteService.class);
		
		intentTemplates.putExtra("receiver", mReceiver);
		intentTemplates.putExtra("operation",(Parcelable) Operation.GET_TEMPLATES);
		
		getActivity().startService(intentTemplates);
		
		
		final Intent intentCards = new Intent(Intent.ACTION_SYNC, null, getActivity(), RemoteService.class);
		
		intentCards.putExtra("receiver", mReceiver);
		intentCards.putExtra("operation",(Parcelable) Operation.GET_CARDS);
		
		getActivity().startService(intentCards);
		
		
		mContainer = (PagerContainer) mContentView.findViewById(R.id.pager_container);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = mContainer.getViewPager();
        //mPagerAdapter = new BusinessTemplateAdapter(getFragmentManager());
        //mPager.setAdapter(mPagerAdapter);
		
        mPager.setPageMargin(30);
        
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
	
	private void updateCardUI(View rootView) {
		int index = 0;
		
		// Only run this when the card data is ready
		if (myCard != null){
			if (!myCard.imageUrl.equals("")) {
				index = Integer.parseInt(myCard.imageUrl);
			}
			
			// Inflate the layout for this fragment
			Log.e("paul", "create fragment");
			
			
			/////// start delete here
			/*
			ImageView imgView = (ImageView) rootView.findViewById(R.id.card_flip_view_image);
			
			Bitmap newCard = ImageUtil.GenerateCardImage(getActivity(), 
					App.templateConfig[index], 
					myCard.firstName + " " + myCard.lastName, 
					myCard.email, 
					myCard.phone, 
					myCard.companyName, 
					myCard.address, 
					myCard.jobTitle);
			
			imgView.setImageBitmap(newCard);
			*/
			///////// Finish delete here
			
			
			mPagerAdapter = new BusinessTemplateAdapter(getFragmentManager(), templates, myCard);
	        mPager.setAdapter(mPagerAdapter);
			
	        mPager.setOffscreenPageLimit(mPagerAdapter.getCount());
	        
	        //If hardware acceleration is enabled, you should also remove
	        // clipping on the pager for its children.
	        mPager.setClipChildren(false);
	        
	        mPagerAdapter.notifyDataSetChanged();
		}
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
		
		Log.d(TAG, "recieved Result in MyCardFragment");
		
		Log.d(TAG, resultData.getString("action") );
		
		
		if (resultData.getString("action") != null && resultData.getString("action").equals(Operation.POST_CARD.toString())) {
			// card saved successfully
			Log.d(TAG, "card saved successfully");
			updatePending = false;
		}
		else if (resultData.getString("action") != null && resultData.getString("action").equals(Operation.GET_CARDS.toString())){
			SimpleCard[] myCards = (SimpleCard[]) resultData.getParcelableArray("cards");
			
			App.myCards = myCards;
			/*
			 * TODO This line crashes after long break
			 */
			myCard = App.myCards[0];
			
			Template currentTemplate = myCard.template;
			
			templates.add(0,currentTemplate);
			
			Log.i(TAG, currentTemplate.toString());
			
			setupInitialFields(myCard);
			
			updateCardUI(getView());
			
			setContentShown(true);
		} else if (resultData.getString("action") != null && resultData.getString("action").equals(Operation.GET_TEMPLATES.toString())) {
			ArrayList<Template> moreTemplates = resultData.getParcelableArrayList("templates");
			
			Log.i(TAG, "recieved " + moreTemplates.size() + " templates");
			
			templates.addAll(moreTemplates);
			
			for (Template t : templates) {
				Log.i(TAG, t.toString());
			}
			
			updateCardUI(getView());
			
			
		} else {
			Log.e(TAG, "UNSUPPORTED OPERATION");
		}
	}
	

	private void setupInitialFields(SimpleCard myCard2) {
		
		editTextName = (EditText) mContentView.findViewById(R.id.edit_name);
		editTextName.setText(myCard.firstName + " " + myCard.lastName);
		editTextName.addTextChangedListener(new cardChangeListener(FieldName.NAME));
		
		editCompanyName = (EditText) mContentView.findViewById(R.id.edit_company);
		editCompanyName.setText(myCard.companyName);
		editCompanyName.addTextChangedListener(new cardChangeListener(FieldName.COMPANY));
		
		editJobTitle = (EditText) mContentView.findViewById(R.id.edit_job_title);
		editJobTitle.setText(myCard.jobTitle);
		editJobTitle.addTextChangedListener(new cardChangeListener(FieldName.TITLE));
		
		editEmail = (EditText) mContentView.findViewById(R.id.edit_email);
		editEmail.setText(myCard.email);
		editEmail.addTextChangedListener(new cardChangeListener(FieldName.EMAIL));
		
		editPhone = (EditText) mContentView.findViewById(R.id.edit_phone);
		editPhone.setText(PhoneNumberUtils.formatNumber(myCard.phone));
		editPhone.addTextChangedListener(new cardChangeListener(FieldName.PHONE));
		
		editAddress = (EditText) mContentView.findViewById(R.id.edit_address);
		editAddress.setText(myCard.address);
		editAddress.addTextChangedListener(new cardChangeListener(FieldName.ADDRESS));
		
		
	}

	private void saveCard() {
		if (myCard != null && updatePending) {
			
			final Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), RemoteService.class);

			
			intent.putExtra("receiver", mReceiver);
			intent.putExtra("operation", (Parcelable) Operation.UPDATE_CARD);
			intent.putExtra("accessToken", App.sessionId);
			intent.putExtra("simpleCardJSON", new Gson().toJson(App.myCards[0]));

			getActivity().startService(intent);
			
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		if (updatePending) {
			Log.e(TAG, "saving now");
			timer.cancel();
			 
			saveCard();
		}
		
	}

	private class cardChangeListener extends PhoneNumberFormattingTextWatcher {
		FieldName mFieldName;
		
		public cardChangeListener(FieldName mFieldName) {
			super();
			this.mFieldName = mFieldName;
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			myCard = App.myCards[0];
			
			switch (mFieldName) {
				case NAME:
				String[] name = s.toString().split(" ");
				Log.i(TAG, name.toString());
				
				if (name.length > 1) {
					App.myCards[0].firstName = name[0];
					App.myCards[0].lastName = name[1];
				}
				else {
					App.myCards[0].firstName = name[0];
					App.myCards[0].lastName = ""; // clear last name if not present
				}
				break;
				case COMPANY:
					String company = s.toString();
					App.myCards[0].companyName = company;
					break;
				case TITLE:
					String jobTitle = s.toString();
					App.myCards[0].jobTitle = jobTitle;
					break;
				case EMAIL:
					String email = s.toString();
					App.myCards[0].email = email;
					break;
				case PHONE:
					String phone = s.toString();
					App.myCards[0].phone = phone;
					break;
				case ADDRESS:
					String address = s.toString();
					App.myCards[0].address = address;
					break;
				default:
					break;
					
			}
			updateCardUI(getView());
		}
		
		@Override 
		public void afterTextChanged(Editable s) {
			if (mFieldName == FieldName.PHONE) {
				super.afterTextChanged(s);
			}
			
			timer.cancel();
			updatePending = true;
			timer = new Timer();
            timer.schedule(new saveCardTimer(), 5000); // 0.5s
		}
		
		// not used
		@Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}


	} 
	
	private class saveCardTimer extends TimerTask {

		@Override
		public void run() {
			saveCard();
		}
		
	}
}
