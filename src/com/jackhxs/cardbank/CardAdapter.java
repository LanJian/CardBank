package com.jackhxs.cardbank;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jackhxs.data.Card;
import com.jackhxs.data.SimpleCard;

public class CardAdapter extends ArrayAdapter<SimpleCard> {

  private SimpleCard[] myData;
  private int myResourceId;
  private Context myContext;

  public CardAdapter(Context context, int resourceId, SimpleCard[] objects) {
    super(context, resourceId, objects);
    myData = objects;
    myResourceId = resourceId;
    myContext = context;
  }


  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    switch(myResourceId) {
      case R.layout.list_view_row:
        return getRowView(position, convertView, parent);
      case R.layout.card_flip_view:
        return getFlipItemView(position, convertView, parent);
    }
    
    return null;
  }

  public View getRowView(int position, View convertView, ViewGroup parent) {
    View row = convertView;

    LayoutInflater inflater = ((Activity)myContext).getLayoutInflater();
    row = inflater.inflate(myResourceId, parent, false);

    TextView txtView = (TextView)row.findViewById(R.id.txtTitle);
    txtView.setText(myData[position].firstName + myData[position].lastName);
    ImageView imgView = (ImageView)row.findViewById(R.id.imgIcon);
    
    imgView.setImageResource(R.drawable.ic_launcher);


    return row;
  }

  public View getFlipItemView(int position, View convertView, ViewGroup parent) {
    View item = convertView;

    LayoutInflater inflater = ((Activity)myContext).getLayoutInflater();
    item = inflater.inflate(myResourceId, parent, false);

    TextView txtView = (TextView)item.findViewById(R.id.card_flip_view_name);
    txtView.setText(myData[position].firstName + " " + myData[position].lastName);

    txtView = (TextView)item.findViewById(R.id.card_flip_view_phoneNo);
    txtView.setText(myData[position].phoneNo);

    txtView = (TextView)item.findViewById(R.id.card_flip_view_email);
    txtView.setText(myData[position].email);
    
    return item;
  }

}
