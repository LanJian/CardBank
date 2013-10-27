package com.jackhxs.cardbank;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jackhxs.data.SimpleCard;
import com.jackhxs.remote.Constants.Operation;
import com.jackhxs.remote.RemoteService;
import com.xtremelabs.imageutils.ImageLoader;

// Fragment with a list view of the contacts
public class CardListFragment extends Fragment {
	private TextView emptyMsg;
	private ListView myListView;
	private CardAdapter myAdapter;
	private ImageLoader mImageLoader;
	private boolean mIsRefer;
	private Integer toRefer;

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		
		mIsRefer = false;
		Bundle args = getArguments();
		if (args != null) {
			mIsRefer = args.getBoolean("isRefer", false);
			toRefer = args.getInt("toRefer", 0);
		}
	}

	public void onListItemClick(AdapterView<?> l, View v, int position, long id) {
		if (mIsRefer) {
			// refer the card

			final Intent serviceIntent = new Intent(Intent.ACTION_SYNC, null, getActivity(),
					RemoteService.class);

			serviceIntent.putExtra("operation", (Parcelable) Operation.REFER);
			serviceIntent.putExtra("referredTo", App.myContacts[position].userId); 
			serviceIntent.putExtra("cardId", App.myContacts[toRefer]._id);
			
			getActivity().startService(serviceIntent);

		} else {
			Intent intent = new Intent(getActivity(), CardFlipView.class);
			intent.putExtra("mode", "contact");
			intent.putExtra("position", position);
			startActivity(intent);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.activity_card_list, container, false);
		mImageLoader = ImageLoader.buildImageLoaderForFragment(this);

		myListView = (ListView) view.findViewById(R.id.list_view);
		emptyMsg = (TextView) view.findViewById(R.id.empty_message);
		
		if (App.myContacts.length > 0) {
			emptyMsg.setVisibility(View.GONE);
		}
		
		ArrayList<SimpleCard> list = new ArrayList<SimpleCard>();
		list.addAll(Arrays.asList(App.myContacts));
		
		myAdapter = new CardAdapter(getActivity(), R.layout.list_view_row, list, mImageLoader);
		myListView.setAdapter(myAdapter);

		myListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				onListItemClick(parent, v, position, id);

				if (mIsRefer) {
					ImageView referredIcon = (ImageView) v.findViewById(R.id.referredIcon);
					referredIcon.setVisibility(View.VISIBLE);
				}
			}
		});

		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroy();
		mImageLoader.destroy();
	}
}
