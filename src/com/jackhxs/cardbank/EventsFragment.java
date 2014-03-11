package com.jackhxs.cardbank;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jackhxs.remote.JSONResultReceiver;

public class EventsFragment extends Fragment implements JSONResultReceiver.Receiver{


	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.events_fragment,
	        container, false);
	    return view;
	  }

	  
	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		// TODO Auto-generated method stub
		
	}
}
