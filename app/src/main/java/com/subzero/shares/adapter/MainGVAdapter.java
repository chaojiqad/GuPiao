package com.subzero.shares.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.subzero.shares.R;
import com.subzero.shares.utils.ViewHolderUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * 主页面中GridView的适配器
 *
 * @author hanj
 */

public class MainGVAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> imagePathList = null;


    public MainGVAdapter(Context context, ArrayList<String> imagePathList) {
        this.context = context;
        this.imagePathList = imagePathList;

    }

    @Override
    public int getCount() {

        if (imagePathList == null) return 0;


        return imagePathList.size();

    }

    @Override
    public Object getItem(int position) {
        return imagePathList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String filePath = (String) getItem(position);
        ImageView imageView = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.main_gridview_item, null);
        }
        imageView = (ImageView) ViewHolderUtils.getView(convertView, R.id.main_gridView_item_photo);
        Picasso.with(context).load(new File(filePath)).into(imageView);
        return convertView;
    }


    public void notifyDataSetChanged(ArrayList<String> imagePathList) {
        this.imagePathList=imagePathList;
        super.notifyDataSetChanged();
    }
}
