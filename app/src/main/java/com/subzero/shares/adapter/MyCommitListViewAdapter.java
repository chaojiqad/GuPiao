package com.subzero.shares.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.subzero.shares.R;
import com.subzero.shares.bean.CommunicateBean;
import com.subzero.shares.utils.ImageUtils;
import com.subzero.shares.utils.ViewHolderUtils;

import java.util.ArrayList;

/**
 * 悬赏令详情页我的发布listview适配器
 * Created by zzy on 5/4/2016.
 */
public class MyCommitListViewAdapter extends BaseAdapter {
    private static final String TAG = "MyCommitList";
    private Context context;
    private ArrayList<CommunicateBean> beans;
    private ArrayList<ImageView> imgs = new ArrayList<ImageView>();
    private int[] ads = {R.id.iv_1, R.id.iv_2, R.id.iv_3};

    public MyCommitListViewAdapter(Context context, ArrayList<CommunicateBean> beans) {
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
            convertView = View.inflate(context, R.layout.item_mycommit_communicate, null);
        }

        imgs.clear();

        ImageView img;
        for (int i = 0; i < ads.length; i++) {
            img = (ImageView) ViewHolderUtils.getView(convertView, ads[i]);
            img.setImageResource(R.mipmap.zero);
            imgs.add(img);
        }

        //实例化控件
        TextView tilteText = (TextView) ViewHolderUtils.getView(convertView, R.id.title_mycommit);
        TextView weatherText = (TextView) ViewHolderUtils.getView(convertView, R.id.weather_mycommit);
        TextView virtualCoin = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_virtual_coin);
        TextView timeText = (TextView) ViewHolderUtils.getView(convertView, R.id.time_communicate);


        //为控件添加数据
        tilteText.setText(beans.get(position).getTitle());
        weatherText.setText(beans.get(position).getCatename());
        virtualCoin.setText(beans.get(position).getRewardmoney());
        timeText.setText(beans.get(position).getTime());
        String[] images = beans.get(position).getImages();
        for (int i = 0; i < images.length; i++) {
            ImageUtils.displayAvatar(context, imgs.get(i), images[i]);
            Log.e(TAG,"悬赏令路径"+images[i]);
        }

        return convertView;
    }

    public void notifyDataSetChanged(ArrayList<CommunicateBean> bean) {

        this.beans = bean;
        super.notifyDataSetChanged();
    }
}
