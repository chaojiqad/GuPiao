package com.subzero.shares.view;


import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

/**
 * 倒计时按钮
 *
 * @author xzf
 */
public class TimeDownButton extends Button {


    private OnClickTimeDownButtonListener buttonListener;

    private int countTime;

    public TimeDownButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        countTime = 60000;
        init();
    }

    public TimeDownButton(Context context) {
        this(context, null);
    }


    private void init() {

        TimeDownButton.this.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (buttonListener != null) {
                    buttonListener.onClick();
                }

            }
        });

    }

    /**
     * 设置按钮的总时间，默认为60000毫秒
     *
     * @param countTime
     */
    public void setButtonCountTime(int countTime) {
        this.countTime = countTime;
    }


    public interface OnClickTimeDownButtonListener {
        void onClick();
    }

    /**
     * 开启按钮不可用状态
     */
    public void startTimeDown() {
        new YZMButtonTimer(countTime, 1000).start();
    }

    /**
     * 设置按钮的回调事件
     *
     * @param buttonListener
     */
    public void setOnClickTimeDownButtonListener(OnClickTimeDownButtonListener buttonListener) {
        this.buttonListener = buttonListener;
    }

    private class YZMButtonTimer extends CountDownTimer {

        public YZMButtonTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            TimeDownButton.this.setText("获取验证码");
            TimeDownButton.this.setClickable(true);
        }

        @Override
        public void onTick(long arg0) {
            TimeDownButton.this.setTextSize(12);
            TimeDownButton.this.setText(arg0 / 1000 + "秒之后重新获取");
            TimeDownButton.this.setClickable(false);
        }

    }

}
