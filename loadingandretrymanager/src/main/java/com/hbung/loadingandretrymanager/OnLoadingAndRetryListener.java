package com.hbung.loadingandretrymanager;

import android.view.View;

/**
 * 作者　　: 李坤
 * 创建时间: 17:08 Administrator
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：布局切换时的监听
 */

public abstract class OnLoadingAndRetryListener {
    public abstract void setRetryEvent(View retryView, ContextData data);

    public abstract void setLoadingEvent(View loadingView, ContextData data);

    public abstract void setEmptyEvent(View emptyView, ContextData data);

    public int generateLoadingLayoutId() {
        return LoadingAndRetryManager.NO_LAYOUT_ID;
    }

    public int generateRetryLayoutId() {
        return LoadingAndRetryManager.NO_LAYOUT_ID;
    }

    public int generateEmptyLayoutId() {
        return LoadingAndRetryManager.NO_LAYOUT_ID;
    }

    public View generateLoadingLayout() {
        return null;
    }

    public View generateRetryLayout() {
        return null;
    }

    public View generateEmptyLayout() {
        return null;
    }

    public boolean isSetLoadingLayout() {
        if (generateLoadingLayoutId() != LoadingAndRetryManager.NO_LAYOUT_ID || generateLoadingLayout() != null)
            return true;
        return false;
    }

    public boolean isSetRetryLayout() {
        if (generateRetryLayoutId() != LoadingAndRetryManager.NO_LAYOUT_ID || generateRetryLayout() != null)
            return true;
        return false;
    }

    public boolean isSetEmptyLayout() {
        if (generateEmptyLayoutId() != LoadingAndRetryManager.NO_LAYOUT_ID || generateEmptyLayout() != null)
            return true;
        return false;
    }


}