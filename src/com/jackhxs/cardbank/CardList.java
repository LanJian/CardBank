package com.jackhxs.cardbank;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CardList extends ListActivity {
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
      "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
      "Linux", "OS/2", "random", "foo", "bar", "yoyo" };
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_list_item_1, values);
    setListAdapter(adapter);
  }

  public void onListItemClick(ListView l, View v, int position, long id) {
    Intent intent = new Intent(this, CardHolder.class);
    startActivity(intent);
  }
}
