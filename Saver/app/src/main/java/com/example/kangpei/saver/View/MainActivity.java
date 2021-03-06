package com.example.kangpei.saver.View;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.kangpei.saver.R;
import com.example.kangpei.saver.View.fragment.ChartFragment;
import com.example.kangpei.saver.View.fragment.MyFragment;
import com.example.kangpei.saver.View.fragment.SaverFragment;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //UI Object
    private TextView saver;
    private TextView report;
    private TextView mine;

    //Fragment Object
    private MyFragment fg3;
    private SaverFragment saverFragment;
    private ChartFragment chartFragment;
    private FragmentManager fManager;
    private static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        fManager = getFragmentManager();
        bindViews();
        activity = this;
        saver.performClick();   //模拟一次点击，既进去后选择第一项
    }


    //UI组件初始化与事件绑定
    private void bindViews() {
        saver = (TextView) findViewById(R.id.saver);
        report = (TextView) findViewById(R.id.report);
        mine = (TextView) findViewById(R.id.mine);

        saver.setOnClickListener(this);
        report.setOnClickListener(this);
        mine.setOnClickListener(this);
    }

    //重置所有文本的选中状态
    private void setSelected(){
        saver.setSelected(false);
        report.setSelected(false);
        mine.setSelected(false);
    }

    //隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(fg3 != null)fragmentTransaction.hide(fg3);
        if (chartFragment!=null)
            fragmentTransaction.hide(chartFragment);
        if (saverFragment!=null){
            fragmentTransaction.hide(saverFragment);
        }
    }


    @Override
    public void onClick(View v) {
        FragmentTransaction fTransaction = fManager.beginTransaction();
        hideAllFragment(fTransaction);
        switch (v.getId()){
            case R.id.report:
                setSelected();
                report.setSelected(true);
                if(chartFragment == null){
                    chartFragment = new ChartFragment();
                    fTransaction.add(R.id.ly_content,chartFragment);
                }else{
                    fTransaction.show(chartFragment);
                    chartFragment.onResume();
                }
                break;
            case R.id.saver:
                setSelected();
                saver.setSelected(true);
                if(saverFragment == null){
                    saverFragment = new SaverFragment();
                    fTransaction.add(R.id.ly_content,saverFragment);
                }else{
                    fTransaction.show(saverFragment);
                }
                break;
            case R.id.mine:
                setSelected();
                mine.setSelected(true);
                if(fg3 == null){
                    fg3 = new MyFragment();
                    fTransaction.add(R.id.ly_content,fg3);
                }else{
                    // fTransaction.show(fg3);
                    fg3.onResume();
                    fTransaction.show(fg3);
                }
                break;

        }
        fTransaction.commit();
    }
    public static Activity getActivity(){
        return activity;
    }
}
