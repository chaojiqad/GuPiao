package com.subzero.shares.activity.usercenter;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.subzero.shares.Interface.Succes;
import com.subzero.shares.R;
import com.subzero.shares.activity.BaseActivity;
import com.subzero.shares.activity.xlistview.XListView;
import com.subzero.shares.adapter.CommonIssueAdapter;
import com.subzero.shares.bean.CommonIssue;
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
 * usercenter常见问题
 * Created by The_p on 2016/4/18.
 */
public class UsercenterCommonIssue extends BaseActivity implements View.OnClickListener, XListView.IXListViewListener {

    private static final String TAG = "CommonIssue";
    private TextView askQuestion;
    private ImageView ivBack;
    private XListView mCommonIssue;
    private ArrayList<CommonIssue> mCommonIssueList;
    private CommonIssueAdapter mAdapter;

    /*记录是否是第一次加载数据*/
    private boolean isOnceLoadData = true;

    private int page = 1;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_user_center_common_issue);
        askQuestion = (TextView) findViewById(R.id.tv_ask_question);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        mCommonIssue = (XListView) findViewById(R.id.lv_user_common);
        mCommonIssue.setPullLoadEnable(true);
        mCommonIssue.setXListViewListener(this);
    }

    @Override
    protected void initData() {

        mCommonIssueList = new ArrayList<CommonIssue>();
        mAdapter = new CommonIssueAdapter(this, mCommonIssueList);
        mCommonIssue.setAdapter(mAdapter);

        getProblems(SharedPreferencesUtils.getTokenId(this), page);

        UsercenterAskQuestion.setSucces(new Succes() {
            @Override
            public void onSucces(int reResultCode) {
                getProblems(SharedPreferencesUtils.getTokenId(UsercenterCommonIssue.this), page=1);
            }
        });
    }

    @Override
    protected void initListener() {
        askQuestion.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ask_question:
                Intent intent = new Intent(this, UsercenterAskQuestion.class);
                startActivity(intent);
                break;
            case R.id.iv_back:
                finish();
                break;
        }

    }

    /**
     * 获取常见问题
     *
     * @param token
     */
    public void getProblems(String token, final int page) {

        if (isOnceLoadData) {
            showLoadingDialog("正在加载……");
        }

        RequestParams params = new RequestParams(WebUrl.GETPROBLEMS);
        params.addBodyParameter("token", token);
        params.addBodyParameter("page", page + "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                Log.e(TAG, "获取常见问题返回的结果：" + result);


                analysisJson(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "错误信息：" + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinished() {

                hideLoaddingDialog();

                lvStop(page);
            }
        });

    }


    /**
     * 解析返回的json数据
     *
     * @param result
     */
    private void analysisJson(String result) {
        try {
            /*解析头部信息*/
            JSONObject js = new JSONObject(result);
            String retCode = js.getString("retCode");
            String message = js.getString("message");
            if (retCode.equals("-1")) {
                Toast.makeText(UsercenterCommonIssue.this, message, Toast.LENGTH_SHORT).show();
                return;
            } else if (retCode.equals("-2")) {
                Toast.makeText(UsercenterCommonIssue.this, message, Toast.LENGTH_SHORT).show();
                startLoginActivity();
                return;
            } else if (retCode.equals("-100")) {
                Toast.makeText(UsercenterCommonIssue.this, message, Toast.LENGTH_SHORT).show();
                return;
            }


             /*解析data部分信息*/
            JSONArray ja = js.getJSONArray("data");

            if (ja.length() > 0 && page == 1) {
                mCommonIssueList.clear();
            }


            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                String title = jo.getString("title");
                String answer = jo.getString("answer");
                mCommonIssueList.add(new CommonIssue(title, answer));
            }

            mAdapter.notifyDataSetChanged(mCommonIssueList);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        getProblems(SharedPreferencesUtils.getTokenId(this), page = 1);
    }

    @Override
    public void onLoadMore() {
        getProblems(SharedPreferencesUtils.getTokenId(this), ++page);
    }

    /**
     * 停止刷新或者是加载的状态
     *
     * @param page
     */
    private void lvStop(int page) {

        isOnceLoadData = false;
        hideLoaddingDialog();

        if (page == 1) {
            mCommonIssue.stopRefresh();
        } else {
            mCommonIssue.stopLoadMore();
        }
    }
}
