package com.jackhxs.cardbank;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.devspark.progressfragment.ProgressFragment;
import com.jackhxs.cardbank.customviews.PagerContainer;
import com.jackhxs.cardbank.customviews.TextConfigView;
import com.jackhxs.data.BusinessCard;
import com.jackhxs.data.template.Template;
import com.jackhxs.data.template.TemplateProperties;
import com.jackhxs.data.template.TextConfig;
import com.jackhxs.remote.Constants;
import com.jackhxs.remote.JSONResultReceiver;
import com.jackhxs.remote.RemoteService;
import com.jackhxs.remote.Constants.Operation;
import com.jackhxs.util.Utils;
import com.jackhxs.util.CardBeamFragment;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SVBar;

@SuppressLint("ValidFragment")
public class MyCardFragment extends CardBeamFragment implements JSONResultReceiver.Receiver{
	private static final String TAG = "CardFragment";
	
	// View holder
	private View mContentView;
    
	// holds the user's current data (values and configurations/template)
	private BusinessCard myCard;
	
	// Used to make API calls
	public JSONResultReceiver mReceiver;
    
	// Used to register editText change listeners
	private enum FieldName {
		NAME,
		COMPANY,
		TITLE,
		EMAIL,
		PHONE,
		ADDRESS
	}; 
	
	// EditTexts and TextConfigViews (Custom Views)  
	private EditText editTextName;
	private TextConfigView nameConfig;
	
	private EditText editCompanyName;
	private TextConfigView companyConfig;
	
	private EditText editJobTitle;
	private TextConfigView jobTitleConfig;
	
	private EditText editEmail;
	private TextConfigView emailConfig;
	
	private EditText editPhone;
	private TextConfigView phoneConfig;
	
	private EditText editAddress;
	private TextConfigView addressConfig;
	
	
	// Timer and bool used to save changes
	private Timer timer=new Timer();
    private boolean updatePending = false;
    
    // ViewPager and adapter used to display cards
    private ViewPager mPager;

    private BusinessTemplateAdapter mPagerAdapter;
    private PagerContainer mContainer;

    // List of templates
    private ArrayList<Template> templates;
    
    
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * LIFECYCLE EVENTS
     *////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		// Create an empty list to store templates
		templates = new ArrayList<Template>();
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {

		mContentView = inflater.inflate(R.layout.my_card_fragment, container, false);
		
		mReceiver = new JSONResultReceiver(new Handler());
		mReceiver.setReceiver(this);
		
		
		getMyCard();
		getTemplates();
		
		mContainer = (PagerContainer) mContentView.findViewById(R.id.pager_container);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = mContainer.getViewPager();
        
        Utils.ConfigurePagerSize(mPager, getActivity());
        
        // Add some spacing between cards
        mPager.setPageMargin((int) (10 * Utils.getDensity(getActivity()))); 
        
        mPager.setOnPageChangeListener(new TemplateChangeListener());
		
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
	public void onPause() {
		super.onPause();
		
		if (updatePending) {
			Log.e(TAG, "saving now");
			timer.cancel();
			 
			saveCard();
		}
		
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * API Calls and CallBacks
	 *////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * This method will load a The user's card
	 */
	private void getMyCard() {
		final Intent intentCards = new Intent(Intent.ACTION_SYNC, null, getActivity(), RemoteService.class);
		
		intentCards.putExtra("receiver", mReceiver);
		intentCards.putExtra("operation",(Parcelable) Operation.GET_CARDS);
		
		Log.i(TAG, "Loading my cards");
		getActivity().startService(intentCards);
	}
	
	/**
	 * This method will load a list of availiable templates
	 */
	private void getTemplates() {
		final Intent intentCards = new Intent(Intent.ACTION_SYNC, null, getActivity(), RemoteService.class);
		
		intentCards.putExtra("receiver", mReceiver);
		intentCards.putExtra("operation",(Parcelable) Operation.GET_TEMPLATES);
		
		Log.i(TAG, "Loading my cards");
		getActivity().startService(intentCards);
	}
	
	/**
	 * This method will actually perform the API call to save the user's changes to the server. 
	 * 
	 * Please Use {@link #delaySaveCard() delaySaveCard} to save changes unless you want to save changes immediately (IE when exiting)
	 */
	private void saveCard() {
		if (myCard != null && updatePending) {
			
			final Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), RemoteService.class);

			
			intent.putExtra("receiver", mReceiver);
			intent.putExtra("operation", (Parcelable) Operation.UPDATE_CARD);
			intent.putExtra("accessToken", App.sessionId);
			//intent.putExtra("simpleCardJSON", new Gson().toJson(App.myCards[0]));
			intent.putExtra("BusinessCard", myCard);

			getActivity().startService(intent);
			
		}
	}
	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		
		Log.d(TAG, "recieved Result in MyCardFragment");
		
		switch (resultCode) {

		case Constants.STATUS_FINISHED: {
			
			Operation command = resultData.getParcelable("operation");
			if (command != null){
				switch (command) {
				case POST_CARD:
				case UPDATE_CARD:
						// card saved successfully
					Log.d(TAG, "card saved successfully");
					updatePending = false;
					break;
				
				case GET_CARDS: 
					BusinessCard[] myCards = (BusinessCard[]) resultData.getParcelableArray("cards");
					
					App.myCards = myCards;
					/*
					 * TODO This line crashes after long break
					 */
					myCard = myCards[0];
					
					Template currentTemplate = myCard.getTemplate();
					
					templates.add(0,currentTemplate);
					
					Log.i(TAG, currentTemplate.toString());
					
					setupInitialFields(myCard);
					
					addNewCards();
					
					setContentShown(true);
					
					break;
				case GET_TEMPLATES:
					ArrayList<Template> moreTemplates = resultData.getParcelableArrayList("templates");
					
					Log.i(TAG, "recieved " + moreTemplates.size() + " templates");
					
					templates.addAll(moreTemplates);
					
					for (Template t : templates) {
						Log.i(TAG, t.toString());
					}
					
					addNewCards();
					
					break;
				default:
					Log.e(TAG, "UNSUPPORTED OPERATION " + command.toString());
				}
			
			}
			
			break;
		}
		case Constants.STATUS_ERROR: 
			Operation command = resultData.getParcelable("operation");
			if (command != null){
				switch (command) {
				case POST_CARD:
				case UPDATE_CARD:
					Log.e(TAG, "failed to save changes: try again");
					// retry to save card by calling saveCard(); again. need to keep track of failed API calls
					
					break;
				case GET_CARDS: 
					Log.e(TAG, "failed to load user cards: try again");
					// retry to download user's cards  by calling getMyCard(); again. need to keep track of failed API calls
					
					break;
				case GET_TEMPLATES:
					Log.e(TAG, "failed to load templates: try again");
					// retry to download user's cards  by calling gettemplates(); again. need to keep track of failed API calls
					
					break;
				default:
					Log.e(TAG, "UNSUPPORTED OPERATION " + command.toString());
					break;
				}
			
			}
			
			break;
		}
		
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * View Controls and delegation methods
	 *////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	
	/**
	 * This method creates the ViewPager pages when there is new data to be displayed.
	 * 
	 *  e.g. when there are new templates.
	 *  
	 *  It will also created the PagerAdapter if not already setup.
	 */
	private void addNewCards() {
		if (myCard != null){
			Log.e(TAG, "Loading cards from templates");
			
			if (mPagerAdapter == null) {
				mPagerAdapter = new BusinessTemplateAdapter(getFragmentManager(), templates, myCard);
		        mPager.setAdapter(mPagerAdapter);
				
		        mPager.setOffscreenPageLimit(4);
		        
		        //If hardware acceleration is enabled, you should also remove
		        // clipping on the pager for its children.
		        mPager.setClipChildren(false);
		        
			}
	        mPagerAdapter.notifyDataSetChanged();
		}
	}
	
	/**
	 * This method updates all the content in the "pages" in the ViewPager. IE when the user makes changes to content
	 * 
	 * @param myCard The business card object holding the updated data.
	 */
	private void updateCardUI(BusinessCard myCard) {
		if (myCard != null){
			Log.e(TAG, "Updating Card elements");
			
			mPagerAdapter.updateCardContent(myCard);
		}
	}
	
	
	
	
	/**
	 * This method will setup the editText fields and the TextConfigViews with their listeners, and populate with the initial values
	 * 
	 * This method should only be called once when the User's card data have been fetched from the server. 
	 * 
	 * @param myCard The business card object holding the initial data.
	 */
	private void setupInitialFields(BusinessCard myCard) {
		
		editTextName = (EditText) mContentView.findViewById(R.id.edit_name);
		editTextName.setText(myCard.getFirstName() + " " + myCard.getLastName());
		editTextName.addTextChangedListener(new cardChangeListener(FieldName.NAME));
		
		nameConfig = (TextConfigView) mContentView.findViewById(R.id.name_config);
		nameConfig.setColor(Color.parseColor(myCard.getTemplate().getProperties().name.color));
		nameConfig.setText((myCard.getFirstName() == null || myCard.getFirstName().equalsIgnoreCase("")) ? "" : myCard.getFirstName().substring(0, 1));
		nameConfig.setOnClickListener(new TextConfigClickListener(myCard.getTemplate().getProperties().name, 
				(myCard.getTemplateConfig().getProperties() == null) ? null :myCard.getTemplateConfig().getProperties().name, FieldName.NAME));
		
		editCompanyName = (EditText) mContentView.findViewById(R.id.edit_company);
		editCompanyName.setText(myCard.getCompanyName());
		editCompanyName.addTextChangedListener(new cardChangeListener(FieldName.COMPANY));
		
		companyConfig = (TextConfigView) mContentView.findViewById(R.id.company_config);
		companyConfig.setColor(Color.parseColor(myCard.getTemplate().getProperties().companyName.color));
		companyConfig.setText((myCard.getCompanyName() == null || myCard.getCompanyName().equalsIgnoreCase("")) ? "" : myCard.getCompanyName().substring(0, 1));
		companyConfig.setOnClickListener(new TextConfigClickListener(myCard.getTemplate().getProperties().companyName,
				(myCard.getTemplateConfig().getProperties() == null) ? null :myCard.getTemplateConfig().getProperties().companyName, FieldName.COMPANY));
		
		editJobTitle = (EditText) mContentView.findViewById(R.id.edit_job_title);
		editJobTitle.setText(myCard.getJobTitle());
		editJobTitle.addTextChangedListener(new cardChangeListener(FieldName.TITLE));
		
		jobTitleConfig = (TextConfigView) mContentView.findViewById(R.id.job_title_config);
		jobTitleConfig.setColor(Color.parseColor(myCard.getTemplate().getProperties().jobTitle.color));
		jobTitleConfig.setText((myCard.getJobTitle() == null || myCard.getJobTitle().equalsIgnoreCase("")) ? "" : myCard.getJobTitle().substring(0, 1));
		jobTitleConfig.setOnClickListener(new TextConfigClickListener(myCard.getTemplate().getProperties().jobTitle,
				(myCard.getTemplateConfig().getProperties() == null) ? null :myCard.getTemplateConfig().getProperties().jobTitle, FieldName.TITLE));
		
		editEmail = (EditText) mContentView.findViewById(R.id.edit_email);
		editEmail.setText(myCard.getEmail());
		editEmail.addTextChangedListener(new cardChangeListener(FieldName.EMAIL));
		
		emailConfig = (TextConfigView) mContentView.findViewById(R.id.email_config);
		emailConfig.setColor(Color.parseColor(myCard.getTemplate().getProperties().email.color));
		emailConfig.setText((myCard.getEmail() == null || myCard.getEmail().equalsIgnoreCase("")) ? "" : myCard.getEmail().substring(0, 1));
		emailConfig.setOnClickListener(new TextConfigClickListener(myCard.getTemplate().getProperties().email,
				(myCard.getTemplateConfig().getProperties() == null) ? null :myCard.getTemplateConfig().getProperties().email, FieldName.EMAIL));
		
		editPhone = (EditText) mContentView.findViewById(R.id.edit_phone);
		editPhone.setText(PhoneNumberUtils.formatNumber(myCard.getPhone()));
		editPhone.addTextChangedListener(new cardChangeListener(FieldName.PHONE));
		
		phoneConfig = (TextConfigView) mContentView.findViewById(R.id.phone_config);
		phoneConfig.setColor(Color.parseColor(myCard.getTemplate().getProperties().phone.color));
		phoneConfig.setText((myCard.getPhone() == null || myCard.getPhone().equalsIgnoreCase("")) ? "" : (PhoneNumberUtils.stripSeparators(myCard.getPhone())).substring(0, 1));
		phoneConfig.setOnClickListener(new TextConfigClickListener(myCard.getTemplate().getProperties().phone,
				(myCard.getTemplateConfig().getProperties() == null) ? null :myCard.getTemplateConfig().getProperties().phone, FieldName.PHONE));
		
		editAddress = (EditText) mContentView.findViewById(R.id.edit_address);
		editAddress.setText(myCard.getAddress());
		editAddress.addTextChangedListener(new cardChangeListener(FieldName.ADDRESS));
		
		addressConfig = (TextConfigView) mContentView.findViewById(R.id.address_config);
		addressConfig.setColor(Color.parseColor(myCard.getTemplate().getProperties().address.color));
		addressConfig.setText((myCard.getAddress() == null || myCard.getAddress().equalsIgnoreCase("")) ? "" : myCard.getAddress().substring(0, 1));
		addressConfig.setOnClickListener(new TextConfigClickListener(myCard.getTemplate().getProperties().address,
				(myCard.getTemplateConfig().getProperties() == null) ? null :myCard.getTemplateConfig().getProperties().address, FieldName.ADDRESS));
		
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * Functions used to save the user changes to the user's profile online
	 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * This method will start a 2 second timer that save changes when the timer reaches zero. 
	 * Calling this method again during the 2 second period will cancel the previous timer and start a new one that will save all previous changes
	 * 
	 */
	private void delaySaveCard() {
		timer.cancel();
		updatePending = true;
		timer = new Timer();
        timer.schedule(new saveCardTimer(), 2000); // 2s
	}
	
	/**
	 * This is the TimerTask that will be triggered when the timer from @link {@link MyCardFragment#delaySaveCard() delaySaveCard}
	 * 
	 * DO NOT use this class directly.
	 * 
	 */
	private class saveCardTimer extends TimerTask {

		@Override
		public void run() {
			saveCard();
		}
		
	}
	
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * Listener classes
	 *///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	/**
	 * This object listens to changes in the input fields + formats the phone number field automatically. It then saves all changes
	 */
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
				
				if (s.toString().length() > 0) {
					nameConfig.setText(s.toString().substring(0, 1));
				}
				
				nameConfig.setColor(Color.parseColor(myCard.getTemplate().getProperties().name.color));
				
				break;
				case COMPANY:
					String company = s.toString();
					myCard.setCompanyName(company);
					if (company.length() > 0) {
						companyConfig.setText(company.substring(0, 1));
					}
					break;
				case TITLE:
					String jobTitle = s.toString();
					myCard.setJobTitle(jobTitle);
					if (jobTitle.length() > 0) {
						jobTitleConfig.setText(jobTitle.substring(0, 1));
					}
					break;
				case EMAIL:
					String email = s.toString();
					myCard.setEmail(email);
					if (email.length() > 0) {
						emailConfig.setText(email.substring(0, 1));
					}
					break;
				case PHONE:
					String phone = s.toString();
					myCard.setPhone(phone);
					if (phone.length() > 0) {
						phoneConfig.setText((PhoneNumberUtils.stripSeparators(myCard.getPhone())).substring(0, 1));
					}
					break;
				case ADDRESS:
					String address = s.toString();
					myCard.setAddress(address);
					if (address.length() > 0) {
						addressConfig.setText(address.substring(0, 1));
					}
					break;
				default:
					break;
					
			}
			updateCardUI(myCard);
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
	
	
	/**
	 * This object listens for template changes and saves them automatically
	 */
	public class TemplateChangeListener implements ViewPager.OnPageChangeListener {
		// unused
		@Override public void onPageScrollStateChanged(int state) {}
		@Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

		@Override
		public void onPageSelected(int position) {
			myCard.setTemplate(templates.get(position));
			
			myCard.getTemplateConfig().setBaseTemplate(myCard.getTemplate().getTemplateName());
			
			/*
			 * TODO update left and top values of template config`
			 * 
			 * TODO Done, but might was to reconsider only resetting top and left values
			 */
			/*
			*/	
			myCard.getTemplateConfig().setProperties(templates.get(position).getProperties());
			
			delaySaveCard();
		}
		
	}
	
	/**
	 * This object listens to TextConfig changes and saves them, and updates the view.
	 */
	
	public class TextConfigClickListener implements View.OnClickListener {

		private TextConfig textConfig;
		private TextConfig personalTextConfig;
		private FieldName fieldName;
		
		private ColorPicker picker;
		private SVBar svBar;
		
		public TextConfigClickListener(TextConfig textConfig, TextConfig personalTextConfig, FieldName fieldName) {
			super();
			this.textConfig = textConfig;
			this.personalTextConfig = personalTextConfig;
			this.fieldName = fieldName;
		}

		@Override
		public void onClick(final View view) {
			
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
			
			// set title
			//alertDialogBuilder.setTitle("Select");
			 
			// set dialog message
			alertDialogBuilder
				.setView(getActivity().getLayoutInflater().inflate(R.layout.text_customize_dialog, null))
				.setCancelable(false)
				.setPositiveButton("OK",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, close
						// current activity
						//MainActivity.this.finish();
						Log.i("Color int", Integer.toString(picker.getColor()));
						Log.i("Color String",String.format("#%06X", 0xFFFFFF & picker.getColor()));
						
						UpdateTemplateConfig(myCard, textConfig, personalTextConfig, String.format("#%06X", 0xFFFFFF & picker.getColor()), fieldName);
						
						MyCardFragment.this.updateCardUI(myCard);
						
						((TextConfigView)view).setColor( picker.getColor() ); 
						
						MyCardFragment.this.delaySaveCard();
					}

				  })
				.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});
 
			// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
			// show it
			alertDialog.show();
			
			
			picker = (ColorPicker) alertDialog.findViewById(R.id.picker);
			svBar = (SVBar) alertDialog.findViewById(R.id.svbar);
			
			picker.addSVBar(svBar);
			
			picker.setNewCenterColor(Color.parseColor(textConfig.color));
			picker.setOldCenterColor(Color.parseColor(textConfig.color));
			picker.setColor(Color.parseColor(textConfig.color));
		 	
		}
		
	}
	
	private BusinessCard UpdateTemplateConfig(BusinessCard myCard, TextConfig textConfig, TextConfig personalTextConfig, String color, FieldName fieldName) {

		if (myCard.getTemplateConfig().getProperties() == null) {
			myCard.getTemplateConfig().setProperties(new TemplateProperties());
		}
		
		if (personalTextConfig == null) {
			personalTextConfig = new TextConfig(textConfig.left, textConfig.top, textConfig.color);
		} else {
			personalTextConfig.left = textConfig.left;
			personalTextConfig.top = textConfig.top;
		}
		
		personalTextConfig.color = color;
		
		switch (fieldName) {
		case NAME:
			// update the TemplateConfig in order to save
			myCard.getTemplateConfig().getProperties().name = personalTextConfig;
			
			// Update the Current Template in order to display correctly
			myCard.getTemplate().getProperties().name.color = color;
			break;
		case COMPANY:
			// update the TemplateConfig in order to save
			myCard.getTemplateConfig().getProperties().companyName = personalTextConfig;
			
			// Update the Current Template in order to display correctly
			myCard.getTemplate().getProperties().companyName.color = color;
			break;
		case TITLE:
			// update the TemplateConfig in order to save
			myCard.getTemplateConfig().getProperties().jobTitle = personalTextConfig;
			
			// Update the Current Template in order to display correctly
			myCard.getTemplate().getProperties().jobTitle.color = color;
			break;
		case EMAIL:
			// update the TemplateConfig in order to save
			myCard.getTemplateConfig().getProperties().email = personalTextConfig;
			
			// Update the Current Template in order to display correctly
			myCard.getTemplate().getProperties().email.color = color;
			break;
		case PHONE:
			// update the TemplateConfig in order to save
			myCard.getTemplateConfig().getProperties().phone = personalTextConfig;
			
			// Update the Current Template in order to display correctly
			myCard.getTemplate().getProperties().phone.color = color;
			break;
		case ADDRESS:
			// update the TemplateConfig in order to save
			myCard.getTemplateConfig().getProperties().address = personalTextConfig;
			
			// Update the Current Template in order to display correctly
			myCard.getTemplate().getProperties().address.color = color;
			break;
		}
		return myCard;
	}
}
