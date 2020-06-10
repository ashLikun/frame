package com.ashlikun.stickyrecyclerview;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @author　　: 李坤
 * 创建时间: 2018/8/31 14:30
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：粘性头部的分割线
 */

public class StickyHeadersItemDecoration extends RecyclerView.ItemDecoration {

    private final HeaderStore headerStore;
    private final AdapterDataObserver adapterDataObserver;
    private boolean overlay;
    private DrawOrder drawOrder;


    public StickyHeadersItemDecoration(HeaderStore headerStore) {
        this(headerStore, false);
    }

    public StickyHeadersItemDecoration(HeaderStore headerStore, boolean overlay) {
        this(headerStore, overlay, DrawOrder.OverItems);
    }

    public StickyHeadersItemDecoration(HeaderStore headerStore, boolean overlay, DrawOrder drawOrder) {
        this.overlay = overlay;
        this.drawOrder = drawOrder;
        this.headerStore = headerStore;
        this.adapterDataObserver = new AdapterDataObserver();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (drawOrder == DrawOrder.UnderItems) {
            drawHeaders(c, parent, state);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (drawOrder == DrawOrder.OverItems) {
            drawHeaders(c, parent, state);
        }
    }

    private void drawHeaders(final Canvas c, RecyclerView parent, RecyclerView.State state) {
        final int childCount = parent.getChildCount();
        final RecyclerView.LayoutManager lm = parent.getLayoutManager();
        //最后一次绘制的y位置
        Float lastY = null;
        for (int i = childCount - 1; i >= 0; i--) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) child.getLayoutParams();
            RecyclerView.ViewHolder holder = parent.getChildViewHolder(child);
            //过滤掉头部或者底部
            if (isHeadAndFooter(holder)) {
                continue;
            }
            if (!lp.isItemRemoved() && !lp.isViewInvalid()) {
                float translationY = child.getTranslationY();
                //只绘制开启粘性后第一个view，或者是头部的view
                if (headerStore.isHeader(holder) || (headerStore.isSticky() && i == headerStore.headerSize)) {
                    //获取头部view，计算位置
                    final StickyRootView header = headerStore.getHeaderViewByItem(holder);
                    if (header != null && header.view.getVisibility() == View.VISIBLE) {
                        int headerHeight = headerStore.getHeaderHeight(holder);
                        float y = getHeaderY(child, lm) + translationY;
                        if (headerStore.isSticky() && lastY != null && lastY < y + headerHeight) {
                            y = lastY - headerHeight;
                        }
                        c.save();
                        c.translate(0, y);
                        header.setCanvas(c);
                        header.draw();
                        c.restore();
                        lastY = y;
                    }
                }
            }
        }

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
        RecyclerView.ViewHolder holder = parent.getChildViewHolder(view);
        if (isHeadAndFooter(holder)) {
            outRect.set(0, 0, 0, 0);
            return;
        }

        boolean isHeader = lp.isItemRemoved() ? headerStore.isHeader(holder) : headerStore.isHeader(holder);


        if (overlay || !isHeader) {
            outRect.set(0, 0, 0, 0);
        } else {
            outRect.set(0, headerStore.getHeaderHeight(holder), 0, 0);
        }
    }

    /**
     * 判断这个holder是否是头部或者底部
     *
     * @param holder
     * @return
     */
    public boolean isHeadAndFooter(RecyclerView.ViewHolder holder) {
        return holder.getItemViewType() == -900002 || holder.getItemViewType() == -900003 || holder.getItemViewType() == -900004;
    }

    public void registerAdapterDataObserver(RecyclerView.Adapter adapter) {
        adapter.registerAdapterDataObserver(adapterDataObserver);
    }

    /**
     * 获取这个view的头部
     *
     * @param item
     * @param lm
     * @return
     */
    private float getHeaderY(View item, RecyclerView.LayoutManager lm) {
        return headerStore.isSticky() && lm.getDecoratedTop(item) < 0 ? 0 : lm.getDecoratedTop(item);
    }


    private class AdapterDataObserver extends RecyclerView.AdapterDataObserver {

        public AdapterDataObserver() {
        }

        @Override
        public void onChanged() {
            headerStore.clear();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            headerStore.onItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            headerStore.onItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            headerStore.onItemRangeMoved(fromPosition, toPosition, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            headerStore.onItemRangeChanged(positionStart, itemCount);
        }
    }

    /**
     * 滚动到这个position(头部的)
     *
     * @param headerPosition
     */
    public void scrollToPostion(int headerPosition, RecyclerView recyclerView) {

    }

}
