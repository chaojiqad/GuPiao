package com.subzero.shares.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.subzero.shares.R;
import com.subzero.shares.bean.OptionalExperDetailCommentBean;

import java.util.List;

/**
 * 顾问诊股Adapter
 * Created by The_p on 2016/4/11.
 */
public class OptionalExperDetailAdapter extends BaseAdapter {
    private Context ct = null;
    private List<OptionalExperDetailCommentBean> comments;
    public OptionalExperDetailAdapter(Context ct, List<OptionalExperDetailCommentBean> comments) {
        this.ct = ct;
        this.comments=comments;
    }
    @Override
    public int getCount() {
        return comments==null?0:comments.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            view = View.inflate(ct, R.layout.activity_expert_analysis_detail_listview_item, null);
            viewHolder.name = (TextView) view.findViewById(R.id.tv_name);
            viewHolder.comment= (TextView) view.findViewById(R.id.tv_comment);
            viewHolder.time= (TextView) view.findViewById(R.id.tv_time);
            viewHolder.star1= (ImageView) view.findViewById(R.id.iv_start1);
            viewHolder.star2= (ImageView) view.findViewById(R.id.iv_start2);
            viewHolder.star3= (ImageView) view.findViewById(R.id.iv_start3);
            viewHolder.star4= (ImageView) view.findViewById(R.id.iv_start4);
            viewHolder.star5= (ImageView) view.findViewById(R.id.iv_start5);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder= (ViewHolder) view.getTag();
        }
        OptionalExperDetailCommentBean CommentBean = comments.get(position);
        viewHolder.comment.setText(CommentBean.getComment());
        viewHolder.time.setText(CommentBean.getTime());
        viewHolder.name.setText(CommentBean.getName());
        String star = CommentBean.getStar();
        switch (star){
            case "1":
                viewHolder.star5.setImageResource(R.mipmap.icon_xx_sel);
                viewHolder.star4.setImageResource(R.mipmap.icon_xx);
                viewHolder.star3.setImageResource(R.mipmap.icon_xx);
                viewHolder.star2.setImageResource(R.mipmap.icon_xx);
                viewHolder.star1.setImageResource(R.mipmap.icon_xx);
                break;
             case "2":
                viewHolder.star5.setImageResource(R.mipmap.icon_xx_sel);
                viewHolder.star4.setImageResource(R.mipmap.icon_xx_sel);
                viewHolder.star3.setImageResource(R.mipmap.icon_xx);
                viewHolder.star2.setImageResource(R.mipmap.icon_xx);
                viewHolder.star1.setImageResource(R.mipmap.icon_xx);
                break;
             case "3":
                viewHolder.star5.setImageResource(R.mipmap.icon_xx_sel);
                viewHolder.star4.setImageResource(R.mipmap.icon_xx_sel);
                viewHolder.star3.setImageResource(R.mipmap.icon_xx_sel);
                viewHolder.star2.setImageResource(R.mipmap.icon_xx);
                viewHolder.star1.setImageResource(R.mipmap.icon_xx);
                break;
             case "4":
                viewHolder.star5.setImageResource(R.mipmap.icon_xx_sel);
                viewHolder.star4.setImageResource(R.mipmap.icon_xx_sel);
                viewHolder.star3.setImageResource(R.mipmap.icon_xx_sel);
                viewHolder.star2.setImageResource(R.mipmap.icon_xx_sel);
                viewHolder.star1.setImageResource(R.mipmap.icon_xx);
                break;
             case "5":
                viewHolder.star5.setImageResource(R.mipmap.icon_xx_sel);
                viewHolder.star4.setImageResource(R.mipmap.icon_xx_sel);
                viewHolder.star3.setImageResource(R.mipmap.icon_xx_sel);
                viewHolder.star2.setImageResource(R.mipmap.icon_xx_sel);
                viewHolder.star1.setImageResource(R.mipmap.icon_xx_sel);
                break;

        }
        return view;
    }

    public void notifyDataSetChanged(List<OptionalExperDetailCommentBean> comments) {
        this.comments=comments;
        super.notifyDataSetChanged();
    }

    private  static class ViewHolder{

        TextView name;
        TextView time;
        TextView comment;
        ImageView star1;
        ImageView star2;
        ImageView star3;
        ImageView star4;
        ImageView star5;
    }

}
