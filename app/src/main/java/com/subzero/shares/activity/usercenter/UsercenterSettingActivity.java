package com.subzero.shares.activity.usercenter;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.subzero.shares.R;
import com.subzero.shares.activity.BaseActivity;

/**
 * usercenter设置
 * Created by The_p on 2016/4/18.
 */
public class UsercenterSettingActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout editPassword;
    private RelativeLayout commonIssue;
    private RelativeLayout aboutUs;
    private RelativeLayout customerService;
    private ImageView ivBack;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_user_center_setting);
        editPassword = (RelativeLayout) findViewById(R.id.rl_edit_password);
        commonIssue = (RelativeLayout) findViewById(R.id.rl_common_issue);
        aboutUs = (RelativeLayout) findViewById(R.id.rl_about_us);
        customerService = (RelativeLayout) findViewById(R.id.customer_service);
        ivBack = (ImageView) findViewById(R.id.iv_back);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        editPassword.setOnClickListener(this);
        commonIssue.setOnClickListener(this);
        aboutUs.setOnClickListener(this);
        customerService.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
               finish();
                break;
            case R.id.rl_edit_password:
                startActivity(new Intent(getApplication(),UsercenterChangePassword.class));
                break;
            case R.id.rl_common_issue:
                Intent intent2 = new Intent(this, UsercenterCommonIssue.class);
                startActivity(intent2);
                break;
            case R.id.rl_about_us:
                Intent intent3 = new Intent(this, UsercenterAboutUs.class);
                startActivity(intent3);
                break;
            case R.id.customer_service:
                startActivity(new Intent(getApplication(),UsercenterConnectCustom.class));
                break;

        }
    }
}
