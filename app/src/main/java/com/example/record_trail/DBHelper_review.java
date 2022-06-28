package com.example.record_trail;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper_review extends SQLiteOpenHelper {
    public DBHelper_review(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE if not exists review ("
                + "_id integer primary key autoincrement,"
                + "date text,"
                + "content text,"
                + "image blob);";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE if exists review";

        db.execSQL(sql);
        onCreate(db);
    }
}

