package com.subzero.shares.activity.usercenter;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.subzero.shares.R;
import com.subzero.shares.activity.BaseActivity;
import com.subzero.shares.activity.xlistview.XListView;
import com.subzero.shares.adapter.UsercenterNewsAdapter;
import com.subzero.shares.bean.UserNotification;
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
 * usercenter消息中心
 * Created by The_p on 2016/4/18.
 */
public class UsercenterNewsActivity extends BaseActivity implements View.OnClickListener, XListView.IXListViewListener {

    private static final String TAG = "Usercenter";
    private ImageView ivBack;
    private XListView lv;

    private ArrayList<UserNotification> mUserNotifications;
    private UsercenterNewsAdapter mAdapter;

    private int page = 1;

    /*记录是否是第一次加载数据*/
    private boolean isOnceLoadData = true;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_user_center_news);
        lv = (XListView) findViewById(R.id.lv);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        lv.setPullLoadEnable(true);
        lv.setXListViewListener(this);

    }

    @Override
    protected void initData() {
        mUserNotifications = new ArrayList<>();
        mAdapter = new UsercenterNewsAdapter(UsercenterNewsActivity.this, mUserNotifications);
        lv.setAdapter(mAdapter);

        getNotificationInfo(SharedPreferencesUtils.getTokenId(this), page);
    }

    @Override
    protected void initListener() {
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public void onRefresh() {
        getNotificationInfo(SharedPreferencesUtils.getTokenId(this), page = 1);
    }

    @Override
    public void onLoadMore() {
        getNotificationInfo(SharedPreferencesUtils.getTokenId(this), ++page);
    }

    /**
     * 获取通知信息
     */
    private void getNotificationInfo(String token, final int page) {

        if (isOnceLoadData) {
            showLoadingDialog("正在加载……");
        }

        RequestParams params = new RequestParams(WebUrl.GETNOTIFICATION);
        params.addBodyParameter("token", token);
        params.addBodyParameter("page", page + "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "获取消息返回的结果：" + result);
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
                lvStop(page);
            }
        });
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
            lv.stopRefresh();
        } else {
            lv.stopLoadMore();
        }
    }

    private void analysisJson(String result) {

        try {
            /*解析头部信息*/
            JSONObject js = new JSONObject(result);
            String retCode = js.getString("retCode");
            String message = js.getString("message");
            if (retCode.equals("-1")) {
                Toast.makeText(UsercenterNewsActivity.this, message, Toast.LENGTH_SHORT).show();
                return;
            } else if (retCode.equals("-2")) {
                Toast.makeText(UsercenterNewsActivity.this, message, Toast.LENGTH_SHORT).show();
                startLoginActivity();
                return;
            } else if (retCode.equals("-100")) {
                Toast.makeText(UsercenterNewsActivity.this, message, Toast.LENGTH_SHORT).show();
                return;
            }

            JSONArray ja = js.getJSONArray("data");

            if(ja.length()>0&&page==1){
                mUserNotifications.clear();
            }

            for (int i = 0; i < ja.length(); i++) {
                JSONObject jb_notification = ja.getJSONObject(i);
                String content = jb_notification.getString("content");
                String time = jb_notification.getString("time");
                UserNotification userNotification = new UserNotification(content, time);
                mUserNotifications.add(userNotification);
            }

            mAdapter.notifyDataSetChanged(mUserNotifications);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
