package com.jackhxs.cardbank;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
 
public class EventsAdapter extends BaseAdapter {
	
	Context context;
    List<String> events;
 
    
    static class ViewHolder {
		public TextView text;
	}
    
    public EventsAdapter(Context context, List<String> events) {
        this.context = context;
        this.events = events;
    }
    

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = null;
		 
        LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
       
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.events_list, null);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.label);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
 
        String event = (String) getItem(position);
 
        holder.text.setText(event);
        
        return convertView;
	  }


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return events.size();
	}


	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return events.get(position);
	}


	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
} 