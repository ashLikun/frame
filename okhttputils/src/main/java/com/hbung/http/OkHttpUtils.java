package com.hbung.http;

import com.hbung.http.request.RequestCall;
import com.hbung.http.request.RequestParam;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;


public class OkHttpUtils implements SuperHttp {

    //单列模式
    private volatile static OkHttpUtils mInstance;
    //okhttp核心类
    private OkHttpClient mOkHttpClient;
    private MainThread mMainThread;//线程切换

    private OkHttpUtils(OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            mOkHttpClient = new OkHttpClient.Builder()
                    .readTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
                    .writeTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
                    .connectTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
                    .build();
        } else {
            mOkHttpClient = okHttpClient;
        }
        mMainThread = new MainThread();
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
    public <T> ExecuteCall execute(RequestCall requestCall, Callback<T> callback) {
        Call call = requestCall.enqueue(callback);
        ExecuteCall exc = new ExecuteCall();
        exc.setCall(call);
        call.enqueue(new OkHttpCallback(mMainThread, exc, callback));
        return exc;
    }

    @Override
    public <T> ExecuteCall execute(RequestParam requestParam, Callback<T> callback) {
        RequestCall requestCall = new RequestCall.Builder(requestParam)
                .build();
        Call call = requestCall.enqueue(callback);
        ExecuteCall exc = new ExecuteCall();
        exc.setCall(call);
        call.enqueue(new OkHttpCallback(mMainThread, exc, callback));
        return exc;
    }

    @Override
    public Response execute(RequestCall requestCall) throws IOException {
        return requestCall.execute();
    }

    @Override
    public Response execute(RequestParam requestParam) throws IOException {
        RequestCall requestCall = new RequestCall.Builder(requestParam)
                .build();
        return requestCall.execute();
    }


}

