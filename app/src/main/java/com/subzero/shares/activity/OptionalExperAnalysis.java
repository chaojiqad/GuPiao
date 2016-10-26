package com.subzero.shares.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.subzero.shares.R;
import com.subzero.shares.activity.xlistview.XListView;
import com.subzero.shares.adapter.ExpertAnalysisAdapter;
import com.subzero.shares.bean.OptionalExperAnalysisDataBean;
import com.subzero.shares.config.WebUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 顾问诊股——普通用户获取顾问列表
 * Created by The_p on 2016/4/11.
 */
public class OptionalExperAnalysis extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, XListView.IXListViewListener {


    private ImageView ivBack;
    private TextView tvRecord;
    private XListView lv;
    //    private ArrayList<OptionalExperAnalysisDataBean> data;
    private ArrayList<OptionalExperAnalysisDataBean> beans;
    private boolean isFirstGetData = true;
    private int page = 1;
    private ExpertAnalysisAdapter adapter;


    @Override
    protected void initView() {
        setContentView(R.layout.activity_optional_expert_analysis);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvRecord = (TextView) findViewById(R.id.tv_record);
        lv = (XListView) findViewById(R.id.lv);
        beans = new ArrayList<OptionalExperAnalysisDataBean>();
        adapter = new ExpertAnalysisAdapter(this, beans);
    }

    @Override
    protected void initData() {
        lv.setAdapter(adapter);
        connectService(page);
    }

    @Override
    protected void initListener() {
        //返回上一界面
        ivBack.setOnClickListener(this);
        //进入问诊记录页面
        tvRecord.setOnClickListener(this);

        lv.setOnItemClickListener(this);
        lv.setPullLoadEnable(true);
        lv.setXListViewListener(this);

    }

    /**
     * 链接服务器,得到顾问列表
     */
    private void connectService(int page) {

        if (isFirstGetData)
            showLoadingDialog("正在加载数据......");
        //http://123.56.82.112/gupiao/index.php/Api/Apizeji/advisorlist
        RequestParams params = new RequestParams(WebUrl.ADVISORDETAILLIST);
        params.addBodyParameter("page", page + "");

        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String s) {
                Log.e("顾问诊股_普通用户获取顾问列表返回的数据", s + "");
                serviceResult(s);
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
                lv.stopRefresh();
                lv.stopLoadMore();
            }
        });
    }

    /**
     * 服务器返回结果处理
     *
     * @param s
     */
    private void serviceResult(String s) {

        try {
            JSONObject jo = new JSONObject(s);
            String retCode = jo.getString("retCode");
            String message = jo.getString("message");
            if ("-1".equals(retCode)) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                return;
            }
            if (page == 1)
                beans.clear();
           Gson gson= new Gson();
            JSONArray ja = jo.getJSONArray("data");
            for (int i = 0; i < ja.length(); i++) {
                String jb=ja.getString(i);
                OptionalExperAnalysisDataBean bean = gson.fromJson(jb, OptionalExperAnalysisDataBean.class);
                beans.add(bean);
            }
            adapter.notifyDataSetChanged(beans);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRefresh() {
        connectService(page = 1);
    }

    @Override
    public void onLoadMore() {
        connectService(++page);
    }

    /**
     * 事件监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回上一界面
            case R.id.iv_back:
                finish();
                break;
            //进入问诊记录页面
            case R.id.tv_record:
                Intent intent = new Intent(OptionalExperAnalysis.this, OptionalExperAnalysisRecord.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        OptionalExperAnalysisDataBean optionalExperAnalysisDataBean = beans.get(position - 1);
        //进入顾问详情页
        Intent intent = new Intent(this, OptionalExperDetail.class);
        intent.putExtra("bean", optionalExperAnalysisDataBean);
        startActivity(intent);
    }


}
