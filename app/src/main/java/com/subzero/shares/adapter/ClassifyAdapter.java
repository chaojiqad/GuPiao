package com.subzero.shares.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.subzero.shares.R;
import com.subzero.shares.bean.ClassifyBean;
import com.subzero.shares.utils.ViewHolderUtils;

import java.util.ArrayList;

/**
 * 课堂类别列表适配器
 * Created by zzy on 5/16/2016.
 */
public class ClassifyAdapter extends BaseAdapter {
    private ArrayList<ClassifyBean> classifyValues;
    private Context context;
    private ArrayList<ImageView> rbtn;
    private boolean isFirstGetData = true;

    public ClassifyAdapter(Context context, ArrayList<ClassifyBean> classify) {
        this.context = context;
        classifyValues = classify;
        rbtn = new ArrayList<ImageView>();
    }

    @Override
    public int getCount() {
        return classifyValues.size();
    }

    @Override
    public Object getItem(int position) {
        return rbtn.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = View.inflate(context, R.layout.item_classify_listview_thrend, null);
        TextView textView = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_item_classify);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_seclect);
        textView.setText(classifyValues.get(position).getName());

        if (classifyValues.get(position).isSelect() == true)
            imageView.setBackgroundDrawable(context.getResources().getDrawable(R.mipmap.bg_icon_xz_sel));
        else
            imageView.setBackgroundDrawable(context.getResources().getDrawable(R.mipmap.bg_icon_xz));

        return convertView;
    }

    public void notifyDataSetChanged(ArrayList<ClassifyBean> classifyValues) {
        this.classifyValues = classifyValues;
        super.notifyDataSetChanged();

    }
}
