package com.softpo.myteacyclopedia.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by my on 2016/11/14.
 */

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    public MySQLiteOpenHelper(Context context) {
        super(context, "tea.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //添加数据表
        db.execSQL("create table collectTea(_id integer primary key autoincrement," +
                "title varchar,source varchar,create_time varchar,nickname varchar,id varchar)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
