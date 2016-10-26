package com.subzero.shares.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.subzero.shares.R;
import com.subzero.shares.utils.ViewHolderUtils;

import java.util.ArrayList;

/**
 * 顾问发布直播获取标签
 * Created by zzy on 6/21/2016.
 */
public class LiveBroadcastReleaseLableAdapter extends BaseAdapter  {
    private Context context;
    private ArrayList<String> beans;

    public LiveBroadcastReleaseLableAdapter(Context context, ArrayList<String> beans) {
        this.context = context;
        this.beans = beans;
    }

    @Override
    public int getCount() {
        return beans.size();
    }

    @Override
    public Object getItem(int position) {
        return beans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = View.inflate(context, R.layout.item_live_optional, null);
        TextView tv = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_lable);
        tv.setText(beans.get(position));
        return convertView;
    }



    public void notifyDataSetChanged(ArrayList<String> beans) {
        this.beans=beans;
        super.notifyDataSetChanged();
    }
}
