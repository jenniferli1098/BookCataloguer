package com.jenniferliang.bookcataloguer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;

public class bookAPIHandler extends JobIntentService {
    static final int JOB_ID = 1006;

    static final String SEARCH_URL = "https://openlibrary.org/search.json";
    private static Context mContext;
    private final static String TAG = "bookAPIHandler";


    static public void enqueueWork(Context context, Intent work) {
        mContext = context;
        enqueueWork(context, bookAPIHandler.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        HttpURLConnection httpConn = null;
        InputStream in = null;
        try {
            //DCLog.d("HttpSenderHandler Cell signal = " + String.valueOf(Connectivity.getSignalStrength(mContext)));
            Bundle bundle = intent.getExtras();
            Hashtable<String, String> input = new Hashtable<String, String>();
                input.put("author", getBundleString(bundle, "author", ""));
                input.put("title", getBundleString(bundle, "title", ""));


            String urlStr = prepareQuery(input,SEARCH_URL);

            URL url = new URL(urlStr);
            httpConn = (HttpURLConnection) url.openConnection();
            if (httpConn == null)
                throw new IOException("Http connection is null");

            httpConn.setRequestMethod("GET");
            httpConn.setConnectTimeout(30000);

            int status = httpConn.getResponseCode();
            String statusMessage = httpConn.getResponseMessage();
            if (status != HttpURLConnection.HTTP_OK)
                throw new ServerException(status, statusMessage + ". Check if server available.");

            in = httpConn.getInputStream();
            BufferedReader inreader = new BufferedReader(new InputStreamReader(in));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = inreader.readLine()) != null) {
                response.append(inputLine);
            }
            inreader.close();
            Log.d(TAG, " onHandleIntent() response :" + response.toString());

            JSONArray results = new JSONObject(response.toString()).getJSONArray("docs");

            String title = "";
            String author = "";
            String isbn = "";

            for (int i = 0; i < results.length(); i++) {
                JSONObject jsonobj = results.getJSONObject(0);

                if (!jsonobj.has("isbn")) {
                    continue;
                }

                title = jsonobj.getString("title");
                author = jsonobj.getJSONArray("author_name").getString(0);
                isbn = jsonobj.getJSONArray("isbn").getString(0);


                break;
            }

            //sending broadcast
            Bundle b = new Bundle();

            b.putString("isbn", isbn);
            b.putString("title", title);
            b.putString("author", author);

            urlStr = "https://openlibrary.org/api/volumes/brief/isbn/"+isbn+".json";

            url = new URL(urlStr);
            httpConn = (HttpURLConnection) url.openConnection();
            if (httpConn == null)
                throw new IOException("Http connection is null");

            httpConn.setRequestMethod("GET");
            httpConn.setConnectTimeout(30000);

            status = httpConn.getResponseCode();
            statusMessage = httpConn.getResponseMessage();
            if (status != HttpURLConnection.HTTP_OK)
                throw new ServerException(status, statusMessage + ". Check if server available.");

            in = httpConn.getInputStream();
            inreader = new BufferedReader(new InputStreamReader(in));
            response = new StringBuffer();
            while ((inputLine = inreader.readLine()) != null) {
                response.append(inputLine);
            }
            inreader.close();
            Log.d(TAG, " onHandleIntent() response :" + response.toString());

            JSONObject result = new JSONObject(response.toString()).getJSONObject("records");
            result = result.getJSONObject(result.names().getString(0)).getJSONObject("data");

            String coverUrl = result.getJSONObject("cover").getString("medium");
            String publishDate = result.getString("publish_date");

            b.putString("coverUrl",coverUrl);
            b.putString("publishDate", publishDate);

            Log.d(TAG, "sending broadcast");
            sendBroadcast("searchResults", b);



        } catch (Exception e) {
            Log.d(TAG, "onHandleIntent " + e.toString());
            final String s;
            if (e instanceof NullPointerException)
                s = "Invalid pointer operation";
            else if (e instanceof ServerException)
                s = ((ServerException) e).getHttpMessage();
            else if (e instanceof InterruptedIOException)
                s = "Timeout reached";
            else if (e instanceof IOException)
                s = "I/O error: " + e.getMessage();
            else
                s = e.getMessage();
            error(s);
            Bundle b = new Bundle();
            b.putString("status", "error");
            sendBroadcast("searchResults", b);
        } finally {
            try {
                if (in != null)
                    in.close();
                if (httpConn != null)
                    httpConn.disconnect();
            } catch (IOException e) {
                Log.d(TAG, "onHandleIntent IOException:" + e.toString());
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"=====googleAPIHandler onDestroy======");
    }

    private void sendBroadcast(String action, Bundle b){

        Intent _intent=new Intent();
        _intent.setAction(action);
        _intent.putExtras(b);
        sendBroadcast(_intent);
    }



    private void error(String message) {
        Log.d(TAG,"googleAPIHandler error message:" + message);
    }

    public String getBundleString(Bundle b, String key, String def)
    {
        String value = b.getString(key);
        if (value == null)
            value = def;
        return value;
    }


    private String prepareQuery(Hashtable<String,String> input, String url) {
        StringBuffer query = new StringBuffer(url + "?");
        boolean isFirst = true;
        for (Enumeration keys = input.keys(); keys.hasMoreElements();) {
            String key = (String) keys.nextElement();
            if (isFirst)
                isFirst = false;
            else
                query.append('&');
            String value = (String) input.get(key);
            query.append(key);
            query.append('=');
            query.append(value);

        }
        Log.d(TAG, " prepare query:" + query.toString());
        return query.toString();
    }


}
