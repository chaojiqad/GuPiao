package com.subzero.shares.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.subzero.shares.R;
import com.subzero.shares.bean.OptionalExperNoRecordBean;
import com.subzero.shares.utils.ImageUtils;
import com.subzero.shares.utils.ViewHolderUtils;

import java.util.ArrayList;

/**
 * 专家顾问诊股记录适配器
 * Created by zzy on 5/11/2016.
 */
public class OptionalExperAnalysisNoRecordAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<OptionalExperNoRecordBean> beans;
    private ArrayList<ImageView> imgs =new ArrayList<ImageView>();
    private int[] ads={R.id.iv_1,R.id.iv_2,R.id.iv_3};

    public OptionalExperAnalysisNoRecordAdapter(Context context, ArrayList beans) {
        this.context = context;
        this.beans = beans;
    }

    @Override
    public int getCount() {
        return beans==null ? 0 : beans.size();
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
        if (convertView == null)
            convertView = View.inflate(context, R.layout.ui_item_optional_expert_analysis_record, null);

        TextView title= (TextView) ViewHolderUtils.getView(convertView,R.id.tv_title);
        TextView time= (TextView) ViewHolderUtils.getView(convertView,R.id.tv_time);
        imgs.clear();
        for (int y=0;y<ads.length;y++){
            ImageView image= (ImageView) ViewHolderUtils.getView(convertView,ads[y]);
//            image.setImageResource(R.drawable.ic_action_camera);
            imgs.add(image);

        }
        for (int i = 0; i <3 ; i++) {
            imgs.get(i).setImageResource(R.mipmap.zero);
        }
        title.setText(beans.get(position).getTitle());
        time.setText(beans.get(position).getTime());
        //显示图片
        String[] img=beans.get(position).getImage();
        for (int i=0;i<img.length;i++){
            ImageUtils.displayAvatar(context,imgs.get(i),img[i]);
        }
        return convertView;
    }

    public void notifyDataSetChanged(ArrayList<OptionalExperNoRecordBean> beans) {
        this.beans = beans;
        super.notifyDataSetChanged();
    }
}
