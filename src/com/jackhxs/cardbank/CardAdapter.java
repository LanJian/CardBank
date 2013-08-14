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
    View row = convertView;

    LayoutInflater inflater = ((Activity)myContext).getLayoutInflater();
    row = inflater.inflate(myResourceId, parent, false);

    TextView txtView = (TextView)row.findViewById(R.id.txtTitle);
    txtView.setText(myData[position].firstName + myData[position].lastName);
    ImageView imgView = (ImageView)row.findViewById(R.id.imgIcon);
    
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
