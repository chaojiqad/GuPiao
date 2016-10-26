package com.subzero.shares.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.subzero.shares.activity.LoginActivity;
import com.subzero.shares.application.MyApplication;
import com.subzero.shares.utils.ActivityCollector;

/**
 * 所有的fragment的基类
 * Created by xzf on 2016/4/5.
 */
public abstract class BaseFragment extends Fragment implements View.OnTouchListener {

    MyApplication application;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        application = (MyApplication) getActivity().getApplication();

        View v = initView();

        if (v == null) return new TextView(application);

        v.setOnTouchListener(this);

        return v;
    }

    /**
     * 防止事件穿透
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    protected abstract View initView();


    /**
     * 结束所有的Activity并打开登录页面  一般情况下tokenID失效调用的
     */
    protected void startLoginActivity() {
        //结束所有的activity
        ActivityCollector.finishAll();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);


    }

    /**
     * 显示图片
     *
     * @param iv
     * @param avatarUrl
     */
    protected void displayAvatar(ImageView iv, String avatarUrl) {

        Picasso.with(getActivity()).load(avatarUrl).into(iv);

    }


    /**
     * 显示对话框   简单的提示作用   按钮本身没有任何逻辑处理
     *
     * @param content
     */
    protected void showAlertDialog(String content) {
        AlertDialog.Builder dialog = new AlertDialog.Builder
                (getActivity());
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
}
