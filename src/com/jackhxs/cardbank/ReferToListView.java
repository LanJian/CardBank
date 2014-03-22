package com.jackhxs.cardbank;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class ReferToListView extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        
        if (savedInstanceState == null) {
            Fragment fragment = new MyContactsFragment();
            Bundle args = new Bundle();
            
            args.putInt("toRefer", getIntent().getIntExtra("toRefer", 0));
            args.putBoolean("isRefer", true);
            
            fragment.setArguments(args);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(android.R.id.content, fragment).commit();
        }
        
    }
}
