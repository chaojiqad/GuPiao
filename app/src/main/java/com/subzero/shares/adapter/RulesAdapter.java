package com.subzero.shares.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.subzero.shares.R;
import com.subzero.shares.bean.Rules;
import com.subzero.shares.utils.ImageUtils;
import com.subzero.shares.utils.ViewHolderUtils;

import java.util.ArrayList;

/**
 * Created by xzf on 2016/5/23.
 */
public class RulesAdapter extends BaseAdapter {
    private static final String TAG = "RulesAdapter";
    private Context context;
    private ArrayList<Rules> beans;
    private ArrayList<ImageView> imgs = new ArrayList<ImageView>();

    private int[] ads = {R.id.iv_1, R.id.iv_2, R.id.iv_3};

    public RulesAdapter(Context context, ArrayList<Rules> beans) {
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
            convertView = View.inflate(context, R.layout.item_rules, null);
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
        TextView timeText = (TextView) ViewHolderUtils.getView(convertView, R.id.time_communicate);


        //为控件添加数据
        tilteText.setText(beans.get(position).getTitle());
        weatherText.setText(beans.get(position).getWebsite());
        timeText.setText(beans.get(position).getTime());
        String[] images = beans.get(position).getImages();
        for (int i = 0; i < images.length; i++) {
            Log.e(TAG,images[i]);
            ImageUtils.displayAvatar(context, imgs.get(i), images[i]);
        }

        return convertView;
    }

    public void notifyDataSetChanged(ArrayList<Rules> beans) {
        this.beans = beans;
        super.notifyDataSetChanged();
    }
}
