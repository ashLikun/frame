package com.ashlikun.stickyrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.HashMap;

/**
 * Created by aurel on 08/11/14.
 */
public class HeaderStore {

    private final RecyclerView parent;
    private final StickyHeadersAdapter adapter;
    /**
     * 头部Position对应于的View
     */
    private final HashMap<Integer, View> headersViewByHeadersIds;

    /**
     * 保存头部view的高度
     */
    private final HashMap<Integer, Integer> headersHeightsByItemsIds;
    private boolean isSticky;
    protected int headerSize;
    protected int footerSize;


    public HeaderStore(RecyclerView parent, StickyHeadersAdapter adapter, boolean isSticky) {
        this.parent = parent;
        this.adapter = adapter;
        this.isSticky = isSticky;
        this.headersViewByHeadersIds = new HashMap();
        this.headersHeightsByItemsIds = new HashMap();
    }

    /**
     * 获取这个Holder对应的头部view
     *
     * @return
     */
    public View getHeaderViewByItem(int position) {
        if (position < 0) {
            return null;
        }
        if (!headersViewByHeadersIds.containsKey(position)) {
            RecyclerView.ViewHolder headerViewHolder = adapter.onCreateViewHolder(parent);
            adapter.onBindViewHolder(headerViewHolder, position);
            layoutHeader(headerViewHolder.itemView);
            headersViewByHeadersIds.put(position, headerViewHolder.itemView);
        }
        return headersViewByHeadersIds.get(position);
    }

    public View getHeaderViewByItem(RecyclerView.ViewHolder itemHolder) {
        return getHeaderViewByItem(findHeaderViewHolderPosition(itemHolder));
    }

    /**
     * 不是头部就找到之前有的,实现粘性
     *
     * @return
     */
    public int findHeaderViewHolderPosition(RecyclerView.ViewHolder itemHolder) {
        int itemPosition = getPosition(itemHolder);
        boolean isFind = false;
        //不是头部就找到之前有的,实现粘性
        if (!isHeader(itemHolder)) {
            if (isSticky()) {
                for (int i = itemPosition; i >= 0; i--) {
                    if (adapter.isHeader(i)) {
                        itemPosition = i;
                        isFind = true;
                        break;
                    }
                }
            }
            if (!isFind) {
                return -1;
            }
        }
        return itemPosition;
    }


    /**
     * 获取头部view的高度
     *
     * @param itemHolder
     * @return
     */
    public int getHeaderHeight(RecyclerView.ViewHolder itemHolder) {
        int itemPosition = getPosition(itemHolder);
        if (itemPosition < 0) {
            return 0;
        }
        if (!headersHeightsByItemsIds.containsKey(itemPosition)) {
            View header = getHeaderViewByItem(itemHolder);
            if (header == null) {
                return 0;
            }
            headersHeightsByItemsIds.put(itemPosition, header.getVisibility() == View.GONE ? 0 : header.getMeasuredHeight());
        }
        return headersHeightsByItemsIds.get(itemPosition);
    }

    /**
     * 这个Position是否是头部,如果是就会绘制
     * 那当前的item于
     *
     * @param itemHolder
     * @return
     */
    public boolean isHeader(RecyclerView.ViewHolder itemHolder) {
        int itemPosition = getPosition(itemHolder);
        if (itemPosition < 0) {
            return false;
        }
        return adapter.isHeader(itemPosition);
    }


    /**
     * 是否是粘性的
     *
     * @return
     */
    public boolean isSticky() {
        return isSticky;
    }

    public void onItemRangeRemoved(int positionStart, int itemCount) {
        headersViewByHeadersIds.clear();
    }

    public void onItemRangeInserted(int positionStart, int itemCount) {
        headersViewByHeadersIds.clear();
    }

    public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
        headersViewByHeadersIds.clear();
    }

    public void onItemRangeChanged(int startPosition, int itemCount) {
        headersViewByHeadersIds.clear();
    }

    public void clear() {
        headersViewByHeadersIds.clear();
    }

    private void layoutHeader(View header) {
        int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth(), View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        header.measure(widthSpec, heightSpec);
        header.layout(0, 0, header.getMeasuredWidth(), header.getMeasuredHeight());
    }


    public void setHeaderSize(int headerSize) {
        this.headerSize = headerSize;
    }

    public void setFooterSize(int footerSize) {
        this.footerSize = footerSize;
    }

    public int getPosition(RecyclerView.ViewHolder itemHolder) {
        return itemHolder.getLayoutPosition() - headerSize;
    }

}
