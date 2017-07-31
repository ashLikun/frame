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
    private RelativeLayout errorRl;//错误根布局
    private HorizontalProgress progressBar;//进度条
    private XWebView webView;//webview
    private TextView swvMessageView;//消息TextView
    private ImageView swvImageView;//图标
    private FlatButton actionButton;//失败按钮

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

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/30 15:03
     * 邮箱　　：496546144@qq.com
     * <p>
     * 方法功能：加载url
     */

    public void loadUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            webView.loadUrl(url);
        }
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/30 15:04
     * 邮箱　　：496546144@qq.com
     * 方法功能：获取错误跟布局
     */
    public RelativeLayout getErrorRoot() {
        return errorRl;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/30 15:04
     * 邮箱　　：496546144@qq.com
     * 方法功能：获取进度条
     */
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

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/30 15:04
     * 邮箱　　：496546144@qq.com
     * <p>
     * 方法功能：设置webview自定义的监听
     */
    public void setListener(XWebView.IWebViewListener listener) {
        this.listener = listener;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/30 15:04
     * 邮箱　　：496546144@qq.com
     * <p>
     * 方法功能：当按钮点击时候的监听。默认内部会实现去调用reload方法刷新
     */
    public void setButtonClick(OnClickListener click) {
        actionButton.setOnClickListener(click);
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/30 15:05
     * 邮箱　　：496546144@qq.com
     * <p>
     * 方法功能：webview去加载另外一个连接，默认会在当前view加载
     */
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

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/30 15:06
     * 邮箱　　：496546144@qq.com
     * <p>
     * 方法功能：当错误的时候，默认ui切换
     */

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

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/30 15:07
     * 邮箱　　：496546144@qq.com
     * <p>
     * 方法功能：页面加载成功
     */
    @Override
    public void onPageFinished(WebView view, String url, boolean isSuccess) {
        //加载完成
        if (listener != null) {
            listener.onPageFinished(view, url, isSuccess);
        }
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/30 15:07
     * 邮箱　　：496546144@qq.com
     * <p>
     * 方法功能：页面开始加载
     */
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

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/30 15:07
     * 邮箱　　：496546144@qq.com
     * <p>
     * 方法功能：页面标题回掉
     */

    @Override
    public void onReceivedTitle(WebView view, String title) {
        if (listener != null) {
            listener.onReceivedTitle(view, title);
        }
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/30 15:08
     * 邮箱　　：496546144@qq.com
     * <p>
     * 方法功能：精度回掉
     */
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
