package com.subzero.shares.activity.communicate;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.subzero.shares.R;
import com.subzero.shares.activity.BaseActivity;
import com.subzero.shares.bean.Rules;
import com.subzero.shares.config.WebUrl;
import com.subzero.shares.utils.ImageUtils;

import java.util.ArrayList;

/**
 * 参赛规则详情
 * Created by zzy on 2016/4/26.
 */
public class GameDetalActivity extends BaseActivity implements View.OnClickListener {

    private String TITLE = "参赛留言";
    private TextView titleText;
    private ImageView shareBar;
    private ArrayList<ImageView> imgs;
    private int[] ids = {R.id.iv_game_detail_img1, R.id.iv_game_detail_img2, R.id.iv_game_detail_img3};
    private Rules rules;
    private TextView mSummary;
    private TextView mSource;
    private TextView mTime;
    private TextView mDetailContent;

    @Override
    protected void initView() {
        setContentView(R.layout.leave_message_communicate);

        titleText = (TextView) findViewById(R.id.textTitle);
        shareBar = (ImageView) findViewById(R.id.ibtn_share);

        mSummary = (TextView) findViewById(R.id.summary);
        mTime = (TextView) findViewById(R.id.time);
        mSource = (TextView) findViewById(R.id.source);
        mDetailContent = (TextView) findViewById(R.id.tv_game_detailcontent);


    }

    @Override
    protected void initData() {
        titleText.setText(TITLE);
        shareBar.setVisibility(View.INVISIBLE);
        Bundle bundle = getIntent().getExtras();
        rules = (Rules) bundle.getSerializable("rules");


        imgs = new ArrayList<>();

        if (rules != null) {
            for (int i = 0; i < rules.getImages().length; i++) {
                imgs.add((ImageView) findViewById(ids[i]));
            }

            mSummary.setText(rules.getTitle());
            mTime.setText(rules.getTime());
            mSource.setText(rules.getWebsite());
            mDetailContent.setText(rules.getMessage());
            for (int i = 0; i < rules.getImages().length; i++) {
                displayAvatar(imgs.get(i), rules.getImages()[i]);
            }
        }


    }

    @Override
    protected void initListener() {

        for (int i = 0; i < rules.getImages().length; i++) {
            imgs.get(i).setOnClickListener(this);
        }

    }

    public void back(View v) {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_game_detail_img1:
                if (!rules.getImages()[0].equals(WebUrl.FILEHOST + "null")) {

                    ImageUtils.startPhotoActivity(this, rules.getImages()[0], imgs.get(0));
                }
                break;

            case R.id.iv_game_detail_img2:
                if (!rules.getImages()[1].equals(WebUrl.FILEHOST + "null")) {
                    ImageUtils.startPhotoActivity(this, rules.getImages()[1], imgs.get(1));
                }
                break;

            case R.id.iv_game_detail_img3:

                if (!rules.getImages()[2].equals(WebUrl.FILEHOST + "null")) {
                    ImageUtils.startPhotoActivity(this, rules.getImages()[2], imgs.get(2));
                }
                break;

        }
    }
}
