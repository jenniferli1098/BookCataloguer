package com.jenniferliang.bookcataloguer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class DisplayNavigationActivity extends AppCompatActivity {

    ListView mainMenu;

    String TAG = "DisplayNavigationActivity";
    int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_navigation);

        mainMenu = findViewById(R.id.mainMenu);

        Intent intent = getIntent();

        id = intent.getIntExtra("id",0);

        final ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("See all books");
        arrayList.add("Add book");
        arrayList.add("Search books");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);

        mainMenu.setAdapter(arrayAdapter);
        mainMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sendMessage(view,position);
            }
        });
    }
    public void sendMessage(View view, int position) {

        Intent intent;

        String str ="";
        switch(position){
            case 0:
                //show all books
                intent = new Intent(this, DisplayAllBooksActivity.class);

                str="show all books";
                break;

            case 1:
                //add a book
                intent = new Intent(this, AddBookActivity.class);
                str="add a book";
                break;

            case 2:
                //search for a book
                intent = new Intent(this, SearchBookActivity.class);
                str="search for a book";
                break;

            default:
                intent = new Intent(this, DisplayNavigationActivity.class);
                break;
        }

        Log.d(TAG,str);
        intent.putExtra("id",id);
        startActivity(intent);
    }
}
