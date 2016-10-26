package com.subzero.shares.activity.rongcloud;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.subzero.shares.R;
import com.subzero.shares.activity.BaseActivity;
import com.subzero.shares.utils.SharedPreferencesUtils;

import java.util.Locale;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ImageMessage;

/**
 * 聊天页面
 * Created by administrator on 2016/5/10.
 */
public class ConversationActivity extends BaseActivity implements View.OnClickListener, RongIM.ConversationBehaviorListener {

    private static final String TAG = "Conversation";
    private String mTargetId;
    private Conversation.ConversationType mConversationType;
    private ImageView mBack;
    private TextView mGroupName;
    private ChatRecevier chatRecevier;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_conversation);
        mBack = (ImageView) findViewById(R.id.iv_back);
        mGroupName = (TextView) findViewById(R.id.tv_con_group_name);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        getIntentDate(intent);
        //  isReconnect(intent);
        //设置会话界面操作的监听器。
        RongIM.setConversationBehaviorListener(this);//设置会话界面操作的监听器。
        //注册广播
        IntentFilter intentFilter = new IntentFilter();
        //监听顾问发布的直播
        intentFilter.addAction("chat");
        chatRecevier = new ChatRecevier();
        registerReceiver(chatRecevier, intentFilter);


    }

    @Override
    protected void initListener() {
        mBack.setOnClickListener(this);

    }

    /**
     * 展示如何从 Intent 中得到 融云会话页面传递的 Uri
     */
    private void getIntentDate(Intent intent) {

        mGroupName.setText(intent.getData().getQueryParameter("title"));

        mTargetId = intent.getData().getQueryParameter("targetId");
        //获取当前的类型
        mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));

        enterFragment(mConversationType, mTargetId);
    }

    /**
     * 加载会话页面 ConversationFragment
     *
     * @param mConversationType 会话类型
     * @param mTargetId         目标 Id
     */
    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {

        ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.conversation);

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", mTargetId).build();

        fragment.setUri(uri);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    /**
     * 判断消息是否是 push 消息
     */
    private void isReconnect(Intent intent) {


        String token = SharedPreferencesUtils.getTokenId(this);


        //push或通知过来
        if (intent != null && intent.getData() != null && intent.getData().getScheme().equals("rong")) {


            Log.e(TAG, "push");


            //通过intent.getData().getQueryParameter("push") 为true，判断是否是push消息
            if (intent.getData().getQueryParameter("push") != null
                    && intent.getData().getQueryParameter("push").equals("true")) {

                reconnect(token);
            } else {

                Log.e(TAG, "通知");


                //程序切到后台，收到消息后点击进入,会执行这里
                if (RongIM.getInstance() == null || RongIM.getInstance().getRongIMClient() == null) {
                    reconnect(token);
                } else {
                    enterFragment(mConversationType, mTargetId);
                }
            }
        }
    }

    /**
     * 重连
     *
     * @param token
     */
    private void reconnect(String token) {

        if (getApplicationInfo().packageName.equals(application.getCurProcessName(getApplicationContext()))) {

            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {

                }

                @Override
                public void onSuccess(String s) {

                    enterFragment(mConversationType, mTargetId);
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }
    }


    @Override
    public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        return false;
    }

    @Override
    public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        return false;
    }

    /**
     * 会话界面操作的监听器：ConversationBehaviorListener 的回调方法，当点击消息时执行。
     *
     * @param context 应用当前上下文。
     * @param message 被点击的消息的实体信息。
     * @return 返回True不执行后续SDK操作，返回False继续执行SDK操作。
     */
    @Override
    public boolean onMessageClick(Context context, View view, Message message) {
        if (message.getContent() instanceof ImageMessage) {
            Intent intent = new Intent(context, PhotoActivity.class);
            ImageMessage imageMessage = (ImageMessage) message.getContent();
            Uri imgUrl = imageMessage.getLocalUri() == null ? imageMessage.getRemoteUri() : imageMessage.getLocalUri();
            intent.putExtra("photo", imgUrl);

            if (imageMessage.getThumUri() != null) {
                intent.putExtra("thumbnail", imageMessage.getThumUri());
            }

            context.startActivity(intent);
        }

        return false;
    }


    @Override
    public boolean onMessageLinkClick(Context context, String s) {
        return false;
    }


    @Override
    public boolean onMessageLongClick(Context context, View view, Message message) {
        return false;
    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(chatRecevier);
    }

    /**
     * 监听直播室状态
     */
    class ChatRecevier extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();


            String[] userid = bundle.getStringArray("userid");

            for (int i = 0; i < userid.length; i++) {

                Log.e(TAG, "要开直播的顾问的id是" + userid[i]);

                if (userid[i].equals(application.userId)) {

                    Toast.makeText(ConversationActivity.this, "本直播室即将开启直播，正在退出……", Toast.LENGTH_LONG).show();

                    // 三秒进入页面
                    new Handler(new Handler.Callback() {

                        @Override
                        public boolean handleMessage(android.os.Message message) {

                            finish();

                            return false;
                        }
                    }).sendEmptyMessageDelayed(0, 3000);


                }
            }
        }
    }
}
