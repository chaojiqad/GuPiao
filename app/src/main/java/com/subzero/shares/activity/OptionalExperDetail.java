package com.subzero.shares.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.subzero.shares.R;
import com.subzero.shares.activity.xlistview.XListView;
import com.subzero.shares.adapter.OptionalExperDetailAdapter;
import com.subzero.shares.bean.OptionalExperAnalysisDataBean;
import com.subzero.shares.bean.OptionalExperDetailCommentBean;
import com.subzero.shares.config.WebUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 顾问详情
 * Created by The_p on 2016/4/15.
 */
public class OptionalExperDetail extends BaseActivity implements View.OnClickListener, XListView.IXListViewListener {
    private static final String TAG = "OptionalExperDetail";
    private ImageView ivBack;
    private TextView tvAsk;
    private TextView tvBrief;
    private TextView tvFortune;
    private TextView tvName;
    private TextView tvGlory;
    private ImageView ivLog;
    private XListView lv;
    private String adviserid;
    private OptionalExperDetailAdapter adapter;
    private OptionalExperAnalysisDataBean bean;
    private ArrayList<OptionalExperDetailCommentBean> comments;
    private boolean isFirstGetData = true;
    private int page = 1;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_expert_analysis_detail1111111);
        lv = (XListView) findViewById(R.id.lv);
        lv.setPullRefreshEnable(true);
        lv.setPullLoadEnable(true);
        View header = View.inflate(this, R.layout.activity_expert_analysis_detail_header, null);
        lv.addHeaderView(header);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvAsk = (TextView) findViewById(R.id.tv_ask);
        tvBrief = (TextView) header.findViewById(R.id.tv_brief);
        tvFortune = (TextView) header.findViewById(R.id.tv_fortune);
        tvName = (TextView) header.findViewById(R.id.tv_name);
        tvGlory = (TextView) header.findViewById(R.id.tv_glory);
        ivLog = (ImageView) header.findViewById(R.id.iv_logo);
        comments = new ArrayList<OptionalExperDetailCommentBean>();
        adapter = new OptionalExperDetailAdapter(this, comments);
        lv.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        //获取传递过来的专家id
        bean = (OptionalExperAnalysisDataBean) getIntent().getSerializableExtra("bean");
        adviserid = bean.getId();
        upDateUI();
        //连接服务器
        getComments(page);
    }

    @Override
    protected void initListener() {
        //返回上一界面
        ivBack.setOnClickListener(this);
        //进入问诊提问界面
        tvAsk.setOnClickListener(this);
        lv.setXListViewListener(this);
    }

    /**
     * 得到数据跟新头部UI
     */
    private void upDateUI() {

        String avatar = bean.getAvatar();
        String desc = bean.getDesc();
        String glory = bean.getGlory();
        String name = bean.getUser_nicename();
        String price = bean.getPrice();

        tvBrief.setText(desc);
        tvFortune.setText(price);
        tvName.setText(bean.getLvname() + "——" + name);
        tvGlory.setText(glory);
        displayAvatar(ivLog, WebUrl.FILEHOST + avatar);
    }

    /**
     * 链接服务器
     */
    private void getComments(int page) {
        //加载dialog
//        if (isFirstGetData)
//            showLoadingDialog("正在加载……");
        //http://123.56.82.112/gupiao/index.php/Api/Apizeji/advisordetail/adviserid/7/page/2
        RequestParams params = new RequestParams(WebUrl.ADVISORDETAIL1);

        params.addBodyParameter("adviserid", adviserid);
        params.addBodyParameter("page", page + "");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {
                Log.e(TAG, "顾问详情返回的数据" + s);
                ServiceResult(s);
            }

            @Override
            public void onError(Throwable throwable, boolean b) {

            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {
//                isFirstGetData = false;
//                hideLoaddingDialog();
                lv.stopLoadMore();
                lv.stopRefresh();
            }
        });
    }

    /**
     * 服务器返回结果
     *
     * @param s
     */
    private void ServiceResult(String s) {
        try {
            JSONObject jo = new JSONObject(s);
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

            if (page == 1)
                comments.clear();
            JSONObject ja = jo.getJSONObject("data");
            JSONArray jaa=ja.getJSONArray("comments");
            Gson gson = new Gson();
            for (int i = 0; i < jaa.length(); i++) {
                String jb = jaa.getString(i);
                OptionalExperDetailCommentBean bean = gson.fromJson(jb, OptionalExperDetailCommentBean.class);
                comments.add(bean);
            }
            adapter.notifyDataSetChanged(comments);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_ask:
                Intent intent = new Intent(OptionalExperDetail.this, OptionalExperDetailAsk.class);
                intent.putExtra("adviserid", adviserid);
                intent.putExtra("price", bean.getPrice());
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        getComments(page = 1);
    }

    @Override
    public void onLoadMore() {
        getComments(++page);
    }
}
