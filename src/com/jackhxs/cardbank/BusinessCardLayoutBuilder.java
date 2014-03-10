package com.jackhxs.cardbank;
import android.app.Activity;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

import com.jackhxs.data.BusinessCard;
import com.jackhxs.data.Template;
import com.jackhxs.data.TextConfig;
import com.jackhxs.remote.Constants;
import com.squareup.picasso.Picasso;


public class BusinessCardLayoutBuilder {

	private RelativeLayout buisinessCardLayout;
	private Template template; 
	private BusinessCard cardDetails;
	
	int width, height;

	float whScale = 0.541f;
	
	private Activity mActivity;

	public BusinessCardLayoutBuilder(RelativeLayout buisinessCardLayout, Template template, 
			BusinessCard cardDetails, int width, Activity mActivity) {
		
		super();
		this.buisinessCardLayout = buisinessCardLayout;
		this.template = template;
		this.cardDetails = cardDetails;
		this.width = width;
		this.height = (int) (width * whScale);
		this.mActivity = mActivity;
	}

	

	public RelativeLayout getLayout() {
		if (buisinessCardLayout == null) {
			buisinessCardLayout = new RelativeLayout(mActivity);
		}
		
		//LayoutParams mLayoutParams = new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		//buisinessCardLayout.setLayoutParams(mLayoutParams);
		
		// Image has to be first (so it appears behind the text
		ImageView background = addBackground(buisinessCardLayout);
		
		Picasso.with(mActivity).setDebugging(true);
	    Picasso.with(mActivity).load(Constants.API_ADDRESS_V1 + template.getImageUrl()).into(background);
	    
	    
	    addField(buisinessCardLayout, template.getProperties().name, cardDetails.getFirstName() + " " + cardDetails.getLastName());
        addField(buisinessCardLayout, template.getProperties().phone, cardDetails.getPhone());
        addField(buisinessCardLayout, template.getProperties().email, cardDetails.getEmail());
        addField(buisinessCardLayout, template.getProperties().companyName, cardDetails.getCompanyName());
        addField(buisinessCardLayout, template.getProperties().address, cardDetails.getAddress());
        addField(buisinessCardLayout, template.getProperties().jobTitle, cardDetails.getJobTitle());
        
	    
		return buisinessCardLayout;
		
	};
	
	private void addField(RelativeLayout mBusinessCardLayout, TextConfig fieldConfig, String value) {
		
		LayoutParams layoutParams = new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

		TextView textView = new TextView(mActivity);
        textView.setText(value);
        textView.setTextColor(Color.BLACK);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        
        
        layoutParams.leftMargin = (int) (fieldConfig.left * width);
        layoutParams.topMargin = (int) ((fieldConfig.top - 0.15) * height);
        
        textView.setTextColor(Color.parseColor(fieldConfig.color));
        
        textView.setLayoutParams(layoutParams);
        mBusinessCardLayout.addView(textView);
		
	}

	private ImageView addBackground(RelativeLayout mBusinessCardLayout) {
		
		LayoutParams layoutParams = new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

		ImageView mImageView = new ImageView(mActivity);
        
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        
        mImageView.setAdjustViewBounds(true);
        
        mImageView.setLayoutParams(layoutParams);
        mBusinessCardLayout.addView(mImageView);
		
        return mImageView;
	}
	
}
