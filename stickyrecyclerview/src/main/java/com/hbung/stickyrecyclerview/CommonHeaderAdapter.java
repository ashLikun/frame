package com.hbung.stickyrecyclerview;

import android.content.Context;
import android.view.ViewGroup;

import com.hbung.adapter.ViewHolder;

import java.util.List;

/**
 * Created by aurel on 24/09/14.
 */
public abstract class CommonHeaderAdapter<T> implements StickyHeadersAdapter<ViewHolder> {
    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;

    public CommonHeaderAdapter(Context context, int layoutId, List<T> datas) {
        mContext = context;
        mLayoutId = layoutId;
        mDatas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        ViewHolder viewHolder = ViewHolder.get(mContext, null, parent, mLayoutId, -1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder headerViewHolder, int position) {
        headerViewHolder.updatePosition(position);
        convert(headerViewHolder, mDatas.get(position));
    }

    public abstract void convert(ViewHolder holder, T t);

    @Override
    public long getHeaderId(int position) {
        return mDatas.get(position).hashCode();
    }

}
