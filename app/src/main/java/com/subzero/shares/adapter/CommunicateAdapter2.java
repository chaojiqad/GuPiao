package com.subzero.shares.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.subzero.shares.R;
import com.subzero.shares.bean.CommunicateBean;
import com.subzero.shares.utils.ImageUtils;
import com.subzero.shares.utils.ViewHolderUtils;
import com.subzero.shares.view.CircleImageView;

import java.util.ArrayList;

/**
 * 交流页第二页listview适配器
 * Created by zzy on 5/4/2016.
 */
public class CommunicateAdapter2 extends BaseAdapter {

    private Context context;
    private ArrayList<CommunicateBean> beans;
    private ArrayList<ImageView> imgs = new ArrayList<ImageView>();

    private int[] ads = {R.id.iv_item_img1, R.id.iv_item_img2, R.id.iv_item_img3};

    public CommunicateAdapter2(Context context, ArrayList<CommunicateBean> beans) {
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
            convertView = View.inflate(context, R.layout.item_listview1_communicate, null);
        }
        imgs.clear();

        ImageView img;
        for (int i = 0; i < ads.length; i++) {
            img = (ImageView) ViewHolderUtils.getView(convertView, ads[i]);
            img.setImageResource(R.mipmap.zero);
            imgs.add(img);
        }

        //实例化ViewPager第二页控件
        TextView userNameText = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_name_user);
        TextView cateNameText = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_weather);
        TextView virtualCoinText = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_virtual_coin);
        TextView timeText = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_time);
        TextView comentText = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_coment);
        TextView titleText = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_item_title);
        CircleImageView headView = (CircleImageView) ViewHolderUtils.getView(convertView, R.id.iv_headView);

        // 为控件添加数据
        timeText.setText(beans.get(position).getTime());
        comentText.setText(beans.get(position).getReplycount());
        titleText.setText(beans.get(position).getTitle());
        virtualCoinText.setText(beans.get(position).getRewardmoney());
        userNameText.setText(beans.get(position).getUsername());
        cateNameText.setText(beans.get(position).getCatename());
        ImageUtils.displayAvatar(context, headView, beans.get(position).getUseravater());

        String[] images = beans.get(position).getImages();


        if (images.length > 0) {

            for (int i = 0; i < images.length; i++) {
                ImageUtils.displayAvatar(context, imgs.get(i), images[i]);
            }


        }


        return convertView;
    }

    public void notifyDataSetChanged(ArrayList<CommunicateBean> beans) {
        this.beans = beans;
        super.notifyDataSetChanged();
    }
}
