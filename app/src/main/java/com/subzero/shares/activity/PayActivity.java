package com.subzero.shares.activity;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.subzero.shares.config.Constants;
import com.subzero.shares.config.WebUrl;
import com.subzero.shares.utils.Util;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class PayActivity extends BaseActivity {

    private IWXAPI api;

    @Override
    protected void initView() {
//		setContentView(R.layout.pay);
        api = WXAPIFactory.createWXAPI(this, null);
        api.registerApp(Constants.APP_ID);
//		Button appayBtn = (Button) findViewById(R.id.appay_btn);
//		appayBtn.setOnClickListener(new View.OnClickListener() {

//			@Override
//			public void onClick(View v) {
//				String url = "http://wxpay.weixin.qq.com/pub_v2/app/app_pay.php?plat=android";
//				Button payBtn = (Button) findViewById(R.id.appay_btn);
//				payBtn.setEnabled(false);
        Toast.makeText(PayActivity.this, "获取订单中...", Toast.LENGTH_SHORT).show();
        RequestParams params = new RequestParams(WebUrl.url);
        params.addBodyParameter("token", "");
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("get server pay params:", result);
                try {
                    JSONObject jo = new JSONObject(result);
                    String retCode = jo.getString("retCode");
                    String message = jo.getString("message");
                    if ("-1".equals(retCode)) {
                        Toast.makeText(PayActivity.this, message, Toast.LENGTH_SHORT).show();
                        return;

                    } else if ("-2".equals(retCode)) {
                        Toast.makeText(PayActivity.this, message, Toast.LENGTH_SHORT).show();
                        startLoginActivity();
                        return;

                    }
                    JSONObject jb=jo.getJSONObject("data");
                    PayReq req = new PayReq();
                    //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                    req.appId = jb.getString("appid");
                    req.partnerId = jb.getString("partnerid");
                    req.prepayId = jb.getString("prepayid");
                    req.nonceStr = jb.getString("noncestr");
                    req.timeStamp = jb.getString("timestamp");
                    req.packageValue = jb.getString("package");
                    req.sign = jb.getString("sign");
                    req.extData = "app data"; // optional
                    Toast.makeText(PayActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                    api.sendReq(req);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

//        try {
//            byte[] buf = Util.httpGet(WebUrl.url);
//            if (buf != null && buf.length > 0) {
//                String content = new String(buf);
//                Log.e("get server pay params:", content);
//                JSONObject json = new JSONObject(content);
//                if (null != json && !json.has("retcode")) {
//                    PayReq req = new PayReq();
//                    //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
//                    req.appId = json.getString("appid");
//                    req.partnerId = json.getString("partnerid");
//                    req.prepayId = json.getString("prepayid");
//                    req.nonceStr = json.getString("noncestr");
//                    req.timeStamp = json.getString("timestamp");
//                    req.packageValue = json.getString("package");
//                    req.sign = json.getString("sign");
//                    req.extData = "app data"; // optional
//                    Toast.makeText(PayActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
//                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
//                    api.sendReq(req);
//                } else {
//                    Log.d("PAY_GET", "返回错误" + json.getString("retmsg"));
//                    Toast.makeText(PayActivity.this, "返回错误" + json.getString("retmsg"), Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                Log.d("PAY_GET", "服务器请求错误");
//                Toast.makeText(PayActivity.this, "服务器请求错误", Toast.LENGTH_SHORT).show();
//            }
//        } catch (Exception e) {
//            Log.e("PAY_GET", "异常：" + e.getMessage());
//            Toast.makeText(PayActivity.this, "异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//				payBtn.setEnabled(true);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }
//		});
//		Button checkPayBtn = (Button) findViewById(R.id.check_pay_btn);
//		checkPayBtn.setOnClickListener(new View.OnClickListener() {

//			@Override
//			public void onClick(View v) {
//				boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
//				Toast.makeText(PayActivity.this, String.valueOf(isPaySupported), Toast.LENGTH_SHORT).show();
//			}
//		});
//	}

}
