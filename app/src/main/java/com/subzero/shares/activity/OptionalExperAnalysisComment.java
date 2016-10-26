package com.subzero.shares.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.subzero.shares.R;
import com.subzero.shares.config.WebUrl;
import com.subzero.shares.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 评价
 * Created by The_p on 2016/4/15.
 */
public class OptionalExperAnalysisComment extends BaseActivity implements View.OnClickListener {

    private ImageView ivBack;
    private TextView tvCommit;
    private TextView tvTitle;
    private ArrayList<ImageView> starts;
    private EditText etComment;
    private int number = 0;
    private String adviserid;
    private int[] ads={R.id.iv_start5,R.id.iv_start4,R.id.iv_start3,R.id.iv_start2,R.id.iv_start1};

    @Override
    protected void initView() {
        setContentView(R.layout.activity_analysis_comment);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvCommit = (TextView) findViewById(R.id.tv_commit);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        etComment = (EditText) findViewById(R.id.et_comment);
        starts=new ArrayList<ImageView>();
        for (int i = 0; i < ads.length; i++) {
            ImageView img= (ImageView) findViewById(ads[i]);
            starts.add(img);
        }
    }

    @Override
    protected void initData() {
        tvTitle.setText("评价");
        etComment.setHint("请输入评价内容");
        adviserid = getIntent().getStringExtra("adviserid");

    }

    @Override
    protected void initListener() {
        ivBack.setOnClickListener(this);
        tvCommit.setOnClickListener(this);
        for (int i = 0; i < ads.length; i++) {
            starts.get(i).setOnClickListener(this);
        }


    }

    @Override
    public void onClick(View v) {
        //点击星星数量
        int i=0;
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_start1:
                i++;
            case R.id.iv_start2:
                i++;
            case R.id.iv_start3:
                i++;
            case R.id.iv_start4:
                i++;
            case R.id.iv_start5:
                i++;
                //为星星设置背景
                for (int x = 0; x < ads.length; x++) {
                    ImageView start=starts.get(x);
                    if (x<i){
                        start.setSelected(true);
                    }else {
                        start.setSelected(false);
                    }
                }
                number=i;
                break;
            case R.id.tv_commit:
                String text = etComment.getText().toString().trim();
                if ("".equals(text)){
                    Toast.makeText(this,"内容不能为空!",Toast.LENGTH_SHORT).show();
                    return;
                }
                connectService(text);
                break;
        }
    }

    /**
     * 链接服务器
     *
     * @param text
     */
    private void connectService(String text) {
        String tokenId = SharedPreferencesUtils.getTokenId(this);
        showLoadingDialog("正在提交回复....");
        RequestParams params = new RequestParams(WebUrl.EVALUATE1);
        params.addBodyParameter("token", tokenId);
        params.addBodyParameter("adviserid", adviserid);
        params.addBodyParameter("message", text);
        params.addBodyParameter("star", number + "");

        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String s) {
                try {
                    JSONObject jo=new JSONObject(s);
                    String retCode = jo.getString("retCode");
                    String message = jo.getString("message");
                    if ("-1".equals(retCode)) {
                        Toast.makeText(OptionalExperAnalysisComment.this, message, Toast.LENGTH_LONG).show();
                        return;
                    } else if ("-2".equals(retCode)) {
                        Toast.makeText(OptionalExperAnalysisComment.this, message, Toast.LENGTH_LONG).show();
                        startLoginActivity();
                        return;
                    }
                    //评论成功
                    Toast.makeText(OptionalExperAnalysisComment.this, message, Toast.LENGTH_LONG).show();
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable throwable, boolean b) {

            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {
                hideLoaddingDialog();
            }
        });


    }
}
