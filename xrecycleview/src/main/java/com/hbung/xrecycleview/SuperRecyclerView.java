package com.hbung.xrecycleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import java.util.Collection;

/**
 * Created by Administrator on 2016/4/28.
 */
public class SuperRecyclerView extends RelativeLayout {
    public SwipeRefreshLayout swipeView;
    RecyclerViewAutoLoadding recyclerView;

    public SuperRecyclerView(Context context) {
        this(context, null);
    }

    public SuperRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) {
            initView();
        }
    }


    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.base_swipe_recycle, this, true);
        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe);
        recyclerView = (RecyclerViewAutoLoadding) findViewById(R.id.list_swipe_target);
        /**
         * 设置集合view的刷新view
         */
        recyclerView.setSwipeRefreshLayout(swipeView);
        setColorSchemeResources(swipeView);
    }

    public void setColorSchemeResources(SwipeRefreshLayout swipeRefreshLayout) {
        TypedArray array = getContext().getTheme().obtainStyledAttributes(new int[]{R.attr.SwipeRefreshLayout_Color1, R.attr.SwipeRefreshLayout_Color2, R.attr.SwipeRefreshLayout_Color3, R.attr.SwipeRefreshLayout_Color4});
        swipeRefreshLayout.setColorSchemeColors(array.getColor(0, 0xff0000), array.getColor(1, 0xff0000), array.getColor(2, 0xff0000), array.getColor(3, 0xff0000));
        array.recycle();
    }

    /**
     * 设置加载更多的回调
     *
     * @param onLoaddingListener
     */
    public void setOnLoaddingListener(OnLoaddingListener onLoaddingListener) {
        recyclerView.setOnLoaddingListener(onLoaddingListener);

    }

    /**
     * 设置下拉刷新的回调
     *
     * @param listener
     */
    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
        if (swipeView != null) {
            swipeView.setOnRefreshListener(listener);
        }
    }


    /**
     * 设置适配器
     */
    public void setAdapter(RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    /**
     * 获取pagingHelp
     */
    public PagingHelp getPagingHelp() {
        return recyclerView.getPagingHelp();
    }

    /**
     * 获取分页的有效数据
     */
    public <T> Collection<T> getValidData(Collection<T> c) {
        return getPagingHelp().getValidData(c);
    }


    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeView;
    }


    public ConfigChang getConfigChang() {
        if (recyclerView instanceof ConfigChang) {
            return (ConfigChang) recyclerView;
        } else {
            return null;
        }
    }

    /**
     * 刷新Swp
     *
     * @return
     */
    public void setRefreshing(boolean refreshing) {
        swipeView.setRefreshing(refreshing);
    }


    public RecyclerViewAutoLoadding getRecyclerView() {
        return recyclerView;
    }


    public StatusChangListener getStatusChangListener() {
        return recyclerView.getStatusChangListener();
    }

    public interface ListSwipeViewListener extends SwipeRefreshLayout.OnRefreshListener, OnLoaddingListener {

    }


}
