package com.subzero.shares.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.subzero.shares.R;
import com.subzero.shares.bean.Classification;

import java.util.ArrayList;

/**
 * 分类
 * Created by xzf on 2016/5/9.
 */
public class ClassificationAdapter extends BaseAdapter {

    private ArrayList<Classification> mClassifications;

    private Context mContext;

    public ClassificationAdapter(Context context, ArrayList<Classification> classifications) {
        this.mContext = context;
        this.mClassifications = classifications;

    }

    @Override
    public int getCount() {
        return mClassifications.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_classify_reward_communicate, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.tv_calssify);
        tv.setText(mClassifications.get(position).getCatename());
        return convertView;
    }

    public void notifyDataSetChanged(ArrayList<Classification> mClassifications) {
        this.mClassifications = mClassifications;
        super.notifyDataSetChanged();
    }
}
