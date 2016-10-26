package com.subzero.shares.activity.newsdetail;

import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.subzero.shares.R;
import com.subzero.shares.activity.BaseActivity;

/**
 * 开户网站
 * Created by zzy on 5/20/2016.
 */
public class WebOpenCountActivity extends BaseActivity {
    private TextView title;
    private ImageView shareBar;
    private WebView webView;
    private String url = "http://www.baidu.com/";

    @Override
    protected void initView() {
        setContentView(R.layout.open_count_thrend);
        title = (TextView) findViewById(R.id.textTitle);
        shareBar = (ImageView) findViewById(R.id.share_communicate);
        webView = (WebView) findViewById(R.id.wv_opencount);
    }

    @Override
    protected void initData() {
        title.setText("开户");
        shareBar.setVisibility(View.INVISIBLE);
        url=getIntent().getStringExtra("link");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl(url);
    }

    @Override
    protected void initListener() {

    }

    public void back(View view) {
        finish();
    }

    /**
     * 监听返回事件
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
