package com.vincent.android.utils;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.content.Context;
import android.util.Log;

/**
 * Created by Feng on 2015-06-28.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    static String name = "user.db";
    static  int dbVersion = 7;

    public DatabaseHelper(Context context){
        super(context, name, null, dbVersion);
    }

    //只在创建的时候用一次
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table user(" +
                "id integer primary key autoincrement," +
                "username varchar(20) not null," +
                "password varchar(20) not null," +
                "mail varchar(20)," +
                "avatar blob," +
                "role char(1) )";
        sqLiteDatabase.execSQL(sql);
        String createAdminAccountSql = "INSERT INTO user (username, password, role) VALUES ('admin', '123456', 'y');";
        sqLiteDatabase.execSQL(createAdminAccountSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
