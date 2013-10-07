package com.jackhxs.cardbank;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.xtremelabs.imageutils.ImageLoader;


public class ReferralsListFragment extends Fragment {
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
		myAdapter = new CardAdapter(getActivity(), R.layout.refer_list_row, App.myContacts, mImageLoader);
		myListView.setAdapter(myAdapter);
        myListView.setDivider(null);
        myListView.setDividerHeight(5);

		return view;
	}

}
