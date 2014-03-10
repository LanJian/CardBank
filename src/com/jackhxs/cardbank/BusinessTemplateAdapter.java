package com.jackhxs.cardbank;

import java.util.ArrayList;
import java.util.List;

import com.jackhxs.data.BusinessCard;
import com.jackhxs.data.Template;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

public class BusinessTemplateAdapter extends FragmentStatePagerAdapter {
	private List<Template> templates;
	private BusinessCard cardDetails;
	private ArrayList<BusinessCardFragment> mBusinessCardFragments;
	
	public BusinessTemplateAdapter(FragmentManager fm, List<Template> templates, BusinessCard myCard) {
    	super(fm);
    	this.templates = templates;
    	this.cardDetails = myCard;
    	this.mBusinessCardFragments = new ArrayList<BusinessCardFragment>();
    }
    
    @Override
    public Fragment getItem(int position) {
    	if (position <= templates.size()){
    		BusinessCardFragment mBusinessCardFragment = new BusinessCardFragment();
    		mBusinessCardFragment.setCardDetails(cardDetails);
    		mBusinessCardFragment.setTemplate(templates.get(position));
    		mBusinessCardFragments.add(mBusinessCardFragment);
    		return mBusinessCardFragment;
    	} else {
    		throw new IndexOutOfBoundsException();
    	}
    }

    @Override
    public int getCount() {
        return templates.size();
    }
    
    public void updateCardContent(BusinessCard card) {
    	for (BusinessCardFragment mBusinessCardFragment : mBusinessCardFragments) {
    		mBusinessCardFragment.updateValues(card);
    	}
    }
}