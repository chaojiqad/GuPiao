package com.subzero.shares.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.subzero.shares.R;
import com.subzero.shares.bean.UserNotification;
import com.subzero.shares.utils.ViewHolderUtils;

import java.util.ArrayList;

/**
 * Created by The_p on 2016/4/18.
 */
public class UsercenterNewsAdapter extends BaseAdapter {

    private Context mContext;

    private ArrayList<UserNotification> mUserNotifications;

    public UsercenterNewsAdapter(Context context, ArrayList<UserNotification> userNotifications) {
        this.mContext = context;
        this.mUserNotifications = userNotifications;
    }

    @Override
    public int getCount() {
        return mUserNotifications.size();
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


        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_user_center_news, null);
        }

        TextView tv_msg = (TextView) ViewHolderUtils.getView(convertView, R.id.item_user_center_tv_msg);
        TextView tv_date = (TextView) ViewHolderUtils.getView(convertView, R.id.item_user_center_tv_date);

        tv_msg.setText(mUserNotifications.get(position).getMessage());
        tv_date.setText(mUserNotifications.get(position).getDateAndtime());

        return convertView;
    }

    /**
     * 刷新
     * @param mUserNotifications
     */
    public void notifyDataSetChanged(ArrayList<UserNotification> mUserNotifications) {
        this.mUserNotifications = mUserNotifications;
        super.notifyDataSetChanged();
    }
}
