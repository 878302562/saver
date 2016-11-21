package com.example.kangpei.saver.Model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kangpei on 14/11/16.
 */

public class MyDbHelper extends SQLiteOpenHelper {
    private final static String CREATE_TABLE="CREATE TABLE "+BillDao.TABLE_NAME+" ("+BillDao.COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +BillDao.COLUMN_Type+" TEXT, "
            +BillDao.COLUMN_Money+" TEXT, "
            +BillDao.COLUMN_Time+" TEXT);";
    private static  final int VERSION=1;
    public static  MyDbHelper dbHelper;
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {//数据库创建时需要执行的方法
       sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {//更新数据库需要执行的方法

    }

    public static MyDbHelper getDbHelper(Context context){//得到构造器的实例
        if (dbHelper==null){
            return new MyDbHelper(context);
        }
        return dbHelper;
    }


    public MyDbHelper(Context context){//构造器
        super(context,"saver.db", null,VERSION);
    }
}
