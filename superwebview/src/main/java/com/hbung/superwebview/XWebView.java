package com.hbung.superwebview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Looper;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
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
    //JavaScript:window.aaaaa.assign('img://'+ document.getElementsByTagName('img').toString())
    private static final String JAVASCRIPT = "javascript:";
    private static final String WINDOW = "window";
    private static final String DIAN = ".";
    private IWebViewListener listener;
    private String jsObject;
    private ErrorInfo errorInfo;

    public XWebView(Context context) {
        super(context);
        init(context, null);
    }

    public XWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, null);
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
        errorInfo = new ErrorInfo();
    }

    @Override
    public void loadUrl(String url) {
        if (!url.contains("data:text/html,chromewebdata")) {
            super.loadUrl(url);
        }
    }

    @Override
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        if (!url.contains("data:text/html,chromewebdata")) {
            super.loadUrl(url, additionalHttpHeaders);
        }
    }

    @Override
    public void goBack() {
        errorInfo.clean();
        super.goBack();
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
        final StringBuilder sb = new StringBuilder();
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
        if (Looper.getMainLooper() != Looper.myLooper()) {//非主线程
            post(new Runnable() {
                @Override
                public void run() {
                    loadUrl(sb.toString());
                }
            });
        } else {
            loadUrl(sb.toString());
        }
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/19 16:08
     * <p>
     * 方法功能：主动调用js  让js调用java   一般用于获取网页信息
     * JavaScript:window.aaaaa.assign('img://'+ document.getElementsByTagName('img').toString())
     */
    public void sendToJava(BridgeParams param) {

        final StringBuilder sb = new StringBuilder();
        sb.append(JAVASCRIPT);
        sb.append(WINDOW);
        sb.append(DIAN);
        sb.append(TextUtils.isEmpty(param.object) ? jsObject : param.object);
        sb.append(DIAN);
        sb.append(param.methodName);
        if ((param.params == null || param.params.length == 0) && (param.paramsFinish == null || param.paramsFinish.length == 0)) {
            sb.append("()");
        } else {
            sb.append("(");
            if (param.paramsFinish != null || param.paramsFinish.length != 0) {
                for (int i = 0; i < param.paramsFinish.length; i++) {
                    sb.append("\"");
                    sb.append(param.paramsFinish[i]);
                    sb.append("\"");

                    if (i < param.paramsFinish.length - 1 || (param.params != null || param.params.length != 0)) {
                        sb.append(",");
                    }
                }
            }
            if (param.params != null || param.params.length != 0) {
                for (int i = 0; i < param.params.length; i++) {
                    sb.append(param.params[i]);
                    if (i < param.params.length - 1) {
                        sb.append(",");
                    }
                }
            }
            sb.append(")");
        }
        if (Looper.getMainLooper() != Looper.myLooper()) {//非主线程
            post(new Runnable() {
                @Override
                public void run() {
                    loadUrl(sb.toString());
                }
            });
        } else {
            loadUrl(sb.toString());
        }
    }

    //错误信息
    public static class ErrorInfo {
        boolean isError = false;
        int errorCode;
        String description;
        String failingUrl;

        public void clean() {
            isError = false;
            errorCode = 0;
            description = null;
            failingUrl = null;
        }
    }

    //通信桥梁参数
    public static class BridgeParams {
        String object; // 注入js的对象名 默认是最后一次addJavascriptInterface的第二个参数 用于回掉java
        String methodName;//java方法名
        Object[] paramsFinish;//静态参数，用于回掉java
        Object[] params;//方法的参数  基本是js代码

        public BridgeParams(String methodName) {
            this.methodName = methodName;
        }

        public BridgeParams setObject(String object) {
            this.object = object;
            return this;
        }


        public BridgeParams setParamsFinish(Object... paramsFinish) {
            this.paramsFinish = paramsFinish;
            return this;
        }

        public BridgeParams setParams(Object... params) {
            this.params = params;
            return this;
        }
    }

    public void setListener(IWebViewListener listener) {
        this.listener = listener;
    }

    public ErrorInfo getErrorInfo() {
        return errorInfo;
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/7 15:50
     * <p>
     * 方法功能：设置监听
     */


    WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            errorInfo.clean();
            if (listener != null) {
                return listener.shouldOverrideUrlLoading(view, url);
            } else {
                return false;
            }
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
        }

        // 旧版本，会在新版本中也可能被调用，所以加上一个判断，防止重复显示
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return;
            }
            //加载错误
            errorInfo.isError = true;
            errorInfo.errorCode = errorCode;
            errorInfo.description = description;
            errorInfo.failingUrl = failingUrl;
        }

        // 新版本，只会在Android6及以上调用
        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            if (request.isForMainFrame()) { // 或者： if(request.getUrl().toString() .equals(getUrl()))
                //加载错误
                errorInfo.isError = true;
                errorInfo.errorCode = error.getErrorCode();
                errorInfo.description = error.getDescription().toString();
                errorInfo.failingUrl = request.getUrl().toString();
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            //加载完成
            if (listener != null) {
                //是否错误
                if (getErrorInfo().isError) {
                    listener.onError(view, getErrorInfo());
                }
                listener.onPageFinished(view, url, !getErrorInfo().isError);
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

        public void onError(WebView view, ErrorInfo errorInfo);

        //加载完成
        public void onPageFinished(WebView view, String url, boolean isSuccess);

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

        @Override
        public void onError(WebView view, ErrorInfo errorInfo) {

        }

        //加载完成
        public void onPageFinished(WebView view, String url, boolean isSuccess) {
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

    @Override
    public void addJavascriptInterface(Object object, String name) {
        super.addJavascriptInterface(object, name);
        this.jsObject = name;
    }
}

