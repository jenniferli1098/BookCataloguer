package com.jenniferliang.bookcataloguer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "bookCataloguer.db";
    public static final String USERS_TABLE_NAME = "users";
    public static final String USERS_COLUMN_ID = "id";
    public static final String USERS_COLUMN_USERNAME = "username";
    public static final String USERS_COLUMN_PASSWORD = "password";

    public static final String BOOKS_TABLE_NAME= "books";
    public static final String BOOKS_COLUMN_ID = "id";
    public static final String BOOKS_COLUMN_TITLE = "title";
    public static final String BOOKS_COLUMN_AUTHOR = "author";
    public static final String BOOKS_COLUMN_USER_ID = "user_id";
    public static final String BOOKS_COLUMN_ISBN = "isbn";
    public static final String BOOKS_COLUMN_COVERURL = "coverUrl";
    public static final String BOOKS_COLUMN_PUBLISHDATE = "publishDate";



    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS books");
        db.execSQL("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY, username TEXT, password TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS books (id INTEGER PRIMARY KEY, title TEXT, author TEXT," +
                " isbn TEXT, user_id INTEGER, coverUrl TEXT, publishDate TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS books");
        onCreate(db);
    }
    public boolean insertUser (String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERS_COLUMN_USERNAME, username);
        contentValues.put(USERS_COLUMN_PASSWORD, password);
        db.insert(USERS_TABLE_NAME, null, contentValues);
        return true;
    }
    public boolean insertBook (Bundle bundle) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BOOKS_COLUMN_TITLE, bundle.getString("title"));
        contentValues.put(BOOKS_COLUMN_AUTHOR, bundle.getString("author"));
        contentValues.put(BOOKS_COLUMN_USER_ID, bundle.getInt("user_id",0));
        contentValues.put(BOOKS_COLUMN_ISBN, bundle.getString("isbn"));
        contentValues.put(BOOKS_COLUMN_COVERURL,bundle.getString("coverUrl"));
        contentValues.put(BOOKS_COLUMN_PUBLISHDATE,bundle.getString("publishDate"));
        db.insert(BOOKS_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String select = USERS_COLUMN_USERNAME + " = '" + username + "'";
        Cursor res;
        try {
             res = db.query(USERS_TABLE_NAME, new String[]{USERS_COLUMN_ID, USERS_COLUMN_USERNAME,
                            USERS_COLUMN_PASSWORD}, select,
                    null, null, null, null);
        }catch(Exception e){
            return null;
        }

        return res;
    }

    public Cursor getBooks(int user_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String select = BOOKS_COLUMN_USER_ID + "=" + user_id;
        Cursor res;
        try {
            res = db.query(BOOKS_TABLE_NAME, new String[]{BOOKS_COLUMN_TITLE, BOOKS_COLUMN_AUTHOR, BOOKS_COLUMN_ISBN,BOOKS_COLUMN_COVERURL,BOOKS_COLUMN_PUBLISHDATE}, select,
                    null, null, null, null);
        }catch(Exception e){
            return null;
        }

        return res;
    }

    public boolean hasBook(int user_id, String isbn) {
        SQLiteDatabase db = this.getReadableDatabase();
        String select = BOOKS_COLUMN_USER_ID + "=" + user_id + " AND "+BOOKS_COLUMN_ISBN+"="+isbn;
        Cursor res;
        try {
            res = db.query(BOOKS_TABLE_NAME, new String[]{BOOKS_COLUMN_TITLE, BOOKS_COLUMN_AUTHOR, BOOKS_COLUMN_ISBN,BOOKS_COLUMN_COVERURL,BOOKS_COLUMN_PUBLISHDATE}, select,
                    null, null, null, null);
        }catch(Exception e){
            return false;
        }

        return res.getCount()>0;
    }
}
