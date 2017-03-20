package com.hbung.httprequest;

import okhttp3.OkHttpClient;

/**
 * Created by zhy on 15/8/17.
 */
public class OkHttpUtils implements SuperHttp {

    //单列模式
    private volatile static OkHttpUtils mInstance;
    //okhttp核心类
    private OkHttpClient mOkHttpClient;

    public OkHttpUtils(OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
        } else {
            mOkHttpClient = okHttpClient;
        }
    }


    public static OkHttpUtils initClient(OkHttpClient okHttpClient) {
        if (mInstance == null) {
            synchronized (OkHttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtils(okHttpClient);
                }
            }
        }
        return mInstance;
    }

    public static OkHttpUtils getInstance() {
        return initClient(null);
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }


    @Override
    public void execute(RequestCall requestCall, Callback callback) {
        requestCall.execute(callback);
    }
}

