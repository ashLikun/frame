package com.ashlikun.stickyrecyclerview;

import android.view.View;

/**
 * @author　　: 李坤
 * 创建时间: 2018/8/31 10:10
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：头部点击事件
 */

public interface OnHeaderClickListener {
    /**
     * 当头部view点击的时候
     *
     * @param header
     * @param position recycleView的adapter对应的position
     */
    void onHeaderClick(View header, long position);
}
