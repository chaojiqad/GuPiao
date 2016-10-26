package com.subzero.shares.activity.newsdetail;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.subzero.shares.R;
import com.subzero.shares.activity.BaseActivity;
import com.subzero.shares.activity.xlistview.XListView;
import com.subzero.shares.bean.BankBean;
import com.subzero.shares.config.WebUrl;
import com.subzero.shares.utils.ViewHolderUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 开户
 * Created by zzy on 2016/4/26.
 */
public class OpenAccountActivity extends BaseActivity implements XListView.IXListViewListener {
    private static final String TAG = "OpenAccountActivity";
    private String TITLE = "开户";
    private TextView titleText;
    private ImageView shareBar;
    private XListView listView;
    private int page = 1;
    private ArrayList<BankBean> beans;
    private MyListViewAdapter adapter;
    /*记录是否是第一次加载数据*/
    private boolean isOnceLoadData = true;

    @Override
    protected void initView() {
        setContentView(R.layout.open_account_thrend);

        listView = (XListView) findViewById(R.id.listView);
        titleText = (TextView) findViewById(R.id.textTitle);
        shareBar = (ImageView) findViewById(R.id.share_communicate);
        beans = new ArrayList<>();
        adapter = new MyListViewAdapter();
    }

    @Override
    protected void initData() {
        titleText.setText(TITLE);
        shareBar.setVisibility(View.INVISIBLE);
        bank(page);
    }

    private void bank(int page) {
        if (isOnceLoadData) {

            showLoadingDialog("正在加载数据....");
        }
        RequestParams params = new RequestParams(WebUrl.BANK);
        params.addBodyParameter("page", page + "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "开户返回的数据:" + result);
                analysis(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                hideLoaddingDialog();
                listView.stopRefresh();
                isOnceLoadData = false;
            }
        });
    }

    private void analysis(String result) {
        try {
            JSONObject jo = new JSONObject(result);
            String retCode = jo.getString("retCode");
            String message = jo.getString("message");
            if ("-1".equals(retCode)) {
                Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
                return;
            } else if ("-2".equals(retCode)) {
                Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
                startLoginActivity();
                return;
            }
            if (page == 1)
                beans.clear();
            JSONArray ja = jo.getJSONArray("data");
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jb = ja.getJSONObject(i);
                String image = jb.getString("image");
                image = WebUrl.FILEHOST1 + image;
                String msg = jb.getString("message");
                String link = jb.getString("link");
                beans.add(new BankBean(image, msg, link));
            }
            adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void initListener() {
        listView.setAdapter(adapter);
        listView.setXListViewListener(this);
    }

    @Override
    public void onRefresh() {
        bank(page = 1);
    }

    @Override
    public void onLoadMore() {

    }

    private class MyListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return beans.size();
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
                convertView = View.inflate(getApplication(), R.layout.item_open_account_thrend, null);
            }
            //实例化控件
            ImageView img = (ImageView) ViewHolderUtils.getView(convertView, R.id.iv_img);
            TextView openCount = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_open_count);
            TextView msg = (TextView) ViewHolderUtils.getView(convertView, R.id.tv_message);
            //为控件添加数据
            final BankBean bean = beans.get(position);
            displayAvatar(img, bean.getImage());
            msg.setText(bean.getMessage());

            openCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(OpenAccountActivity.this, WebOpenCountActivity.class);
                    intent.putExtra("link", bean.getLink());
                    startActivity(intent);
                }
            });

            return convertView;
        }

    }

    public void back(View view) {
        finish();
    }

}
