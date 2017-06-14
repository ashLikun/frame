package com.hbung.superwebview;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Map;

/**
 * 作者　　: 李坤
 * 创建时间:2017/6/7　14:46
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public class XWebView extends WebView {
    private static final String JAVASCRIPT = "javascript:";
    private IWebViewListener listener;
    private String url;

    @Override
    public String getUrl() {
        return url;
    }

    public XWebView(Context context) {
        this(context, null);
    }

    public XWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        WebSettings webSett = getSettings();
        webSett.setJavaScriptEnabled(true);
        webSett.setLoadWithOverviewMode(true);
        setWebViewClient(webViewClient);
        setWebChromeClient(webChromeClient);
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
        StringBuilder sb = new StringBuilder();
        sb.append(JAVASCRIPT);
        sb.append(methodName);
        if (params == null || params.length == 0) {
            sb.append("()");
        } else {
            sb.append("(");
            for (int i = 0; i < params.length; i++) {
                sb.append("\"");
                sb.append(params[i]);
                sb.append("\"");
                if (i < params.length - 1) {
                    sb.append(",");
                }
            }
            sb.append(")");
        }
        loadUrl(sb.toString());
    }

    @Override
    public void loadUrl(String url) {
        super.loadUrl(url);
        if (!TextUtils.isEmpty(url) && !url.startsWith(JAVASCRIPT)) {
            this.url = url;
        }
    }

    @Override
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        super.loadUrl(url, additionalHttpHeaders);
        this.url = url;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/7 15:50
     * <p>
     * 方法功能：设置监听
     */
    public void setListener(IWebViewListener listener) {
        this.listener = listener;
    }


    WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (listener != null) {
                return listener.shouldOverrideUrlLoading(view, url);
            } else {
                return false;
            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            //加载错误
            if (listener != null) {
                listener.onReceivedError(view, request, error);
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            //加载完成
            if (listener != null) {
                listener.onPageFinished(view, url);
            }
        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //加载开始
            if (listener != null) {
                listener.onPageStarted(view, url, favicon);
            }
        }
    };
    WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            //页面标题
            if (listener != null) {
                listener.onReceivedTitle(view, title);
            }
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //进度改变
            if (listener != null) {
                listener.onProgressChanged(view, newProgress);
            }
        }
    };

    public interface IWebViewListener {

        //在点击请求的是连接是才会调用，重写此方法返回true表明点击网页里面的链接还是在当前的webView里跳转，不跳到浏览器里边
        public boolean shouldOverrideUrlLoading(WebView view, String url);

        //加载错误
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error);

        //加载完成
        public void onPageFinished(WebView view, String url);

        //加载开始
        public void onPageStarted(WebView view, String url, Bitmap favicon);

        //页面标题
        public void onReceivedTitle(WebView view, String title);

        //进度改变
        public void onProgressChanged(WebView view, int newProgress);
    }

    public static abstract class WebViewListener implements IWebViewListener {
        //在点击请求的是连接是才会调用，重写此方法返回true表明点击网页里面的链接还是在当前的webView里跳转，不跳到浏览器里边
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        //加载错误
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {

        }

        //加载完成
        public void onPageFinished(WebView view, String url) {
        }

        //加载开始
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

        }

        //页面标题
        public void onReceivedTitle(WebView view, String title) {

        }

        //进度改变
        public void onProgressChanged(WebView view, int newProgress) {

        }
    }

}
