package com.subzero.shares.alipay;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by xzf on 2016/5/26.
 */
public class AlipayUtils {


    // 商户PID
    //public static final String PARTNER = "2088221951306319";
    public static final String PARTNER = "2088421566113223";
    // 商户收款账号
    //  public static final String SELLER = "2865338540@qq.com";
    public static final String SELLER = "mirandanzld@gmail.com";

    // 商户私钥，pkcs8格式
    // public static final String RSA_PRIVATE = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAOCTYYINJjJk2iH0HlAnNCEos+yCwdhMA8iKx9IZ4mz+2Z273l7yeMim3616LVpAH9ipcjb33iAzOk1ssXXEtNgD8zW6jEomJK9D2ozk567CxVJX3DvYNNgRe6P3+GDD8Wz0f5Wr2E8/HUoRo3s5d6tAne2aXLSof5yID6iCr7ALAgMBAAECgYAh86YmNz6zF7QytelvTL4A1Vp+QzZzMOrRgsm65dbpi43y3tg7moTQMQpX+8SnhFJMOzHdRZP7YkNKOUiK+XYtecl+jJDpl44g+oJx7RsreYp1wkVEi+ZRNIe4HPcYBmGOfiwdNRcMC533DvHT9QsagvnSW6CvYA9UDg06py18AQJBAPNCbCF1UE5WiCbrlNwrD9xGiIZlojlYyB16rpwHRotnMCbnGLDr1XPRtm/G30+NVfyI85aq20yIS9uXg8CP1YsCQQDsVnM/MD27fo8RRQyCWjc4m/ONlkGJFPrmO/SDwJr1N72wR68S/3aFe0Fx4lJbazkZzYbYGvuMkw/WSxB9s9+BAkBrjI9gD4zsxac2hetvl/5QJkMQIHkjn5QCLI5sX28aDQol+QBaoDnkFfDx5FwwwM0mdKo1j96bLJtVP9y5Ux5XAkAWGFL38lujHsbjV3A+8448FaBkeUqxNSvrwGkwtB4IlOiABYOhpA6Bli9dJUqLz29sMSHNLGCeGNWfNC9bgSiBAkBG9CTJsgIvx2HVpl26GySklyfqAVEU68uu6/ocgEdcM4EQ+QckYfeKAEuCBRFDOcq5ZzPZpEHBszPAtq0J2Vc1";
    public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKbOK/i/JSnZkoXJZ+65x6pcq3NUmlO8joOUfh0hGZP8dNR2qXWN1qL5U+WWjB6bSl3EAQs/Lu5meoYYeQ5N8STey3LPM+cGbaELVW2ece2XYIH7GN6hHUG6Oe6pLC/I2F7IcvEK4MBnpmWVI0EtXFvQLA7TQjBEsgHjKOq8+8RlAgMBAAECgYEAiqHnocEbXgDZ6h9OJfZ5n3mNdTjnWpjnySvs9MDBxWCYqMxGYtEThje6JqJMqlNeJzfztik5FCJPyoskcWMN+sLKoHmTD/iuBoE4cIT+zLrtzjpu5YK4tvAOgHNVX5IDlRLa843bwk1yG/5iC2SFhuTK/y20eYq0UVBXGHTwuIECQQDZtu0aggbCaJXdqc5hvkC7IE/hLPUqozM+KEyGxcrTOLonahfh29/peu3nHMuhtRUwmelwc9/FiMlyNBBdnhUZAkEAxCNqVQE+FD1MFGOqh3kHozfmQsTKE/REjMZ8u4SEZV/9j353/3SWAGB6EKjO0Ja8DmNfJ7gT2ggYQKNx9P1nLQJAG98lP42B3AWTmgsRFsZKdypNz5/3yvuVpdF26pILwzqx6jXU1N9XLQbFyh6PmyYZjl1F21WaQime0HYF2lZQkQJADwkn/Zw6xhJs4qNBT2FX1ItUe4nac+0l1CTjKvzdqWfSaE2ppUuqtByxPtWyBQuJHxE/GixiWTSFp27gLYe/gQJAflTMoR8+bgA26oPEwBht4O6nReUPdHee/54n9RpVOL6NpfxSc4WKl6t1+DyB30rWKUOYsBTBT/e8SXTyAnWy7A==";
    private static final String TAG = "AlipayUtils";

    /**
     * create the order info. 创建订单信息
     */
    public static String getOrderInfo(String subject, String body, String price, String parameter) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo(parameter) + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + 0.01 + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://123.56.82.112/gupiao/index.php/Api/Apipay/pay1" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    private static String getOutTradeNo(String parameter) {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);
        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);

        Log.e(TAG, "订单号码是：" + key + parameter);
        return key + parameter;
    }

    public static String encode(String sign) {
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sign;
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    public static String getSignType() {
        return "sign_type=\"RSA\"";
    }


}
