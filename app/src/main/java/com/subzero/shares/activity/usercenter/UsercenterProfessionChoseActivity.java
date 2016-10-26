package com.subzero.shares.activity.usercenter;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.subzero.shares.R;
import com.subzero.shares.activity.BaseActivity;
import com.subzero.shares.activity.MainActivity;

import java.util.ArrayList;

/**
 * 行业选择
 * <p/>
 * Created by zzy on 2016/4/27.
 */
public class UsercenterProfessionChoseActivity extends BaseActivity implements View.OnClickListener {

    private String TITLE = "行业选择";
    private TextView titleText;
    private ImageView shareBar;

    private ArrayList<TextView> textViews;
    private int[] ids = {R.id.tv_0_0, R.id.tv_0_1, R.id.tv_0_2, R.id.tv_0_3,
            R.id.tv_1_0, R.id.tv_1_1, R.id.tv_1_2,
            R.id.tv_2_0, R.id.tv_2_1, R.id.tv_2_2,
            R.id.tv_3_0, R.id.tv_3_1, R.id.tv_3_2, R.id.tv_3_3, R.id.tv_3_4,
            R.id.tv_4_0, R.id.tv_4_1, R.id.tv_4_2, R.id.tv_4_3, R.id.tv_4_4, R.id.tv_4_5, R.id.tv_4_6,
            R.id.tv_5_0, R.id.tv_5_1, R.id.tv_5_2, R.id.tv_5_3, R.id.tv_5_4,
            R.id.tv_6_0, R.id.tv_6_1, R.id.tv_6_2, R.id.tv_6_3, R.id.tv_6_4, R.id.tv_6_5, R.id.tv_6_6, R.id.tv_6_7,
            R.id.tv_7_0, R.id.tv_7_1, R.id.tv_7_2,
            R.id.tv_8_0, R.id.tv_8_1, R.id.tv_8_2,
            R.id.tv_9_0, R.id.tv_9_1, R.id.tv_9_2,
            R.id.tv_10_0, R.id.tv_10_1, R.id.tv_10_2, R.id.tv_10_3, R.id.tv_10_4,
            R.id.tv_11_0, R.id.tv_11_1, R.id.tv_11_2};

    @Override
    protected void initView() {
        setContentView(R.layout.user_ccenter_choseprofession);

        textViews = new ArrayList<TextView>();
        titleText = (TextView) findViewById(R.id.textTitle);
        shareBar = (ImageView) findViewById(R.id.share_communicate);

        for (int i = 0; i < ids.length; i++) {
            TextView textView;
            textView = (TextView) findViewById(ids[i]);
            textViews.add(textView);
        }

    }

    @Override
    protected void initData() {
        titleText.setText(TITLE);
        shareBar.setVisibility(View.INVISIBLE);

    }

    @Override
    protected void initListener() {
        for (int i = 0; i < ids.length; i++) {
            textViews.get(i).setOnClickListener(this);
        }
    }

    public void back(View v) {
        finish();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(UsercenterProfessionChoseActivity.this,
                MainActivity.class);
        TextView city = (TextView) v;
        intent.putExtra("profession", city.getText().toString().trim());
        setResult(RESULT_OK, intent);
        finish();
    }
}
