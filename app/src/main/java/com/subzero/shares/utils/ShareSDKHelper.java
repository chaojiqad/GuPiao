package com.subzero.shares.utils;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.subzero.shares.Interface.Succes;
import com.subzero.shares.R;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by xzf on 2016/5/17.
 */
public class ShareSDKHelper {

    private String shareImgUrl;
    private String titleUrl;
    private String url;
    private String siteUrl;
    private Succes succes;

    private Activity context;


    private static final String TAG = "ShareSDKHelper";
    private static final int SHARECODE = 1008;

    public ShareSDKHelper(Activity context, String shareImgUrl, String titleUrl, String url, String siteUrl, Succes succes) {
        this.context = context;
        this.shareImgUrl = shareImgUrl;
        this.titleUrl = titleUrl;
        this.url = url;
        this.siteUrl = siteUrl;
        this.succes = succes;
    }


    /**
     * 分享的代码
     */
    public void showShare() {
        ShareSDK.initSDK(context);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        /*************************/
        oks.setTitle(context.getString(R.string.app_name));
        oks.setTitleUrl(titleUrl);
        oks.setText("我在【" + context.getString(R.string.app_name) + "】看到这篇文章，很赞的！\n");
        oks.setUrl(url);
        oks.setComment("我是测试评论文本");
        oks.setSite(context.getString(R.string.app_name));
        oks.setImageUrl(shareImgUrl);
        oks.setSiteUrl(siteUrl);
        oks.setCallback(new MyPlatformActionListener());
        /*************************/
        oks.show(context);
    }

    private final class MyPlatformActionListener implements PlatformActionListener {


        @Override
        public void onCancel(Platform platform, int action) {
            Log.e(TAG, "取消分享");
            Toast.makeText(context, "分享失败，打开客户端试试吧", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onComplete(Platform platform, int action, java.util.HashMap<java.lang.String, java.lang.Object> res) {

            Log.e(TAG, "分享完成");
            Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
            if(succes!=null){
                succes.onSucces(SHARECODE);
            }


        }

        @Override
        public void onError(Platform platform, int action, java.lang.Throwable t) {
            Toast.makeText(context, "分享失败", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "分享出错: action = " + action + " t = " + t);

        }
    }
}
