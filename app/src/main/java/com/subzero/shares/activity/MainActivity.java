package com.subzero.shares.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.subzero.shares.R;
import com.subzero.shares.application.MyApplication;
import com.subzero.shares.utils.SharedPreferencesUtils;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * 欢迎页面
 * Created by xzf on 2016/4/25.
 */

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void initView() {
        setContentView(R.layout.main_activity);
    }

    @Override
    protected void initData() {

        // 三秒进入页面
        new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message message) {

                String tokenId = SharedPreferencesUtils.getTokenId(MainActivity.this);

                Intent intent;
                //判断用户是否登陆过
                if (TextUtils.isEmpty(tokenId)) {
                    intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {

                    connectRongYun(SharedPreferencesUtils.getUserRcToken(MainActivity.this));

                }


                return false;
            }
        }).sendEmptyMessageDelayed(0, 3000);

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
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                    Log.e(TAG, "Token错误");
                }

                /**
                 * 连接融云成功
                 */
                @Override
                public void onSuccess(String userid) {

                    Log.e(TAG, "onSuccess（userID）:" + userid);

                    setConversationTime("00:00:01",1439);

                    startActivity(new Intent(MainActivity.this, IndexFragmentActivity.class));
                    finish();

                }

                /**
                 * 连接融云失败
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                    Log.e(TAG, "onError:" + errorCode);
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            });

            RongIM.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
                @Override
                public boolean onReceived(io.rong.imlib.model.Message arg0, int arg1) {
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

    @Override
    protected void initListener() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }
}