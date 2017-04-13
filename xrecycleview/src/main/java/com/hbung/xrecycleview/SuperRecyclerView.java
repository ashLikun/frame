package com.hbung.xrecycleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import java.util.Collection;

/**
 * 作者　　: 李坤
 * 创建时间: 16:32 Administrator
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：带有下拉刷新和自动分页的RecyclerView
 */

public class SuperRecyclerView extends RelativeLayout {
    public RefreshLayout refreshLayout;
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
        refreshLayout = (RefreshLayout) findViewById(R.id.swipe);
        recyclerView = (RecyclerViewAutoLoadding) findViewById(R.id.list_swipe_target);
        /**
         * 设置集合view的刷新view
         */
        recyclerView.setRefreshLayout(refreshLayout);
        setColorSchemeResources(refreshLayout);
    }

    public void setColorSchemeResources(RefreshLayout refreshLayout) {
        TypedArray array = getContext().getTheme().obtainStyledAttributes(new int[]{R.attr.SwipeRefreshLayout_Color1, R.attr.SwipeRefreshLayout_Color2, R.attr.SwipeRefreshLayout_Color3, R.attr.SwipeRefreshLayout_Color4});
        refreshLayout.setColorSchemeColors(array.getColor(0, 0xff0000), array.getColor(1, 0xff0000), array.getColor(2, 0xff0000), array.getColor(3, 0xff0000));
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
    public void setOnRefreshListener(RefreshLayout.OnRefreshListener listener) {
        if (refreshLayout != null) {
            refreshLayout.setOnRefreshListener(listener);
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


    public RefreshLayout getRefreshLayout() {
        return refreshLayout;
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
        refreshLayout.setRefreshing(refreshing);
    }


    public RecyclerViewAutoLoadding getRecyclerView() {
        return recyclerView;
    }


    public StatusChangListener getStatusChangListener() {
        return recyclerView.getStatusChangListener();
    }

    public void addItemDecoration(RecyclerView.ItemDecoration decor) {
        recyclerView.addItemDecoration(decor);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layout) {
        recyclerView.setLayoutManager(layout);
    }


    /**
     * 作者　　: 李坤
     * 创建时间: 16:46 Administrator
     * 邮箱　　：496546144@qq.com
     * <p>
     * 功能介绍：下拉和加载更多的集合借口
     */

    public interface ListSwipeViewListener extends RefreshLayout.OnRefreshListener, OnLoaddingListener {

    }


}
