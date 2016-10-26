package com.subzero.shares.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.subzero.shares.R;
import com.subzero.shares.bean.OptionalStockPoolItemBean;
import com.subzero.shares.bean.OptionalStockPoolItemPhotoBean;
import com.subzero.shares.bean.OptionalStockPoolItemPhotoUrlBean;
import com.subzero.shares.config.WebUrl;
import com.subzero.shares.utils.ImageUtils;
import com.subzero.shares.utils.ViewHolderUtils;

import java.util.ArrayList;

/**
 * 量化股票池Adapter
 * Created by The_p on 2016/4/7.
 */
public class StockPoolAdapter extends BaseAdapter {
    private Context ct = null;
    private ArrayList<OptionalStockPoolItemBean> data = null;

    public StockPoolAdapter(Context ct, ArrayList<OptionalStockPoolItemBean> data) {
        this.ct = ct;
        this.data = data;

    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
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

        if (convertView == null) {
            convertView = View.inflate(ct, R.layout.ui_item_stockpool, null);
        }

        ImageView iv = (ImageView) ViewHolderUtils.getView(convertView, R.id.iv_item_stockpool);
        TextView title = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_title);
        TextView vip = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_vip);
        TextView time = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_date);
        OptionalStockPoolItemBean item = data.get(position);
        String times = item.getTime();
        String timeSubstring = times.substring(0, 10);
        title.setText(item.getTitle());
        vip.setText(item.getCate());
        time.setText(timeSubstring);

        String image = item.getImage();
        OptionalStockPoolItemPhotoBean itemPhoto = new Gson().fromJson(image, OptionalStockPoolItemPhotoBean.class);
        ArrayList<OptionalStockPoolItemPhotoUrlBean> photo = (ArrayList<OptionalStockPoolItemPhotoUrlBean>) itemPhoto.getPhoto();
        OptionalStockPoolItemPhotoUrlBean optionalStockPoolItemPhotoUrlBean = photo.get(0);
        String url = optionalStockPoolItemPhotoUrlBean.getUrl();
        ImageUtils.displayAvatar(ct, iv, WebUrl.FILEHOST1 + url);
        return convertView;


    }

    public void notifyDataSetChanged(ArrayList<OptionalStockPoolItemBean> data) {
        this.data = data;
        super.notifyDataSetChanged();
    }

}
