package com.subzero.shares.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.subzero.shares.R;
import com.subzero.shares.bean.ThrenFragmentBean;
import com.subzero.shares.config.WebUrl;
import com.subzero.shares.utils.ImageUtils;
import com.subzero.shares.utils.ViewHolderUtils;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 首页ListView适配器
 * Created by zzy on 2016/4/29.
 */
public class ThrendFagmentListViewAdapter extends BaseAdapter {
    private ArrayList<ThrenFragmentBean> beans;
    private Context context;

    public ThrendFagmentListViewAdapter(Context context,ArrayList<ThrenFragmentBean> beans) {
        this.beans=beans;
        this.context=context;
    }

    @Override
    public int getCount() {
        return beans==null?0:beans.size();
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
            convertView = View.inflate(context, R.layout.item_listview_thrend, null);
        }
        //实例化控件
        ImageView img= (ImageView) ViewHolderUtils.getView(convertView,R.id.iv_item_thrend);
        TextView tiltlText= (TextView) ViewHolderUtils.getView(convertView,R.id.tv_item_title_thrend);
        TextView newssourceText= (TextView) ViewHolderUtils.getView(convertView,R.id.tv_news_source);
        TextView timeText= (TextView) ViewHolderUtils.getView(convertView,R.id.tv_item_time_thrend);
        //为控件赋值
        tiltlText.setText(beans.get(position).getTitle());
        timeText.setText(beans.get(position).getTime());
        newssourceText.setText(beans.get(position).getWebsite());
        ImageUtils.displayAvatar(context,img,beans.get(position).getImage());


        return convertView;
    }

    public void notifyDataSetChanged(ArrayList<ThrenFragmentBean> consultationBeans) {
        this.beans=consultationBeans;
        super.notifyDataSetChanged();
    }
}

