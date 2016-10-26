package com.subzero.shares.activity.usercenter;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.subzero.shares.R;
import com.subzero.shares.activity.BaseActivity;
import com.subzero.shares.config.WebUrl;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * 联系客服
 * Created by zzy on 2016/4/26.
 */
public class UsercenterConnectCustom extends BaseActivity {
    private static final String TAG = "ConnectCustom";
    private String TITLE = "联系客服";
    private TextView titleText;
    private TextView mEmail;

    private ImageView shareBar;

    @Override
    protected void initView() {
        setContentView(R.layout.user_center_custom_service);

        titleText = (TextView) findViewById(R.id.textTitle);
        shareBar = (ImageView) findViewById(R.id.share_communicate);
        mEmail = (TextView) findViewById(R.id.tv_user_custom_service_email);

        titleText.setText(TITLE);
        shareBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initData() {
        getPost("联系客服");
    }

    @Override
    protected void initListener() {

    }

    public void back(View v) {
        finish();
    }

    /**
     * 关于我们或者是联系客服
     *
     * @param keyword
     */
    public void getPost(String keyword) {

        showLoadingDialog("正在加载……");

        RequestParams params = new RequestParams(WebUrl.ABOUTLTD);
        params.addBodyParameter("keyword", keyword);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "联系客服&关于我们返回的结果：" + result);
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
                Toast.makeText(UsercenterConnectCustom.this, message, Toast.LENGTH_SHORT).show();
                return;
            } else if (retCode.equals("-2")) {
                Toast.makeText(UsercenterConnectCustom.this, message, Toast.LENGTH_SHORT).show();
                startLoginActivity();
                return;
            } else if (retCode.equals("-100")) {
                Toast.makeText(UsercenterConnectCustom.this, message, Toast.LENGTH_SHORT).show();
                return;
            }

            String content = js.getJSONObject("data").getString("content");
            mEmail.setText(content);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
