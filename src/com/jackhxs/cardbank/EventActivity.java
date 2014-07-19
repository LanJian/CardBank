package com.jackhxs.cardbank;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.jackhxs.data.Event;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.EditText;
import android.widget.TextView;

public class EventActivity extends FragmentActivity {

	TextView eventName, eventHost, eventTime;
	
	final SimpleDateFormat originalTime = new SimpleDateFormat("EEE, MMM dd, yyyy 'at' h:mm a", Locale.US);

	final SimpleDateFormat simplifiedTime = new SimpleDateFormat("MMM dd, h:mm a", Locale.US);

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        eventName = (TextView) findViewById (R.id.event_name);
        eventHost = (TextView) findViewById (R.id.event_host);
        eventTime = (TextView) findViewById (R.id.event_date);
        
     
        Intent eventIntent = getIntent();
        
        Event event = eventIntent.getParcelableExtra("EVENT");
        
        
        String sTime = "", eTime = "";
        
        Date date;
		try {
			date = originalTime.parse(event.getStartTime());
		    sTime = simplifiedTime.format(date);
	        date = originalTime.parse(event.getEndTime());
	        eTime = simplifiedTime.format(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        eventName.setText(event.getName());
        eventHost.setText(String.format(getString(R.string.event_host), event.getHost()));
        eventTime.setText(String.format(getString(R.string.event_date), sTime, eTime));
        
        
    }
}
