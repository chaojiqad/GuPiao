package com.subzero.shares.activity.usercenter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
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
 * 开通会员
 * Created by zzy on 2016/4/25.
 */
public class UsercenterOpenVipActivity extends BaseActivity {

    private String TITLE = "开通会员";
    private TextView titleText;
    private ImageView shareBar;
    private TextView mealtitleText;
    private GridView gridView;
    private ArrayList<View> views;
    private TextView time;
    private TextView money;
    private int i = 4;
    private int flag;
    private RadioButton radioBtn1;
    private RadioButton radioBtn2;
    private ArrayList<RadioButton> radioBtns;
    private String[] moneyValue1 = new String[4];
    private String[] timeValue = {"一个月", "半年", "三个月", "一年"};
    private String[] moneyValue2 = new String[4];

    private int levelId;

    private String timefiled = "onemonth";
    /**
     * 充值的金额
     */
    private String mPayMoney;


    public static final String TAG = "Usercenter";


    private static Succes mSucces;

    /************
     * alipay--start
     *****************/
    private static final int SDK_PAY_FLAG = 1;

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
                        if (mSucces != null) {
                            mSucces.onSucces(50000);
                        }

                        Toast.makeText(UsercenterOpenVipActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        finish();

                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(UsercenterOpenVipActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(UsercenterOpenVipActivity.this, "支付失败", Toast.LENGTH_SHORT).show();


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

    /************
     * alipay--end
     *****************/

    @Override
    protected void initView() {
        setContentView(R.layout.user_center_openvip);

        gridView = (GridView) findViewById(R.id.gridview_usercenter);
        titleText = (TextView) findViewById(R.id.textTitle);
        mPay = (TextView) findViewById(R.id.tv_ensure_pay);
        shareBar = (ImageView) findViewById(R.id.share_communicate);
        mealtitleText = (TextView) findViewById(R.id.tv_openvip_texttitle);
        radioBtn1 = (RadioButton) findViewById(R.id.rd1_pay_newsdetail_thrend);
        radioBtn2 = (RadioButton) findViewById(R.id.rd2_pay_newsdetail_thrend);
        radioBtn1.setChecked(true);

        radioBtns = new ArrayList<>();
        views = new ArrayList<>();

        radioBtns.add(radioBtn1);
        radioBtns.add(radioBtn2);
    }

    @Override
    protected void initData() {

        levelId = Integer.valueOf(application.levelId.get(0));

        titleText.setText(TITLE);
        shareBar.setVisibility(View.INVISIBLE);

        i = getIntent().getIntExtra("flag", 4);
        if (i == 1) {
            i = 2;
        } else if (i == 2) {
            i = 1;
        }
        flag = getIntent().getFlags();
        if (flag == 1) {
            mealtitleText.setText("您将开通/升级黄金会员");
            levelId = Integer.valueOf(application.levelId.get(1));
            Log.e(TAG, "等级id" + levelId);
        } else if (flag == 2) {
            mealtitleText.setText("您将开通/升级铂金会员");
            levelId = Integer.valueOf(application.levelId.get(2));
            Log.e(TAG, "等级id" + levelId);
        }
        if (i != 4) {
            titleText.setText("续费缴费");
        }

        if (application.mPayList != null && application.mPayList.size() > 0) {
            for (int i = 0; i < application.mPayList.size(); i++) {
                if (i == 1) {

                    moneyValue1[0] = application.mPayList.get(i).get("onemonth");
                    moneyValue1[1] = application.mPayList.get(i).get("halfyear");
                    moneyValue1[2] = application.mPayList.get(i).get("threemonth");
                    moneyValue1[3] = application.mPayList.get(i).get("oneyear");

                } else if (i == 2) {
                    moneyValue2[0] = application.mPayList.get(i).get("onemonth");
                    moneyValue2[1] = application.mPayList.get(i).get("halfyear");
                    moneyValue2[2] = application.mPayList.get(i).get("threemonth");
                    moneyValue2[3] = application.mPayList.get(i).get("oneyear");
                }
            }
        }


    }

    @Override
    protected void initListener() {
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView.setAdapter(new MyGridViewAdapter());
        for (RadioButton btn : radioBtns) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchState(v.getId());
                }
            });
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                for (View v : views) {
                    time = (TextView) v.findViewById(R.id.tv_time_usercenter);
                    money = (TextView) v.findViewById(R.id.tv_money_usercenter);
                    v.setBackgroundResource(R.drawable.bg_pay_moneyvalue_unchecked_newsdetail_thrend);
                    time.setTextColor(getResources().getColor(R.color.gray_dark_bg));
                    money.setTextColor(getResources().getColor(R.color.paymoney_unchecked));
                }
                time = (TextView) view.findViewById(R.id.tv_time_usercenter);
                money = (TextView) view.findViewById(R.id.tv_money_usercenter);
                view.setBackgroundResource(R.drawable.bg_pay_moneyvalue_checked_newsdetail_thrend);
                time.setTextColor(getResources().getColor(R.color.paymoney_checked));
                money.setTextColor(getResources().getColor(R.color.paymoney_checked));


                if (flag == 1) {
                    mPayMoney = moneyValue1[position];

                } else if (flag == 2) {
                    mPayMoney = moneyValue2[position];
                }

                if (position == 0) {
                    timefiled = "onemonth";
                } else if (position == 2) {
                    timefiled = "threemonth";
                } else if (position == 1) {
                    timefiled = "halfyear";
                } else if (position == 3) {
                    timefiled = "oneyear";
                }

                Log.e(TAG, "金额为" + mPayMoney);
            }

        });

        mPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioBtn1.isChecked()) {
                    payForAliay("会员充值", flag == 1 ? "黄金会员充值" : "铂金会员充值", mPayMoney.toString().trim());
                } else {
                    payForWXZF();
                }

            }
        });
    }

    public static void setSucces(Succes succes) {
        mSucces = succes;

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

    private void payForWXZF() {

        Toast.makeText(UsercenterOpenVipActivity.this, "微信支付", Toast.LENGTH_SHORT).show();


    }


    /**
     * 支付宝支付
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
        String orderInfo = AlipayUtils.getOrderInfo(orderName, orderDesc, price, fl + 2 + fl + SharedPreferencesUtils.getUserId(this) + fl + levelId + fl + timefiled);

        Log.e(TAG, "订单号" + fl + 2 + fl + SharedPreferencesUtils.getUserId(this) + fl + levelId + fl + timefiled);

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
                PayTask alipay = new PayTask(UsercenterOpenVipActivity.this);
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


    private class MyGridViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = View.inflate(getApplicationContext(), R.layout.item_gridview_usercenter, null);
            convertView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            time = (TextView) convertView.findViewById(R.id.tv_time_usercenter);
            money = (TextView) convertView.findViewById(R.id.tv_money_usercenter);
            //为控件赋值
            if (flag == 1) {
                money.setText(moneyValue1[position]);
            } else if (flag == 2) {
                money.setText(moneyValue2[position]);
            }
            time.setText(timeValue[position]);
            //为控件赋值颜色
            if (position == 0 && i == 4) {
                convertView.setBackgroundResource(R.drawable.bg_pay_moneyvalue_checked_newsdetail_thrend);
                time.setTextColor(getResources().getColor(R.color.paymoney_checked));
                money.setTextColor(getResources().getColor(R.color.paymoney_checked));
                if (flag == 1) {
                    mPayMoney = moneyValue1[position];

                } else if (flag == 2) {
                    mPayMoney = moneyValue2[position];
                }
            } else if (i == position) {
                convertView.setBackgroundResource(R.drawable.bg_pay_moneyvalue_checked_newsdetail_thrend);
                time.setTextColor(getResources().getColor(R.color.paymoney_checked));
                money.setTextColor(getResources().getColor(R.color.paymoney_checked));
                if (flag == 1) {
                    mPayMoney = moneyValue1[position];

                } else if (flag == 2) {
                    mPayMoney = moneyValue2[position];
                }
                if (position == 0) {
                    timefiled = "onemonth";
                } else if (position == 2) {
                    timefiled = "threemonth";
                } else if (position == 1) {
                    timefiled = "halfyear";
                } else if (position == 3) {
                    timefiled = "oneyear";
                }
            } else {
                convertView.setBackgroundResource(R.drawable.bg_pay_moneyvalue_unchecked_newsdetail_thrend);
                time.setTextColor(getResources().getColor(R.color.gray_dark_bg));
                money.setTextColor(getResources().getColor(R.color.paymoney_unchecked));
            }


            Log.e(TAG, "金额为" + mPayMoney);

            views.add(convertView);
            return convertView;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    public void back(View v) {
        finish();
    }
}
