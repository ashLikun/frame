package com.hbung.loadingandretrymanager;

/**
 * 作者　　: 李坤
 * 创建时间: 17:09 Administrator
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：按钮点击时的回调
 */


public interface OnRetryClickListion {
    //数据加载失败点击重新加载
    void onRetryClick(ContextData data);

    //数据为空点击重新加载
    void onEmptyClick(ContextData data);
}
