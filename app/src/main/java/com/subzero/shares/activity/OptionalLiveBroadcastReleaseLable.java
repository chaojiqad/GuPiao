package com.subzero.shares.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.subzero.shares.R;
import com.subzero.shares.activity.xlistview.XListView;
import com.subzero.shares.adapter.LiveBroadcastReleaseLableAdapter;
import com.subzero.shares.config.WebUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Optional盘中直播发布界面
 * Created by The_p on 2016/4/21.
 */
public class OptionalLiveBroadcastReleaseLable extends BaseActivity implements XListView.IXListViewListener, AdapterView.OnItemClickListener {

    private static final String TAG = "OptionalLiveBroad";
    private XListView lv;
    private ArrayList<String> beans;
    private LiveBroadcastReleaseLableAdapter adapter;
    private int page = 1;
    private boolean isFirstGetData = true;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_optional_live_broadcast_release_lable);
        lv = (XListView) findViewById(R.id.xlv);
        lv.setPullLoadEnable(true);
        beans = new ArrayList<String>();
        adapter = new LiveBroadcastReleaseLableAdapter(this, beans);
        lv.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        gettags(page);
    }

    @Override
    protected void initListener() {
        lv.setXListViewListener(this);
        lv.setOnItemClickListener(this);
    }

    private void gettags(int page) {
        if (isFirstGetData)
            showLoadingDialog("正在获取标签....");
        RequestParams params = new RequestParams(WebUrl.GETTAGS);
        params.addBodyParameter("page", "" + page);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "盘中直播发布界面" + result);
                analysisReult(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                lv.stopLoadMore();
                isFirstGetData = false;
                hideLoaddingDialog();
            }
        });
    }

    private void analysisReult(String result) {
        try {
            JSONObject jo = new JSONObject(result);
            String retCode = jo.getString("retCode");
            String message = jo.getString("message");
            if ("-1".equals(retCode)) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                return;
            } else if ("-2".equals(retCode)) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                startLoginActivity();
                return;
            }
            JSONArray ja = jo.getJSONArray("data");
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jb = ja.getJSONObject(i);
                String tag = jb.getString("tagname");
                beans.add(tag);
            }
            adapter.notifyDataSetChanged(beans);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void back(View view) {
        finish();
    }


    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        gettags(++page);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra("cls", beans.get(position - 1));
        setResult(RESULT_OK, intent);
        finish();
    }
}
