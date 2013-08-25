package com.jackhxs.cardbank;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jackhxs.data.SimpleCard;

@SuppressLint("ValidFragment")
public class CardFragment extends Fragment {
	private SimpleCard myCard;

	@SuppressLint("ValidFragment")
	public CardFragment(SimpleCard card) {
		myCard = card;
		this.setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// Inflate the menu items for use in the action bar
		inflater.inflate(R.menu.main_activity_actions, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, 
			Bundle savedInstanceState) {

		// Inflate the layout for this fragment
		Log.e("paul", "create fragment");
		View item = inflater.inflate(R.layout.card_flip_view, container, false);

		TextView txtView = (TextView) item
				.findViewById(R.id.card_flip_view_name);
		txtView.setText(myCard.firstName + " " + myCard.lastName);

		txtView = (TextView) item.findViewById(R.id.card_flip_view_phoneNo);
		txtView.setText(myCard.phoneNo);

		txtView = (TextView) item.findViewById(R.id.card_flip_view_email);
		txtView.setText(myCard.email);

		ImageView imgView = (ImageView) item
				.findViewById(R.id.card_flip_view_image);
		imgView.setImageResource(myCard.image);

		return item;
	}

}