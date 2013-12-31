package com.jackhxs.cardbank;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jackhxs.data.SimpleCard;
import com.jackhxs.remote.Constants.Operation;
import com.jackhxs.remote.JSONResultReceiver;
import com.jackhxs.remote.RemoteService;
import com.jackhxs.util.ImageUtil;
import com.xtremelabs.imageutils.ImageLoader;

public class CardAdapter extends ArrayAdapter<SimpleCard> {

    private static final int[] ICON_BG_COLORS = { 0xff1b7c59, 0xffe76e66,
            0xffdca46b, 0xff5699a9, 0xff695b8e, 0xff8c5e7a };

    private ArrayList<SimpleCard> myData;
    private int myResourceId;
    private Context myContext;
    
    public CardAdapter(Context context, int resourceId,
            ArrayList<SimpleCard> objects, ImageLoader mImageLoader) {
        super(context, resourceId, objects);
        myData = objects;
        myResourceId = resourceId;
        myContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        switch (myResourceId) {
        case R.layout.list_view_row:
            return getRowView(position, convertView, parent);
        case R.layout.card_flip_view:
            return getFlipItemView(position, convertView, parent);
        case R.layout.refer_list_row:
            return getReferListRowView(position, convertView, parent);
        }

        return null;
    }

    public View getRowView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        LayoutInflater inflater = ((Activity) myContext).getLayoutInflater();
        row = inflater.inflate(myResourceId, parent, false);

        TextView txtView = (TextView) row.findViewById(R.id.txtTitle);
        txtView.setText(myData.get(position).firstName + " "
                + myData.get(position).lastName);

        txtView = (TextView) row.findViewById(R.id.imgIcon);
        txtView.setText(myData.get(position).lastName.substring(0, 1).toUpperCase(Locale.US));
        int rand = (int) (Math.random() * ICON_BG_COLORS.length);
        txtView.setBackgroundColor(ICON_BG_COLORS[rand]);

        return row;
    }

    public View getReferListRowView(int position, View convertView,
            ViewGroup parent) {
        View row = convertView;

        LayoutInflater inflater = ((Activity) myContext).getLayoutInflater();
        row = inflater.inflate(myResourceId, parent, false);

        String name, phone, email, company, jobTitle, address;

        name = myData.get(position).firstName + " "
                + myData.get(position).lastName;
        phone = myData.get(position).phoneNo;
        email = myData.get(position).email;
        company = myData.get(position).company;
        address = myData.get(position).address;
        jobTitle = myData.get(position).jobTitle;

        ImageView imgView = (ImageView) row
                .findViewById(R.id.refer_list_row_image);
        int index = Integer.parseInt(myData.get(position).imageUrl);
        Bitmap newCard = ImageUtil.GenerateCardImage((Activity) myContext,
                App.templateConfig[index], name, email, phone, company, address, jobTitle);
        imgView.setImageBitmap(newCard);

        final int positionCopy = position;
        Button addButton = (Button) row
                .findViewById(R.id.refer_list_row_button_add);
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.e("paul", "click add refer");
                addReferral(positionCopy);
            }
        });

        Button ignoreButton = (Button) row
                .findViewById(R.id.refer_list_row_button_ignore);
        ignoreButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.e("paul", "click ignore refer");
                // TODO: add backend support to remove card from referral list
                removeCard(positionCopy);
            }
        });

        return row;
    }

    public View getFlipItemView(int position, View convertView, ViewGroup parent) {
        View item = convertView;
        String name, phone, email, company, jobTitle, address;

        LayoutInflater inflater = ((Activity) myContext).getLayoutInflater();
        item = inflater.inflate(myResourceId, parent, false);

        TextView txtView = (TextView) item
                .findViewById(R.id.card_flip_view_name);
        txtView.setText(myData.get(position).firstName + " "
                + myData.get(position).lastName);
        name = txtView.getText().toString();

        txtView = (TextView) item.findViewById(R.id.card_flip_view_phoneNo);
        txtView.setText(myData.get(position).phoneNo);
        phone = txtView.getText().toString();
        Linkify.addLinks(txtView, Linkify.PHONE_NUMBERS);

        txtView = (TextView) item.findViewById(R.id.card_flip_view_email);
        txtView.setText(myData.get(position).email);
        email = txtView.getText().toString();
        Linkify.addLinks(txtView, Linkify.EMAIL_ADDRESSES);
        
        txtView = (TextView) item.findViewById(R.id.card_flip_view_company);
        txtView.setText(myData.get(position).company);
        company = txtView.getText().toString();

        txtView = (TextView) item.findViewById(R.id.card_flip_view_jobTitle);
        txtView.setText(myData.get(position).jobTitle);
        jobTitle = txtView.getText().toString();

        txtView = (TextView) item.findViewById(R.id.card_flip_view_address);
        txtView.setText(myData.get(position).address);
        address = txtView.getText().toString();
        
        ImageView imgView = (ImageView) item
                .findViewById(R.id.card_flip_view_image);
        //myImageLoader.loadImage(imgView, myData[position].imageUrl);
        int index = Integer.parseInt(myData.get(position).imageUrl);
        Bitmap newCard = ImageUtil.GenerateCardImage((Activity) myContext,
                App.templateConfig[index], name, email, phone, company, address, jobTitle);
        imgView.setImageBitmap(newCard);

        return item;
    }

    public void addReferral(int position) {
        final Intent serviceIntent = new Intent(Intent.ACTION_SYNC, null,
                getContext(), RemoteService.class);

        JSONResultReceiver receiver = new JSONResultReceiver(new Handler());
        receiver.setReceiver(new JSONResultReceiver.Receiver() {

            @Override
            public void onReceiveResult(int resultCode, Bundle resultData) {
                Log.e("paul", "receive result add referal");
                Log.e("add referral", Integer.toString(resultCode));
                Log.e("add referral", resultData.toString());
            }
        });

        serviceIntent.putExtra("receiver", receiver);
        serviceIntent.putExtra("operation", (Parcelable) Operation.POST_CONTACT);
        serviceIntent.putExtra("newContactJSON", new Gson().toJson(myData.get(position)));

        getContext().startService(serviceIntent);
        removeCard(position);
    }

    public void removeCard(int position) {
    	String referralId = myData.get(position).referralId;
    	
        this.remove(myData.get(position));
        this.notifyDataSetChanged();
        
        final Intent serviceIntent = new Intent(Intent.ACTION_SYNC, null,
                getContext(), RemoteService.class);

        JSONResultReceiver receiver = new JSONResultReceiver(new Handler());
        receiver.setReceiver(new JSONResultReceiver.Receiver() {

            @Override
            public void onReceiveResult(int resultCode, Bundle resultData) {
                Log.e("paul", "receive result add referal");
                Log.e("add referral", Integer.toString(resultCode));
                Log.e("add referral", resultData.toString());
            }
        });

        serviceIntent.putExtra("receiver", receiver);
        serviceIntent.putExtra("operation", (Parcelable) Operation.DEL_REFER);
        serviceIntent.putExtra("referralId", referralId);
        
        getContext().startService(serviceIntent);
    }
}
