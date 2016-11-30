package com.example.kangpei.saver.View.fragment;


import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kangpei.saver.GlobalConstants;
import com.example.kangpei.saver.Model.bean.Bill;
import com.example.kangpei.saver.Model.db.BillDao;
import com.example.kangpei.saver.MyApplication;
import com.example.kangpei.saver.R;
import com.example.kangpei.saver.Utils.MDDialog;
import com.example.kangpei.saver.Utils.SPUtils;
import com.example.kangpei.saver.View.MainActivity;
import com.example.kangpei.saver.View.adaper.BillChartAdapter;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by kangpei on 21/11/16.
 */

public class ChartFragment extends Fragment implements AdapterView.OnItemClickListener {

    private List<Bill> bills;
    private float totalIn, totalOut;//总收入和总支出
    private HashMap<String,Float> map;
    private final String TOTALOUTKEY = "totalOutKey";
    private PieChart pieChart;
    private TextView tvTotalOut;
    private TextView tvTotalIn;
    private static int clicked=0;
    private BillDao billDao;
    //测试使用
    private LineChartView lineChartView;//用于显示特定类别的view
    //private MainActivity mainActivity;
    private ListView listView;
    private TextView textView;
    private List<PointValue> pointValues=new ArrayList<>();//点的值的链表
    private List<AxisValue> axisValues=new ArrayList<>();//坐标的值

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ChartFragment--------","========chartfRAGMENT_CREATED");
        onInit();

    }
    //    }
    public  List<Bill> getBills(){
        return bills;
    }


    @Override
    public void onResume() {
        super.onResume();
        //onresume方法是否执行
        Log.d("onresume","onresume执行了");
        onInit();
        tvTotalIn.setText(String.valueOf(totalIn));
        tvTotalOut.setText(String.valueOf(totalOut));
        BillChartAdapter billChartAdapter=new BillChartAdapter(MyApplication.getContext(),R.layout.bill_item,getBills());
        listView.setAdapter(billChartAdapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bill bill=bills.get(i);//获取一个bill
                final List<Bill> bills=billDao.getCertainTypeBillList(bill);//得到对应类别的bill
                Log.d("长按事件","长按事件"+bills.size());
                new MDDialog.Builder(MainActivity.getActivity()).setContentView(R.layout.content_dialog2)
                        .setContentViewOperator(new MDDialog.ContentViewOperator() {
                            @Override
                            public void operate(View contentView) {
                                //这里的contentView就是上面代码中传入的自定义的View或者layout资源inflate出来的view
                                //textView=(TextView) contentView.findViewById(R.id.text);
                                lineChartView=(LineChartView) contentView.findViewById(R.id.line_chart);
                                //这里的contentView就是上面代码中传入的自定义的View或者layout资源inflate出来的view，
                                // 目的是方便在确定/取消按键中对contentView进行操作，如获取数据等。
                                //textView=(TextView) contentView.findViewById(R.id.text);
                                pointValues.clear();
                                axisValues.clear();
                                lineChartView=(LineChartView) contentView.findViewById(R.id.line_chart);
                                getYAxisPoints(bills);
                                Log.d("getYAxisPoints",pointValues.size()+"pointvalues的大小");
                                setXAxisLables(bills);
                                Log.d("setXAxisLables",axisValues.size()+"axisValues的大小");
                                initLineChart(bills);
                            }
                        }).setTitle(GlobalConstants.types[Integer.parseInt(bill.getTypeId())]+" expenditure history").setPositiveButtonMultiListener(new MDDialog.OnMultiClickListener() {
                    @Override
                    public void onClick(View clickedView, View contentView) {
                    }
                }).setShowNegativeButton(false).setWidthMaxDp(600).setShowTitle(true).setShowButtons(true).create().show();

                //Toast.makeText(MyApplication.getContext(),"123",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        showChart(pieChart,getPieData());


    }

    public void onInit(){

        totalIn=0;
        totalOut=0;
        pointValues.clear();
        axisValues.clear();
        billDao=new BillDao(MyApplication.getContext());
        bills=billDao.getBillOfCurrentMonth();

        Log.d("chartFragment","getbillslist执行了");
//        HashSet<List<Bill>> bills1=billDao.getAllBillGroupByMonth();
//        Log.d("chartFragment",bills1.size()+"bills1是一个hashset，存放了历史的所有数据，每隔月份时一个list");
//        for (List<Bill> bills:bills1){
//            for (Bill bill:bills) {
//                Log.d("ChartFragment----", "本月的bill的值 " + bill.getMoney() + "bill的时间 " + bill.getTime() + "bill的id");
//            }
//        }


        map=new HashMap<>();
        //initBill();
        Log.d("chartFragment------",bills.size()+"");
        for (Bill bill:bills){
            Log.d("typeid","bills.typeid:"+bill.getTypeId());
            Log.d("typeid","bills.money:"+bill.getMoney()+"");

            if (bill.getTypeId().equals("0")){//说明点击的是收入
                totalIn+=Float.parseFloat(bill.getMoney());
                Log.d("chartFragment",totalIn+"");
            }else {
                totalOut+=Float.parseFloat(bill.getMoney());
                Log.d("ChartFragment",totalOut+"");
            }

            if (map.containsKey(bill.getTypeId())){
                map.put(bill.getTypeId(),map.get(bill.getTypeId())+Float.parseFloat(bill.getMoney()));
            }else {
                map.put(bill.getTypeId(),Float.parseFloat(bill.getMoney()));
            }//将对应的id值和钱放入hashmap中去
        }

        SPUtils.putValue(MyApplication.getContext(),TOTALOUTKEY,totalOut);
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
        listView=(ListView)view.findViewById(R.id.list_view);
        BillChartAdapter billChartAdapter=new BillChartAdapter(MyApplication.getContext(),R.layout.bill_item,getBills());
        listView.setAdapter(billChartAdapter);
        listView.setOnItemClickListener(this);
        tvTotalOut.setText(String.valueOf(totalOut));
        showChart(pieChart,getPieData());
        Log.d("ChartFragment","showChart方法执行了");
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
            DecimalFormat decimalFormat=new DecimalFormat(".00");
            float f= map.get(index+"")/(totalIn+totalOut);
            Log.d("float值",f+"-------");
            f=Float.parseFloat(decimalFormat.format(f));
            Log.d("float值",f+"-------");
            //f=f/100;
            Log.d("float值",f+"-------");
            //xValues.add("");
            yValues.add(new Entry(f/*Float.parseFloat(decimalFormat.format((map.get(index + "")) / (totalIn + totalOut)))/100*/, i));
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
        pieChart.setDrawCenterText(true);
        pieChart.setHoleRadius(60f);  //半径
        pieChart.setTransparentCircleRadius(64f); // 半透明圈
        pieChart.setFitsSystemWindows(true);
        pieChart.setDescription("");
        pieChart.setDrawCenterText(true);  //饼状图中间可以添加文字
        pieChart.setScrollbarFadingEnabled(true);
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


    //轻点击用于删除一个类别
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


        final Bill bill=bills.get(i);
        new MDDialog.Builder(MainActivity.getActivity()).setContentView(R.layout.content_dialog1)
                .setContentViewOperator(new MDDialog.ContentViewOperator() {
                    @Override
                    public void operate(View contentView) {
                        //这里的contentView就是上面代码中传入的自定义的View或者layout资源inflate出来的view
                        textView=(TextView) contentView.findViewById(R.id.text);
                    }
                }).setTitle("Alert:").setPositiveButtonMultiListener(new MDDialog.OnMultiClickListener() {
            @Override
            public void onClick(View clickedView, View contentView) {
                //这里的contentView就是上面代码中传入的自定义的View或者layout资源inflate出来的view，
                // 目的是方便在确定/取消按键中对contentView进行操作，如获取数据等。
                textView=(TextView) contentView.findViewById(R.id.text);
                if (billDao.deleteBill(bill)){
                    onResume();
                    Toast.makeText(MyApplication.getContext(), "delete successful", Toast.LENGTH_SHORT).show();
                }

            }
        }).setWidthMaxDp(600).setShowTitle(true).setShowButtons(true).create().show();
//        if (billDao.deleteBill(bill)) {
////        onInit();
////        showChart(pieChart,getPieData());
//            onResume();
//            Toast.makeText(MyApplication.getContext(), "delete successful", Toast.LENGTH_SHORT).show();
//        }else {
//            Toast.makeText(MyApplication.getContext(),"delete failed",Toast.LENGTH_SHORT).show();
//        }
    }

    //设置x轴的坐标
    private void setXAxisLables(List<Bill> bills){
        int i=0;
        for (Bill bill:bills){
            axisValues.add(new AxisValue(i).setLabel(bill.getTime()));
            i++;
        }
    }

    //设置y轴的坐标
    private void getYAxisPoints(List<Bill> bills){
        int i=0;
        for (Bill bill:bills){
            pointValues.add(new PointValue(i,Integer.parseInt(bill.getMoney())));
            i++;
        }
    }


    private void initLineChart(List<Bill> bills){
        Line line = new Line(pointValues).setColor(Color.parseColor("#FFCD41"));  //折线的颜色（橙色）
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(true);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
//      line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.parseColor("#FFCD41"));  //设置字体颜色
        axisX.setName("date");  //表格名称
        axisX.setTextSize(10);//设置字体大小
        axisX.setMaxLabelChars(bills.size()); //最多几个X轴坐标，
        axisX.setHasSeparationLine(true);
        axisX.setValues(axisValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        //data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线

        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴
        axisY.setName("Money");//y轴标注
        axisY.setTextSize(10);//设置字体大小
        axisY.setTextColor(Color.parseColor("#FFCD41"));
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边


        //设置行为属性，支持缩放、滑动以及平移
        lineChartView.setInteractive(true);
        lineChartView.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
        lineChartView.setMaxZoom((float) 2);//最大方法比例
        lineChartView.setContainerScrollEnabled(true, ContainerScrollType.VERTICAL);
        lineChartView.setLineChartData(data);
        lineChartView.setVisibility(View.VISIBLE);

        Viewport v = new Viewport(lineChartView.getMaximumViewport());
        v.left = 0;
        v.right= bills.size()-1;
        lineChartView.setCurrentViewport(v);
    }


}

