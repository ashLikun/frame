package com.ashlikun.wheelview3d.adapter;

/**
 * 作者　　: 李坤
 * 创建时间:2017/3/31　11:08
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public abstract class BaseLoopAdapter<T> {
    private final LoopDataObservable loopDataObservable = new LoopDataObservable();

    public abstract int getItemCount();

    public abstract T getItem(int position);

    public abstract String getShowText(int position);

    public void registerDataSetObserver(LoopDataObserver observer) {
        loopDataObservable.registerObserver(observer);
    }

    public void unregisterDataSetObserver(LoopDataObserver observer) {
        loopDataObservable.unregisterObserver(observer);
    }

    public void notifyDataSetChanged() {
        loopDataObservable.notifyChanged();
    }
}
