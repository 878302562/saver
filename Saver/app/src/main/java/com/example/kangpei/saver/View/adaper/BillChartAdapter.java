package com.example.kangpei.saver.View.adaper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kangpei.saver.GlobalConstants;
import com.example.kangpei.saver.Model.bean.Bill;
import com.example.kangpei.saver.MyApplication;
import com.example.kangpei.saver.R;
import com.example.kangpei.saver.View.fragment.ChartFragment;

import java.util.List;

/**
 * Created by kangpei on 22/11/16.
 */

public class BillChartAdapter extends ArrayAdapter<Bill> {

    private int resourceId;
    private ChartFragment chartFragment;
    public BillChartAdapter(Context context, int resource, List<Bill> objects) {
         super(context,resource,objects);
        resourceId=resource;
        chartFragment=new ChartFragment();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

          Bill bill=getItem(position);//得到一个bill对象
          View view= LayoutInflater.from(MyApplication.getContext()).inflate(resourceId,null);
        TextView money=(TextView)view.findViewById(R.id.money);//获取金额
        TextView type=(TextView)view.findViewById(R.id.type);
        ImageView imageView=(ImageView)view.findViewById(R.id.typeImage);
        TextView time=(TextView)view.findViewById(R.id.time);

        //设置金额和类型
       // Log.d("BillchartAdapter  money",bill.getMoney());
        money.setText("$: "+bill.getMoney());
        type.setText(GlobalConstants.types[Integer.parseInt(bill.getTypeId())]);
        String name = "type_" + bill.getTypeId() + "_normal";
        int resourceId = MyApplication.getContext().getResources().getIdentifier(name, "drawable", MyApplication.getContext().getPackageName());
        imageView.setImageResource(resourceId);
        time.setText(bill.getTime());
        return view;
    }
}
