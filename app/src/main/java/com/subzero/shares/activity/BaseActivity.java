package com.subzero.shares.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.subzero.shares.application.MyApplication;
import com.subzero.shares.utils.ActivityCollector;


/**
 * 所有activity的基类
 * Created by xzf on 2016/4/15.
 */
public abstract class BaseActivity extends FragmentActivity {

     /*被继承的成员变量只能在方法里面 初始化 */
    protected MyApplication application;
    private ProgressDialog progressDialog;

    // 填写从短信SDK应用后台注册得到的APPKEY
    protected static String APPKEY = "12278167db3a0";
    // 填写从短信SDK应用后台注册得到的APPSECRET
    protected static String APPSECRET = "dd91ae4c7570af57d998ff28a7f06b77";

    private static final String TAG = "BaseActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
        initView();
        ActivityCollector.addActivity(this);
        application = (MyApplication) getApplication();
        initData();
        initListener();
    }


    @TargetApi(19)
    private void initWindow() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 初始化视图
     */

    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化事件
     */
    protected abstract void initListener();


    /**
     * 显示加载的对话框
     *
     * @param content
     */
    public void showLoadingDialog(String content) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog
                    (this);
        }
        progressDialog.setMessage(content);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    /**
     * 隐藏对话框
     */
    public void hideLoaddingDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }

    }

    /**
     * 结束所有的Activity并打开登录页面  一般情况下tokenID失效调用的
     */
    protected void startLoginActivity() {
        //结束所有的activity
        ActivityCollector.finishAll();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);


    }

    /**
     * 显示图片
     *
     * @param avatarUrl
     */
    protected void displayAvatar(ImageView iv, String avatarUrl) {
        Picasso.with(this).load(avatarUrl).into(iv);
    }

    /**
     * 显示对话框   简单的提示作用   按钮本身没有任何逻辑处理
     *
     * @param content
     */
    protected void showAlertDialog(String content) {
        AlertDialog.Builder dialog = new AlertDialog.Builder
                (this);
        dialog.setMessage(content);
        dialog.setCancelable(false);
        dialog.setNegativeButton("知道了", new DialogInterface.
                OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }


    /**
     * 显示加载的对话框不能取消
     *
     * @param content
     */
    public void showLoadingDialogForNotCancel(String content) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog
                    (this);
        }
        progressDialog.setMessage(content);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }


}
