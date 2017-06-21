package utils.hbung.com.utils;

import android.view.View;

import com.hbung.http.Callback;

/**
 * 作者　　: 李坤
 * 创建时间:2017/6/15　15:29
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public abstract class HttpCallBack<T> implements Callback<T>, View.OnClickListener {

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onSuccess(T responseBody) {

    }
}
