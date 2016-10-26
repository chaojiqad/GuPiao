package com.subzero.shares.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.subzero.shares.R;
import com.subzero.shares.activity.communicate.GameActivity;
import com.subzero.shares.bean.CommunicateBean;
import com.subzero.shares.utils.ViewHolderUtils;

import java.util.ArrayList;

/**
 * 交流页第一页listview适配器
 * Created by zzy on 5/4/2016.
 */
public class CommunicateAdapter1 extends BaseAdapter {

    private int TYPE;
    private Context context;
    private ArrayList<CommunicateBean> beans;


    public CommunicateAdapter1(Context context,ArrayList<CommunicateBean> beans) {
        this.context = context;
        this.beans = beans;
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0)
            TYPE = 0;
        else
            TYPE = 1;
        return TYPE;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return 5;
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

            switch (TYPE) {
                case 0:
                    convertView = View.inflate(context, R.layout.item0_listview0_communicate, null);
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            context.startActivity(new Intent(context, GameActivity.class));
                        }
                    });
                    break;
                case 1:
                    convertView = View.inflate(context, R.layout.item_listview0_communicate, null);
                    break;
            }


        }

        //实例化ViewPager第一页控件
        ImageView img0 = (ImageView) ViewHolderUtils.getView(convertView, R.id.iv_item0_listview0_communicate);
        ImageView imgGroup = (ImageView) ViewHolderUtils.getView(convertView, R.id.iv_icon_group);
        TextView nameGrooup = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_name_group);
        TextView numGrooup = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_num_group);



        return convertView;
    }

}