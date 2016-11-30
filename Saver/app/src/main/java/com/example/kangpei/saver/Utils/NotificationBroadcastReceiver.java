package com.example.kangpei.saver.Utils;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by kangpei on 23/11/16.
 */

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    public static final String TYPE="type";//这个type是为了更新notification而设置的
    @Override
    public void onReceive(Context context, Intent intent) {
       String action=intent.getAction();
        int type=intent.getIntExtra(TYPE,-1);
        if (type!=-1){
            NotificationManager manager=(NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(type);//取消通知
        }
        if (action.equals("notification_enabled")){
            //// TODO: 23/11/16
        }
        if (action.equals("notification_cancelled")){
            //// TODO: 23/11/16  
        }
    }
}
