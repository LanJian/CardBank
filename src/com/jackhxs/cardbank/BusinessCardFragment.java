package com.jackhxs.cardbank;

import com.jackhxs.data.BusinessCard;
import com.jackhxs.data.Template;
import com.jackhxs.data.TemplateProperties.TextConfig;
import com.jackhxs.remote.Constants;
import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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

	private Context mContext;
	
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
        
        final RelativeLayout mBusinessCardLayout = (RelativeLayout) rootView.findViewById(R.id.business_card_layout);
        final ImageView background = (ImageView) rootView.findViewById(R.id.business_card_background);
        
        mContext = getActivity();
        
        Picasso.with(getActivity()).setDebugging(true);
        Picasso.with(getActivity()).load(Constants.API_ADDRESS_V1 + template.imageUrl).into(background);
        
        final ViewTreeObserver vto = background.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @SuppressLint("NewApi")
			public boolean onPreDraw() {
            	background.getViewTreeObserver().removeOnPreDrawListener(this);
                
            	height = background.getMeasuredHeight();
                width = background.getMeasuredWidth();
                Log.i(TAG, "Height: " + height + " Width: " + width);
                
                
                addField(mBusinessCardLayout, template.properties.name, cardDetails.getFirstName() + " " + cardDetails.getLastName());
                addField(mBusinessCardLayout, template.properties.phone, cardDetails.getPhone());
                addField(mBusinessCardLayout, template.properties.email, cardDetails.getEmail());
                addField(mBusinessCardLayout, template.properties.companyName, cardDetails.getCompanyName());
                addField(mBusinessCardLayout, template.properties.address, cardDetails.getAddress());
                addField(mBusinessCardLayout, template.properties.jobTitle, cardDetails.getJobTitle());
                
                return true;
            }
        });
        
        return rootView;
    }
    
	private void addField(RelativeLayout mBusinessCardLayout, TextConfig fieldConfig, String value) {
		
		LayoutParams layoutParams = new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

		TextView textView = new TextView(mContext);
        textView.setText(value);
        textView.setTextColor(Color.BLACK);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        
        
        layoutParams.leftMargin = (int) (fieldConfig.left * width);
        layoutParams.topMargin = (int) (fieldConfig.top * height);
        
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