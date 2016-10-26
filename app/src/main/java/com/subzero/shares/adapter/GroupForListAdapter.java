package com.subzero.shares.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.subzero.shares.R;
import com.subzero.shares.activity.MainActivity;
import com.subzero.shares.bean.WXGroup;
import com.subzero.shares.utils.ViewHolderUtils;

import java.util.ArrayList;

/**
 * Created by xzf on 2016/4/29.
 */
public class GroupForListAdapter extends BaseAdapter {

    private static final String TAG = "GroupForList";
    private Activity mContext;

    private ArrayList<WXGroup> mWXGroups;

    public GroupForListAdapter(Activity context, ArrayList<WXGroup> wxGroups) {
        this.mContext = context;
        this.mWXGroups = wxGroups;
    }

    @Override
    public int getCount() {
        return mWXGroups.size();
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
            convertView = View.inflate(mContext, R.layout.item_group_content, null);
        }

        GridView gv = (GridView) ViewHolderUtils.getView(convertView, R.id.gv_item_group_content_);

        final String[] groups = mWXGroups.get(position).getGroups();

        for (int i=0;i<groups.length;i++){
            Log.e(TAG,"群组的信息"+groups[i]);
        }

        gv.setAdapter(new GroupForGridAdapter(mContext, groups));

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String wxgroup = groups[position];

                Intent intent = new Intent(mContext,
                        MainActivity.class);
                intent.putExtra("wxgroup", wxgroup);
                mContext.setResult(mContext.RESULT_OK, intent);

                mContext.finish();

            }
        });

        return convertView;
    }


    public void notifyDataSetChanged(ArrayList<WXGroup> wxGroups) {
        super.notifyDataSetChanged();
        this.mWXGroups = wxGroups;
    }
}
