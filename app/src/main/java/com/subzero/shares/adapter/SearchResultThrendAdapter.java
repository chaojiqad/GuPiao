package com.subzero.shares.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.subzero.shares.R;
import com.subzero.shares.bean.ThrenFragmentBean;
import com.subzero.shares.utils.ImageUtils;
import com.subzero.shares.utils.ViewHolderUtils;

import java.util.ArrayList;

/**
 * 首页搜索课程结果展示适配器
 * Created by zzy on 5/6/2016.
 */
public class SearchResultThrendAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ThrenFragmentBean> beans;
    private boolean isFirstGetData = true;

    public SearchResultThrendAdapter(Context context, ArrayList<ThrenFragmentBean> beans) {
        this.context = context;
        this.beans = beans;
    }

    @Override
    public int getCount() {
        return beans.size() == 0 ? 0: beans.size();
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
//        //实例化控件
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

    public void notifyDataSetChanged(ArrayList<ThrenFragmentBean> beans) {
        this.beans = beans;
        super.notifyDataSetChanged();
    }
}
