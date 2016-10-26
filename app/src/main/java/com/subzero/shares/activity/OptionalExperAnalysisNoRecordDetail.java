package com.subzero.shares.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.subzero.shares.R;
import com.subzero.shares.activity.xlistview.XListView;
import com.subzero.shares.adapter.OptionalExperAnalysisNoRecordDetailAdapter;
import com.subzero.shares.bean.OptionalExperNoRecordBean;
import com.subzero.shares.bean.OptionalExperNoRecordReplyBean;
import com.subzero.shares.config.WebUrl;
import com.subzero.shares.utils.ImageUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 专家回复详情页
 * Created by zzy on 5/11/2016.
 */
public class OptionalExperAnalysisNoRecordDetail extends BaseActivity implements XListView.IXListViewListener, View.OnClickListener {
    private String TAG = "OptionalExperAnalysisNoRecordDetail";
    private XListView listView;
    private TextView title;
    private TextView time;
    private TextView content;
    private TextView reply;
    private ArrayList<ImageView> imgs;
    private OptionalExperAnalysisNoRecordDetailAdapter adapter;
    private ArrayList<OptionalExperNoRecordReplyBean> beans;
    private int page = 1;
    private OptionalExperNoRecordBean bean;
    private boolean isFirstGetData = true;
    private int[] ads = {R.id.iv_image1, R.id.iv_image2, R.id.iv_image3};
    private String[] imgUrl;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_analysis_record_detail1111111);
        listView = (XListView) findViewById(R.id.lv);
        listView.setPullRefreshEnable(true);
        listView.setPullLoadEnable(true);
        View view = View.inflate(this, R.layout.activity_analysis_record_detail_header, null);
        listView.addHeaderView(view);
        imgs = new ArrayList<ImageView>();
        title = (TextView) view.findViewById(R.id.tv_title);
        time = (TextView) view.findViewById(R.id.tv_time);
        content = (TextView) view.findViewById(R.id.tv_content);
        reply = (TextView) findViewById(R.id.tv_reply_user);
        for (int i = 0; i < ads.length; i++) {
            ImageView image = (ImageView) view.findViewById(ads[i]);
            imgs.add(image);
        }
        beans = new ArrayList<>();
        adapter = new OptionalExperAnalysisNoRecordDetailAdapter(this, beans);

    }

    @Override
    protected void initData() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bean = (OptionalExperNoRecordBean) bundle.get("data");
            title.setText(bean.getTitle());
            time.setText(bean.getTime());
            content.setText(bean.getMessage());
            imgUrl = bean.getImage();
            for (int y = 0; y < imgUrl.length; y++) {
                if (!("null".equals(imgUrl[y])))
                ImageUtils.displayAvatar(this, imgs.get(y), imgUrl[y]);
            }

        }
        getReplays(page);
        listView.setAdapter(adapter);
    }

    @Override
    protected void initListener() {
        listView.setXListViewListener(this);
        reply.setOnClickListener(this);
        for (int i = 0; i < imgUrl.length; i++) {
            final ImageView img = imgs.get(i);
            final String url = imgUrl[i];
            if (!"null".equals(url))
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageUtils.startPhotoActivity(OptionalExperAnalysisNoRecordDetail.this, url, img);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, OptionalReplyActivity.class);
        intent.putExtra("recordid", bean.getId());
        startActivityForResult(intent, 100);
//        startActivity(intent);
    }


    private void getReplays(int page) {
//        if (isFirstGetData)
//            showLoadingDialog("正在加载数据.....");

        RequestParams params = new RequestParams(WebUrl.GETRECORDREPLYS);
        params.addBodyParameter("recordid", bean.getId());
        params.addBodyParameter("page", page + "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "顾问角色收到的回复：" + result);
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
                listView.stopLoadMore();
                listView.stopRefresh();
//                isFirstGetData = false;
//                hideLoaddingDialog();
            }
        });
    }

    private void analysisResult(String result) {
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
            //解析数据
            if (page == 1)
                beans.clear();
            JSONArray ja = jb.getJSONArray("data");
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                String avatar = jo.getString("avatar");
                String level = jo.getString("level");
                String name = jo.getString("name");
                String time = jo.getString("reply_time");
                String content = jo.getString("content");
                avatar = WebUrl.FILEHOST + avatar;
                beans.add(new OptionalExperNoRecordReplyBean(avatar, level, name, time, content));
            }
            adapter.notifyDataSetChanged(beans);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRefresh() {
        getReplays(page = 1);
    }

    @Override
    public void onLoadMore() {
        getReplays(++page);
    }

    public void back(View view) {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            getReplays(page = 1);
        }
    }
}
