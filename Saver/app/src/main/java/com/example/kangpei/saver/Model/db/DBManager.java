package com.example.kangpei.saver.Model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.kangpei.saver.Model.bean.Bill;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
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
            Log.d("dbmanager----","bill.getid+"+bill.getTypeId());
            contentValues.put(BillDao.COLUMN_Type,bill.getTypeId());
            contentValues.put(BillDao.COLUMN_Money,bill.getMoney());
            contentValues.put(BillDao.COLUMN_Time,bill.getTime());//存入时需要存进去年月日，时分秒
            db.insert(BillDao.TABLE_NAME,null,contentValues);
            Log.d("qweqeqeqeq","manager的bill执行了");
            contentValues.clear();
        }
    }


    public synchronized List<Bill> getBillOfCurrentMonth(){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        List<Bill> bills=new ArrayList<>();
        if (db.isOpen()){
            SimpleDateFormat dateFormat=new SimpleDateFormat("yy/MM/dd");
            String date=dateFormat.format(new Date());
            Log.d("DBManager","time的值为"+date);
            String timeString=date.substring(3,5);//截取月份的值
            int i=Integer.parseInt(timeString);
            Log.d("DBManager","time的值为"+timeString);
            Cursor cursor=db.rawQuery("select * from money_table where substr(time_new,4,2) = "+"'"+i+"'"+" order by id desc",null);
            Bill bill;
            while (cursor.moveToNext()){
                bill=new Bill();
                bill.setTypeId(cursor.getString(cursor.getColumnIndex(BillDao.COLUMN_Type)));
                bill.setMoney(cursor.getString(cursor.getColumnIndex(BillDao.COLUMN_Money)));
                bill.setTime(cursor.getString(cursor.getColumnIndex(BillDao.COLUMN_Time)).substring(0,8));//取出来的时候只需要取出年月日
                bills.add(bill);
            }
            cursor.close();
        }
        return bills;
    }


    /*+ " where " + BillDao.COLUMN_Time + " > ?", new String[]{time + ""}*/
    //根据日期获取账单
    public synchronized List<Bill> getBill() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Bill> bills = new ArrayList<>();
        if (db.isOpen()) {
            Cursor cursor = db.rawQuery("select * from money_table order by id desc",null);
            Bill bill;
            while (cursor.moveToNext()) {
                bill = new Bill();
                bill.setTypeId(cursor.getString(cursor.getColumnIndex(BillDao.COLUMN_Type)));
                bill.setMoney(cursor.getString(cursor.getColumnIndex(BillDao.COLUMN_Money)));
                bill.setTime(cursor.getString(cursor.getColumnIndex(BillDao.COLUMN_Time)).substring(0,8));
                bills.add(bill);
            }
            cursor.close();
        }
        return bills;
    }

    public synchronized boolean deleteSelectedBill(Bill bill){
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        int size=getBill().size();
        Log.d("DBManager----delete","删除前的大小"+size);
        if (db.isOpen()){
            String time=bill.getTime();
            db.execSQL("delete from money_table where "+BillDao.COLUMN_Money+" = "+"'"+bill.getMoney()+"'"+" and "+BillDao.COLUMN_Type+" = "+"'"+bill.getTypeId()+"'"+" and "+BillDao.COLUMN_Time+" = "+"'"+time+"'");
        }
        Log.d("DBManager--delete","删除后的大小"+getBill().size());
        return true;
    }

    public synchronized List<Bill> getCertainTypeBillList(Bill bill){

        String typeId=bill.getTypeId();
        int i=Integer.parseInt(bill.getTime().toString().substring(3,5));
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        List<Bill> bills=new ArrayList<>();

        if (db.isOpen()){
            Bill bill1;
            Cursor cursor= db.rawQuery("select * from money_table where "+BillDao.COLUMN_Type+" = "+"'"+typeId+"'"+" and substr(time_new,4,2) = "+"'"+i+"'"+" order by id desc",null);
            while (cursor.moveToNext()){
                bill1=new Bill();
                bill1.setTypeId(cursor.getString(cursor.getColumnIndex(BillDao.COLUMN_Type)));
                bill1.setMoney(cursor.getString(cursor.getColumnIndex(BillDao.COLUMN_Money)));
                bill1.setTime(cursor.getString(cursor.getColumnIndex(BillDao.COLUMN_Time)));
                bills.add(bill1);
            }
            cursor.close();
        }
        return bills;
    }

    public synchronized HashSet<List<Bill>> getAllBillGroupByMonth(){
        SQLiteDatabase db=dbHelper.getReadableDatabase();
        HashSet<List<Bill>> hashSet=new LinkedHashSet<>();
        List<Bill> bills=new ArrayList<>();
        SimpleDateFormat dateFormat=new SimpleDateFormat("yy/MM/dd");
        String timeString=dateFormat.format(new Date()).substring(3,5);
        int currentMonth=Integer.parseInt(timeString);
        if (db.isOpen()){
            Cursor cursor=db.rawQuery("select * from money_table order by id desc",null);
            Bill bill;
            while (cursor.moveToNext()){
                bill=new Bill();
                bill.setTypeId(cursor.getString(cursor.getColumnIndex(BillDao.COLUMN_Type)));
                bill.setMoney(cursor.getString(cursor.getColumnIndex(BillDao.COLUMN_Money)));
                bill.setTime(cursor.getString(cursor.getColumnIndex(BillDao.COLUMN_Time)).substring(0,8));
                if (Integer.parseInt(bill.getTime().substring(3,5))==currentMonth){
                    bills.add(bill);//如果是当前月的，添加到list里面去
                }else {
                    //首先将bills添加到hashset里面去
                    if (bills.size()>0){
                        hashSet.add(bills);
                    }
                    //然后将bills清空
                    bills.clear();
                    while (currentMonth!=Integer.parseInt(bill.getTime().substring(3,5))) {
                        currentMonth--;//当前月自减
                    }
                    if (Integer.parseInt(bill.getTime().substring(3,5))==currentMonth){
                        //自减后当当前月份等于bill的月份时，继续添加
                        bills.add(bill);
                    }

                }

            }
            if (bills.size()>0){
                hashSet.add(bills);
                bills.clear();
            }
        }
        return  hashSet;
    }

}
