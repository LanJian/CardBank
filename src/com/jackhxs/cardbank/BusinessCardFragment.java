package com.jackhxs.cardbank;

import com.jackhxs.data.BusinessCard;
import com.jackhxs.data.Template;
import com.jackhxs.data.TemplateProperties.TextConfig;
import com.jackhxs.remote.Constants;
import com.squareup.picasso.Picasso;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BusinessCardFragment extends Fragment {
	private static final String TAG = "BusinessCardFragment";
	
	private Template template; 
	private BusinessCard cardDetails;
	
	private int padding = 0;
	int width, height;

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		
		outState.putParcelable("template", template);
		outState.putParcelable("cardDetails", cardDetails);
	}
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
        	template = savedInstanceState.getParcelable("template");
        	cardDetails = savedInstanceState.getParcelable("cardDetails");
        }
		
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.business_card_fragment, container, false);
        
        RelativeLayout mBusinessCardLayout = (RelativeLayout) rootView.findViewById(R.id.business_card_layout);
        ImageView background = (ImageView) rootView.findViewById(R.id.business_card_background);
        
        getMeasurements();
        
        //Log.i(TAG, Constants.API_ADDRESS_V1 + template.imageUrl);
        
        Picasso.with(getActivity()).setDebugging(true);
        Picasso.with(getActivity()).load(Constants.API_ADDRESS_V1 + template.imageUrl).into(background);
        
        addField(mBusinessCardLayout, template.properties.name, cardDetails.getFirstName() + " " + cardDetails.getLastName());
        addField(mBusinessCardLayout, template.properties.phone, cardDetails.getPhone());
        addField(mBusinessCardLayout, template.properties.email, cardDetails.getEmail());
        addField(mBusinessCardLayout, template.properties.companyName, cardDetails.getCompanyName());
        addField(mBusinessCardLayout, template.properties.address, cardDetails.getAddress());
        addField(mBusinessCardLayout, template.properties.jobTitle, cardDetails.getJobTitle());
        
        return rootView;
    }
    
	
	private void getMeasurements() {
		
		float whScale = 0.541667f; // width to height ratio
		
		Display display = getActivity().getWindowManager().getDefaultDisplay();
	    DisplayMetrics outMetrics = new DisplayMetrics ();
	    display.getMetrics(outMetrics);

	    float density  = getActivity().getResources().getDisplayMetrics().density;
	    float dpWidth  = outMetrics.widthPixels / density;
		
	    dpWidth = dpWidth - 20; // there is a 10dp padding on either side
	    float dpHeight = dpWidth * whScale;
	    
	    width = (int) (dpWidth * density);
	    height = (int) (dpHeight * density);
	}


	private void addField(RelativeLayout mBusinessCardLayout, TextConfig fieldConfig, String value) {
		
		LayoutParams layoutParams = new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

		
        TextView textView = new TextView(getActivity());
        textView.setText(value);
        textView.setTextColor(Color.BLACK);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        
        /*
        Log.i("nameConfig", Boolean.toString(fieldConfig == null));
        Log.i("nameConfig.left", Float.toString(fieldConfig.left));
        Log.i("nameConfig.top", Float.toString(fieldConfig.top));
        
        Log.i("layoutParams", Boolean.toString(layoutParams == null));
        Log.i("layoutParams.leftMargin", Float.toString(layoutParams.leftMargin));
        Log.i("layoutParams.topMargin", Float.toString(layoutParams.topMargin));
        
        Log.i("width", Float.toString(width));
        Log.i("height", Float.toString(height));
        */
        layoutParams.leftMargin = (int) fieldConfig.left;// * width;
        layoutParams.topMargin = (int) fieldConfig.top;// * height;
        
        textView.setTextColor(Color.parseColor(fieldConfig.color));
        
        textView.setLayoutParams(layoutParams);
        mBusinessCardLayout.addView(textView);
		
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

	public int getPadding() {
		return padding;
	}

	public void setPadding(int padding) {
		this.padding = padding;
	}
    
}