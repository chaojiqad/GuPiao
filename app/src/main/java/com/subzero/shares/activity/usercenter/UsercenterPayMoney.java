package com.subzero.shares.activity.usercenter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.subzero.shares.Interface.Succes;
import com.subzero.shares.R;
import com.subzero.shares.activity.BaseActivity;
import com.subzero.shares.alipay.AlipayUtils;
import com.subzero.shares.alipay.PayResult;
import com.subzero.shares.alipay.SignUtils;
import com.subzero.shares.utils.SharedPreferencesUtils;

import java.util.ArrayList;

/**
 * 充值
 * Created by zzy on 2016/4/26.
 */
public class UsercenterPayMoney extends BaseActivity implements View.OnClickListener {

    private String TITLE = "充值";
    private TextView titleText;
    private ImageView shareBar;

    private RadioButton radioBtn1;
    private RadioButton radioBtn2;
    private ArrayList<RadioButton> radioBtns;

    /************
     * alipay--start
     *****************/
    private static final int SDK_PAY_FLAG = 1;

    private static Succes mSucces;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        mSucces.onSucces(2000);
                        finish();
                        Toast.makeText(UsercenterPayMoney.this, "支付成功", Toast.LENGTH_SHORT).show();


                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(UsercenterPayMoney.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(UsercenterPayMoney.this, "支付失败", Toast.LENGTH_SHORT).show();


                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
    private TextView mPay;
    private EditText mPrice;

    @Override
    protected void initView() {
        setContentView(R.layout.user_center_paymoney);

        titleText = (TextView) findViewById(R.id.textTitle);
        shareBar = (ImageView) findViewById(R.id.share_communicate);

        radioBtn1 = (RadioButton) findViewById(R.id.rd1_pay_newsdetail_thrend);
        radioBtn2 = (RadioButton) findViewById(R.id.rd2_pay_newsdetail_thrend);
        mPrice = (EditText) findViewById(R.id.et_ac_pay_money_price);
        mPay = (TextView) findViewById(R.id.tv_ensure_pay);
        radioBtn1.setChecked(true);

        radioBtns = new ArrayList<>();


        radioBtns.add(radioBtn1);
        radioBtns.add(radioBtn2);
    }

    @Override
    protected void initData() {
        titleText.setText(TITLE);
        shareBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initListener() {
        mPay.setOnClickListener(this);
        for (RadioButton btn : radioBtns) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchState(v.getId());
                }
            });
        }

    }

    //切换支付选项按钮的状态
    private void switchState(int id) {
        switch (id) {
            case R.id.rd1_pay_newsdetail_thrend:
                radioBtn2.setChecked(false);
                break;
            case R.id.rd2_pay_newsdetail_thrend:
                radioBtn1.setChecked(false);
                break;
        }
    }

    /**
     * 支付宝支付  (虚拟币充值)
     *
     * @param orderName
     * @param orderDesc
     * @param price
     */
    private void payForAliay(String orderName, String orderDesc, String price) {

        if (TextUtils.isEmpty(AlipayUtils.PARTNER) || TextUtils.isEmpty(AlipayUtils.RSA_PRIVATE) || TextUtils.isEmpty(AlipayUtils.SELLER)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }
        String fl = "_";
        String orderInfo = AlipayUtils.getOrderInfo(orderName, orderDesc, price, fl + 1 + fl + SharedPreferencesUtils.getUserId(this));

        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = SignUtils.sign(orderInfo, AlipayUtils.RSA_PRIVATE);
        /**
         * 编码操作
         */
        sign = AlipayUtils.encode(sign);

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + AlipayUtils.getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(UsercenterPayMoney.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public void back(View v) {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_ensure_pay:
                if (radioBtn1.isChecked()) {
                    payForAliay("虚拟币充值", "虚拟币充值", mPrice.getText().toString().trim());
                } else {
                    payForWXZF();
                }
                break;
        }

    }

    public static void setSucces(Succes succes) {
        mSucces = succes;
    }

    private void payForWXZF() {
        Toast.makeText(UsercenterPayMoney.this, "微信支付", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
