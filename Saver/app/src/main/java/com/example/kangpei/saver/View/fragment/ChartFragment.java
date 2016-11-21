package com.example.kangpei.saver.View.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kangpei.saver.GlobalConstants;
import com.example.kangpei.saver.Model.bean.Bill;
import com.example.kangpei.saver.Model.db.BillDao;
import com.example.kangpei.saver.MyApplication;
import com.example.kangpei.saver.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by kangpei on 21/11/16.
 */

public class ChartFragment extends Fragment {

    private List<Bill> bills;
    private float totalIn, totalOut;//总收入和总支出
    private HashMap<String,Float> map;
    private PieChart pieChart;
    private TextView tvTotalOut;
    private TextView tvTotalIn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ChartFragment--------","========chartfRAGMENT_CREATED");
        onInit();
    }

    public void onInit(){
        BillDao billDao=new BillDao(MyApplication.getContext());
        //bills=billDao.getBillList(TimeUtils.getFirstDayofMonth());
        bills=new ArrayList<>();
        map=new HashMap<>();
        bills.add(new Bill("123","0","1"));
        bills.add(new Bill("12","1","2"));
        bills.add(new Bill("34","0","1"));
        bills.add(new Bill("15","2","1"));
        Log.d("chartFragment------",bills.size()+"");
        for (Bill bill:bills){
            if (bill.getTypeId().equals("0")){//说明点击的是收入
                totalIn+=Float.parseFloat(bill.getMoney());
            }else {
                totalOut+=Float.parseFloat(bill.getMoney());
            }

            if (map.containsKey(bill.getTypeId())){
                map.put(bill.getTypeId(),map.get(bill.getTypeId())+Float.parseFloat(bill.getMoney()));
            }else {
                map.put(bill.getTypeId(),Float.parseFloat(bill.getMoney()));
            }//将对应的id值和钱放入hashmap中去
        }
        Log.d("chartFragment--------",totalIn+"oncreate方法");
        Log.d("chartFragment--------",totalOut+"oncreate方法");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_chart,container,false);
        Log.d("ChartFragment--------","========ONCREATED_VIEW_EXECUTED");
        tvTotalIn=(TextView)view.findViewById(R.id.tvTotalIn);
        tvTotalOut=(TextView)view.findViewById(R.id.tvTotalOut);
        pieChart=(PieChart)view.findViewById(R.id.pie);
        tvTotalIn.setText(String.valueOf(totalIn));
        tvTotalOut.setText(String.valueOf(totalOut));
        showChart(pieChart,getPieData());
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("ChartFragment--------","========onViewCreated_Executed");
        showChart(pieChart,getPieData());

    }

    public PieData getPieData(){
        return setPieData();
    }

    public PieData setPieData(){
        ArrayList<String> xValues = new ArrayList<String>();  //xVals用来表示每个饼块上的内容
        ArrayList<Entry> yValues = new ArrayList<Entry>();  //yVals用来表示封装每个饼块的实际数据
        ArrayList<Integer> colors = new ArrayList<Integer>();

        Set set = map.keySet();
        Iterator iterator = set.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            String in = String.valueOf(iterator.next());
            int index = Integer.valueOf(in);
            xValues.add(GlobalConstants.types[index]);
            yValues.add(new Entry(map.get(index + "") / (totalIn + totalOut), i));
            colors.add(GlobalConstants.colors[index]);
            i++;
        }
        //y轴的集合
        PieDataSet pieDataSet = new PieDataSet(yValues, ""/*显示在比例图上*/);
        pieDataSet.setSliceSpace(0f); //设置个饼状图之间的距离
        pieDataSet.setColors(colors);

      DisplayMetrics metrics = MyApplication.getContext().getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px); // 选中态多出的长度

        PieData pieData = new PieData(xValues, pieDataSet);
        return pieData;
    }


    public void showChart(PieChart pieChart,PieData pieData){

        pieChart.setHoleColorTransparent(true);

        pieChart.setHoleRadius(60f);  //半径
        pieChart.setTransparentCircleRadius(64f); // 半透明圈

        pieChart.setDescription("");
        pieChart.setDrawCenterText(true);  //饼状图中间可以添加文字

        pieChart.setDrawHoleEnabled(true);

        pieChart.setRotationAngle(90); // 初始旋转角度
        pieChart.setRotationEnabled(true); // 可以手动旋转

        pieChart.setUsePercentValues(true);  //显示成百分比

        pieChart.setCenterText("");  //饼状图中间的文字

        //设置数据
        pieChart.setData(pieData);

        Legend mLegend = pieChart.getLegend();  //设置比例图
        mLegend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);  //最右边显示
//      mLegend.setForm(LegendForm.LINE);  //设置比例图的形状，默认是方形
        mLegend.setXEntrySpace(7f);
        mLegend.setYEntrySpace(5f);

        pieChart.animateXY(1000, 1000);  //设置动画
        // mChart.spin(2000, 0, 360);
    }


}

