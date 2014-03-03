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
import android.widget.Toast;

import com.devspark.progressfragment.ProgressFragment;
import com.google.gson.Gson;
import com.jackhxs.data.BusinessCard;
import com.jackhxs.data.Template;
import com.jackhxs.remote.JSONResultReceiver;
import com.jackhxs.remote.RemoteService;
import com.jackhxs.remote.Constants.Operation;
import com.jackhxs.util.ImageUtil;

@SuppressLint("ValidFragment")
public class MyCardFragment extends ProgressFragment implements JSONResultReceiver.Receiver{
	private static final String TAG = "CardFragment";
	
	private View mContentView;
    
	private BusinessCard myCard;
	
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
	public MyCardFragment(BusinessCard card) {
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
		
		
		final Intent intentCards = new Intent(Intent.ACTION_SYNC, null, getActivity(), RemoteService.class);
		
		intentCards.putExtra("receiver", mReceiver);
		intentCards.putExtra("operation",(Parcelable) Operation.GET_CARDS);
		
		Log.i(TAG, "Loading my cards");
		getActivity().startService(intentCards);
		
		final Intent intentTemplates = new Intent(Intent.ACTION_SYNC, null, getActivity(), RemoteService.class);
		
		intentTemplates.putExtra("receiver", mReceiver);
		intentTemplates.putExtra("operation",(Parcelable) Operation.GET_TEMPLATES);
		
		Log.i(TAG, "Loading templates");
		getActivity().startService(intentTemplates);
		
		mContainer = (PagerContainer) mContentView.findViewById(R.id.pager_container);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = mContainer.getViewPager();
        
        // Add some spacing between cards
        mPager.setPageMargin(30); 
        
        mPager.setOnPageChangeListener(new SelectorChangeListener());
		
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
		if (myCard != null){
			Log.e(TAG, "Updating Card elements");
			
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
			BusinessCard[] myCards = (BusinessCard[]) resultData.getParcelableArray("cards");
			
			App.myCards = myCards;
			/*
			 * TODO This line crashes after long break
			 */
			myCard = App.myCards[0];
			
			Template currentTemplate = myCard.getTemplate();
			
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
	

	private void setupInitialFields(BusinessCard myCard2) {
		
		editTextName = (EditText) mContentView.findViewById(R.id.edit_name);
		editTextName.setText(myCard.getFirstName() + " " + myCard.getLastName());
		editTextName.addTextChangedListener(new cardChangeListener(FieldName.NAME));
		
		editCompanyName = (EditText) mContentView.findViewById(R.id.edit_company);
		editCompanyName.setText(myCard.getCompanyName());
		editCompanyName.addTextChangedListener(new cardChangeListener(FieldName.COMPANY));
		
		editJobTitle = (EditText) mContentView.findViewById(R.id.edit_job_title);
		editJobTitle.setText(myCard.getJobTitle());
		editJobTitle.addTextChangedListener(new cardChangeListener(FieldName.TITLE));
		
		editEmail = (EditText) mContentView.findViewById(R.id.edit_email);
		editEmail.setText(myCard.getEmail());
		editEmail.addTextChangedListener(new cardChangeListener(FieldName.EMAIL));
		
		editPhone = (EditText) mContentView.findViewById(R.id.edit_phone);
		editPhone.setText(PhoneNumberUtils.formatNumber(myCard.getPhone()));
		editPhone.addTextChangedListener(new cardChangeListener(FieldName.PHONE));
		
		editAddress = (EditText) mContentView.findViewById(R.id.edit_address);
		editAddress.setText(myCard.getAddress());
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
			//myCard = App.myCards[0];
			
			switch (mFieldName) {
				case NAME:
				String[] name = s.toString().split(" ");
				Log.i(TAG, name.toString());
				
				if (name.length > 1) {
					myCard.setFirstName(name[0]);
					myCard.setLastName(name[1]);
				}
				else {
					myCard.setFirstName(name[0]);
					myCard.setLastName(""); // clear last name if not present
				}
				break;
				case COMPANY:
					String company = s.toString();
					myCard.setCompanyName(company);
					break;
				case TITLE:
					String jobTitle = s.toString();
					myCard.setJobTitle(jobTitle);
					break;
				case EMAIL:
					String email = s.toString();
					myCard.setEmail(email);
					break;
				case PHONE:
					String phone = s.toString();
					myCard.setPhone(phone);
					break;
				case ADDRESS:
					String address = s.toString();
					myCard.setAddress(address);
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
			
			delaySaveCard();
		}
		
		// not used
		@Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}


	} 
	
	private void delaySaveCard() {
		timer.cancel();
		updatePending = true;
		timer = new Timer();
        timer.schedule(new saveCardTimer(), 2000); // 2s
	}
	
	private class saveCardTimer extends TimerTask {

		@Override
		public void run() {
			saveCard();
		}
		
	}
	
	public class SelectorChangeListener implements ViewPager.OnPageChangeListener {
		// unused
		@Override public void onPageScrollStateChanged(int state) {}
		@Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

		@Override
		public void onPageSelected(int position) {
			Toast.makeText(getActivity(), "template " + position + " selected", Toast.LENGTH_SHORT).show();	
			
			myCard.setTemplate(templates.get(position));
			
			myCard.getTemplateConfig().setBaseTemplate(myCard.getTemplate().templateName);
			
			delaySaveCard();
		}

	    
	}
}
