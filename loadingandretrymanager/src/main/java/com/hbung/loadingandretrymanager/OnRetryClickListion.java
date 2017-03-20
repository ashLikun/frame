package com.hbung.loadingandretrymanager;

/**
 * Created by Administrator on 2016/7/28.
 */

public interface OnRetryClickListion {
    //数据加载失败点击重新加载
    void onRetryClick(ContextData data);

    //数据为空点击重新加载
    void onEmptyClick(ContextData data);
}
