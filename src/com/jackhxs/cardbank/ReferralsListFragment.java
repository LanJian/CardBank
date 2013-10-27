package com.jackhxs.cardbank;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.jackhxs.data.SimpleCard;
import com.xtremelabs.imageutils.ImageLoader;


public class ReferralsListFragment extends Fragment {
	private TextView emptyMsg;

	private ListView myListView;
	private CardAdapter myAdapter;
    private ImageLoader mImageLoader;

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.list_view, container, false);
        mImageLoader = ImageLoader.buildImageLoaderForFragment(this);

		myListView = (ListView) view.findViewById(R.id.list_view);
		
		emptyMsg = (TextView) view.findViewById(R.id.empty_message);
		
		if (App.myContacts.length > 0) {
			emptyMsg.setVisibility(View.GONE);
		}
		
		
        ArrayList<SimpleCard> list = new ArrayList<SimpleCard>();
        list.addAll(Arrays.asList(App.myContacts));
		myAdapter = new CardAdapter(getActivity(), R.layout.refer_list_row, list, mImageLoader);
		myListView.setAdapter(myAdapter);
        myListView.setDivider(null);
        myListView.setDividerHeight(5);

		return view;
	}

}
