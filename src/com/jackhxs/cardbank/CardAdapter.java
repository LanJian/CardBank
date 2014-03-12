package com.jackhxs.cardbank;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.PhoneNumberUtils;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
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
	private static String TAG = "CardAdapter";
	
    private ArrayList<BusinessCard> mBusinessCards;
    private int mResourceId;
    private Context mContext;
    
    public CardAdapter(Context context, int resourceId,
            ArrayList<BusinessCard> BusinessCards, ImageLoader mImageLoader) {
        super(context, resourceId, BusinessCards);
        mBusinessCards = BusinessCards;
        mResourceId = resourceId;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
    	LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        convertView = inflater.inflate(mResourceId, parent, false);

        
        RelativeLayout cardLayout = (RelativeLayout) convertView.findViewById(R.id.card_flip_view_card_frame);
        
        View phoneLayout = (RelativeLayout) convertView.findViewById(R.id.card_flip_view_phone_layout);
        TextView phoneNumber = (TextView) convertView.findViewById(R.id.card_flip_view_phone);
        
        View emailLayout = (RelativeLayout) convertView.findViewById(R.id.card_flip_view_email_layout);
        TextView emailAddress = (TextView) convertView.findViewById(R.id.card_flip_view_email);
        
        TextView streetAddress = (TextView) convertView.findViewById(R.id.card_flip_view_address);
        
        
        phoneNumber.setText(PhoneNumberUtils.formatNumber(mBusinessCards.get(position).getPhone()));
        phoneLayout.setOnClickListener(new PhoneClickListener(mBusinessCards.get(position).getPhone(), mContext));
        
        emailAddress.setText(mBusinessCards.get(position).getEmail());
        emailLayout.setOnClickListener(new EmailClickListener(mBusinessCards.get(position).getEmail(), mContext));
        
        streetAddress.setText(mBusinessCards.get(position).getAddress());
        Linkify.addLinks(streetAddress, Linkify.MAP_ADDRESSES);
        
        BusinessCardLayoutBuilder mBusinessCardLayoutBuilder = new BusinessCardLayoutBuilder(cardLayout, mBusinessCards.get(position).getTemplate(), 
        		mBusinessCards.get(position), getCardWidth((Activity) mContext), (Activity) mContext);
        
        mBusinessCardLayoutBuilder.getLayout();
        
        return convertView;
    }

    private int getCardWidth(Activity mActivity) {
    	
    	 Display display = mActivity.getWindowManager().getDefaultDisplay();
         Point size = new Point();
         display.getSize(size);
         int width = size.x;
         
         return width;
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
