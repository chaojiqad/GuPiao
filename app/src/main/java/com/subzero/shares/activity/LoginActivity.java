package com.subzero.shares.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.subzero.shares.R;
import com.subzero.shares.application.MyApplication;
import com.subzero.shares.config.WebUrl;
import com.subzero.shares.utils.SharedPreferencesUtils;
import com.subzero.shares.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;

/**
 * Created by xzf on 2016/4/18.
 */
public class LoginActivity extends BaseActivity implements OnClickListener {


    private static final String TAG = "LoginActivity";
    private EditText mPhoneNum;
    private EditText mPassWrod;
    private TextView mRetPwd;
    private Button mLogin;
    private Button mRegist;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_login);
        mPhoneNum = (EditText) findViewById(R.id.et_ac_login_phone_num);
        mPassWrod = (EditText) findViewById(R.id.et_ac_login_pwd);
        mRetPwd = (TextView) findViewById(R.id.tv_ac_login_ret_pwd);
        mLogin = (Button) findViewById(R.id.btn_ac_login_login);
        mRegist = (Button) findViewById(R.id.btn_ac_login_regist);

    }

    @Override
    protected void initData() {
        mPhoneNum.setText(SharedPreferencesUtils.getUserName(this));
        SharedPreferencesUtils.clearUserId_tokenId(this);

    }

    @Override
    protected void initListener() {
        mRetPwd.setOnClickListener(this);
        mLogin.setOnClickListener(this);
        mRegist.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_ac_login_ret_pwd:

                startActivity(new Intent(this, RetrievePasswordActivity.class));

                break;

            case R.id.btn_ac_login_login:

                String phoneNum = mPhoneNum.getText().toString().trim();
                String pwd = mPassWrod.getText().toString().trim();

                if (!StringUtils.isPhoneNum(phoneNum)) {
                    Toast.makeText(this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(this, "密码不允许为空", Toast.LENGTH_SHORT).show();
                    return;
                }



                if (pwd.length() < 6||pwd.length()>16) {
                    Toast.makeText(this, "密码长度必须在6-16位之间", Toast.LENGTH_SHORT).show();
                    return;
                }

                userlogin(phoneNum, pwd);

                break;

            case R.id.btn_ac_login_regist:

                startActivity(new Intent(this, RegistActivity.class));

                break;

        }
    }


    /**
     * 登陆
     *
     * @param phone
     * @param pwd
     */
    private void userlogin(final String phone, String pwd) {

        showLoadingDialog("正在登陆……");

        RequestParams params = new RequestParams(WebUrl.LOGIN);
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("password", pwd);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "登陆返回的结果：" + result);
                analysisJson(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "错误信息：" + ex.getMessage());
                hideLoaddingDialog();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
                hideLoaddingDialog();
            }

            @Override
            public void onFinished() {
                SharedPreferencesUtils.setUserName(LoginActivity.this, phone);
            }
        });
    }


    /**
     * 解析服务器返回的json
     *
     * @param result
     */
    private void analysisJson(String result) {

        try {
            /*解析头部信息*/
            JSONObject js = new JSONObject(result);
            String retCode = js.getString("retCode");
            String message = js.getString("message");
            if (!retCode.equals("1")) {
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                hideLoaddingDialog();
                return;
            }

            /*解析data部分信息*/
            JSONObject user = js.getJSONObject("data");
            String userid = user.getString("userid");
            String token = user.getString("token");
            String usertype = user.getString("usertype");
            String levelid = user.getString("levelid");
            String name = user.getString("name");
            String avatar = user.getString("avatar");


            //保存userID 和 tokenID
            SharedPreferencesUtils.setUserId(LoginActivity.this, userid);
            SharedPreferencesUtils.setTokenId(LoginActivity.this, token);
            SharedPreferencesUtils.setUserType(LoginActivity.this, usertype);
            SharedPreferencesUtils.setUserLevel(LoginActivity.this, levelid);

            Log.e(TAG,"用户类型"+usertype);

            rytoken(userid, name, WebUrl.FILEHOST + avatar);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取融云tokenID
     *
     * @param userid
     * @param name
     * @param avatar
     */
    private void rytoken(final String userid, String name, String avatar) {
        RequestParams params = new RequestParams(WebUrl.GETRCTOKEN);
        params.addBodyParameter("userid", userid);
        params.addBodyParameter("name", name);
        params.addBodyParameter("avatar", avatar);
        x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        Log.e(TAG, "获取融云返回的结果：" + result);
                        analysisJsonForRc(result);
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
                }

        );

    }


    /**
     * 解析融云返回的信息
     *
     * @param result
     */
    private void analysisJsonForRc(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            String rc_token = jsonObject.getString("token");
            SharedPreferencesUtils.setUserRcToken(this, rc_token);


            connectRongYun(SharedPreferencesUtils.getUserRcToken(LoginActivity.this));


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 建立与融云服务器的连接
     *
     * @param token
     */
    private void connectRongYun(String token) {

        if (getApplicationInfo().packageName.equals(MyApplication.getCurProcessName(getApplicationContext()))) {

            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，需要向 App Server 重新请求一个新的 Token
                 */
                @Override

                public void onTokenIncorrect() {

                    Log.e(TAG, "Token错误");
                }

                /**
                 * 连接融云成功
                 */
                @Override
                public void onSuccess(String userid) {

                    Log.e(TAG, "onSuccess（userID）:" + userid);

                    setConversationTime("00:00:01",1439);

                    startActivity(new Intent(LoginActivity.this, IndexFragmentActivity.class));
                    finish();

                }

                /**
                 * 连接融云失败
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                    Log.e(TAG, "onError:" + errorCode);
                }
            });

            RongIM.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
                @Override
                public boolean onReceived(Message arg0, int arg1) {
                    return true;
                }
            });


        }
    }

    /**
     * 设置勿扰时间
     *
     * @param startTime 设置勿扰开始时间 格式为：HH:mm:ss
     * @param spanMins  0 < 间隔时间 < 1440
     */
    private void setConversationTime(final String startTime, final int spanMins) {

        if (RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null && !TextUtils.isEmpty(startTime)) {

            if (spanMins > 0 && spanMins < 1440) {
                Log.e(TAG, "----设置勿扰时间startTime；" + startTime + "---spanMins:" + spanMins);

                RongIM.getInstance().getRongIMClient().setNotificationQuietHours(startTime, spanMins, new RongIMClient.OperationCallback() {

                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        Log.e(TAG, "----设置会话通知周期-oonError:" + errorCode.getValue());
                    }
                });
            } else {
            }
        }
    }

}
