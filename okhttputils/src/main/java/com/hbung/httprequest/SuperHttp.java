package com.hbung.httprequest;

/**
 * 作者　　: 李坤
 * 创建时间:2017/3/17　15:44
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public interface SuperHttp {
    //默认的超时时间
    public static final long DEFAULT_MILLISECONDS = 10_000L;

    public void execute(RequestCall requestCall, Callback callback);

}
