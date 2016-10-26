package com.subzero.shares.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.subzero.shares.R;
import com.subzero.shares.bean.OptionalLiveCommentBean;
import com.subzero.shares.utils.ViewHolderUtils;

import java.util.ArrayList;

/**
 * 顾问诊股Adapter
 * Created by The_p on 2016/4/11.
 */
public class OptionalLiveBroadcastDetailAdapter extends BaseAdapter {
    private Context context = null;
    private ArrayList<OptionalLiveCommentBean> beans;

    public OptionalLiveBroadcastDetailAdapter(Context context,ArrayList<OptionalLiveCommentBean> beans) {
        this.context = context;
        this.beans = beans;

    }
    @Override
    public int getCount() {
        return beans.size();
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
            convertView = View.inflate(context, R.layout.ui_item_live_broadcast_detail_listview_item, null);
        }
        TextView name= (TextView) ViewHolderUtils.getView(convertView,R.id.tv_comment_holder);
        TextView time= (TextView) ViewHolderUtils.getView(convertView,R.id.tv_comment_time);
        TextView content= (TextView) ViewHolderUtils.getView(convertView,R.id.tv_comment_message);

        OptionalLiveCommentBean bean=beans.get(position);
        name.setText(bean.getComName());
        time.setText(bean.getTime());
        content.setText(bean.getContent());
        return convertView;
    }

    public void notifyDataSetChanged(ArrayList<OptionalLiveCommentBean> beans) {
        this.beans=beans;
        super.notifyDataSetChanged();
    }
}
