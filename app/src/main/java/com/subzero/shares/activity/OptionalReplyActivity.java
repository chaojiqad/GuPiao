package com.subzero.shares.activity;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.subzero.shares.R;
import com.subzero.shares.config.WebUrl;
import com.subzero.shares.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 回复
 * Created by zzy on 5/13/2016.
 */
public class OptionalReplyActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "OptionalReplyActivity";
    private TextView title;
    private TextView commit;
    private EditText reply;
    private String recordid;

    @Override
    protected void initView() {
        setContentView(R.layout.user_center_question);
        title = (TextView) findViewById(R.id.tv_title);
        commit = (TextView) findViewById(R.id.tv_replyuser);
        reply= (EditText) findViewById(R.id.et_reply);
    }

    @Override
    protected void initData() {
        title.setText("回复");
        reply.setHint(" ");
        recordid = getIntent().getStringExtra("recordid");
    }

    @Override
    protected void initListener() {
        commit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String content = reply.getText().toString().trim();
        if ("".equals(content)){
            Toast.makeText(this,"回复内容不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        replyUser(content);
    }

    private void replyUser(String content) {

        showLoadingDialog("正在提交回复....");
        RequestParams params = new RequestParams(WebUrl.ADVISORUSERREPLY);
        params.addBodyParameter("token", SharedPreferencesUtils.getTokenId(this));
        params.addBodyParameter("recordid", recordid);
        params.addBodyParameter("content", content);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "回复返回的数据：" + result);
                printReult(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    private void printReult(String result) {

        try {
            JSONObject jb = new JSONObject(result);
            String retCode = jb.getString("retCode");
            String message = jb.getString("message");
            if (retCode.equals("-1")) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                return;
            } else if (retCode.equals("-2")) {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                startLoginActivity();
                return;
            }
            hideLoaddingDialog();
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            setResult(RESULT_OK);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void back(View view) {
        finish();
    }

}
