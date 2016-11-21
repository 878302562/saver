package com.example.kangpei.saver.Model.db;

import android.content.Context;

import com.example.kangpei.saver.Model.bean.Bill;

import java.util.List;

/**
 * Created by kangpei on 14/11/16.
 */

public class BillDao {

    public static final String TABLE_NAME="money_table";
    public static final String COLUMN_ID="id";//primary key
    public static final String COLUMN_Type="type";//花费的类型
    public static final String COLUMN_Money="money";//花费的金额
    public static final String COLUMN_Time="time";//记账时间

    public BillDao(Context context){
        DBManager.getDbManager().onInit(context);
    }

    //保存单个的记录
    public void saveBill(Bill bill){
     DBManager.getDbManager().saveBill(bill);
    }
    public List<Bill> getBillList(long time){
      return  DBManager.getDbManager().getBill(time);
    }

}
