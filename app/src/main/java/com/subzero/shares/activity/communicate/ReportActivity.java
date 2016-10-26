package com.subzero.shares.activity.communicate;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.subzero.shares.Interface.Succes;
import com.subzero.shares.R;
import com.subzero.shares.activity.BaseActivity;
import com.subzero.shares.config.WebUrl;
import com.subzero.shares.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 举报,我来回答页面
 * Created by zzy on 2016/4/15.
 */
public class ReportActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "ReportActivity";
    private String id;
    private ImageView shareBar;
    private TextView titleText;
    private TextView commit;
    private TextView numWword;
    private EditText reporContent;
    private Intent intent;


    private static Succes mSucces;

    @Override
    protected void initView() {
        setContentView(R.layout.reportlayout_communicate);

        shareBar = (ImageView) findViewById(R.id.share_communicate);
        titleText = (TextView) findViewById(R.id.textTitle);
        commit = (TextView) findViewById(R.id.tv_commmit);
        numWword = (TextView) findViewById(R.id.tv_num_word);
        reporContent = (EditText) findViewById(R.id.et_conrent_report);
        intent = getIntent();
    }

    public static void setSucces(Succes succes) {
        mSucces = succes;
    }

    @Override
    protected void initData() {
        shareBar.setVisibility(View.INVISIBLE);
        titleText.setText(intent.getStringExtra("title"));
        id = intent.getStringExtra("commentid");
    }

    @Override
    protected void initListener() {
        //提交按钮监听事件
        commit.setOnClickListener(this);
        reporContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.toString().length();
                numWword.setText(length + "/200");
            }
        });
    }

    @Override
    public void onClick(View v) {
        String content = reporContent.getText().toString();
        if (content.equals("")) {
            Toast.makeText(getApplication(), "提交的内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (intent.getStringExtra("title").equals("举报"))
            report(SharedPreferencesUtils.getTokenId(ReportActivity.this), id, content, WebUrl.REPORT);
        else
            report(SharedPreferencesUtils.getTokenId(ReportActivity.this), id, content, WebUrl.ANSWER);
    }

    /**
     * 举报
     *
     * @param token
     * @param commentid
     * @param message
     */
    private void report(String token, String commentid, String message, String path) {

        showLoadingDialog("正在提交数据……");

        RequestParams params = new RequestParams(path);
        params.addBodyParameter("token", token);
        if (intent.getStringExtra("title").equals("举报")) {
            params.addBodyParameter("commentid", commentid);
        } else {
            params.addBodyParameter("rewardid", commentid);
        }

        params.addBodyParameter("message", message);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "举报我的发布信息：" + result);
                analyzeResult(result);
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
                hideLoaddingDialog();
                Log.e(TAG, "执行onFinished（）");
            }
        });
    }

    /**
     * 解析json
     *
     * @param result
     */
    private void analyzeResult(String result) {
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

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            if(mSucces!=null){
                mSucces.onSucces(10000);
            }

            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void back(View view) {
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
