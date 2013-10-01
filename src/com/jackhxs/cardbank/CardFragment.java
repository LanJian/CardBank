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
import com.xtremelabs.imageutils.ImageLoader;

@SuppressLint("ValidFragment")
public class CardFragment extends Fragment {
	private SimpleCard myCard;
    private ImageLoader mImageLoader;

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
		String name, phone, email;
		
		// Inflate the layout for this fragment
		Log.e("paul", "create fragment");
		View item = inflater.inflate(R.layout.card_flip_view, container, false);

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
		
		ImageView imgView = (ImageView) item
				.findViewById(R.id.card_flip_view_image);
		
//		mImageLoader = ImageLoader.buildImageLoaderForFragment(this);
//		mImageLoader.loadImage(imgView, myCard.imageUrl);

		int index = Integer.parseInt(myCard.imageUrl);
		Bitmap newCard = ImageUtil.GenerateCardImage(getActivity(), App.templateConfig[index], name, email, phone);
		imgView.setImageBitmap(newCard);

		return item;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		String name, phone, email;
		myCard = ((App) getActivity().getApplication()).myCards[0];
		
		TextView txtView = (TextView) getView().findViewById(R.id.card_flip_view_name);
		txtView.setText(myCard.firstName + " " + myCard.lastName);
		name = txtView.getText().toString();
		
		txtView = (TextView) getView().findViewById(R.id.card_flip_view_phoneNo);
		txtView.setText(myCard.phoneNo);
		phone = txtView.getText().toString();
		
		txtView = (TextView) getView().findViewById(R.id.card_flip_view_email);
		txtView.setText(myCard.email);
		email = txtView.getText().toString();
		
		ImageView imgView = (ImageView) getView() .findViewById(R.id.card_flip_view_image);
		int index = Integer.parseInt(myCard.imageUrl);
		Bitmap newCard = ImageUtil.GenerateCardImage(getActivity(), App.templateConfig[index], name, email, phone);
		imgView.setImageBitmap(newCard);

	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		//mImageLoader.destroy();
	}
}
