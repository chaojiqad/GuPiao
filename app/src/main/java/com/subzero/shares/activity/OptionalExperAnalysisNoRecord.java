package com.subzero.shares.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.subzero.shares.R;
import com.subzero.shares.activity.xlistview.XListView;
import com.subzero.shares.adapter.OptionalExperAnalysisNoRecordAdapter;
import com.subzero.shares.bean.OptionalExperNoRecordBean;
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
 * 专家进入的顾问诊股
 * Created by zzy on 5/11/2016.
 */
public class OptionalExperAnalysisNoRecord extends BaseActivity implements AdapterView.OnItemClickListener, XListView.IXListViewListener {
    private final String TAG = "AnalysisNoRecord";
    private XListView lv;
    private int page = 1;
    private ArrayList<OptionalExperNoRecordBean> beans;
    private boolean isFirstGetData = true;
    private OptionalExperAnalysisNoRecordAdapter adapter;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_optional_expert_analysis_norecord);
        lv = (XListView) findViewById(R.id.lv);
        beans = new ArrayList<OptionalExperNoRecordBean>();
        adapter = new OptionalExperAnalysisNoRecordAdapter(this, beans);
    }

    @Override
    protected void initData() {
        lv.setPullLoadEnable(true);
        lv.setPullRefreshEnable(true);
        lv.setAdapter(adapter);
        getDataFromServer(page);
    }

    @Override
    protected void initListener() {
        lv.setOnItemClickListener(this);
        lv.setXListViewListener(this);
    }

    private void getDataFromServer(int page) {
        if (isFirstGetData)
            showLoadingDialog("正在加载数据.....");
        RequestParams params = new RequestParams(WebUrl.GETADVISORRECORD);
        params.addBodyParameter("token", SharedPreferencesUtils.getTokenId(this));
        params.addBodyParameter("page", page + "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "顾问返回的数据" + result);
                analysisResult(result);
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
                lv.stopRefresh();
                isFirstGetData = false;
                hideLoaddingDialog();
            }
        });
    }

    private void analysisResult(String result) {
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
            }
            //解析数据
            if (page == 1) {
                beans.clear();
            }
            JSONArray ja = js.getJSONArray("data");
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jb = ja.getJSONObject(i);
                String id = jb.getString("id");
                String title = jb.getString("title");
                String msg = jb.getString("content");
                String time = jb.getString("time");
                String image = jb.getString("images");
//                Log.e(TAG, "图片image:" + image);
                String[] images = image.split(",");
                for (int y = 0; y < images.length; y++) {
                    if (!"null".equals(images[y])){
                        images[y] = WebUrl.FILEHOST + images[y];
                        Log.e(TAG, "图片URl:" + y + images[y]);
                    }
                }
                beans.add(new OptionalExperNoRecordBean(id, title, msg, time, images));
            }
            adapter.notifyDataSetChanged(beans);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, OptionalExperAnalysisNoRecordDetail.class);
        intent.putExtra("data", beans.get(position - 1));
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        getDataFromServer(page = 1);
    }

    @Override
    public void onLoadMore() {
        getDataFromServer(++page);

    }

    public void back(View view) {
        finish();
    }


}
