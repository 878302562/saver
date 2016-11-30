package com.example.kangpei.saver.Utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.example.kangpei.saver.R;
import com.example.kangpei.saver.View.fragment.ChartFragment;

/**
 * Created by kangpei on 23/11/16.
 */

/*
*  通知工具类，用于处理当达到预算设定值得时候，发出通知
* */
public class NotifyUtils {

    private Context context;
    private NotificationManager manager;
    public NotifyUtils(Context context){//通知构造器，获得一个通知管理的对象
        this.context=context;
        manager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void postNotification(String contentTitle,String contentText){
        Notification.Builder builder=new Notification.Builder(context);
        Intent intent=new Intent(context, ChartFragment.class);// 点击通知后跳转到图表界面
        PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        //pendinginatent获得intent的对象，类似于一个intent，跳转新的页面
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.mipmap.ic_launcher);//设置头标
        builder.setContentTitle(contentTitle);
        builder.setContentText(contentText);
        builder.setWhen(System.currentTimeMillis());
        builder.setAutoCancel(true);//设置自动取消
        builder.setTicker("new message");
        Notification notification=builder.build();
        notification.flags=Notification.FLAG_NO_CLEAR;
        manager.notify(0,notification);
    }

    public void cancelByid(){
        manager.cancel(0);//取消id为0的通知
    }
    public void cancelAll(){
        manager.cancelAll();//取消所有的通知
    }

    public static void notification(Context context){
        NotificationManager manager=(NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context);//得到一个建立通知的对象
        builder.setContentTitle("测试通知").setContentText("这个是内容")
                .setTicker("通知来了").setWhen(System.currentTimeMillis())//设置时间
                .setPriority(Notification.PRIORITY_DEFAULT)//设置优先级
                .setVibrate(new long[]{0,300,500,700})//设置震动
                .setLights(0xff0000ff, 300, 0);//设置灯光
        manager.notify(1,builder.build());//发送通知
    }

}
