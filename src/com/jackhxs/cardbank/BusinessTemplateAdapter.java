package com.jackhxs.cardbank;

import java.util.List;

import com.jackhxs.data.SimpleCard;
import com.jackhxs.data.Template;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class BusinessTemplateAdapter extends FragmentStatePagerAdapter {
	private List<Template> templates;
	private SimpleCard cardDetails;
	
	public BusinessTemplateAdapter(FragmentManager fm, List<Template> templates, SimpleCard myCard) {
    	super(fm);
    	this.templates = templates;
    	this.cardDetails = myCard;
    }
    
    @Override
    public Fragment getItem(int position) {
    	if (position <= templates.size()){
    		BusinessCardFragment mBusinessCardFragment = new BusinessCardFragment();
    		mBusinessCardFragment.setCardDetails(cardDetails);
    		mBusinessCardFragment.setTemplate(templates.get(position));
    		return mBusinessCardFragment;
    	} else {
    		throw new IndexOutOfBoundsException();
    	}
    }

    @Override
    public int getCount() {
        return templates.size();
    }
}