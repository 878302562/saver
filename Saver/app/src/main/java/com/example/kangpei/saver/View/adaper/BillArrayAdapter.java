package com.example.kangpei.saver.View.adaper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kangpei.saver.Model.bean.BillType;
import com.example.kangpei.saver.MyApplication;
import com.example.kangpei.saver.R;

import java.util.List;

/**
 * Created by Robin on 2016/11/20.
 */

public class BillArrayAdapter extends ArrayAdapter<BillType> {

    public BillArrayAdapter(Context context, int textViewResourceId, List<BillType> billTypes) {
        super(context, textViewResourceId, billTypes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(getContext(), R.layout.item, null);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.img = (ImageView) convertView.findViewById(R.id.img_icon);
        viewHolder.name = (TextView) convertView.findViewById(R.id.tv_name);
        int resId = MyApplication.getContext().getResources().getIdentifier("type_" + getItem(position).getTypeId() + "_normal", "drawable", MyApplication.getContext().getPackageName());
        viewHolder.img.setImageResource(resId);
        viewHolder.name.setText(getItem(position).getTypeName());
        return convertView;
    }

    public class ViewHolder {
        public ImageView img;
        public TextView name;
    }
}
