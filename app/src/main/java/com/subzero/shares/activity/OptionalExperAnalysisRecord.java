package com.subzero.shares.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.subzero.shares.R;
import com.subzero.shares.activity.xlistview.XListView;
import com.subzero.shares.adapter.ExpertAnalysisAdapterRecord;
import com.subzero.shares.bean.OptionalExperAnalysisRecordDataBean;
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
 * 问诊记录
 * Created by The_p on 2016/4/15.
 */
public class OptionalExperAnalysisRecord extends BaseActivity implements View.OnClickListener, XListView.IXListViewListener, AdapterView.OnItemClickListener {
    private static final String TAG = "AnalysisRecord";
    private ImageView ivBack;
    private XListView lv;
    private ArrayList<OptionalExperAnalysisRecordDataBean> beans;
    private ExpertAnalysisAdapterRecord adapter;
    private int page = 1;
    private boolean isFirstGetData = true;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_optional_expert_analysis_record);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        lv = (XListView) findViewById(R.id.lv);
        beans = new ArrayList<OptionalExperAnalysisRecordDataBean>();
        adapter = new ExpertAnalysisAdapterRecord(OptionalExperAnalysisRecord.this, beans);
    }

    @Override
    protected void initData() {
        connectService(page);
        lv.setAdapter(adapter);
    }

    @Override
    protected void initListener() {
        //返回上一界面
        ivBack.setOnClickListener(this);
        lv.setPullLoadEnable(true);
        lv.setXListViewListener(this);
        lv.setOnItemClickListener(this);

    }

    /**
     * 连接服务器
     */
    private void connectService(int page) {
        if (isFirstGetData)
            showLoadingDialog("正在加载数据.....");
        String tokenId = SharedPreferencesUtils.getTokenId(this);
        RequestParams params = new RequestParams(WebUrl.GETPUTQUESTIONSTORECORD1);
        params.addBodyParameter("token", tokenId);
        params.addBodyParameter("page", page + "");

        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String s) {
                Log.e(TAG, "普通用户诊股记录:" + s);
                analysisResult(s);
            }

            @Override
            public void onError(Throwable throwable, boolean b) {

            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {
                isFirstGetData = false;
                hideLoaddingDialog();
                lv.stopLoadMore();
                lv.stopRefresh();
            }
        });
    }

    //分析结果
    private void analysisResult(String result) {
//        OptionalExperAnalysisRecordBean optionalExperAnalysisRecordBean = new Gson().fromJson(result, OptionalExperAnalysisRecordBean.class);
        try {
            JSONObject jo = new JSONObject(result);
            String retCode = jo.getString("retCode");
            String message = jo.getString("message");
            if ("-1".equals(retCode)) {
                Toast.makeText(OptionalExperAnalysisRecord.this, message, Toast.LENGTH_SHORT).show();
                return;
            } else if ("-2".equals(retCode)) {
                Toast.makeText(OptionalExperAnalysisRecord.this, message, Toast.LENGTH_SHORT).show();
                startLoginActivity();
                return;
            }
            if (page == 1)
                beans.clear();
            JSONArray ja=jo.getJSONArray("data");
            Gson gson= new Gson();
            for (int i = 0; i <ja.length(); i++) {
                String js=ja.getString(i);
                OptionalExperAnalysisRecordDataBean bean = gson.fromJson(js, OptionalExperAnalysisRecordDataBean.class);
                beans.add(bean);
            }
            adapter.notifyDataSetChanged(beans);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //返回上一界面
    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public void onRefresh() {
        connectService(page = 1);
    }

    @Override
    public void onLoadMore() {
        connectService(++page);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        OptionalExperAnalysisRecordDataBean dataBean = beans.get(position - 1);
        Intent intent = new Intent(this, OptionalExperAnalysisRecordDetail.class);
        intent.putExtra("dataBean", dataBean);
        startActivity(intent);
    }
}
