package com.hbung.superwebview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hbung.flatbutton.FlatButton;

/**
 * 作者　　: 李坤
 * 创建时间:2017/6/7　15:57
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */
public class SuperWebView extends FrameLayout implements XWebView.IWebViewListener {
    private RelativeLayout errorRl;
    private HorizontalProgress progressBar;
    private XWebView webView;
    private TextView swvMessageView;
    private ImageView swvImageView;
    private FlatButton actionButton;


    private XWebView.IWebViewListener listener;

    public SuperWebView(Context context) {
        this(context, null);
    }

    public SuperWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.super_webview, this);
        errorRl = (RelativeLayout) findViewById(R.id.swvErrorRl);
        progressBar = (HorizontalProgress) findViewById(R.id.swvProgressBar);
        webView = (XWebView) findViewById(R.id.swvXwebView);
        swvMessageView = (TextView) findViewById(R.id.swvMessageView);
        swvImageView = (ImageView) findViewById(R.id.swvImageView);
        actionButton = (FlatButton) findViewById(R.id.actionButton);
        webView.setListener(this);
        actionButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                reload();
            }
        });
    }


    public void loadUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            webView.loadUrl(url);
        }
    }


    public RelativeLayout getErrorRoot() {
        return errorRl;
    }

    public HorizontalProgress getProgressBar() {
        return progressBar;
    }

    public XWebView getWebView() {
        return webView;
    }

    public TextView getMessageView() {
        return swvMessageView;
    }

    public ImageView getImageView() {
        return swvImageView;
    }

    public FlatButton getActionButton() {
        return actionButton;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/7 16:07
     * <p>
     * 方法功能：设置加载进度
     *
     * @param progress >= 100 进度条隐藏
     */

    public void setProgress(int progress) {
        if (progressBar != null) {
            progressBar.setVisibility(progress < 100 ? VISIBLE : GONE);
            progressBar.setProgress(progress);
        }
    }

    public void setListener(XWebView.IWebViewListener listener) {
        this.listener = listener;
    }

    public void setButtonClick(OnClickListener click) {
        actionButton.setOnClickListener(click);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (listener != null) {
            return listener.shouldOverrideUrlLoading(view, url);
        }
        if (webView != null) {
            webView.loadUrl(url);
        }
        return true;
    }

    @Override
    public void onError(WebView view, XWebView.ErrorInfo errorInfo) {
        if (webView != null) {
            webView.setVisibility(GONE);
            errorRl.setVisibility(VISIBLE);
            progressBar.setVisibility(GONE);
        }
        //加载错误
        if (listener != null) {
            listener.onError(view, errorInfo);
        }
    }

    @Override
    public void onPageFinished(WebView view, String url, boolean isSuccess) {
        //加载完成
        if (listener != null) {
            listener.onPageFinished(view, url, isSuccess);
        }
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        if (webView != null) {
            webView.setVisibility(VISIBLE);
            progressBar.setVisibility(VISIBLE);
            errorRl.setVisibility(GONE);
        }
        //加载开始
        if (listener != null) {
            listener.onPageStarted(view, url, favicon);
        }
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        if (listener != null) {
            listener.onReceivedTitle(view, title);
        }
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        setProgress(newProgress);
        //进度改变
        if (listener != null) {
            listener.onProgressChanged(view, newProgress);
        }
    }

    public void onResume() {
        webView.onResume();
    }

    public void onPause() {
        webView.onPause();
    }

    public void destroy() {
        webView.destroy();
    }

    public boolean canGoBack() {
        return webView.canGoBack();
    }

    public void goBack() {
        webView.goBack();
    }


    public void reload() {
        webView.reload();
    }

    public WebView.HitTestResult getHitTestResult() {
        return webView.getHitTestResult();
    }

    @SuppressLint("JavascriptInterface")
    public void addJavascriptInterface(Object object, String name) {
        webView.addJavascriptInterface(object, name);
    }

    public String getUrl() {
        return webView.getUrl();
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/7 14:51
     * <p>
     * 方法功能：调用js方法
     *
     * @param methodName 方法名
     * @param params     参数
     */
    public void sendToJs(String methodName, Object... params) {
        webView.sendToJs(methodName, params);
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/19 16:08
     * <p>
     * 方法功能：主动调用js  让js调用java   一般用于获取网页信息
     * JavaScript:window.aaaaa.assign('img://'+ document.getElementsByTagName('img').toString())
     */
    public void sendToJava(XWebView.BridgeParams bridgeParams) {
        webView.sendToJava(bridgeParams);
    }
}
