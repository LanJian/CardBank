package com.jackhxs.cardbank;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.jackhxs.remote.JSONResultReceiver;

public class EventsFragment extends Fragment implements JSONResultReceiver.Receiver{

	static final String[] EVENET_LIST = 
            new String[] { "Android", "iOS", "WindowsMobile", "Blackberry"};

	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.events_fragment,
	        container, false);
	    
	    
	    final ListView eventList = (ListView) view.findViewById(R.id.event_list);
	    final View emptyView = view.findViewById(R.id.empty);
	    		
	    
	    final ArrayList<String> events = new ArrayList<String>();
	    
	    
	    
	    final EventsAdapter adapter = new EventsAdapter(getActivity(), events);
	    eventList.setAdapter(adapter);

	    eventList.setEmptyView(emptyView);
	    
	    Button button = (Button) view.findViewById(R.id.join_event);
		
		// add button listener
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// get prompts.xml view
				LayoutInflater li = LayoutInflater.from(getActivity());
				View promptsView = li.inflate(R.layout.join_event_dialog, null);
 
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						getActivity());
 
				// set prompts.xml to alertdialog builder
				alertDialogBuilder.setView(promptsView);
 
				final EditText userInput = (EditText) promptsView
						.findViewById(R.id.event_id);
 
				// set dialog message
				alertDialogBuilder
					.setCancelable(false)
					.setPositiveButton("OK",
					  new DialogInterface.OnClickListener() {
					    public void onClick(DialogInterface dialog,int id) {
						// get user input and set it to result
						// edit text
					    	Log.i("event id", userInput.getText().toString());
					    	
					    	Log.i("list", "" + events.size());
					    	events.add(userInput.getText().toString());
					    	Log.i("list", "" + events.size());
					    	
					    	adapter.notifyDataSetChanged();
					    }
					  })
					.setNegativeButton("Cancel",
					  new DialogInterface.OnClickListener() {
					    public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
					    }
					  });
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
			}
 
		});
	    
		
		eventList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Fragment fragment = new MyContactsFragment();
				
				FragmentManager mFragmentManager = getActivity().getSupportFragmentManager();
				
				FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
				
				mFragmentTransaction.addToBackStack(null);
				
				mFragmentTransaction.add(R.id.content_frame, fragment);
				
				mFragmentTransaction.commit();
				
//				/mFragmentTransaction.beginTransaction().replace(R.id.content_frame, fragment).commit();
			}
			
		});
		
	    return view;
	  }

	  
	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		// TODO Auto-generated method stub
		
	}
}
