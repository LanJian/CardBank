package com.jackhxs.cardbank;

import com.jackhxs.data.BusinessCard;
import com.jackhxs.data.Template;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

public class BusinessCardFragment extends Fragment {
	private static final String TAG = "BusinessCardFragment";
	
	private Template template; 
	private BusinessCard cardDetails;
	
	int width;

	float whScale = 0.541f;
	
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