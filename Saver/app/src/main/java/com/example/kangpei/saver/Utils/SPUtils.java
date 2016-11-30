package com.example.kangpei.saver.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.Map;

/**
 * Created by kangpei on 23/11/16.
 */

/*
* shared preference 的工具类，用于存放设置界面的数据。，比
* 如说图片值，预算值，比例值，还有是否允许通知，或取消通知
* */
public class SPUtils {

    //设置保存在手机里面的文件名
    public static final String FILE_NAME="saver";//
    //保存数据，是一个键值对模型，，键始终设置未字符串类型
    public static void putValue(Context context, String key, Object value){

        SharedPreferences.Editor editor =context.
                getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE).edit();
        //表示只允许当前程序获取一个读取filename文件的editor对象
        if (value instanceof String){
            //如果是string类型
            editor.putString(key,(String)value);
            Log.d("sptulis-----",value.toString()+"------------");
        }else if (value instanceof Boolean){
            editor.putBoolean(key,(Boolean)value);
        }else if (value instanceof Integer){
            editor.putInt(key,(Integer)value);
        }else if (value instanceof Float){
            editor.putFloat(key,(Float)value);
        }else if (value instanceof Long){
            editor.putLong(key,(Long)value);
        }else {
            editor.putString(key,value.toString());
        }
        editor.commit();//提交
    }

    //获取sharedpreference里面的值
    //返回的会是一个类型和value类型一样的object对象
    //注意此时程序默认为object是一种类型
    public static Object getObject(Context context,String key, Object object){
        SharedPreferences preferences=context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        //表示只允许当前程序获取一个读取filename文件
        if (object instanceof String){
            //如果是string类型
            Object object1=preferences.getString(key,(String)object);
            Log.d("sputlis----56767--",object1.toString()+"---------");
            return object1;
        }else if (object instanceof Boolean){
            return preferences.getBoolean(key,(Boolean)object);
        }else if (object instanceof Integer){
          return   preferences.getInt(key,(Integer)object);
        }else if (object instanceof Float){
            return preferences.getFloat(key,(Float)object);
        }else if (object instanceof Long){
           return preferences.getLong(key,(Long)object);
        }else {
          return   preferences.getString(key,(String)object);
        }
    }

    //利用一个map来返回所有的键值对，
    public static Map<String,?> getAllKeyValue(Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getAll();
    }

    /*
    * 清理掉所有存储在sharedpreference的值
    * */
    public static void clearSharedPreference(Context context){
        SharedPreferences.Editor editor=context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE).edit();
        editor.clear();//清空
        editor.commit();
    }

    /*
    * 判断是否保存某个键的值
    * */
    public static boolean isContain(Context context,String key){

        SharedPreferences preferences=context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);

        return preferences.contains(key);
    }

    public static void remove(Context context,String key){
        SharedPreferences.Editor editor=context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE).edit();
        editor.remove(key);
        editor.commit();
    }
}
