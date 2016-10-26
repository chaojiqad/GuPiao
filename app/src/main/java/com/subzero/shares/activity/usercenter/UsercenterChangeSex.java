package com.subzero.shares.activity.usercenter;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.subzero.shares.R;
import com.subzero.shares.activity.BaseActivity;
import com.subzero.shares.activity.MainActivity;

/**
 * 修改性别
 * Created by zzy on 2016/4/26.
 */
public class UsercenterChangeSex extends BaseActivity implements View.OnClickListener {

    private String TITLE = "修改性别";
    private TextView titleText;
    private TextView mOk;
    private ImageView shareBar;
    private LinearLayout llMan;
    private LinearLayout llWoman;
    private LinearLayout llSecret;
    private ImageView start1;
    private ImageView start2;
    private ImageView start3;

    private String selectedSex;

    @Override
    protected void initView() {
        setContentView(R.layout.user_center_change_sex);

        titleText = (TextView) findViewById(R.id.textTitle);
        shareBar = (ImageView) findViewById(R.id.share_communicate);
        llMan = (LinearLayout) findViewById(R.id.ll_man);
        llWoman = (LinearLayout) findViewById(R.id.ll_woman);
        llSecret = (LinearLayout) findViewById(R.id.ll_secret);
        start1 = (ImageView) findViewById(R.id.iv_start_man);
        start2 = (ImageView) findViewById(R.id.iv_start_woman);
        start3 = (ImageView) findViewById(R.id.iv_start_secret);
        mOk = (TextView) findViewById(R.id.tv_ensure_sex);
    }

    @Override
    protected void initData() {
        titleText.setText(TITLE);
        shareBar.setVisibility(View.INVISIBLE);

        start2.setVisibility(View.INVISIBLE);
        start3.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initListener() {
        llMan.setOnClickListener(this);
        llWoman.setOnClickListener(this);
        llSecret.setOnClickListener(this);
        mOk.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        start1.setVisibility(View.INVISIBLE);
        start2.setVisibility(View.INVISIBLE);
        start3.setVisibility(View.INVISIBLE);

        switch (v.getId()) {
            case R.id.ll_man:

                start1.setVisibility(View.VISIBLE);
                selectedSex = "男";


                break;
            case R.id.ll_woman:
                start2.setVisibility(View.VISIBLE);
                selectedSex = "女";


                break;
            case R.id.ll_secret:
                start3.setVisibility(View.VISIBLE);

                selectedSex = "保密";


                break;


            case R.id.tv_ensure_sex:

                Intent intent = new Intent(UsercenterChangeSex.this,
                        MainActivity.class);
                intent.putExtra("sex", selectedSex);
                setResult(RESULT_OK, intent);
                finish();

                break;

        }
    }

    public void back(View v) {
        finish();
    }
}
