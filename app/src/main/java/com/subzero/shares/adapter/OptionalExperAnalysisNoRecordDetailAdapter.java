package com.subzero.shares.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.subzero.shares.R;
import com.subzero.shares.bean.OptionalExperNoRecordReplyBean;
import com.subzero.shares.utils.ImageUtils;
import com.subzero.shares.utils.ViewHolderUtils;

import java.util.ArrayList;


/**
 * 专家问诊记录详情
 * Created by zzy on 2016/5/5.
 */
public class OptionalExperAnalysisNoRecordDetailAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<OptionalExperNoRecordReplyBean> beans;

    public OptionalExperAnalysisNoRecordDetailAdapter(Context context, ArrayList<OptionalExperNoRecordReplyBean> beans) {
        this.context = context;
        this.beans = beans;
    }

    @Override
    public int getCount() {
        return beans == null ? 0 : beans.size();
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
            convertView = View.inflate(context, R.layout.activity_analysis_norecord_detail_listview_item, null);
        }
        ImageView avatar = (ImageView) ViewHolderUtils.getView(convertView, R.id.iv_professional);
        TextView level = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_level);
        TextView time = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_time);
        TextView content = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_content);

        OptionalExperNoRecordReplyBean bean = beans.get(position);
        ImageUtils.displayAvatar(context, avatar, bean.getAvatar());
        String rt = bean.getReplyTime();
        String[] rts = rt.split(" ");
        String times = rts[1].substring(0, 5);
        time.setText(times);
        String lle = bean.getLevle();
        if (lle.equals("")) {
            level.setText(bean.getName());
        } else {
            level.setText(bean.getLevle() + "-" + bean.getName());
        }
        content.setText(bean.getContent());

        return convertView;
    }


    public void notifyDataSetChanged(ArrayList<OptionalExperNoRecordReplyBean> beans) {
        this.beans = beans;
        super.notifyDataSetChanged();
    }
}
