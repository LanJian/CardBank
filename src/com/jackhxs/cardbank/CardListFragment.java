package com.jackhxs.cardbank;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.jackhxs.remote.Constants.Operation;
import com.jackhxs.remote.JSONResultReceiver;
import com.jackhxs.remote.RemoteService;
import com.xtremelabs.imageutils.ImageLoader;

// Fragment with a list view of the contacts
public class CardListFragment extends Fragment {
	private ListView myListView;
	private CardAdapter myAdapter;
    private ImageLoader mImageLoader;
    private boolean mIsRefer;

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
        mIsRefer = false;
        Bundle args = getArguments();
        if (args != null) {
            mIsRefer = getArguments().getBoolean("isRefer", false);
        }
	}


	public void onListItemClick(AdapterView<?> l, View v, int position, long id) {
        if (mIsRefer) {
            // refer the card

            final Intent serviceIntent = new Intent(Intent.ACTION_SYNC, null, getActivity(),
                    RemoteService.class);

            serviceIntent.putExtra("operation", (Parcelable) Operation.REFER);
            serviceIntent.putExtra("referredTo", App.myContacts[position].firstName);

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

		App app = (App) getActivity().getApplication();

		myListView = (ListView) view.findViewById(R.id.list_view);
		myAdapter = new CardAdapter(getActivity(), R.layout.list_view_row, app.myContacts, mImageLoader);

		myListView.setAdapter(myAdapter);

		myListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> l, View v, int position, long id) {
				onListItemClick(l, v, position, id);
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
