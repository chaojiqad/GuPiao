package com.subzero.shares.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.subzero.shares.R;
import com.subzero.shares.utils.ViewHolderUtils;

/**
 * Created by xzf on 2016/4/29.
 */
public class GroupForGridAdapter extends BaseAdapter {

    private Context mContext;

    private String[] mGroups;

    public GroupForGridAdapter(Context context, String[] groups) {
        mContext = context;
        mGroups = groups;
    }

    @Override
    public int getCount() {
        return mGroups.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.ui_item_usercenter_group, null);
        }

        TextView tv = (TextView) ViewHolderUtils.getView(convertView, R.id.tv);

        tv.setText(mGroups[position]);

        return convertView;
    }
}
