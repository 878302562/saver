package com.example.kangpei.saver.View.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.support.annotation.Nullable;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.kangpei.saver.GlobalConstants;
import com.example.kangpei.saver.MyApplication;
import com.example.kangpei.saver.R;
import com.example.kangpei.saver.Utils.MDDialog;
import com.example.kangpei.saver.Utils.SPUtils;
import com.example.kangpei.saver.View.CirclePicker;
import com.example.kangpei.saver.View.ExpandGridView;
import com.example.kangpei.saver.View.IconView;
import com.example.kangpei.saver.View.MainActivity;
import com.github.mikephil.charting.renderer.CandleStickChartRenderer;

import java.io.IOException;
import java.io.InterruptedIOException;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

/**
 * Created by kangpei on 20/11/16.
 */

public class MyFragment extends Fragment implements View.OnClickListener{
    private String content;
    private int pickervalue;
    private int imgCount;
    private String name;
    private String struri;
    private final String pickerValueKey = "pickerValueKey";
    private final String nameKey = "nameKey";
    private final String APPImgId = "APPImgIdKey";
//    private final String imguri = "imguriKey";
    private Button bt_picker;
    private CirclePicker picker;
    private Button bt_chooseFromAPP;
//    private Button bt_chooseFromStorage;
    private AlertDialog.Builder builder;
    private ImageView img_avatar;
    private IconView img_show;
    private Bitmap bitmap;
    private TextView tv_name;
    private EditText et_name;

    private Button budget;
    private EditText editText;
    private final String BUDGET="budget";
    private final String PERCENT="percent";
    private final String ISCHECKED="isChecked";
    private Switch aSwitch;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.from(MyApplication.getContext()).inflate(R.layout.fragment_setting, container, false);
        //struri = null;
        bt_picker = (Button) view.findViewById(R.id.bt_picker);
        img_avatar = (ImageView) view.findViewById(R.id.img_avatar);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        bt_picker.setOnClickListener(reminderSettingListener);
        img_avatar.setOnClickListener(changeAvatarListener);
        tv_name.setOnClickListener(changeNameListener);
        budget=(Button)view.findViewById(R.id.budget);
        aSwitch=(Switch)view.findViewById(R.id.switch1);
        aSwitch.setOnClickListener(this);
        budget.setText("Input Budget:");
        budget.setOnClickListener(this);
        int imgid = MyApplication.getContext().getResources().getIdentifier("avatar_0", "drawable", MyApplication.getContext().getPackageName());

        if(!SPUtils.isContain(MyApplication.getContext(),APPImgId)) {
            name="Click to change name";
            SPUtils.putValue(MyApplication.getContext(), APPImgId, imgid);
            SPUtils.putValue(MyApplication.getContext(), nameKey, name);
        }
//        if ((Integer)SPUtils.getObject(MyApplication.getContext(),APPImgId,1)!=imgid){
//            int i = (Integer)SPUtils.getObject(MyApplication.getContext(),APPImgId,1);
//            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),i);
//            img_avatar.setImageBitmap(bitmap);
//        }
        Log.d("SETTING_FRAGMENT","CREATED");
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        int bu=(int) SPUtils.getObject(MyApplication.getContext(),BUDGET,0);
        boolean isChecked=(Boolean) SPUtils.getObject(MyApplication.getContext(),ISCHECKED,true);
        budget.setText("Your Budget:"+bu+"");
        String name = (String) SPUtils.getObject(MyApplication.getContext(),nameKey,"");
        int pickerValue = (Integer) SPUtils.getObject(MyApplication.getContext(),pickerValueKey,1);
        bt_picker.setText("Reminder Setting: " + pickerValue + "%");
        tv_name.setText(name);
        int resourceId = (Integer) SPUtils.getObject(MyApplication.getContext(),APPImgId,1);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), resourceId);
//        struri = (String) SPUtils.getObject(MyApplication.getContext(), imguri, "");
//        Log.d("myfragment------",struri+"asdads");
//        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
//        if(struri != null && !struri.equals("")) {
//            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
//                Log.d("---------------------", "--------------------------nononono");
//                ActivityCompat.requestPermissions(
//                        MainActivity.getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//
//                try {
//                    Log.d("---------------------", struri);
//                    bm = MediaStore.Images.Media.getBitmap(MainActivity.getActivity().getContentResolver(), Uri.parse(struri));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//            } else {
//                Log.d("---------------------", "--------------------------");
//
//                try {
//                    Log.d("---------------------", "--------------------------1111");
//                    bm = MediaStore.Images.Media.getBitmap(MainActivity.getActivity().getContentResolver(), Uri.parse(struri));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        }

        img_avatar.setImageBitmap(bm);
        Log.d("settingfRAGMENT","ONRESUME___"+isChecked);
        aSwitch.setEnabled(isChecked);//设置switch是否被选中
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }

    private View.OnClickListener changeNameListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            builder = new AlertDialog.Builder(MainActivity.getActivity());
            builder.setTitle("Change Name");             //设置标题
            View layout = View.inflate(MyApplication.getContext(), R.layout.dialog_name, null);
            builder.setView(layout);
            et_name = (EditText) layout.findViewById(R.id.et_name);
            builder.setPositiveButton("ok", okNameListener);
            builder.create().show();
        }
    };
    private DialogInterface.OnClickListener okNameListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            name = et_name.getText().toString();
            if(name.equals("")){
                name = "Click to change name";
            }
            tv_name.setText(name);
            SPUtils.putValue(MyApplication.getContext(),nameKey,name);
        }
    };
    private View.OnClickListener changeAvatarListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            builder = new AlertDialog.Builder(MainActivity.getActivity());
            builder.setTitle("Change Avatar");             //设置标题
//            builder.setIcon(R.drawable.reminder);//设置图标，图片id即可
            View layout = View.inflate(MyApplication.getContext(), R.layout.dialog_avatar, null);
            builder.setView(layout);
            bt_chooseFromAPP = (Button) layout.findViewById(R.id.bt_chooseFromAPP);
//            bt_chooseFromStorage = (Button) layout.findViewById(R.id.bt_chooseFromStorage);
            img_show = (IconView) layout.findViewById(R.id.img_show);
            String name = "avatar_" + imgCount;
            int resourceId = MyApplication.getContext().getResources().getIdentifier(name, "drawable", MyApplication.getContext().getPackageName());
            img_show.setImageResource(resourceId);
            bt_chooseFromAPP.setOnClickListener(chooseFromAPPListener);
//            bt_chooseFromStorage.setOnClickListener(chooseFromStorageListener);
            builder.setPositiveButton("ok", okavatarListener);
            builder.create().show();
        }
    };
    private DialogInterface.OnClickListener okavatarListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            if(bitmap == null){
                img_avatar.setImageResource(R.drawable.avatar_0);
            }else{
                img_avatar.setImageBitmap(bitmap);
            }

        }
    };
    private View.OnClickListener chooseFromAPPListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            startActivityForResult(intent, 1);

            imgCount++;
            if (imgCount == 12) {
                imgCount = 0;
            }
            String name = "avatar_" + imgCount;
            int resourceId = MyApplication.getContext().getResources().getIdentifier(name, "drawable", MyApplication.getContext().getPackageName());
            SPUtils.putValue(MyApplication.getContext(),APPImgId,resourceId);
            bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
            struri = null;
            img_show.setImageBitmap(bitmap);

        }
    };
//    private View.OnClickListener chooseFromStorageListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            Intent intent = new Intent(Intent.ACTION_PICK);
//            intent.setType("image/*");
//            startActivityForResult(intent, 2);
//        }
//    };

    //重写onActivityResult方法
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
////        if(requestCode == Activity.RESULT_OK && requestCode == 1){
////            Bundle bundle = data.getExtras();
////            bitmap = (Bitmap) bundle.get("data");
////            Log.d("sdlfk","2sfjalsdf--------");
////            img_show.setImageBitmap(bitmap);
////        }
//        if (resultCode == RESULT_OK && requestCode == 2) {
////            Log.i("uri", "GalleryUri:    " + data.getData());
//            try {
//                String s = data.getData().toString();
//                //String s = getRealPathFromURI(data.getData());
//                SPUtils.putValue(MyApplication.getContext(),imguri,s);
//                bitmap = MediaStore.Images.Media.getBitmap(MainActivity.getActivity().getContentResolver(), data.getData());
//                img_show.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == SMALL_CAPTURE && resultCode == RESULT_OK) {
//            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
//            smallimg.setImageBitmap(imageBitmap);
//        }
//    }

    private View.OnClickListener reminderSettingListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            builder = new AlertDialog.Builder(MainActivity.getActivity());
            builder.setTitle("Setting");             //设置标题
            builder.setIcon(R.drawable.reminder);//设置图标，图片id即可

            //  载入布局
            View layout = View.inflate(MyApplication.getContext(), R.layout.dialog_picker, null);

            builder.setView(layout);
            builder.setPositiveButton("ok", null);
            picker = (CirclePicker) layout.findViewById(R.id.picker);
            picker.setPickerListener(pickerListener);
            builder.create().show();
        }
    };
    private CirclePicker.CirclePickerListener pickerListener = new CirclePicker.CirclePickerListener() {
        @Override
        public void onPickerStop() {

        }

        @Override
        public void onPickerStart(int time) {

        }

        @Override
        public void onPickerPause(int time) {

        }

        @Override
        public void onPickerpickingValueChanged(int time) {
//            Log.d("onTimerTimingValue",time+"");

        }

        @Override
        public void onPickerSetValueChanged(int time) {
//            Log.d("onTimerSetValueCHanged",time+"");
            pickervalue = time;
            SPUtils.putValue(MyApplication.getContext(),pickerValueKey,pickervalue);
            bt_picker.setText("Reminder Setting: " + time + "%");
        }

        @Override
        public void onTimerSetValueChange(int time) {
//            Log.d("onTimerSetValueChange",time+"");

        }
    };
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.budget:
                new MDDialog.Builder(MainActivity.getActivity()).setContentView(R.layout.content_dialog)
                        .setContentViewOperator(new MDDialog.ContentViewOperator() {
                            @Override
                            public void operate(View contentView) {
                                //这里的contentView就是上面代码中传入的自定义的View或者layout资源inflate出来的view
                                editText=(EditText)contentView.findViewById(R.id.edit0);
                                editText.setHint("Input Your Budget");
                            }
                        }).setTitle("Monthly Budget").setPositiveButtonMultiListener(new MDDialog.OnMultiClickListener() {
                    @Override
                    public void onClick(View clickedView, View contentView) {
                        //这里的contentView就是上面代码中传入的自定义的View或者layout资源inflate出来的view，
                        // 目的是方便在确定/取消按键中对contentView进行操作，如获取数据等。
                        editText=(EditText)contentView.findViewById(R.id.edit0);
                        //budget.setText(new StringBuilder(budget.getText().toString()).append(editText.getText().toString()));
                        //budget.setText("Your Budget:"+editText.getText().toString());
                        GlobalConstants.BUDGET=Integer.parseInt(editText.getText().toString());
                        SPUtils.putValue(MyApplication.getContext(),BUDGET,GlobalConstants.BUDGET);

                        budget.setText("Your Budget:"+GlobalConstants.BUDGET+"");
                    }
                }).setWidthMaxDp(600).setShowTitle(true).setShowButtons(true).create().show();
                // String string=editText.getText().toString();
                break;
            case R.id.switch1:
                aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if (isChecked){
                            //// TODO: 23/11/16 通知开启
                            SPUtils.putValue(MyApplication.getContext(),ISCHECKED,true);
                            Log.d("settingfRAGMENT","ONClick___"+isChecked);
                        }else {
                            //// TODO: 23/11/16 通知关闭
                            SPUtils.putValue(MyApplication.getContext(),ISCHECKED,false);
                            Log.d("settingfRAGMENT","ONCLICK___"+isChecked);
                        }
                    }
                });

        }
    }
//    private String getRealPathFromURI(Uri contentURI) {
//        String result;
//        Cursor cursor = MainActivity.getActivity().getContentResolver().query(contentURI, null, null, null, null);
//        if (cursor == null) {
//            // Source is Dropbox or other similar local file path
//            result = contentURI.getPath();
//        } else {
//            cursor.moveToFirst();
//            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
//            result = cursor.getString(idx);
//            cursor.close();
//        }
//        return result;
//    }



}
