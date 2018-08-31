package com.ashlikun.stickyrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * @author　　: 李坤
 * 创建时间: 2018/8/31 9:29
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：基础的头部适配器
 */

public interface StickyHeadersAdapter<HeaderViewHolder extends RecyclerView.ViewHolder> {

    /**
     * 创建一个ViewHolder
     *
     * @param parent
     * @return
     */
    HeaderViewHolder onCreateViewHolder(ViewGroup parent);

    /**
     * 绑定数据源
     *
     * @param headerViewHolder
     * @param position
     */
    void onBindViewHolder(HeaderViewHolder headerViewHolder, int position);

    /**
     * 用于区分是否是头部
     *
     * @param position
     * @return
     */
    boolean isHeader(int position);
}
