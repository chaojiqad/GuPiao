package com.subzero.shares.activity;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.subzero.shares.R;
import com.subzero.shares.config.WebUrl;
import com.subzero.shares.utils.StringUtils;
import com.subzero.shares.view.TimeDownButton;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;

/**
 * 找回密码
 * Created by xzf on 2016/4/15.
 */
public class RetrievePasswordActivity extends BaseActivity implements View.OnClickListener {


    private EditText mPhoneNum;
    private EditText mCheckNum;
    private EditText mRetPwd;
    private TimeDownButton mGetCheckNum;
    private ImageButton mBack;
    private Button mOk;

    public static final String TAG = "RetrievePassword";

    protected Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;

            if (result == SMSSDK.RESULT_COMPLETE) {
                System.out.println("--------result" + event);

                if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {

                    Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();
                }

            } else {

                try {
                    ((Throwable) data).printStackTrace();
                    Throwable throwable = (Throwable) data;

                    JSONObject object = new JSONObject(throwable.getMessage());
                    String des = object.optString("detail");

                    if (!TextUtils.isEmpty(des)) {
                        Toast.makeText(RetrievePasswordActivity.this, des, Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    SMSLog.getInstance().w(e);
                }


            }


        }
    };

    @Override
    protected void initView() {
        setContentView(R.layout.activity_retrieve_pwd);
        mPhoneNum = (EditText) findViewById(R.id.et_ac_rie_pwd_phone_num);
        mCheckNum = (EditText) findViewById(R.id.et_ac_rie_pwd_check_num);
        mRetPwd = (EditText) findViewById(R.id.et_ac_rie_pwd_pwd);
        mGetCheckNum = (TimeDownButton) findViewById(R.id.btn_ac_rie_pwd_check_num);
        mOk = (Button) findViewById(R.id.btn_ac_rie_pwd_ok);
        mBack = (ImageButton) findViewById(R.id.ibtn_ac_rie_pwd_back);
    }


    @Override
    protected void initData() {

        initSMS();

    }

    @Override
    protected void initListener() {
 /*设置倒计时按钮的点击事件*/
        mGetCheckNum.setOnClickTimeDownButtonListener(new TimeDownButton.OnClickTimeDownButtonListener() {
            @Override
            public void onClick() {
                if (!StringUtils.isPhoneNum(mPhoneNum.getText().toString().trim())) {
                    Toast.makeText(RetrievePasswordActivity.this, "手机号码的格式不正确", Toast.LENGTH_SHORT).show();
                    return;
                }
                //开启倒计时
                mGetCheckNum.startTimeDown();
                getCheckNum(mPhoneNum.getText().toString().trim());
            }
        });

        mBack.setOnClickListener(this);
        mOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ac_rie_pwd_ok:

                if(!StringUtils.isPhoneNum(mPhoneNum.getText().toString().trim())){
                    Toast.makeText(this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mRetPwd.getText().toString().trim().length() < 6||mRetPwd.getText().toString().trim().length()>16) {

                    Toast.makeText(this, "密码长度必须在6-16位之间", Toast.LENGTH_SHORT).show();

                    return;

                }

                if (TextUtils.isEmpty(mCheckNum.getText().toString().trim())) {
                    Toast.makeText(RetrievePasswordActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                forGetPwd(mPhoneNum.getText().toString().trim(), mRetPwd.getText().toString().trim(), mCheckNum.getText().toString().trim());
                break;

            case R.id.ibtn_ac_rie_pwd_back:

                finish();

                break;
        }
    }


    private void initSMS() {
        SMSSDK.initSDK(this, APPKEY, APPSECRET);
        EventHandler eh = new EventHandler() {

            @Override
            public void afterEvent(int event, int result, Object data) {

                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                mHandler.sendMessage(msg);

            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }

    /**
     * 注册接口
     *
     * @param phone
     * @param pwd
     * @param checkNum
     */
    private void forGetPwd(String phone, String pwd, String checkNum) {

        showLoadingDialog("正在请求……");

        RequestParams params = new RequestParams(WebUrl.GETFORPWD);

        params.addBodyParameter("phone", phone);
        params.addBodyParameter("checkNum", checkNum);
        params.addBodyParameter("password", pwd);

        Log.e(TAG, "phone:" + phone + "^^^^^^^^^checkNum:" + checkNum + "^^^^^^^^^^^passwprd:" + pwd);

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                hideLoaddingDialog();
                Log.e(TAG, "找回密码返回的结果：" + result);
                analysisJson(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                hideLoaddingDialog();
                Toast.makeText(x.app(), "错误信息：" + ex.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                hideLoaddingDialog();
                Toast.makeText(x.app(), "cancelled", Toast.LENGTH_LONG).show();
            }





            @Override
            public void onFinished() {
                hideLoaddingDialog();
            }
        });
    }


    private void analysisJson(String result) {

        try {
            /*解析头部信息*/
            JSONObject js = new JSONObject(result);
            String retCode = js.getString("retCode");
            String message = js.getString("message");
            if (!retCode.equals("1")) {
                Toast.makeText(RetrievePasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(RetrievePasswordActivity.this, message, Toast.LENGTH_SHORT).show();

            finish();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取验证码
     *
     * @return
     */
    private void getCheckNum(String phone) {
        SMSSDK.getVerificationCode("86", phone);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }
}
