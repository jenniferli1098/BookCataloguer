package com.jenniferliang.bookcataloguer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class imageLoader extends JobIntentService {

    public static final int JOB_ID = 1023;


    static public void enqueueWork(Context context, Intent work) {
        enqueueWork(context, imageLoader.class, JOB_ID, work);
    }
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        //set up things

        String coverUrl = intent.getStringExtra("coverUrl");
        Bitmap bmp = null;
        try{
        URL url = new URL(coverUrl);
        bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (Exception e) {

        }


        Bundle b = new Bundle();
        b.putParcelable("BitmapImage",bmp);
        Log.d("BROADCAST", "sending broadcast");

        Intent _intent = new Intent();
        intent.putExtras(b);
        sendBroadcast(_intent);


    }
}
