package com.jackhxs.cardbank;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Bitmap;
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
import com.jackhxs.util.ImageUtil;

@SuppressLint("ValidFragment")
public class CardFragment extends Fragment {
	private SimpleCard myCard;
	
	private void updateUI(View item) {
		int index = 0;
		if (!myCard.imageUrl.equals("")) {
			index = Integer.parseInt(myCard.imageUrl);
		}
		
		String name, phone, email, company, address, jobTitle;
		
		// Inflate the layout for this fragment
		Log.e("paul", "create fragment");
		
		TextView txtView = (TextView) item
				.findViewById(R.id.card_flip_view_name);
		txtView.setText(myCard.firstName + " " + myCard.lastName);
		name = txtView.getText().toString();
		
		txtView = (TextView) item.findViewById(R.id.card_flip_view_phoneNo);
		txtView.setText(myCard.phoneNo);
		phone = txtView.getText().toString();

		txtView = (TextView) item.findViewById(R.id.card_flip_view_email);
		txtView.setText(myCard.email);
		email = txtView.getText().toString();
		
		txtView = (TextView) item.findViewById(R.id.card_flip_view_company);
        txtView.setText(myCard.company);
        company = txtView.getText().toString();

        txtView = (TextView) item.findViewById(R.id.card_flip_view_jobTitle);
        txtView.setText(myCard.jobTitle);
        jobTitle = txtView.getText().toString();

        txtView = (TextView) item.findViewById(R.id.card_flip_view_address);
        txtView.setText(myCard.address);
        address = txtView.getText().toString();
		
		ImageView imgView = (ImageView) item
				.findViewById(R.id.card_flip_view_image);
		
		Bitmap newCard = ImageUtil.GenerateCardImage(getActivity(), App.templateConfig[index], name, email, phone, company, address, jobTitle);
		imgView.setImageBitmap(newCard);
	}
	
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

		
		View item = inflater.inflate(R.layout.card_flip_view, container, false);
		updateUI(item);
		return item;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		myCard = App.myCards[0];
		updateUI(getView());
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		//mImageLoader.destroy();
	}
}
