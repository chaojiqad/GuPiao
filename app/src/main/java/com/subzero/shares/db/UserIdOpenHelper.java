package com.subzero.shares.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by xzf on 2016/5/17.
 */
public class UserIdOpenHelper extends SQLiteOpenHelper {

    private Context context;

    public static final String CREATE_USER = "create table UserGroup(" +
            " id integer primary key autoincrement, " +
            " userid text, " + "username text," +
            " avatar text,"+"groupid text)";

    public static final String CREATE_GROUP = "create table Groups(" +
            " id integer primary key autoincrement, " +
            " groupid text, " + "grouptitle text," +
            " groupavatar text)";


    public UserIdOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_GROUP);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
