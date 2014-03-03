package com.jackhxs.cardbank;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jackhxs.data.BusinessCard;
import com.jackhxs.util.ImageUtil;
import com.xtremelabs.imageutils.ImageLoader;

public class CardAdapter extends ArrayAdapter<BusinessCard> {

    private ArrayList<BusinessCard> myData;
    private int myResourceId;
    private Context myContext;
    
    public CardAdapter(Context context, int resourceId,
            ArrayList<BusinessCard> objects, ImageLoader mImageLoader) {
        super(context, resourceId, objects);
        myData = objects;
        myResourceId = resourceId;
        myContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
    	LayoutInflater inflater = ((Activity) myContext).getLayoutInflater();
        convertView = inflater.inflate(myResourceId, parent, false);

        View phoneLayout = (RelativeLayout) convertView.findViewById(R.id.card_flip_view_phone_layout);
        TextView phoneNumber = (TextView) convertView.findViewById(R.id.card_flip_view_phone);
        
        View emailLayout = (RelativeLayout) convertView.findViewById(R.id.card_flip_view_email_layout);
        TextView emailAddress = (TextView) convertView.findViewById(R.id.card_flip_view_email);
        
        TextView streetAddress = (TextView) convertView.findViewById(R.id.card_flip_view_address);
        
        
        phoneNumber.setText(PhoneNumberUtils.formatNumber(myData.get(position).getPhone()));
        phoneLayout.setOnClickListener(new PhoneClickListener(myData.get(position).getPhone(), myContext));
        
        emailAddress.setText(myData.get(position).getEmail());
        emailLayout.setOnClickListener(new EmailClickListener(myData.get(position).getEmail(), myContext));
        
        streetAddress.setText(myData.get(position).getAddress());
        Linkify.addLinks(streetAddress, Linkify.MAP_ADDRESSES);
        
        ImageView imgView = (ImageView) convertView
                .findViewById(R.id.card_flip_view_image);
        //myImageLoader.loadImage(imgView, myData[position].imageUrl);
        int index = Integer.parseInt(myData.get(position).getImageUrl());
        Bitmap newCard = ImageUtil.GenerateCardImage((Activity) myContext,
                App.templateConfig[index],
                myData.get(position).getFirstName() + " " + myData.get(position).getLastName(), 
                myData.get(position).getEmail(), 
                myData.get(position).getPhone(),
                myData.get(position).getCompanyName(),
                myData.get(position).getAddress(),
                myData.get(position).getJobTitle());
        imgView.setImageBitmap(newCard);

        return convertView;
    }

   
    private class PhoneClickListener implements OnClickListener {

    	private String mPhoneNumber;
    	private Context mContext;
    	
    	
		public PhoneClickListener(String phone, Context mContext) {
			this.mPhoneNumber = phone;
			this.mContext = mContext;
		}

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_CALL);

			intent.setData(Uri.parse("tel:" + mPhoneNumber));
			mContext.startActivity(intent);
		}
    	
    }
    
    
    private class EmailClickListener implements OnClickListener {

    	private String mEmailAdress;
    	private Context mContext;
    	
    	
		public EmailClickListener(String email, Context mContext) {
			this.mEmailAdress = email;
			this.mContext = mContext;
		}

		@Override
		public void onClick(View v) {
			final Intent emailIntent = new Intent( android.content.Intent.ACTION_SEND);

		    emailIntent.setType("plain/text");

		    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
		            new String[] { mEmailAdress });

		    mContext.startActivity(Intent.createChooser(emailIntent, mContext.getResources().getString(R.string.email_option)));
		}
    	
    }
    
}
