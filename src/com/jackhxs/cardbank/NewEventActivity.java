package com.jackhxs.cardbank;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;

import com.jackhxs.data.Event;

import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

public class NewEventActivity extends FragmentActivity {
	
	private EditText eventName, eventHost, eventLocation;
	
	private TextView eventStartTime, eventEndTime;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        final SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM dd, yyyy 'at' h:mm a", Locale.US);

        eventName = (EditText) findViewById (R.id.event_name);
        eventHost = (EditText) findViewById (R.id.event_host);
        eventLocation = (EditText) findViewById (R.id.event_location);
        eventStartTime = (TextView) findViewById (R.id.event_stime);
        eventEndTime = (TextView) findViewById (R.id.event_etime);
        
        final AtomicLong startTime = new AtomicLong();
        final AtomicLong endTime = new AtomicLong();
        
        final View dialogView = View.inflate(this, R.layout.date_time_picker, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        eventStartTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
		            @Override
		            public void onClick(View view) {

		                 DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
		                 TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

		                 Calendar calendar = new GregorianCalendar(datePicker.getYear(),
		                                    datePicker.getMonth(),
		                                    datePicker.getDayOfMonth(),
		                                    timePicker.getCurrentHour(),
		                                    timePicker.getCurrentMinute());

		                 startTime.set(calendar.getTimeInMillis());
		                 
		                 eventStartTime.setText(sdf.format(calendar.getTime()));
		                 
		                 alertDialog.dismiss();
		            }});
		        alertDialog.setView(dialogView);
		        alertDialog.show();
			}
        	
        });
        
       
        eventEndTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
		            @Override
		            public void onClick(View view) {

		                 DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
		                 TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

		                 Calendar calendar = new GregorianCalendar(datePicker.getYear(),
		                                    datePicker.getMonth(),
		                                    datePicker.getDayOfMonth(),
		                                    timePicker.getCurrentHour(),
		                                    timePicker.getCurrentMinute());

		                 endTime.set(calendar.getTimeInMillis());
		                 
		                 eventEndTime.setText(sdf.format(calendar.getTime()));
		                 
		                 alertDialog.dismiss();
		            }});
		        alertDialog.setView(dialogView);
		        alertDialog.show();
			}
        	
        });
        
    }

	private void createEvent() {
		Event event = new Event();
        
		event.setId("fsd");
		event.setOwner("sdfa");
		event.setName(eventName.getText().toString());
		event.setHost(eventHost.getText().toString());
		event.setLocation(eventLocation.getText().toString());
		event.setStartTime(eventStartTime.getText().toString());
		event.setEndTime(eventEndTime.getText().toString());
		
		Intent resultIntent = new Intent();
		resultIntent.putExtra("EVENT", event);
		
		setResult(Activity.RESULT_OK, resultIntent);
		
		finish();
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_event_menu, menu);
		return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_save_event:
                createEvent();
            	return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}