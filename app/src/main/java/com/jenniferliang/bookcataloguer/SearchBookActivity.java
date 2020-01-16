package com.jenniferliang.bookcataloguer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SearchBookActivity extends AppCompatActivity {

    private int id;


    Context mContext;
    MyReceiver receiver;


    EditText titleField;
    EditText authorField;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);

        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);


        mContext = this.getApplicationContext();

        titleField = findViewById(R.id.titleField);
        authorField = findViewById(R.id.authorField);

        btn = findViewById(R.id.searchButton);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("functionName", "search");
                String title = titleField.getText().toString();
                String author = authorField.getText().toString();
                if(title.equals("") && author.equals("")) {
                    Toast.makeText(mContext, "Please enter a title or author",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                bundle.putString("title", title);
                bundle.putString("author", author);

                Intent _intent = new Intent();
                _intent.putExtras(bundle);
                bookAPIHandler.enqueueWork(mContext, _intent);
            }
        });





        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("searchResults");
        registerReceiver(receiver, filter);

        Log.d("TAG","Listening to receiver");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null)
            this.unregisterReceiver(receiver);
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

                    Intent intent1 = new Intent(mContext, DisplayBookInfoActivity.class);

                    intent1.putExtras(b);

                    intent1.putExtra("id",id);
                    startActivity(intent1);


                }

            }
            catch (Exception e){}
        }
    }
}
