package com.example.record_trail;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper_favorite extends SQLiteOpenHelper {
    public DBHelper_favorite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE if not exists favorite ("
                + "_id integer primary key autoincrement,"
                + "id text);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE if exists favorite";

        db.execSQL(sql);
        onCreate(db);
    }
}

