package com.ashlikun.supergridlayout;

import android.content.Context;
import android.view.ViewGroup;

import com.ashlikun.adapter.recyclerview.BaseAdapter;

import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/10/22　16:00
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */
public class FlexboxAdapter<T> extends BaseAdapter<T, FlexViewHolder> {
    public FlexboxAdapter(Context context, int layoutId, List<T> datas) {
        super(context, layoutId, datas);
    }

    public FlexboxAdapter(Context context, List<T> datas) {
        super(context, datas);
    }

    @Override
    public void convert(FlexViewHolder holder, T t) {

    }

    @Override
    public FlexViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        FlexViewHolder holder = new FlexViewHolder(mContext, getItemLayout(parent, getLayoutId()), -1);
        setListener(parent, holder, viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(FlexViewHolder holder, int position) {
        holder.setPosition(position);
        setListener(recyclerView, holder, holder.getItemViewType());
        convert(holder, getItemData(position));
    }
}
