package com.subzero.shares.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.subzero.shares.R;
import com.subzero.shares.activity.xlistview.XListView;
import com.subzero.shares.adapter.OptionalExperAnalysisRecordDetailAdapter;
import com.subzero.shares.bean.OptionalExperAnalysisRecordDataBean;
import com.subzero.shares.bean.OptionalExperAnalysisRecordDetailDataBean;
import com.subzero.shares.config.WebUrl;
import com.subzero.shares.utils.ImageUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


/**
 * 问诊记录详情-----普通用户
 * Created by The_p on 2016/4/15.
 */
public class OptionalExperAnalysisRecordDetail extends BaseActivity implements View.OnClickListener, XListView.IXListViewListener {

    private static final String TAG = "AnalysisRecordDetail";
    private ImageView ivBack;
    private XListView lv;
    private TextView reply;
    private TextView tvTitle;
    private TextView tvTime;
    private TextView commentTitle;
    private TextView tvContent;
    private String id;
    private int page = 1;
    private boolean isFirstGetData = true;
    private OptionalExperAnalysisRecordDataBean dataBean;
    private OptionalExperAnalysisRecordDetailAdapter adapter;
    private List<OptionalExperAnalysisRecordDetailDataBean> beans;
    private ArrayList<ImageView> imgs;
    private int[] ads = {R.id.iv_image1, R.id.iv_image2, R.id.iv_image3};
    private String[] image;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_analysis_record_detail1111111);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        reply = (TextView) findViewById(R.id.tv_reply_user);
        lv = (XListView) findViewById(R.id.lv);
        View header = View.inflate(this, R.layout.activity_analysis_record_detail_header, null);
        lv.addHeaderView(header);
        lv.setPullLoadEnable(true);
        tvTitle = (TextView) header.findViewById(R.id.tv_title);
        tvTime = (TextView) header.findViewById(R.id.tv_time);
        commentTitle = (TextView) header.findViewById(R.id.tv_comment_title);
        tvContent = (TextView) header.findViewById(R.id.tv_content);
        imgs = new ArrayList<ImageView>();
        for (int i = 0; i < ads.length; i++) {
            ImageView image = (ImageView) header.findViewById(ads[i]);
            imgs.add(image);
        }
        beans = new ArrayList<OptionalExperAnalysisRecordDetailDataBean>();
        adapter = new OptionalExperAnalysisRecordDetailAdapter(this, beans);

    }

    @Override
    protected void initData() {
        commentTitle.setText("收到的回复");
        Intent intent = getIntent();
        dataBean = intent.getParcelableExtra("dataBean");
        id = dataBean.getId();
        upUI();
        getReplys(page);
        lv.setAdapter(adapter);
    }

    @Override
    protected void initListener() {
        //返回上一界面
        ivBack.setOnClickListener(this);
        lv.setXListViewListener(this);
        reply.setOnClickListener(this);

    }

    /**
     * 跟新UI
     */
    private void upUI() {
        String title = dataBean.getTitle();
        String time = dataBean.getTime();
        String images = dataBean.getImages();
        String content = dataBean.getContent();
        tvTitle.setText(title);
        tvTime.setText(time);
        tvContent.setText(content);
        if (images != null) {
            image = images.split(",");
            for (int i = 0; i < image.length; i++) {
                final ImageView img = imgs.get(i);
                final String url = WebUrl.FILEHOST + image[i];
                ImageUtils.displayAvatar(this, imgs.get(i), url);
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageUtils.startPhotoActivity(OptionalExperAnalysisRecordDetail.this, url, img);
                    }
                });
            }
        }
    }

    /**
     * 连接服务器取回回复
     */
    private void getReplys(int page) {
//        if (isFirstGetData)
//            showLoadingDialog("正在加载数据.....");
        RequestParams params = new RequestParams(WebUrl.GETRECORDDETAIL);
        params.addBodyParameter("recordid", id);
        params.addBodyParameter("page", page + "");
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String s) {
                Log.e(TAG, "诊股记录详情收到的回复" + s);
                resultService(s);
            }

            @Override
            public void onError(Throwable throwable, boolean b) {

            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {
//                isFirstGetData = false;
//                hideLoaddingDialog();
                lv.stopLoadMore();
                lv.stopRefresh();
            }
        });
    }

    /**
     * 服务器返回结果处理
     *
     * @param s
     */
    private void resultService(String s) {
        try {
            JSONObject jo = new JSONObject(s);
            String retCode = jo.getString("retCode");
            String message = jo.getString("message");
            if ("-1".equals(retCode)) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                return;

            } else if ("-2".equals(retCode)) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                startLoginActivity();
                return;
            }
            if (page == 1)
                beans.clear();
            JSONArray ja = jo.getJSONArray("data");
            Gson gson = new Gson();
            for (int i = 0; i < ja.length(); i++) {
                String jb = ja.getString(i);
                OptionalExperAnalysisRecordDetailDataBean bean = gson.fromJson(jb, OptionalExperAnalysisRecordDetailDataBean.class);
                beans.add(bean);
            }
            adapter.notifyDataSetChanged(beans);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回上一界面
            case R.id.iv_back:
                finish();
                break;
            //进入回复页面
            case R.id.tv_reply_user:
                Intent intent = new Intent(OptionalExperAnalysisRecordDetail.this, OptionalReplyActivity.class);
                intent.putExtra("recordid", id);
                startActivityForResult(intent, 99);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            beans.clear();
            getReplys(page = 1);
        }
    }

    @Override
    public void onRefresh() {
        getReplys(page = 1);
    }

    @Override
    public void onLoadMore() {
        getReplys(++page);
    }
}
