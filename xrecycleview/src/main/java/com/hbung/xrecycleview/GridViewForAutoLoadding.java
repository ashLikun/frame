package com.hbung.xrecycleview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/14.
 */
public class GridViewForAutoLoadding extends GridViewWithHeaderAndFooter implements BaseSwipeInterface, StatusChangListener, ConfigChang {
    public PagingHelp pagingHelp = null;
    private OnLoaddingListener onLoaddingListener;
    private FooterView footerView;
    private ArrayList<OnScrollListener> scrollListeners = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private OnItemClickListener itemClickListener;

    public GridViewForAutoLoadding(Context context) {
        this(context, null);
    }

    public GridViewForAutoLoadding(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        addFooterView(footerView = new FooterView(context));
        footerView.setStatus(LoadState.Init);
        super.setOnScrollListener(new ScrollListener());
        super.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(parent, view, position - getHeaderViewsCount(), id);
                }
            }
        });
    }

    public GridViewForAutoLoadding(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void addOnScrollListener(OnScrollListener scrollListeners) {
        this.scrollListeners.add(scrollListeners);
    }

    @Override
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        addOnScrollListener(l);
    }

    private final class ScrollListener implements OnScrollListener {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

            for (OnScrollListener s : scrollListeners) {
                s.onScrollStateChanged(view, scrollState);
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            int lastItemIndex = getLastVisiblePosition(); // 获取当前屏幕最后Item的ID
            if ((swipeRefreshLayout == null || !swipeRefreshLayout.isRefreshing()) && totalItemCount - getFooterViewsCount() - getHeaderViewsCount() > 0) {
                if (lastItemIndex + 1 >= totalItemCount) {// 达到数据的最后一条记录
                    if (footerView.isLoadMoreEnabled() && totalItemCount > 1 && footerView.getStates() != LoadState.Loadding && footerView.getStates() != LoadState.NoData && onLoaddingListener != null) {//可以加载
                        setState(LoadState.Loadding);
                        onLoaddingListener.onLoadding();
                    }
                }
            } else {
                setState(LoadState.Hint);
            }
            for (OnScrollListener s : scrollListeners) {
                s.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        }
    }


    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    public void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    public LoadState getState() {
        return footerView.getStates();
    }

    public void setState(LoadState state) {
        footerView.setStatus(state);
    }

    public OnLoaddingListener getOnLoaddingListener() {
        return onLoaddingListener;
    }

    public void setOnLoaddingListener(OnLoaddingListener onLoaddingListener) {
        this.onLoaddingListener = onLoaddingListener;
        if (pagingHelp == null) {
            pagingHelp = new PagingHelp(getContext());
        } else {
            pagingHelp.clear();
        }
        pagingHelp.setStatusChangListener(this);
    }


    @Override
    public void complete() {
        if (footerView.getStates() != LoadState.NoData) {
            setState(LoadState.Complete);
        }
    }

    /**
     * 没有更多数据加载
     */
    @Override
    public void noData() {
        setDataSize();
        setState(LoadState.NoData);
    }

    public void setDataSize() {
        if (footerView != null) {
            footerView.setDataSize(pagingHelp.getRecordCount());
        }
    }

    @Override
    public void setAutoloaddingCompleData(String autoloaddingCompleData) {
        if (footerView != null) {
            footerView.setAutoloaddingCompleData(autoloaddingCompleData);
        }

    }

    @Override
    /**
     * 初始化状态
     */
    public void init() {
        setState(LoadState.Init);
    }

    @Override
    public void failure() {
        if (footerView.getStates() != LoadState.NoData) {
            setState(LoadState.Failure);
        }
    }

    @Override
    public void setAutoloaddingNoData(String autoloaddingNoData) {
        if (footerView != null) {
            footerView.setAutoloaddingNoData(autoloaddingNoData);
        }
    }

    @Override
    public boolean isLoadMoreEnabled() {
        return footerView == null ? false : footerView.isLoadMoreEnabled();
    }

    @Override
    public void setLoadMoreEnabled(boolean loadMoreEnabled) {
        if (footerView != null) {
            footerView.setLoadMoreEnabled(loadMoreEnabled);
        }
    }

    @Override
    public PagingHelp getPagingHelp() {
        return pagingHelp;
    }

    @Override
    public StatusChangListener getStatusChangListener() {
        return this;
    }
}
