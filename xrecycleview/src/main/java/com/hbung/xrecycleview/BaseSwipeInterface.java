package com.hbung.xrecycleview;

import android.support.v4.widget.SwipeRefreshLayout;

/**
 * Created by Administrator on 2016/8/8.
 */

public interface BaseSwipeInterface {
    void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout);

    void setOnLoaddingListener(OnLoaddingListener swipeRefreshLayout);

    PagingHelp getPagingHelp();

    StatusChangListener getStatusChangListener();
}
