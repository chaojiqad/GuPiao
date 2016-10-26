package com.subzero.shares.activity.usercenter;

import android.text.TextUtils;
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
 * usercenter提问界面
 * Created by The_p on 2016/4/18.
 */
public class UsercenterAskQuestion extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "UsercenterAsk";
    private ImageView ivBack;

    private TextView mSub;

    private EditText mContent;

    public static Succes mSucces;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_user_center_question);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        mSub = (TextView) findViewById(R.id.tv_user_question_content_sub);
        mContent = (EditText) findViewById(R.id.et_user_question_content);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        ivBack.setOnClickListener(this);
        mSub.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_user_question_content_sub:
                if (TextUtils.isEmpty(mContent.getText().toString().trim())) {
                    Toast.makeText(UsercenterAskQuestion.this, "请输入你的问题", Toast.LENGTH_SHORT).show();
                    return;
                }

                putQuestionsTo(SharedPreferencesUtils.getTokenId(this), mContent.getText().toString().trim());

                break;

            case R.id.iv_back:
                finish();
                break;
        }

    }

    public static void setSucces(Succes succes){
        mSucces=succes;
    }

    public void putQuestionsTo(String token, String content) {

        showLoadingDialog("正在请求……");

        RequestParams params = new RequestParams(WebUrl.PUTQUESTIONSTO);
        params.addBodyParameter("token", token);
        params.addBodyParameter("content", content);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "提问返回的结果：" + result);
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
                Toast.makeText(UsercenterAskQuestion.this, message, Toast.LENGTH_SHORT).show();
                return;
            } else if (retCode.equals("-2")) {
                Toast.makeText(UsercenterAskQuestion.this, message, Toast.LENGTH_SHORT).show();
                startLoginActivity();
                return;
            } else if (retCode.equals("-100")) {
                Toast.makeText(UsercenterAskQuestion.this, message, Toast.LENGTH_SHORT).show();
                return;
            }

            if(mSucces!=null){
                mSucces.onSucces(7000);
            }

            Toast.makeText(UsercenterAskQuestion.this, message, Toast.LENGTH_SHORT).show();

            finish();


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
