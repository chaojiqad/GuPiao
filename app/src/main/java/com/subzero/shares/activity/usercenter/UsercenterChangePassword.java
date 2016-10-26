package com.subzero.shares.activity.usercenter;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
 * 修改密码
 * Created by zzy on 2016/4/26.
 */
public class UsercenterChangePassword extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "ChangePassword";
    private String TITLE = "修改密码";
    private TextView titleText;
    private TextView mOk;
    private ImageView shareBar;
    private EditText mOldPwd;
    private EditText mNewPwd;
    private EditText mEnsuerPwd;

    @Override
    protected void initView() {
        setContentView(R.layout.user_center_change_password);

        titleText = (TextView) findViewById(R.id.textTitle);
        mOk = (TextView) findViewById(R.id.tv_ok);
        mOldPwd = (EditText) findViewById(R.id.et_old_password);
        mNewPwd = (EditText) findViewById(R.id.et_new_password);
        mEnsuerPwd = (EditText) findViewById(R.id.et_ensuer_password);
        shareBar = (ImageView) findViewById(R.id.share_communicate);
    }

    @Override
    protected void initData() {
        titleText.setText(TITLE);
        shareBar.setVisibility(View.INVISIBLE);


    }

    @Override
    protected void initListener() {
        mOk.setOnClickListener(this);
    }

    public void back(View v) {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_ok:

                if (TextUtils.isEmpty(mOldPwd.getText().toString().trim()) || TextUtils.isEmpty(mEnsuerPwd.getText().toString().trim()) || TextUtils.isEmpty(mNewPwd.getText().toString().trim())) {
                    Toast.makeText(this, "密码不允许为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!mEnsuerPwd.getText().toString().trim().equals(mNewPwd.getText().toString().trim())) {
                    Toast.makeText(this, "两次输入的密码不一样", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(mEnsuerPwd.getText().toString().trim().length()<6||mEnsuerPwd.getText().toString().trim().length()>16){
                    Toast.makeText(this, "密码长度必须在6-16位之间", Toast.LENGTH_SHORT).show();
                    return;
                }

                modifyPasswords(SharedPreferencesUtils.getTokenId(this), mOldPwd.getText().toString().trim(), mNewPwd.getText().toString().trim());


                break;
        }
    }

    /**
     * 修改密码
     *
     * @param token
     * @param oldPwd
     * @param newPwd
     */
    public void modifyPasswords(String token, String oldPwd, String newPwd) {

        showLoadingDialog("正在提交……");

        RequestParams params = new RequestParams(WebUrl.MODIFYPASSWORD);
        params.addBodyParameter("token", token);
        params.addBodyParameter("oldpassword", oldPwd);
        params.addBodyParameter("newpassword", newPwd);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "修改密码返回的结果：" + result);
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
                Toast.makeText(UsercenterChangePassword.this, message, Toast.LENGTH_SHORT).show();
                return;
            } else if (retCode.equals("-2")) {
                Toast.makeText(UsercenterChangePassword.this, message, Toast.LENGTH_SHORT).show();
                startLoginActivity();
                return;
            } else if (retCode.equals("-100")) {
                Toast.makeText(UsercenterChangePassword.this, message, Toast.LENGTH_SHORT).show();
                return;
            } else if (retCode.equals("-3")) {
                Toast.makeText(UsercenterChangePassword.this, message, Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(UsercenterChangePassword.this, message, Toast.LENGTH_SHORT).show();


            finish();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
