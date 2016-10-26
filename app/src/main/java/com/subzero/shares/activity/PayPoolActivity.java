package com.subzero.shares.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.subzero.shares.R;
import com.subzero.shares.alipay.AlipayUtils;
import com.subzero.shares.alipay.PayResult;
import com.subzero.shares.alipay.SignUtils;
import com.subzero.shares.config.WebUrl;
import com.subzero.shares.utils.SharedPreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/**
 * 购买 直播 阅读股票量化池权限 支付页面
 * Created by zzy on 2016/4/19.
 */
public class PayPoolActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "PayPoolActivity";
    //    private String[] moneyValue = {"5元", "20元", "50元", "80元", "100元", "200元"};
    private String TITLE = "支付";
    //    private GridView payGridView;
    private TextView titleText;
    private TextView pay;
    private ImageView shareBar;
    private RadioButton radioBtn1;
    private RadioButton radioBtn2;
    private RadioButton radioBtn3;
    private ArrayList<RadioButton> radioBtns;
//    private ArrayList<TextView> textViews;
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
                        finish();
                        Toast.makeText(PayPoolActivity.this, "支付成功", Toast.LENGTH_SHORT).show();


                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(PayPoolActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(PayPoolActivity.this, "支付失败", Toast.LENGTH_SHORT).show();


                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };
    private String money;
    private String postid;
    private String type;

    @Override
    protected void initView() {
        setContentView(R.layout.pay_pool_optional);

//        payGridView = (GridView) findViewById(R.id.gv_pay_newsdetail_thrend);
        titleText = (TextView) findViewById(R.id.textTitle);
        pay = (TextView) findViewById(R.id.tv_pay);
        shareBar = (ImageView) findViewById(R.id.share_communicate);

        radioBtn1 = (RadioButton) findViewById(R.id.rd1_pay_newsdetail_thrend);
        radioBtn2 = (RadioButton) findViewById(R.id.rd2_pay_newsdetail_thrend);
        radioBtn3 = (RadioButton) findViewById(R.id.rd3_pay_newsdetail_thrend);
        radioBtn3.setChecked(true);

        radioBtns = new ArrayList<RadioButton>();
//        textViews = new ArrayList<TextView>();

        radioBtns.add(radioBtn1);
        radioBtns.add(radioBtn2);
        radioBtns.add(radioBtn3);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        money = intent.getStringExtra("price");
        postid = intent.getStringExtra("id");
        type = intent.getFlags() + "";
        titleText.setText(TITLE);
        shareBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initListener() {

        for (RadioButton btn : radioBtns) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchState(v.getId());
                }
            });
        }

        pay.setOnClickListener(this);
        //  payGridView.setAdapter(new MyGridViewAdapter());
/*        payGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView textView = (TextView) view;

                for (TextView tv : textViews) {
                    tv.setTextColor(getResources().getColor(R.color.paymoney_unchecked));
                    tv.setBackgroundResource(R.drawable.bg_pay_moneyvalue_unchecked_newsdetail_thrend);
                }
                textView.setTextColor(getResources().getColor(R.color.paymoney_checked));
                textView.setBackgroundResource(R.drawable.bg_pay_moneyvalue_checked_newsdetail_thrend);

            }
        });*/
    }

    //切换支付选项按钮的状态
    private void switchState(int id) {

        switch (id) {
            case R.id.rd1_pay_newsdetail_thrend:
                radioBtn2.setChecked(false);
                radioBtn3.setChecked(false);
                break;
            case R.id.rd2_pay_newsdetail_thrend:
                radioBtn1.setChecked(false);
                radioBtn3.setChecked(false);
                break;
            case R.id.rd3_pay_newsdetail_thrend:
                radioBtn2.setChecked(false);
                radioBtn1.setChecked(false);
                break;
        }
    }

  /*  private class MyGridViewAdapter extends BaseAdapter {

        @Override
        public int getWealth() {
            return 6;
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

            TextView textView;
            if (convertView == null) {
                textView = new TextView(getApplication());
                textView.setText(moneyValue[position]);
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(22);
                textView.setTextColor(getResources().getColor(R.color.paymoney_unchecked));
                textView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));//设置textView对象布局
                textViews.add(textView);
            } else {

                textView = (TextView) convertView;

            }

            if (position == 0) {
                textView.setBackgroundResource(R.drawable.bg_pay_moneyvalue_checked_newsdetail_thrend);
                textView.setTextColor(getResources().getColor(R.color.paymoney_checked));
            } else
                textView.setBackgroundResource(R.drawable.bg_pay_moneyvalue_unchecked_newsdetail_thrend);

            return textView;
        }
    }*/


    @Override
    public void onClick(View v) {
        for (RadioButton btn : radioBtns) {
            if (btn.isChecked()) {
                switch (btn.getId()) {
                    case R.id.rd1_pay_newsdetail_thrend:
                        payForAliay("权限付费", "权限付费", money);
                        break;
                    case R.id.rd2_pay_newsdetail_thrend:
                        Intent intent = new Intent(this, com.subzero.shares.activity.PayActivity.class);
                        intent.putExtra("money", money);
                        startActivity(intent);
                        break;
                    case R.id.rd3_pay_newsdetail_thrend:
                        payVirtualMoney();
                        break;
                }
            }

        }
    }

    //虚拟币支付
    private void payVirtualMoney() {
        pay.setEnabled(false);
        showLoadingDialogForNotCancel("正在支付....");
        String token = SharedPreferencesUtils.getTokenId(this);

//       支付虚拟币 判断支付类型
        if (type.equals("4")){
//        股票量化池
            type="2";
        }else {
//        盘中直播
            type="1";
        }
        RequestParams params = new RequestParams(WebUrl.VMONEYPAY);
        params.addBodyParameter("token", token);
        params.addBodyParameter("postid", postid);
        params.addBodyParameter("money", money);
        params.addBodyParameter("type", type);
        Log.e(TAG, "支付参数:" + token + ";" + postid + ";" + money + ";" + type);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e(TAG, "支付返回的结果:" + result);
                try {
                    JSONObject jo = new JSONObject(result);
                    String retCode = jo.getString("retCode");
                    String message = jo.getString("message");
                    if ("-1".equals(retCode)) {
                        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e(TAG, "老子报错了");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                pay.setEnabled(true);
                hideLoaddingDialog();
            }
        });
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

        String orderInfo = AlipayUtils.getOrderInfo(orderName, orderDesc, price, fl + type + fl + SharedPreferencesUtils.getUserId(this) + fl + postid);


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
                PayTask alipay = new PayTask(PayPoolActivity.this);
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
}
