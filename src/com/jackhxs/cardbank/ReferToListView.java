package com.jackhxs.cardbank;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class ReferToListView extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        if (savedInstanceState == null) {
            Fragment fragment = new CardListFragment();
            Bundle args = new Bundle();
            
            args.putInt("toRefer", getIntent().getIntExtra("toRefer", 0));
            args.putBoolean("isRefer", true);
            
            fragment.setArguments(args);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(android.R.id.content, fragment).commit();
        }
        */
    }
}
