package com.hbung.http.mode;

import android.text.TextUtils;

import com.hbung.http.httpquest.Callback;
import com.hbung.http.httpquest.HttpSubscription;
import com.hbung.http.httpquest.RequestParam;

import java.util.Map;

import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 作者　　: 李坤
 * 创建时间:2016/9/24　16:24
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public abstract class ImlHttpInterface implements HttpInterface {

    public abstract BaseServiceApi getBaseServiceApi();

    public abstract BaseServiceApi getLongServiceApi();

    public abstract String getPath();

    private String getPath(RequestParam param) {
        if (param != null && !TextUtils.isEmpty(param.getPath())) {
            return param.getPath();
        } else {
            return getPath();
        }
    }


    Observable.Transformer schedulersTransformer = new Observable.Transformer() {
        @Override
        public Object call(Object observable) {
            return ((Observable) observable).subscribeOn(Schedulers.io())
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
    public <T> Subscription toSubscribe(Observable<T> o, Subscriber<T> s) {
        return o.compose(schedulersTransformer)
                .subscribe(s);//订阅
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //     不要传具体的url路径  如ashx,php,html， 一般在生成的时候在baseUrl已经有了（后台只写了一个接口文件）
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 作者　　: 李坤
     * 创建时间: 2016/9/24 16:09
     * <p>
     * 方法功能：POST请求
     *
     * @param maps : 一定要传Action
     */
    public <T> Subscription post(Callback<T> subscriber,
                                 Map<String, String> maps) {
        return toSubscribe(getBaseServiceApi().post(getPath(), maps),
                new HttpSubscription<T>(subscriber));
    }

    public <T> Subscription post(Callback<T> subscriber,
                                 RequestParam param) {
        if (param.isHavaHeader()) {
            return toSubscribe(getBaseServiceApi().post(param.getHeaders(), getPath(param), param.getStringMap()),
                    new HttpSubscription<T>(subscriber));
        }
        return toSubscribe(getBaseServiceApi().post(getPath(param), param.getStringMap()),
                new HttpSubscription<T>(subscriber));
    }


    public <T> Subscription get(Callback<T> subscriber,
                                Map<String, String> maps) {

        return toSubscribe(getBaseServiceApi().get(getPath(), maps),
                new HttpSubscription<T>(subscriber));
    }

    public <T> Subscription get(Callback<T> subscriber,
                                RequestParam param) {
        if (param.isHavaHeader()) {
            return toSubscribe(getBaseServiceApi().get(param.getHeaders(), getPath(param), param.getStringMap()),
                    new HttpSubscription<T>(subscriber));
        }
        return toSubscribe(getBaseServiceApi().get(getPath(param), param.getStringMap()),
                new HttpSubscription<T>(subscriber));
    }


    /**
     * 作者　　: 李坤
     * 创建时间: 2016/9/24 16:09
     * <p>
     * 方法功能：POST请求
     *
     * @param maps : 一定要传Action
     */
    public <T> Subscription uploadFiles(Callback<T> subscriber,
                                        Map<String, RequestBody> maps) {
        return toSubscribe(getLongServiceApi().uploadFiles(getPath(), maps),
                new HttpSubscription<T>(subscriber));
    }

    public <T> Subscription uploadFiles(Callback<T> subscriber,
                                        RequestParam param) {
        if (param.isHavaHeader()) {
            return toSubscribe(getBaseServiceApi().uploadFiles(param.getHeaders(), getPath(param), param.getPartMap()),
                    new HttpSubscription<T>(subscriber));
        }
        return toSubscribe(getLongServiceApi().uploadFiles(getPath(param), param.getPartMap()),
                new HttpSubscription<T>(subscriber));
    }
}
