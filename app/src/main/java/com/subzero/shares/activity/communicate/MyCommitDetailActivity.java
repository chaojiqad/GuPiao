package com.subzero.shares.activity.communicate;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.subzero.shares.R;
import com.subzero.shares.activity.BaseActivity;
import com.subzero.shares.utils.ViewHolderUtils;

/**
 * 点击我的发布Item  跳转悬赏令详情的Activity
 * Created by zzy on 2016/4/16.
 */
public class MyCommitDetailActivity extends BaseActivity {

    private String[] texts = {"悬赏令", "我的发布"};
    private ListView listView;
    private ImageView shareBar;
    private TextView myCommitText;
    private TextView titleText;


    @Override
    protected void initView() {
        setContentView(R.layout.rewarddetail_mycommit_communicate);

        listView = (ListView) findViewById(R.id.listView);
        shareBar = (ImageView) findViewById(R.id.share_communicate);
        myCommitText = (TextView) findViewById(R.id.myCommit_text_rewardDetail);
        titleText = (TextView) findViewById(R.id.textTitle);

    }

    @Override
    protected void initData() {

        titleText.setText(texts[0]);
        listView.setAdapter(new MyListViewAdapter());
        shareBar.setVisibility(View.INVISIBLE);
        myCommitText.setText(texts[1]);
    }

    @Override
    protected void initListener() {

    }


    private class MyListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 6;
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
                convertView = View.inflate(getApplication(), R.layout.item_listview1_communicate, null);
            }

            //实例化控件
            TextView userNameText = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_name_user);
            TextView weatherText = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_weather);
            TextView virtualCoinText = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_virtual_coin);
            TextView timeText = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_time);
            TextView comentText = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_coment);
            TextView titleText = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_item_title);

            ImageView headView = (ImageView) ViewHolderUtils.getView(convertView, R.id.iv_headView);
            ImageView img1 = (ImageView) ViewHolderUtils.getView(convertView, R.id.iv_item_img1);
            ImageView img2 = (ImageView) ViewHolderUtils.getView(convertView, R.id.iv_item_img2);
            ImageView img3 = (ImageView) ViewHolderUtils.getView(convertView, R.id.iv_item_img3);

            //为控件添加数据
            return convertView;
        }
    }

    public void back(View view) {
        finish();
    }
}
