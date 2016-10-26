package com.subzero.shares.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.subzero.shares.R;
import com.subzero.shares.bean.ChatBean;
import com.subzero.shares.utils.ImageUtils;
import com.subzero.shares.utils.ViewHolderUtils;

import java.util.ArrayList;

/**
 * Created by shw on 2016/5/9.
 */
public class TalkGroupAdapter extends BaseAdapter {

    private static final String TAG = "TalkGroupList";
    private Context mContext;

    private ArrayList<ChatBean> mChatbeans;

    public TalkGroupAdapter(Activity context, ArrayList<ChatBean> chatbeans) {

        this.mContext = context;
        this.mChatbeans = chatbeans;
    }

    @Override
    public int getCount() {
        return mChatbeans == null ? 0 : mChatbeans.size();
    }

    @Override
    public Object getItem(int position) {
        return mChatbeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_talk_group, null);

        }

        ImageView group = (ImageView) ViewHolderUtils.getView(convertView, R.id.iv_group);
        TextView groupName = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_group_name);
        TextView membercount = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_membercount);


        ChatBean chatBean = mChatbeans.get(position);

        ImageUtils.displayAvatar(mContext, group, chatBean.getGroupavatar());

        groupName.setText(chatBean.getGroupName());
        membercount.setText("("+chatBean.getMembercount()+")");

        return convertView;
    }

    public void notifyDataSetChanged(ArrayList<ChatBean> chatBeans) {
        this.mChatbeans = chatBeans;
        super.notifyDataSetChanged();
    }
}



