package com.jackhxs.cardbank;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jackhxs.data.BusinessCard;
import com.jackhxs.remote.Constants.Operation;
import com.jackhxs.remote.JSONResultReceiver;
import com.jackhxs.remote.RemoteService;
import com.jackhxs.util.ImageUtil;
import com.xtremelabs.imageutils.ImageLoader;

public class ContactsAdapter extends BaseAdapter {

    private static final int[] ICON_BG_COLORS = { 0xff1b7c59, 0xffe76e66,
            0xffdca46b, 0xff5699a9, 0xff695b8e, 0xff8c5e7a };

    private ArrayList<BusinessCard> mBusinessCards;
    private Activity mContext;
    
    static class ViewHolder {
    	//RelativeLayout contactCard;
    	TextView imgIcon;
    	TextView jobTitle;
    	TextView fullName;
    	ImageView referredIcon;
    	int position;
    }
    
    public ContactsAdapter(Activity context, ArrayList<BusinessCard> mBusinessCards) {
    	this.mBusinessCards = mBusinessCards;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	ViewHolder viewHolderItem;

    	if(convertView==null){
	        // inflate the layout
	        LayoutInflater inflater = mContext.getLayoutInflater();
	        convertView = inflater.inflate(R.layout.list_view_row, parent, false);

	        // well set up the ViewHolder
	        viewHolderItem = new ViewHolder();
	        //viewHolderItem.contactCard = (RelativeLayout) convertView.findViewById(R.id.contact_card);
	        viewHolderItem.imgIcon = (TextView) convertView.findViewById(R.id.imgIcon);
	        viewHolderItem.jobTitle = (TextView) convertView.findViewById(R.id.jobTitle);
	        viewHolderItem.fullName = (TextView) convertView.findViewById(R.id.fullName);
	        viewHolderItem.referredIcon = (ImageView) convertView.findViewById(R.id.referredIcon);
	    	
	        // store the holder with the view.
	        convertView.setTag(viewHolderItem);
	    }else{
	        // we've just avoided calling findViewById() on resource everytime
	        // just use the viewHolder
	    	viewHolderItem = (ViewHolder) convertView.getTag();
	    }

    	
    	BusinessCard card = mBusinessCards.get(position);
        if (card != null) {
        	
        	//BusinessCardLayoutBuilder mBusinessCardLayoutBuilder = new BusinessCardLayoutBuilder(viewHolderItem.contactCard, card.getTemplate(), 
        	//		card, getCardWidth((Activity) mContext), (Activity) mContext);
            
        	//mBusinessCardLayoutBuilder.getLayout();
            
        	viewHolderItem.fullName.setText(mBusinessCards.get(position).getFirstName() + " " + mBusinessCards.get(position).getLastName());
        	viewHolderItem.jobTitle.setText(mBusinessCards.get(position).getJobTitle() + ", " + mBusinessCards.get(position).getCompanyName());
            
        	viewHolderItem.imgIcon.setText(mBusinessCards.get(position).getLastName().substring(0, 1).toUpperCase(Locale.US));
            int rand = (int) (Math.random() * ICON_BG_COLORS.length);
            viewHolderItem.imgIcon.setBackgroundColor(ICON_BG_COLORS[rand]);
            
        }
    	
        return convertView;
    }

    private int getCardWidth(Activity mActivity) {
    	
   	 Display display = mActivity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        
        return width;
   }
    
	@Override
	public int getCount() {
		return mBusinessCards.size();
	}

	@Override
	public Object getItem(int position) {
		return mBusinessCards.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}


}
