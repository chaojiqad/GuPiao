package com.subzero.shares.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.subzero.shares.R;

/**
 * usercenter收入Adapter
 * Created by The_p on 2016/4/18.
 */
public class UsercenterIncomeAdapter extends BaseAdapter {
    private Context ct = null;

    public UsercenterIncomeAdapter(Context ct) {
        this.ct = ct;
    }
    @Override
    public int getCount() {
        return 20;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        View view = View.inflate(ct, R.layout.item_user_center_incomes, null);
        View view = null;
        if (convertView == null) {
            view = View.inflate(ct, R.layout.item_user_center_incomes, null);
        } else {
            view = convertView;
        }
        return view;
    }
}
