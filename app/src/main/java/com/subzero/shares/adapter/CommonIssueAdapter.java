package com.subzero.shares.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.subzero.shares.R;
import com.subzero.shares.bean.CommonIssue;
import com.subzero.shares.utils.ViewHolderUtils;

import java.util.ArrayList;

/**
 * Created by xzf on 2016/4/28.
 */
public class CommonIssueAdapter extends BaseAdapter {

    private Context mContext;

    private ArrayList<CommonIssue> mCommonIssueList;

    public CommonIssueAdapter(Context context, ArrayList<CommonIssue> commonIssueList) {
        this.mContext = context;
        this.mCommonIssueList = commonIssueList;
    }

    @Override
    public int getCount() {
        return mCommonIssueList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_user_center_icommon, null);
        }

        TextView title = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_item_center_title);
        TextView answer = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_item_center_content);


        title.setText(mCommonIssueList.get(position).getTitle());
        answer.setText(mCommonIssueList.get(position).getAnswer());

        return convertView;

    }

    public void notifyDataSetChanged(ArrayList<CommonIssue> commonIssueList) {
        this.mCommonIssueList = commonIssueList;
        super.notifyDataSetChanged();
    }
}
