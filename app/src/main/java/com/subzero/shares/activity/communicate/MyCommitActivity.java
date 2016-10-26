package com.subzero.shares.activity.communicate;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.subzero.shares.R;
import com.subzero.shares.activity.BaseActivity;
import com.subzero.shares.activity.xlistview.XListView;
import com.subzero.shares.adapter.MyCommitListViewAdapter;
import com.subzero.shares.bean.CommunicateBean;
import com.subzero.shares.config.WebUrl;
import com.subzero.shares.utils.SharedPreferencesUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 我的发布
 * Created by zzy on 2016/4/12.
 */
public class MyCommitActivity extends BaseActivity implements XListView.IXListViewListener {

    private String title = "我的发布";
    private ImageView shareBar;
    private TextView titleText;
    private ArrayList<CommunicateBean> beans;
    private MyCommitListViewAdapter mListViewAdapter;
    private String TAG = "MyCommitActivity";
    private int page = 1;
    private XListView xListView;
    private boolean isFirstGetData = true;

    @Override
    protected void initView() {
        setContentView(R.layout.mycommit_communicate);
        xListView = (XListView) findViewById(R.id.listView);
        xListView.setPullLoadEnable(true);
        shareBar = (ImageView) findViewById(R.id.share_communicate);
        titleText = (TextView) findViewById(R.id.textTitle);
        beans = new ArrayList<>();
    }

    @Override
    protected void initData() {
        showLoadingDialog("正在加载数据.....");
        getDataFromServer(page);
        mListViewAdapter = new MyCommitListViewAdapter(this, beans);
        xListView.setAdapter(mListViewAdapter);
        shareBar.setVisibility(View.INVISIBLE);
        titleText.setText(title);
    }

    @Override
    protected void initListener() {

        xListView.setOnItemClickListener(new MyOnItemClickListener());
        xListView.setXListViewListener(this);
    }

    private void getDataFromServer(final int page) {
        RequestParams params = new RequestParams(WebUrl.MYCOMMIT);
        params.addBodyParameter("token", SharedPreferencesUtils.getTokenId(this));
        params.addBodyParameter("page", "" + page);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "交流页我的发布信息：" + result);
                analyzeResult(result, page);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "交流页错误信息：" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                xListView.stopRefresh();
                xListView.stopLoadMore();
                if (isFirstGetData) {
                    isFirstGetData = false;
                    hideLoaddingDialog();
                }
                Log.e(TAG, "执行onFinished（）");
            }
        });
    }

    private void analyzeResult(String result, int page) {
        try {
            JSONObject js = new JSONObject(result);
            String retCode = js.getString("retCode");
            String message = js.getString("message");
            if (retCode.equals("-1")) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                return;
            } else if (retCode.equals("-2")) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                startLoginActivity();
                return;
            } else if (retCode.equals("-100")) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                return;
            }
            if (page == 1)
                beans.clear();
            //解析数据
            JSONArray ja = js.getJSONArray("data");
            CommunicateBean bean = null;
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = (JSONObject) ja.get(i);

                String id = jo.getString("id");
                String useravatar = jo.getString("useravatar");
                String useravatarUrl = WebUrl.FILEHOST + useravatar;
                String uname = jo.getString("user_nicename");
                String catename = jo.getString("catename");
                String time = jo.getString("time");
                String content = jo.getString("content");
                String rewardmoney = jo.getString("rewardmoney");
                String replycount = jo.getString("replycount");
                String title = jo.getString("title");
                String userid = jo.getString("userid");
                String images = jo.getString("images");
                String[] imgs = images.split(",");

                for (int j = 0; j < imgs.length; j++) {
                    imgs[j] = WebUrl.FILEHOST + imgs[j];
                }

                bean = new CommunicateBean(id, useravatarUrl, uname, catename, time, content, replycount, rewardmoney, title, imgs,userid);
                beans.add(bean);
                //Log.e(TAG, "交流页我的发布返回的信息：" + "id" + id + ";useravatar:" + useravatar + ";user_nicename:" + uname + ";catename:" + catename + ";time:" + time + ";time:" + content + ";rewardmoney:" + rewardmoney + ";replycount:" + replycount + ";title:" + title + ";images:" + images);
                //Log.e(TAG, "beans.size():" + beans.size());

            }

            mListViewAdapter.notifyDataSetChanged(beans);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRefresh() {
        getDataFromServer(page = 1);
    }

    @Override
    public void onLoadMore() {
        getDataFromServer(++page);
    }


    private class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getApplication(), RewardDetailActivity.class);
            intent.addFlags(2);
            intent.putExtra("data", beans.get(position - 1));
            startActivity(intent);
        }
    }

    public void back(View view) {
        finish();
    }
}
