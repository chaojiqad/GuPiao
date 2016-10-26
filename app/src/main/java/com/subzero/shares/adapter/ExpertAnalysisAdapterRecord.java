package com.subzero.shares.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.subzero.shares.R;
import com.subzero.shares.bean.OptionalExperAnalysisRecordDataBean;
import com.subzero.shares.config.WebUrl;
import com.subzero.shares.utils.ImageUtils;
import com.subzero.shares.utils.ViewHolderUtils;

import java.util.ArrayList;


/**
 * 问诊记录Adapter
 * Created by The_p on 2016/4/15.
 */
public class ExpertAnalysisAdapterRecord extends BaseAdapter {
    private static final String TAG = "Expert";
    private Context context = null;
    private ArrayList<OptionalExperAnalysisRecordDataBean> data;
    private ArrayList<ImageView> imgs = new ArrayList<ImageView>();
    private int[] ads = {R.id.iv_1, R.id.iv_2, R.id.iv_3};

    public ExpertAnalysisAdapterRecord(Context context, ArrayList<OptionalExperAnalysisRecordDataBean> data) {
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
        imgs.clear();
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.ui_item_optional_expert_analysis_record, null);
        }
        TextView title = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_title);
        TextView time = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_time);
        for (int i = 0; i < ads.length; i++) {
            ImageView iv = (ImageView) ViewHolderUtils.getView(convertView, ads[i]);
            imgs.add(iv);
            imgs.get(i).setImageResource(R.mipmap.zero);
        }

        OptionalExperAnalysisRecordDataBean dataBean = data.get(position);
        title.setText(dataBean.getTitle());
        time.setText(dataBean.getTime());
        String images = dataBean.getImages();
        if (!TextUtils.isEmpty(images)) {
            String[] image = images.split(",");
            for (int i = 0; i < image.length; i++) {
                ImageUtils.displayAvatar(context, imgs.get(i), WebUrl.FILEHOST + image[i]);
            }
        }

        return convertView;
    }

    public void notifyDataSetChanged(ArrayList<OptionalExperAnalysisRecordDataBean> data) {
        this.data = data;
        super.notifyDataSetChanged();
    }

}

