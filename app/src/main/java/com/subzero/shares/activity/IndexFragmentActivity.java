package com.subzero.shares.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.subzero.shares.R;
import com.subzero.shares.activity.newsdetail.UploadActivity;
import com.subzero.shares.fragment.CommunicationFragment;
import com.subzero.shares.fragment.MyFragment;
import com.subzero.shares.fragment.OptionalMachineFragment;
import com.subzero.shares.fragment.TrendFragment;
import com.subzero.shares.utils.ActivityCollector;
import com.subzero.shares.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import io.rong.imkit.RongIM;

/**
 * 首页
 * <p>
 * 趋势：TrendFragment
 * 择机：OptionalMachineFragment
 * 交流：CommunicationFragment
 * 我的：MyFragment
 * <p>
 * Created by xzf on 2016/4/5.
 */
public class IndexFragmentActivity extends FragmentActivity {

    private static final String TAG = "IndexFragment";
    private RadioGroup mRadioGroup;

    private List<String> listFmName;
    private long mExitTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_index);

        ActivityCollector.addActivity(this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        initView();
        initData();
        initListener();

    }

    //初始化视图
    private void initView() {
        mRadioGroup = (RadioGroup) findViewById(R.id.rg_activtiy_index_foot_navi);
    }

    //初始化数据
    private void initData() {

        setJPushAlias();
        setJPushTags();



        listFmName = new ArrayList<>();
        initFragment();
    }


    //初始化监听
    private void initListener() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_activity_index_foot_navi_1:
                        switchFragment(0);
                        break;
                    case R.id.rb_activity_index_foot_navi_2:
                        switchFragment(1);
                        break;
                    case R.id.rb_activity_index_foot_navi_3:
                        switchFragment(2);
                        break;
                    case R.id.rb_activity_index_foot_navi_4:
                        switchFragment(3);
                        break;
                }
            }
        });

    }

    //初始化fragment
    private void initFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        TrendFragment trendFragment = new TrendFragment();
        listFmName.add("trendFragment");
        transaction.add(R.id.fl_body, trendFragment, "trendFragment").show(trendFragment);

        OptionalMachineFragment optionalMachineFragment = new OptionalMachineFragment();
        listFmName.add("optionalMachineFragment");
        transaction.add(R.id.fl_body, optionalMachineFragment, "optionalMachineFragment").hide(optionalMachineFragment);

        CommunicationFragment communicationFragment = new CommunicationFragment();
        listFmName.add("communicationFragment");
        transaction.add(R.id.fl_body, communicationFragment, "communicationFragment").hide(communicationFragment);

        MyFragment myFragment = new MyFragment();
        listFmName.add("myFragment");
        transaction.add(R.id.fl_body, myFragment, "myFragment").hide(myFragment);

        transaction.commit();

    }

    //切换fragment
    private void switchFragment(int index) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < listFmName.size(); i++) {
            if (i != index) {// 隐藏的
                transaction.hide(manager.findFragmentByTag(listFmName.get(i)));
            } else if (i == index) {// 显示的
                transaction.show(manager.findFragmentByTag(listFmName.get(index)));
            }
        }
        transaction.commit();
    }

    //点击Search按钮，打开UploadActivity
    public void search(View view) {

        startActivity(new Intent(this, UploadActivity.class));
    }


    /**
     * 设置极光推送的标签
     */
    private void setJPushTags() {
        Set<String> set = new HashSet<>();
        set.add(SharedPreferencesUtils.getUserId(this));
        set.add(SharedPreferencesUtils.getUserLevel(IndexFragmentActivity.this));

        Log.e(TAG, "您设置的tag-userid:" + SharedPreferencesUtils.getUserId(IndexFragmentActivity.this));
        Log.e(TAG, "您设置的tag-userlevel:" + SharedPreferencesUtils.getUserLevel(IndexFragmentActivity.this));
        //为推送设置标签
        JPushInterface.setTags(IndexFragmentActivity.this, set, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                Log.e(TAG, "结果码" + i + "^^^^^^^^s=" + s);
            }
        });

    }

    /**
     * 设置别名
     */
    private void setJPushAlias() {
        String alias = SharedPreferencesUtils.getUserType(IndexFragmentActivity.this);
        Log.e(TAG, "你的别名是:" + alias);
        JPushInterface.setAlias(IndexFragmentActivity.this, alias, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                Log.e(TAG, "结果码" + i + "^^^^^^^^s=" + s);
            }
        });
    }

    /*不保存fragment的状态   这样用来解决fragment重叠的问题*/
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
        //退出当前的登陆
        RongIM.getInstance().logout();
        Log.e(TAG,"退出当前登陆");

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {

                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();

            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
