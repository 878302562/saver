package com.example.kangpei.saver.View.fragment;


import android.app.Fragment;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kangpei.saver.GlobalConstants;
import com.example.kangpei.saver.Model.bean.Bill;
import com.example.kangpei.saver.Model.bean.BillType;
import com.example.kangpei.saver.Model.db.BillDao;
import com.example.kangpei.saver.MyApplication;
import com.example.kangpei.saver.R;
import com.example.kangpei.saver.Utils.NotifyUtils;
import com.example.kangpei.saver.Utils.SPUtils;
import com.example.kangpei.saver.View.ExpandGridView;
import com.example.kangpei.saver.View.MainActivity;
import com.example.kangpei.saver.View.adaper.BillArrayAdapter;
import com.example.kangpei.saver.View.adaper.ViewPagerAdapter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robin on 2016/11/21.
 */

public class SaverFragment extends Fragment {

    private final String pickerValueKey = "pickerValueKey";
    private final String ISCHECKED="isChecked";
    private final String BUDGET="budget";
    private final String TOTALOUTKEY = "totalOutKey";
    private ViewPager viewPager;
    private ArrayList<View> viewList;
    private ViewPagerAdapter viewPagerAdapter;
    private List<BillType> billTypeList;
    private ImageView img_flag;
    private TextView tv_calculate;
    private TextView tv_total;
    private StringBuffer stringBuffer;
    private Bill bill;
    private ImageView img_left;
    private ImageView img_right;
    private NotifyUtils notifyUtils;

    private Button bt_0;
    private Button bt_1;
    private Button bt_2;
    private Button bt_3;
    private Button bt_4;
    private Button bt_5;
    private Button bt_6;
    private Button bt_7;
    private Button bt_8;
    private Button bt_9;
    private Button bt_C;
    private Button bt_dot;
    private Button bt_delete;
    private Button bt_submit;
    private Button bt_plus;
    private View view;

//    protected void onCreateView(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
//        init();
//
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.main,container,false);
        init();

        return view;
    }

    private void init(){
        initButtons();
        stringBuffer = new StringBuffer();
        bill = new Bill();
        bill.setTypeId("0");
        tv_calculate = (TextView)view.findViewById(R.id.tv_calculate);
        tv_total = (TextView)view.findViewById(R.id.tv_total);
        billTypeList = getBillTypeList();
        img_flag = (ImageView)view.findViewById(R.id.img_flag);
        img_left = (ImageView)view.findViewById(R.id.img_left);
        img_right = (ImageView)view.findViewById(R.id.img_right);
//        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar);
//        toolbar.setTitle("Add an Item");
        viewPager = (ViewPager)view.findViewById(R.id.viewPager);
        viewList = new ArrayList<View>();
        viewList.add(changePager(1));
        viewList.add(changePager(2));
        viewList.add(changePager(3));
        viewPagerAdapter = new ViewPagerAdapter(viewList);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOnPageChangeListener(new PageChangeListener());
        notifyUtils=new NotifyUtils(MyApplication.getContext());

/////////test
    }
    public List<BillType> getBillTypeList(){
        BillType billType;
        billTypeList = new ArrayList<>();
        for(int i = 0; i < 20; i++){
            billType = new BillType();
            billType.setTypeId(i);
            billType.setTypeName(GlobalConstants.types[i]);
            billTypeList.add(billType);
        }
        return billTypeList;
    }
    public View changePager(int i){
        View view = View.inflate(MyApplication.getContext(), R.layout.viewpager_gird, null);
        ExpandGridView expandGridView = (ExpandGridView)view.findViewById(R.id.gridview);
        final List<BillType> pagerList = new ArrayList<BillType>();
        if(i == 1){
            pagerList.addAll(billTypeList.subList(0, 8));
        }else if(i == 2){
            pagerList.addAll(billTypeList.subList(8, 16));
        }else{
            pagerList.addAll(billTypeList.subList(16, billTypeList.size()));
        }
        BillArrayAdapter billArrayAdapter = new BillArrayAdapter(MyApplication.getContext(),1,pagerList);
        expandGridView.setAdapter(billArrayAdapter);
        expandGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                changeui(pagerList.get(position));
                bill.setTypeId(pagerList.get(position).getTypeId()+"");
                stringBuffer = new StringBuffer();
                tv_calculate.setText("0.0");
                tv_total.setText("0.0");
            }
        });
        return view;
    }
    public void changeui(BillType billType){
        String name = "type_" + billType.getTypeId() + "_normal";
        int resourceId = MyApplication.getContext().getResources().getIdentifier(name, "drawable", MyApplication.getContext().getPackageName());
        img_flag.setImageResource(resourceId);
    }
    public void initButtons(){
        bt_0 = (Button)view.findViewById(R.id.bt_0);
        bt_1 = (Button)view.findViewById(R.id.bt_1);
        bt_2 = (Button)view.findViewById(R.id.bt_2);
        bt_3 = (Button)view.findViewById(R.id.bt_3);
        bt_4 = (Button)view.findViewById(R.id.bt_4);
        bt_5 = (Button)view.findViewById(R.id.bt_5);
        bt_6 = (Button)view.findViewById(R.id.bt_6);
        bt_7 = (Button)view.findViewById(R.id.bt_7);
        bt_8 = (Button)view.findViewById(R.id.bt_8);
        bt_9 = (Button)view.findViewById(R.id.bt_9);
        bt_C = (Button)view.findViewById(R.id.bt_C);
        bt_dot = (Button)view.findViewById(R.id.bt_dot);
        bt_delete = (Button)view.findViewById(R.id.bt_delete);
        bt_plus = (Button)view.findViewById(R.id.bt_plus);
        bt_submit = (Button)view.findViewById(R.id.bt_submit);
        bt_0.setOnClickListener(listener);
        bt_1.setOnClickListener(listener);
        bt_2.setOnClickListener(listener);
        bt_3.setOnClickListener(listener);
        bt_4.setOnClickListener(listener);
        bt_5.setOnClickListener(listener);
        bt_6.setOnClickListener(listener);
        bt_7.setOnClickListener(listener);
        bt_8.setOnClickListener(listener);
        bt_9.setOnClickListener(listener);
        bt_C.setOnClickListener(listener);
        bt_dot.setOnClickListener(listener);
        bt_delete.setOnClickListener(listener);
        bt_plus.setOnClickListener(listener);
        bt_submit.setOnClickListener(listener);
    }
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String tag = (String)view.getTag();
            if(tag.equals("0") || tag.equals("1") || tag.equals("2") || tag.equals("3") || tag.equals("4") || tag.equals("5") || tag.equals("6")
                    || tag.equals("7") || tag.equals("8") || tag.equals("9")){
                stringBuffer.append(tag);
                tv_total.setText(plus(stringBuffer));
                tv_calculate.setText(stringBuffer.toString());
                //System.out.println(tag);
            }else if(tag.equals("+")){
                if (tv_total.getText().equals("0.0")) {
                    return;
                }
                stringBuffer.append(tag);
                tv_calculate.setText(stringBuffer.toString());
                tv_total.setText(plus(stringBuffer));

            }else if(tag.equals("delete")){
                if (stringBuffer.length() == 0){
                    tv_total.setText("0.0");
                    tv_calculate.setText("0.0");
                    return;
                }
                stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                tv_calculate.setText(stringBuffer.toString());
                tv_total.setText(plus(stringBuffer));
            }else if(tag.equals("C")){
                stringBuffer = new StringBuffer();
                tv_total.setText("0.0");
                tv_calculate.setText("0.0");
            }else if(tag.equals("submit")){
                if (tv_total.getText().equals("0.0")) {
                    return;
                }

                bill.setMoney(tv_total.getText().toString());
                SimpleDateFormat sDateFormat = new SimpleDateFormat("yy/MM/dd");
                String date = sDateFormat.format(new java.util.Date());
                System.out.println(date);
                bill.setTime(date);
                BillDao billDao = new BillDao(MyApplication.getContext());
                billDao.saveBill(bill);
                stringBuffer = new StringBuffer();
                tv_total.setText("0.0");
                tv_calculate.setText("0.0");
                checkBudget();
                Toast toast = Toast.makeText(MyApplication.getContext(), "success", Toast.LENGTH_SHORT);
                toast.show();



            }else if(tag.equals(".")){
                if (tv_total.getText().equals("0.0") || stringBuffer.charAt(stringBuffer.length() - 1) == '.') {
                    return;
                }
                stringBuffer.append(tag);
                tv_calculate.setText(stringBuffer.toString());
                tv_total.setText(plus(stringBuffer));
            }
        }
    };
    public void checkBudget(){
        boolean isChecked=(Boolean) SPUtils.getObject(MyApplication.getContext(),ISCHECKED,true);
        int budget = (Integer) SPUtils.getObject(MyApplication.getContext(),BUDGET,1);
        int percent = (Integer) SPUtils.getObject(MyApplication.getContext(),pickerValueKey,1);
        float totalOut = (Float) SPUtils.getObject(MyApplication.getContext(),TOTALOUTKEY,1.0f);
        Log.d("totalOut",totalOut+"");
        Log.d("budget",""+budget);
        Log.d("percent",percent+"");
        Log.d("check",isChecked+"");
        if(totalOut>(percent*budget/100) && isChecked){
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            notifyUtils.postNotification("Reminder","You have spent:" +decimalFormat.format(totalOut/budget*100)+"% of your budget!");
        }
    }
    private String plus(StringBuffer stringBuffer) {
        if (!stringBuffer.toString().contains("+"))
            return stringBuffer.toString();
        String num = new String(stringBuffer);
        String[] nums = num.split("\\+");
        float total = 0f;
        for (int i = 0; i < nums.length; i++) {
            total += Float.valueOf(nums[i]);
        }
        return String.valueOf(total);
    }
    class PageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            if(arg0 == 0){
                img_left.setImageResource(R.drawable.empty);
                img_right.setImageResource(R.drawable.right);
            }else if(arg0 == 1){
                img_left.setImageResource(R.drawable.left);
                img_right.setImageResource(R.drawable.right);
            }else if(arg0 == 2){
                img_left.setImageResource(R.drawable.left);
                img_right.setImageResource(R.drawable.empty);
                Log.d("asdfsdf","page 3");
            }
//            for (int i = 0; i < mimageViews.length; i++) {
//                mimageViews[arg0].setBackgroundResource(R.drawable.stop2);
//                if (arg0 != i) {
//                    mimageViews[i].setBackgroundResource(R.drawable.stop1);
//                }
//            }
        }
    }

}
