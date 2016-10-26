package com.subzero.shares.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.subzero.shares.R;
import com.subzero.shares.activity.OptionalExperAnalysisComment;
import com.subzero.shares.bean.OptionalExperAnalysisRecordDetailDataBean;
import com.subzero.shares.config.WebUrl;
import com.subzero.shares.utils.ImageUtils;
import com.subzero.shares.utils.ViewHolderUtils;

import java.util.List;


/**
 * 普通用户——问诊记录详情ADAPTER
 * Created by The_p on 2016/5/5.
 */
public class OptionalExperAnalysisRecordDetailAdapter extends BaseAdapter implements View.OnClickListener {
    private Context context;
    private List<OptionalExperAnalysisRecordDetailDataBean> data;

    public OptionalExperAnalysisRecordDetailAdapter(Context context, List<OptionalExperAnalysisRecordDetailDataBean> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
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
            convertView = View.inflate(context, R.layout.activity_analysis_record_detail_listview_item, null);
        }

        ImageView ivProfessional = (ImageView) ViewHolderUtils.getView(convertView, R.id.iv_professional);
        TextView tvLevel = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_level);
        TextView tvTime = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_time);
        TextView tvContent = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_content);
        TextView tvEvaluate = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_evaluate);

        tvEvaluate.setVisibility(View.VISIBLE);
        OptionalExperAnalysisRecordDetailDataBean DetailDataBean = data.get(position);
//        String rt = DetailDataBean.getReply_time().substring(11, 16);
        String rt = DetailDataBean.getReply_time();
        if ("user".equals(DetailDataBean.getLevel())) {
            tvLevel.setText(DetailDataBean.getName());
            tvEvaluate.setVisibility(View.GONE);
        } else {
            tvLevel.setText(DetailDataBean.getLevel() + "-" + DetailDataBean.getName());
            tvEvaluate.setOnClickListener(this);
        }
        tvTime.setText(rt);
        tvContent.setText(DetailDataBean.getContent());
        ImageUtils.displayAvatar(context, ivProfessional, WebUrl.FILEHOST + DetailDataBean.getAvatar());
        return convertView;
    }

    /**
     * 评价
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, OptionalExperAnalysisComment.class);
        intent.putExtra("adviserid", data.get(0).getAdvisorid());
        context.startActivity(intent);
    }

    public void notifyDataSetChanged(List<OptionalExperAnalysisRecordDetailDataBean> data) {
        this.data = data;
        super.notifyDataSetChanged();
    }

}
