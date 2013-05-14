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

public class CardAdapter extends ArrayAdapter<Card> {

  private Card[] myData;
  private int myResourceId;
  private Context myContext;

  public CardAdapter(Context context, int resourceId, Card[] objects) {
    super(context, resourceId, objects);
    myData = objects;
    myResourceId = resourceId;
    myContext = context;
  }


  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View row = convertView;

    LayoutInflater inflater = ((Activity)myContext).getLayoutInflater();
    row = inflater.inflate(myResourceId, parent, false);

    TextView txtView = (TextView)row.findViewById(R.id.txtTitle);
    ImageView imgView = (ImageView)row.findViewById(R.id.imgIcon);
    System.out.println(myData[position].getName());
    txtView.setText(myData[position].getName());
    imgView.setImageResource(R.drawable.ic_launcher);

    //WeatherHolder holder = null;

    //if(row == null)
    //{
      //LayoutInflater inflater = ((Activity)myContext).getLayoutInflater();
      //row = inflater.inflate(myResourceId, parent, false);

      //holder = new WeatherHolder();
      //holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
      //holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);

      //row.setTag(holder);
    //}
    //else
    //{
      //holder = (WeatherHolder)row.getTag();
    //}

    //Weather weather = data[position];
    //holder.txtTitle.setText(weather.title);
    //holder.imgIcon.setImageResource(weather.icon);

    return row;
  }

}
