package com.subzero.shares.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.subzero.shares.R;
import com.subzero.shares.bean.OptionalLiveBean;
import com.subzero.shares.utils.ImageUtils;
import com.subzero.shares.utils.ViewHolderUtils;

import java.util.ArrayList;

/**
 * 盘中直播Adapter
 * Created by The_p on 2016/4/8.
 */
public class LiveBroadcastAdapter extends BaseAdapter {
    private Context context ;
    private ArrayList<OptionalLiveBean> beans;

    public LiveBroadcastAdapter(Context context,ArrayList<OptionalLiveBean> beans) {
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
            convertView = View.inflate(context, R.layout.ui_item_live_broadcast, null);
        }
        ImageView header= (ImageView) ViewHolderUtils.getView(convertView,R.id.iv_item_livebroadcast);
        TextView title= (TextView) ViewHolderUtils.getView(convertView,R.id.tv_item_title);
        TextView advisor= (TextView) ViewHolderUtils.getView(convertView,R.id.tv_advisor);
        TextView cls= (TextView) ViewHolderUtils.getView(convertView,R.id.tv_cls);
        TextView date= (TextView) ViewHolderUtils.getView(convertView,R.id.tv_date);
        TextView time= (TextView) ViewHolderUtils.getView(convertView,R.id.tv_time);
        TextView state= (TextView) ViewHolderUtils.getView(convertView,R.id.tv_state);

        OptionalLiveBean bean=beans.get(position);
        ImageUtils.displayAvatar(context,header,bean.getAvatar());
        title.setText(bean.getTitle());
        advisor.setText(bean.getAdvisor());
        cls.setText(bean.getTag());
        date.setText(bean.getDate());
        time.setText(bean.getTime());
        state.setText(bean.getState());
        return convertView;
    }

    public void notifyDataSetChanged(ArrayList<OptionalLiveBean> beans) {
        this.beans=beans;
        super.notifyDataSetChanged();
    }
}
