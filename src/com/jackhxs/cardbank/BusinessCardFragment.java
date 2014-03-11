package com.jackhxs.cardbank;

import com.jackhxs.data.BusinessCard;
import com.jackhxs.data.template.Template;
import com.jackhxs.data.template.TextConfig;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BusinessCardFragment extends Fragment {
	private static final String TAG = "BusinessCardFragment";
	
	private Template template; 
	private BusinessCard cardDetails;
	
	int width;

	float whScale = 0.541f;
	
	RelativeLayout mBusinessCardLayout;
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		
		outState.putParcelable("template", template);
		outState.putParcelable("cardDetails", cardDetails);
	}
    
	@SuppressLint("NewApi")
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
        	template = savedInstanceState.getParcelable("template");
        	cardDetails = savedInstanceState.getParcelable("cardDetails");
        }
		
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.business_card_fragment, container, false);
        
        mBusinessCardLayout = (RelativeLayout) rootView.findViewById(R.id.business_card_layout);
        
        
        // We use an observer to measure the exact width of the card after it is attached to the activity
        final ViewTreeObserver vto = mBusinessCardLayout.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @SuppressLint("NewApi")
			public boolean onPreDraw() {
            	mBusinessCardLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                
            	width = mBusinessCardLayout.getMeasuredWidth();
            	
                BusinessCardLayoutBuilder mBusinessCardLayoutBuilder = new BusinessCardLayoutBuilder(mBusinessCardLayout, template, 
                		cardDetails, width, getActivity());
                
                mBusinessCardLayoutBuilder.getLayout();
                
                return true;
            }
        });
        
        
        return rootView;
    }
    
	public void updateValues(BusinessCard card) {
		Log.d(TAG, "updating card values");
		
		TextView name = (TextView) mBusinessCardLayout.getChildAt(1);
		TextView phone = (TextView) mBusinessCardLayout.getChildAt(2);
		TextView email = (TextView) mBusinessCardLayout.getChildAt(3);
		TextView companyName = (TextView) mBusinessCardLayout.getChildAt(4);
		TextView address = (TextView) mBusinessCardLayout.getChildAt(5);
		TextView jobTitle = (TextView) mBusinessCardLayout.getChildAt(6);
		
		/*
		name.setText(card.getFirstName() + " " + card.getLastName());
		phone.setText(card.getPhone());
		email.setText(card.getEmail());
		companyName.setText(card.getCompanyName());
		address.setText(card.getAddress());
		jobTitle.setText(card.getJobTitle());
		*/
		
		updateTextView(name, card.getFirstName() + " " + card.getLastName(), template.getProperties().name);
		updateTextView(phone, card.getPhone(), template.getProperties().phone);
		updateTextView(email, card.getEmail(), template.getProperties().email);
		updateTextView(companyName, card.getCompanyName(), template.getProperties().companyName);
		updateTextView(address, card.getAddress(), template.getProperties().address);
		updateTextView(jobTitle, card.getJobTitle(), template.getProperties().jobTitle);
		
	}
	
	public void updateTextView(TextView view, String text, TextConfig  config) {
		view.setText(text);
		view.setTextColor(Color.parseColor(config.color));
		
	}
	
	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public BusinessCard getCardDetails() {
		return cardDetails;
	}

	public void setCardDetails(BusinessCard cardDetails) {
		this.cardDetails = cardDetails;
	}

}