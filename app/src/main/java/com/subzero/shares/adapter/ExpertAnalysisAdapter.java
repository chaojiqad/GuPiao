package com.subzero.shares.adapter;


import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.subzero.shares.R;
import com.subzero.shares.bean.OptionalExperAnalysisDataBean;
import com.subzero.shares.config.WebUrl;
import com.subzero.shares.utils.ImageUtils;
import com.subzero.shares.utils.ViewHolderUtils;

import java.util.ArrayList;


/**
 * 顾问诊股普通用户获得专家列表
 * Created by The_p on 2016/4/11.
 */
public class ExpertAnalysisAdapter extends BaseAdapter {
    private static final String TAG = "顾问诊股普通用户Adapter";
    private Context context = null;
    private ArrayList<OptionalExperAnalysisDataBean> data;
    private int[] adsStart={R.id.iv_start5,R.id.iv_start4,R.id.iv_start3,R.id.iv_start2,R.id.iv_start1};
    private ArrayList<ImageView> starts=new ArrayList<ImageView>();
    public ExpertAnalysisAdapter(Context context, ArrayList<OptionalExperAnalysisDataBean> data) {
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
            convertView = View.inflate(context, R.layout.ui_item_expert_analysis, null);
        }
        starts.clear();
        TextView brief = (TextView) ViewHolderUtils.getView(convertView,R.id.tv_brief);
        TextView glory = (TextView) ViewHolderUtils.getView(convertView,R.id.tv_glory);
        TextView level = (TextView) ViewHolderUtils.getView(convertView,R.id.tv_professional_title);
        ImageView expertAvatar = (ImageView) ViewHolderUtils.getView(convertView,R.id.iv_expert);
        for (int i = 0; i <adsStart.length; i++) {
            ImageView start= (ImageView) ViewHolderUtils.getView(convertView,adsStart[i]);
            starts.add(start);
        }
        OptionalExperAnalysisDataBean DataBean = data.get(position);
        level.setText(DataBean.getLvname()+":"+DataBean.getUser_nicename());
        brief.setText(DataBean.getDesc());
        glory.setText(DataBean.getGlory());
        String url = DataBean.getAvatar();
        Log.e(TAG, "专家头像URL：" + url);
        if (url != null) {
            ImageUtils.displayAvatar(context, expertAvatar, WebUrl.FILEHOST + url);
        }
        String star = DataBean.getStar();
        for (int i = 0; i <adsStart.length; i++) {
            starts.get(i).setImageResource(R.mipmap.icon_xx);
        }
        int startCount=Integer.parseInt(star);
        for (int i = 0; i < startCount; i++) {
            starts.get(i).setImageResource(R.mipmap.icon_xx_sel);
        }

        return convertView;
    }

    public void notifyDataSetChanged(ArrayList<OptionalExperAnalysisDataBean> data) {
        this.data = data;
        super.notifyDataSetChanged();
    }


}
