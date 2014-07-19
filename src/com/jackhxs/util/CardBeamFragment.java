package com.jackhxs.util;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devspark.progressfragment.ProgressFragment;
import com.jackhxs.cardbank.R;

public class CardBeamFragment extends ProgressFragment{

	@Override
	public void setContentShown(boolean shown) {
        if (isVisible()) {
        	super.setContentShown(shown);
        }
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.cardbeam_progress_layout, container, false);
	}	
}

