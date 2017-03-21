package com.hbung.http;

import okhttp3.Call;
import rx.Subscriber;

/**
 * 作者　　: 李坤
 * 创建时间:2017/3/21　16:47
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public class ExecuteCall {
    private Call call;
    private boolean isCompleted = false;//是否完成
    private Subscriber subscriber;


    public void cancel() {
        if (call != null) {
            call.cancel();
        }
    }


    public boolean isCompleted() {
        return isCompleted;
    }

    public boolean isCanceled() {
        return call == null ? true : call.isCanceled();
    }

    protected void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    protected void setCall(Call call) {
        this.call = call;
    }

    public Call getCall() {
        return call;
    }

    public Subscriber getSubscriber() {
        return subscriber;
    }

    protected void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }
}
