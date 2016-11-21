package com.example.kangpei.saver.Model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.kangpei.saver.Model.bean.Bill;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kangpei on 14/11/16.
 */

public class DBManager {

    /*
    * 数据库操作的工具类，负责数据的增删改查
    * */
    private static DBManager dbManager= new DBManager();
    private MyDbHelper dbHelper;

    public void onInit(Context context){//得到一个dbhelper的实例，用于初始化数据库
          dbHelper=MyDbHelper.getDbHelper(context);
    }

    public static synchronized DBManager getDbManager(){//得到dbmanager实例
        return dbManager;
    }

    //添加bill的链表数据数据
    public synchronized boolean saveBillData(List<Bill> bills){
        SQLiteDatabase db=dbHelper.getWritableDatabase();//得到sqllite对象
        int i=0;
        if (bills.size()>0) {
            if(db.isOpen()){
                db.delete(BillDao.TABLE_NAME,null,null);
            for (Bill bill : bills) {
                ContentValues contentValues=new ContentValues();
                contentValues.put(BillDao.COLUMN_Type,bill.getTypeId());
                contentValues.put(BillDao.COLUMN_Money,bill.getMoney());
                contentValues.put(BillDao.COLUMN_Time,bill.getTime());
                db.replace(BillDao.TABLE_NAME,null,contentValues);
                i++;
            }
            }
        }
        return i==bills.size();
    }

    //添加单个账单数据
    public synchronized void saveBill(Bill bill){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        if (db.isOpen()){
            ContentValues contentValues=new ContentValues();
            contentValues.put(BillDao.COLUMN_Type,bill.getTypeId());
            contentValues.put(BillDao.COLUMN_Money,bill.getMoney());
            contentValues.put(BillDao.COLUMN_Time,bill.getTime());
            db.insert(BillDao.TABLE_NAME,null,contentValues);
            contentValues.clear();
        }
    }

   

    //根据日期获取账单
   public synchronized List<Bill> getBill(long time) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Bill> bills = new ArrayList<>();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from " + BillDao.TABLE_NAME + " where " + BillDao.COLUMN_Time + " > ?", new String[]{time + ""});
            Bill bill;
            while (cursor.moveToNext()) {
                bill = new Bill();
                bill.setTypeId(cursor.getString(cursor.getColumnIndex(BillDao.COLUMN_Type)));
                bill.setMoney(cursor.getString(cursor.getColumnIndex(BillDao.COLUMN_Money)));
                bill.setTime(cursor.getString(cursor.getColumnIndex(BillDao.COLUMN_Time)));
                bills.add(bill);
            }
            cursor.close();
        }
        return bills;
    }

}
