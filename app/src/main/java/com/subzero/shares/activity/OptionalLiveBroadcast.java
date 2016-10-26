package com.subzero.shares.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.subzero.shares.R;
import com.subzero.shares.activity.xlistview.XListView;
import com.subzero.shares.adapter.LiveBroadcastAdapter;
import com.subzero.shares.adapter.LiveBroadcastGuWenAdapter;
import com.subzero.shares.bean.OptionalLiveBean;
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
 * 盘中直播
 * Created by The_p on 2016/4/7.
 */
public class OptionalLiveBroadcast extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, XListView.IXListViewListener {


    private static final String TAG = "OptionalLiveBroadcast";
    private ImageView ivBack;
    private TextView tvRelease;
    private XListView lv;
    private int page = 1;
    private ArrayList<OptionalLiveBean> beans;
    private LiveBroadcastAdapter adapter2;
    private LiveBroadcastGuWenAdapter adapter1;
    private boolean isFirstGetdata = true;
    private String token;
    private int flag;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_optional_live_broadcast);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvRelease = (TextView) findViewById(R.id.tv_release);
        lv = (XListView) findViewById(R.id.lv);
        beans = new ArrayList<OptionalLiveBean>();
        token = SharedPreferencesUtils.getTokenId(this);
    }

    @Override
    protected void initData() {
        flag = getIntent().getFlags();
        if (flag == 1) {
            adapter1 = new LiveBroadcastGuWenAdapter(this, beans);
            lv.setAdapter(adapter1);
            getDirectSeedingGuWen(page);
        } else {
            adapter2 = new LiveBroadcastAdapter(this, beans);
            tvRelease.setVisibility(View.INVISIBLE);
            lv.setOnItemClickListener(this);
            lv.setAdapter(adapter2);
            getDirectSeeding(page);
        }
        lv.setPullLoadEnable(true);
        lv.setXListViewListener(this);

    }


    private void getDirectSeedingGuWen(int page) {
        if (isFirstGetdata)
            showLoadingDialog("正在加载数据....");
        RequestParams params = new RequestParams(WebUrl.GETMYRELEASE);
        params.addBodyParameter("token", token);
        params.addBodyParameter("page", page + "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "顾问盘中直播列表：" + result);
                analysisResult(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                isFirstGetdata = false;
                hideLoaddingDialog();
                lv.stopRefresh();
                lv.stopLoadMore();
            }
        });
    }

    private void getDirectSeeding(int page) {
        if (isFirstGetdata)
            showLoadingDialog("正在加载数据....");
        RequestParams params = new RequestParams(WebUrl.GETDIRECTSEEDING);
        params.addBodyParameter("token", token);
        System.out.println("tkoen:" + token);
        params.addBodyParameter("page", page + "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "用户盘中直播列表：" + result);
                analysisResult(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                isFirstGetdata = false;
                hideLoaddingDialog();
                lv.stopRefresh();
                lv.stopLoadMore();
            }
        });
    }

    private void analysisResult(String result) {
        try {
            JSONObject jo = new JSONObject(result);
            String retCode = jo.getString("retCode");
            String message = jo.getString("message");
            if ("-1".equals(retCode)) {
                Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
                return;
            } else if ("-2".equals(retCode)) {
                Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
                startLoginActivity();
                return;
            } else if ("-100".equals(retCode)) {
                Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
                return;
            }
            if (page == 1)
                beans.clear();
            JSONArray ja = jo.getJSONArray("data");
            //解析顾问发布的直播列表
            if (flag == 1) {
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jb = ja.getJSONObject(i);
                    String id = jb.getString("roomid");
                    String title = jb.getString("title");
                    String tag = jb.getString("tag");
                    String time = jb.getString("start_time");
                    beans.add(new OptionalLiveBean(title, tag, id, time));

                }
                adapter1.notifyDataSetChanged(beans);
            } else {
                //解析用户发布的直播列表
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jb = ja.getJSONObject(i);
                    String id = jb.getString("liveid");
                    String roomid = jb.getString("roomid");
                    String uid = jb.getString("uid");
                    String title = jb.getString("title");
                    String tag = jb.getString("tag");
                    String desc = jb.getString("desc");
                    String state = jb.getString("state");
                    String avatar = jb.getString("avatar");
                    avatar = WebUrl.FILEHOST + avatar;
                    String advisor = jb.getString("advisor");
                    String date = jb.getString("date");
                    String time = jb.getString("time");
                    beans.add(new OptionalLiveBean(id, roomid, uid, title, tag, desc, state, avatar, advisor, date, time));
                }
                adapter2.notifyDataSetChanged(beans);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override

    protected void initListener() {
        //返回上一界面
        ivBack.setOnClickListener(this);
        tvRelease.setOnClickListener(this);
    }

    /**
     * 返回上一界面
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_release:
                Intent intent = new Intent(this, OptionalLiveBroadcastRelease.class);
                startActivityForResult(intent, 101);
                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            getDirectSeedingGuWen(page=1);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, OptionalLiveBroadcastDetail.class);
        intent.putExtra("bean", beans.get(position - 1));
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        if (flag == 1) {
            getDirectSeedingGuWen(page = 1);
        } else {
            getDirectSeeding(page = 1);

        }
    }

    @Override
    public void onLoadMore() {
        if (flag == 1) {
            getDirectSeedingGuWen(++page);
        } else {
            getDirectSeeding(++page);
        }

    }
}
