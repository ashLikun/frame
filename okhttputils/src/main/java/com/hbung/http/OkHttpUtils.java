package com.hbung.http;

import com.hbung.http.request.RequestCall;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class OkHttpUtils implements SuperHttp {

    //单列模式
    private volatile static OkHttpUtils mInstance;
    //okhttp核心类
    private OkHttpClient mOkHttpClient;

    public OkHttpUtils(OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            mOkHttpClient = new OkHttpClient.Builder()
                    .readTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
                    .writeTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
                    .connectTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
                    .build();
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
    public <T> ExecuteCall execute(RequestCall requestCall, Callback<T> callback) {
        Call call = requestCall.enqueue(callback);
        ExecuteCall exc = new ExecuteCall();
        exc.setCall(call);
        call.enqueue(new OkHttpCallback(exc, callback));
        return exc;
    }

    @Override
    public Response execute(RequestCall requestCall) throws IOException {
        return requestCall.execute();
    }

    private class OkHttpCallback<ResultType> implements okhttp3.Callback {
        HttpSubscription sub;
        ExecuteCall exc;

        public OkHttpCallback(ExecuteCall exc, Callback<ResultType> callback) {
            this.exc = exc;
            Observable<Response> obs = Observable.create(new Observable.OnSubscribe<Response>() {
                @Override
                public void call(Subscriber<? super Response> subscriber) {
                    sub.onStart();
                }
            });
            toSubscribe(obs, sub = new HttpSubscription(callback));
        }

        @Override
        public void onFailure(Call call, IOException e) {
            sub.onError(e);
            exc.setCompleted(true);
            sub.onCompleted();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.isSuccessful()) {
                sub.onNext(response);
            } else {
                sub.onError(new HttpException(response));
            }
            exc.setCompleted(true);
            sub.onCompleted();
        }
    }


    //线程切换
    Observable.Transformer schedulersTransformer = new Observable.Transformer<Observable, Observable>() {
        @Override
        public Observable call(Observable observable) {
            return observable.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };

    /**
     * 订阅
     *
     * @param o 被监听者，相当于网络访问
     * @param s 监听者，  相当于回调监听
     */
    protected <T> Subscription toSubscribe(Observable<T> o, Subscriber<T> s) {
        return o.compose(schedulersTransformer)
                .subscribe(s);//订阅
    }
}

