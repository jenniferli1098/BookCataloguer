package com.jenniferliang.bookcataloguer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DisplayAllBooksActivity extends AppCompatActivity {

    int id;

    String TAG = "DisplayAllBooksActivity";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<ListItem> listItemList;
    private List<String> isbns;
    Context mContext;

    DBHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_books);

        id = getIntent().getIntExtra("id",0);
        mydb = new DBHelper(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItemList = new ArrayList<>();
        isbns = new ArrayList<>();

        mContext = this.getApplicationContext();


        Cursor cursor = mydb.getBooks(id);

        if(cursor.getCount()>0) {
            while (cursor.moveToNext()) {
                String title = cursor.getString(
                        cursor.getColumnIndexOrThrow("title"));
                String author = cursor.getString(
                        cursor.getColumnIndexOrThrow("author"));
                String isbn = cursor.getString(
                        cursor.getColumnIndexOrThrow("isbn"));
                Log.d(TAG,title+isbn);
                listItemList.add(new ListItem(title, author));
                isbns.add(isbn);

            }
        }
        cursor.close();


        adapter = new MyAdapter(listItemList,isbns, id, this);

        recyclerView.setAdapter(adapter);
    }

}
