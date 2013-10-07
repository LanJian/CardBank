package com.jackhxs.cardbank;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jackhxs.data.SimpleCard;
import com.jackhxs.util.ImageUtil;
import com.xtremelabs.imageutils.ImageLoader;

public class CardAdapter extends ArrayAdapter<SimpleCard> {

  private static final int[] ICON_BG_COLORS = {
    0xff1b7c59, 0xffe76e66, 0xffdca46b, 0xff5699a9, 0xff695b8e, 0xff8c5e7a
  };

  private SimpleCard[] myData;
  private int myResourceId;
  private Context myContext;
  private ImageLoader myImageLoader;

  public CardAdapter(Context context, int resourceId, SimpleCard[] objects, ImageLoader mImageLoader) {
    super(context, resourceId, objects);
    myData = objects;
    myResourceId = resourceId;
    myContext = context;
    myImageLoader = mImageLoader;
  }


  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    switch(myResourceId) {
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

    LayoutInflater inflater = ((Activity)myContext).getLayoutInflater();
    row = inflater.inflate(myResourceId, parent, false);

    TextView txtView = (TextView)row.findViewById(R.id.txtTitle);
    txtView.setText(myData[position].firstName + " " + myData[position].lastName);

    txtView = (TextView)row.findViewById(R.id.imgIcon);
    txtView.setText(myData[position].lastName.substring(0,1).toUpperCase());
    int rand = (int)(Math.random() * ICON_BG_COLORS.length);
    txtView.setBackgroundColor(ICON_BG_COLORS[rand]);

    return row;
  }

  public View getReferListRowView(int position, View convertView, ViewGroup parent) {
    View row = convertView;

    LayoutInflater inflater = ((Activity)myContext).getLayoutInflater();
    row = inflater.inflate(myResourceId, parent, false);

    String name, phone, email;

    name = myData[position].firstName + " " + myData[position].lastName;
    phone = myData[position].phoneNo;
    email = myData[position].email;

    ImageView imgView = (ImageView)row.findViewById(R.id.refer_list_row_image);
    int index = Integer.parseInt(myData[position].imageUrl);
    Bitmap newCard = ImageUtil.GenerateCardImage((Activity) myContext, App.templateConfig[index], name, email, phone);
    imgView.setImageBitmap(newCard);

    Button addButton = (Button)row.findViewById(R.id.refer_list_row_button_add);
    addButton.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Log.e("paul", "click add refer");
        }
    });

    Button ignoreButton = (Button)row.findViewById(R.id.refer_list_row_button_ignore);
    ignoreButton.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Log.e("paul", "click ignore refer");
        }
    });

    return row;
  }

  public View getFlipItemView(int position, View convertView, ViewGroup parent) {
    View item = convertView;
    String name, phone, email;
    
    LayoutInflater inflater = ((Activity)myContext).getLayoutInflater();
    item = inflater.inflate(myResourceId, parent, false);

    TextView txtView = (TextView)item.findViewById(R.id.card_flip_view_name);
    txtView.setText(myData[position].firstName + " " + myData[position].lastName);
    name = txtView.getText().toString();

    txtView = (TextView)item.findViewById(R.id.card_flip_view_phoneNo);
    txtView.setText(myData[position].phoneNo);
    phone = txtView.getText().toString();
    Linkify.addLinks(txtView, Linkify.PHONE_NUMBERS);

    txtView = (TextView)item.findViewById(R.id.card_flip_view_email);
    txtView.setText(myData[position].email);
    email = txtView.getText().toString();
    Linkify.addLinks(txtView, Linkify.EMAIL_ADDRESSES);

    ImageView imgView = (ImageView)item.findViewById(R.id.card_flip_view_image);
    //myImageLoader.loadImage(imgView, myData[position].imageUrl);
    int index = Integer.parseInt(myData[position].imageUrl);
    Bitmap newCard = ImageUtil.GenerateCardImage((Activity) myContext, App.templateConfig[index], name, email, phone);
    imgView.setImageBitmap(newCard);
    
    return item;
  }
}
