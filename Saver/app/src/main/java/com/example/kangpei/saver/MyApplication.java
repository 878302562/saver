package com.example.kangpei.saver;

import android.app.Application;

/**
 * Created by kangpei on 14/11/16.
 */

public class MyApplication extends Application {

    private static MyApplication instance=new MyApplication();

    public static MyApplication getContext(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
    }
}
