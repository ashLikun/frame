package com.hbung.httprequest;

/**
 * 作者　　: 李坤
 * 创建时间:2016/12/30　17:31
 * 邮箱　　：496546144@qq.com
 * 功能介绍：ss
 */
public interface Callback<ResultType> {
    void onStart();

    void onCompleted();

    void onError(Throwable e);

    void onSuccess(ResultType responseBody);
}
