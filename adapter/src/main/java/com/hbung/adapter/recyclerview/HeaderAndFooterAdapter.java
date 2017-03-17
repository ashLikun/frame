package com.hbung.adapter.recyclerview;

import android.support.v7.widget.RecyclerView;

/**
 * 作者　　: 李坤
 * 创建时间:2017/1/4　13:40
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public abstract class HeaderAndFooterAdapter<H extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<H> {
    int headerSize;
    int footerSize;

    public int getFooterSize() {
        return footerSize;
    }

    public void setFooterSize(int footerSize) {
        this.footerSize = footerSize;
    }

    public int getHeaderSize() {
        return headerSize;
    }

    public void setHeaderSize(int headerSize) {
        this.headerSize = headerSize;
    }
}
