package com.ashlikun.stickyrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by aurel on 24/09/14.
 */
public abstract class CommonHeaderAdapter<T> implements StickyHeadersAdapter<CommonHeaderAdapter.ViewHolder> {
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private SparseArray<View> mViews;
        private int mPosition;
        private Context mContext;
        private int mLayoutId;

        public ViewHolder(Context context, View itemView, int position) {
            super(itemView);
            mContext = context;
            mPosition = position;
            mViews = new SparseArray();
            itemView.setTag(itemView.getId(), this);
        }




        public static ViewHolder get(final Context context, View convertView,
                                     ViewGroup parent, int layoutId, int position) {
            if (convertView == null) {
                View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
                ViewHolder holder = new ViewHolder(context, itemView, position);
                holder.mLayoutId = layoutId;
                return holder;
            } else {
                ViewHolder holder = (ViewHolder) convertView.getTag(convertView.getId());
                holder.mPosition = position;
                return holder;
            }
        }

        /**
         * 通过viewId获取控件
         *
         * @param viewId
         * @return
         */
        public <T extends View> T getView(int viewId) {
            View view = mViews.get(viewId);
            if (view == null) {
                view = itemView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }



        public void updatePosition(int position) {
            mPosition = position;
        }

        public int getmPosition() {
            return mPosition;
        }

        public int getLayoutId() {
            return mLayoutId;
        }



    }

}
