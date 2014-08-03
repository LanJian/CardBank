package com.jackhxs.cardbank;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.jackhxs.data.Event;
import com.jackhxs.remote.Constants;
import com.jackhxs.remote.JSONResultReceiver;
import com.jackhxs.remote.RemoteService;
import com.jackhxs.remote.Constants.Operation;

public class EventsFragment extends Fragment implements JSONResultReceiver.Receiver{

	static final String TAG = EventsFragment.class.getSimpleName();
	
	static final String[] EVENET_LIST = 
            new String[] { "Android", "iOS", "WindowsMobile", "Blackberry"};

	private ArrayList<String> eventNames;
	private ArrayList<Event> mEvents;
	
	private EventsAdapter mEventsAdapter;
	
	// Used to make API calls
	public JSONResultReceiver mReceiver;
	
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setHasOptionsMenu(true);
	}


	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.events_fragment,
	        container, false);
	    
	    mReceiver = new JSONResultReceiver(new Handler());
		mReceiver.setReceiver(this);
		
	    final ListView eventList = (ListView) view.findViewById(R.id.event_list);
	    final View emptyView = view.findViewById(R.id.empty);
	    		
	    eventNames = new ArrayList<String>();
	    mEvents = new ArrayList<Event>();
	    
	    
	    mEventsAdapter = new EventsAdapter(getActivity(), eventNames);
	    eventList.setAdapter(mEventsAdapter);

	    eventList.setEmptyView(emptyView);
	    
	   
		eventList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				
				Intent intent = new Intent(getActivity(), EventActivity.class);
				
				intent.putExtra("EVENT", mEvents.get(position));
				
				startActivity(intent);
				
				/*
				Fragment fragment = new MyContactsFragment();
				
				FragmentManager mFragmentManager = getActivity().getSupportFragmentManager();
				
				FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
				
				mFragmentTransaction.addToBackStack(null);
				
				mFragmentTransaction.add(R.id.content_frame, fragment);
				
				mFragmentTransaction.commit();
				
//				/mFragmentTransaction.beginTransaction().replace(R.id.content_frame, fragment).commit();
				
				*/
			}
			
		});
		
		final Intent intentCards = new Intent(Intent.ACTION_SYNC, null, getActivity(), RemoteService.class);
		
		intentCards.putExtra("receiver", mReceiver);
		intentCards.putExtra("operation",(Parcelable) Operation.GET_EVENTS);
		
		Log.i(TAG, "getting events");
		getActivity().startService(intentCards);
		
	    return view;
	  }

	  
	@Override
	public void onCreateOptionsMenu(
	      Menu menu, MenuInflater inflater) {
	   inflater.inflate(R.menu.events_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	   switch (item.getItemId()) {
	      case R.id.action_new_event:
	         createEvent();
	         return true;
	      case R.id.action_join_event:
		         joinEvent();
		         return true;
		  default:
	         return super.onOptionsItemSelected(item);
	   }
	}

	
	private void createEvent() {
		Intent intent = new Intent(getActivity(), NewEventActivity.class);  
        
		startActivityForResult(intent, 0);
	}


	private void joinEvent() {
		// TODO Auto-generated method stub
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
			    	
			    	Log.i("list", "" + eventNames.size());
			    	Event event = new Event();
			        
					event.setEventId(userInput.getText().toString());
					event.setOwner("Or Bar");
					event.setEventName("CardBeam Monthly Drinks");
					event.setHost("CardBeam");
					event.setLocation("CardBeam Office");
					event.setStartTime("Fri, Aug 29, 2014 at 5:00 PM");
					event.setEndTime("Fri, Aug 29, 2014 at 7:00 PM");
			    	
					mEvents.add(event);
			    	Log.i("list", "" + eventNames.size());
			    	
			    	eventNames.add(event.getEventName());
			    	
			    	mEventsAdapter.notifyDataSetChanged();
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


	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		
		switch (resultCode) {

		case Constants.STATUS_FINISHED: {
			
			Operation command = resultData.getParcelable("operation");
			if (command != null){
				switch (command) {
				case GET_EVENTS:
						// Event Created Successfully
					Log.d(TAG, "Event Created Successfully");
					
					ArrayList<Event> events = resultData.getParcelableArrayList("events");
					
					mEvents.clear();
					mEvents.addAll(events);
					
					eventNames.clear();
					
					for (int i = 0; i < events.size(); i++) {
						eventNames.add(mEvents.get(i).getEventName());
					}
					
					
					mEventsAdapter.notifyDataSetChanged();
					
					break;
				
				// no other cases for now
				default:
					Log.e(TAG, "UNSUPPORTED OPERATION " + command.toString());
				}
			
			}
			
			break;
		}
		case Constants.STATUS_ERROR: 
			Operation command = resultData.getParcelable("operation");
			if (command != null){
				switch (command) {
				case POST_CARD:
				case UPDATE_CARD:
					Log.e(TAG, "failed to get events: try again");
				
					Toast.makeText(getActivity(), "Failed to get events", Toast.LENGTH_LONG).show();
					
					break;
				default:
					Log.e(TAG, "UNSUPPORTED OPERATION " + command.toString());
					break;
				}
			
			}
			
			break;
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	  super.onActivityResult(requestCode, resultCode, data);
	  
	  Log.i(TAG, "Events activity Finished");
	  
	  if (data != null) {
		  Log.i(TAG, "Event intent is not empty");
		  
		  Event event = data.getParcelableExtra("EVENT");
	   
		  eventNames.add(event.getEventName());
		  mEvents.add(event);
		  
		  mEventsAdapter.notifyDataSetChanged();
	  }
	  
	}
}
