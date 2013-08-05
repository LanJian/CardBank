package com.jackhxs.cardbank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.jackhxs.data.Card;

public class CardList extends Activity {
  private ListView myListView;

  public void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    //String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
    //"Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
    //"Linux", "OS/2", "random", "foo", "bar", "yoyo" };
    //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
    //android.R.layout.simple_list_item_1, values);
    //setListAdapter(adapter);

    setContentView(R.layout.activity_card_list);
    myListView = (ListView) findViewById(R.id.list_view);

    Card[] cardData = {};
    CardAdapter adapter = new CardAdapter(this, R.layout.list_view_row,
        cardData);
    myListView.setAdapter(adapter);
    myListView.setOnItemClickListener(new OnItemClickListener() {
      public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        onListItemClick(l, v, position, id);
      }
    });
  }

  public void onListItemClick(AdapterView<?> l, View v, int position, long id) {
    Intent intent = new Intent(this, CardView.class);
    startActivity(intent);
  }
}
