package com.hbung.http.mode;

import com.hbung.http.httpquest.Callback;
import com.hbung.http.httpquest.RequestParam;

import java.util.Map;

import okhttp3.RequestBody;
import rx.Subscription;

/**
 * 作者　　: 李坤
 * 创建时间:2017/1/18　11:15
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public interface HttpInterface {

    <T> Subscription post(Callback<T> subscriber,
                          Map<String, String> maps);

    <T> Subscription post(Callback<T> subscriber,
                          RequestParam param);

    <T> Subscription get(Callback<T> subscriber,
                         Map<String, String> maps);

    <T> Subscription get(Callback<T> subscriber,
                         RequestParam param);

    <T> Subscription uploadFiles(Callback<T> subscriber,
                                 Map<String, RequestBody> maps);

    <T> Subscription uploadFiles(Callback<T> subscriber,
                                 RequestParam param);
}
