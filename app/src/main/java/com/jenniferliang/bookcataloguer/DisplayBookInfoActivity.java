package com.jenniferliang.bookcataloguer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class DisplayBookInfoActivity extends AppCompatActivity {

    String TAG = "DisplayInfoBookActivity";
    int id;


    MyReceiver receiver;

    //Button toggle
    Button addBtn;


    ImageView imageView;
    TextView titleTV;
    TextView authorTV;
    TextView publishDateTV;

    Context mContext;

    Bundle bundle;
    DBHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"opening "+TAG);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_book_info);

        mydb = new DBHelper(this);
        mContext = this;

        imageView = findViewById(R.id.imageView);
        titleTV = findViewById(R.id.titleTV);
        authorTV = findViewById(R.id.authorTV);
        publishDateTV = findViewById(R.id.publishDateTV);


        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);

        bundle = intent.getExtras();
        String title = getBundleString(bundle,"title","Title");
        String author = getBundleString(bundle,"author","Author");
        String publish = getBundleString(bundle,"publishDate","Published: ");
        final String isbn = getBundleString(bundle,"isbn","");
        String coverUrl = getBundleString(bundle,"coverUrl","");

        Log.d(TAG,"isbn: "+isbn);
        titleTV.setText(title);
        authorTV.setText(author);
        publishDateTV.setText(publish);

        //find out if user has book and decide on button
        addBtn = findViewById(R.id.addBtn);
        if (mydb.hasBook(id,isbn)){
            addBtn.setText("Remove");
        }else{
            addBtn.setText("Add");
        }

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addBtn.getText().toString().equals("Remove")){
                    //remove book from db
                    mydb.deleteBook(id,isbn);
                    addBtn.setText("Add");

                }else{
                    //add book to db
                    mydb.insertBook(id, bundle);
                    addBtn.setText("Remove");

                }
            }
        });


        Intent _intent = new Intent();
        _intent.putExtra("coverUrl",coverUrl);
        imageLoader.enqueueWork(mContext, _intent);


        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("picture");
        registerReceiver(receiver, filter);

    }
    public String getBundleString(Bundle b, String key, String def)
    {
        String value = b.getString(key);
        if (value == null)
            value = def;
        return value;
    }



    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                Log.d(TAG, " receiving picture");
                String intentAction = intent.getAction();
                Log.d(TAG, " onReceive=" + intentAction);

                Bitmap bmp = intent.getExtras().getParcelable("BitmapImage");
                imageView.setImageBitmap(bmp);

            }
            catch (Exception e){}
        }
    }
}
