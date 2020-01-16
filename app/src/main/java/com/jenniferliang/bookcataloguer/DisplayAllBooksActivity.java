package com.jenniferliang.bookcataloguer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DisplayAllBooksActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<ListItem> listItemList;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_books);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItemList = new ArrayList<>();

        mContext = this.getApplicationContext();


        for(int i = 0; i < 10; i++) {
            ListItem listItem = new ListItem(
                    "heading "+i,
                    "Lorem ipsum dummy text"
            );
            listItemList.add(listItem);
        }
        adapter = new MyAdapter(listItemList, this);

        recyclerView.setAdapter(adapter);
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String intentAction = intent.getAction();
                Log.d("TAG", " onReceive=" + intentAction);
                if (intentAction.equalsIgnoreCase("searchResults")) {

                    //String value = intent.getExtras().getString("open_now");
                    //TextView tv = findViewById(R.id.openinghours);
                    //tv.setText(value);
                    //load
                    Bundle b = intent.getExtras();
                    ArrayList<String> titles = b.getStringArrayList("titles");
                    ArrayList<String> authors = b.getStringArrayList("authors");
                    ArrayList<String> isbns = b.getStringArrayList("isbns");

                    listItemList.clear();
                    for(int i = 0; i < titles.size(); i++) {
                        listItemList.add(new ListItem(titles.get(i), authors.get(i)));
                    }


                    adapter.notifyDataSetChanged();
                    Log.d("TAG", "Change in list is notified");


                }

            }
            catch (Exception e){}
        }
    }
}
