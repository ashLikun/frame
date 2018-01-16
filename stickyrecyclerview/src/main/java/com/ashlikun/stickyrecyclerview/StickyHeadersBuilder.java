package com.ashlikun.stickyrecyclerview;

import android.support.v7.widget.RecyclerView;

/**
 * Created by aurel on 16/10/14.
 */
public class StickyHeadersBuilder {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private StickyHeadersAdapter headersAdapter;
    private OnHeaderClickListener headerClickListener;
    private boolean overlay;
    private boolean isSticky;
    private DrawOrder drawOrder;
    private int headerSize;
    private int footerSize;

    public StickyHeadersBuilder() {
        this.isSticky = true;
        this.drawOrder = DrawOrder.OverItems;
    }

    public StickyHeadersBuilder setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        return this;
    }

    public StickyHeadersBuilder setStickyHeadersAdapter(StickyHeadersAdapter adapter) {
        return setStickyHeadersAdapter(adapter, false);
    }

    public StickyHeadersBuilder setStickyHeadersAdapter(StickyHeadersAdapter adapter, boolean overlay) {
        this.headersAdapter = adapter;
        this.overlay = overlay;
        return this;
    }

    public StickyHeadersBuilder setAdapter(RecyclerView.Adapter adapter) {
//        if (!adapter.hasStableIds()) {
//            throw new IllegalArgumentException("Adapter must have stable ids");
//        }
        this.adapter = adapter;
        return this;
    }

    public StickyHeadersBuilder setOnHeaderClickListener(OnHeaderClickListener headerClickListener) {
        this.headerClickListener = headerClickListener;

        return this;
    }

    public StickyHeadersBuilder setSticky(boolean isSticky) {
        this.isSticky = isSticky;

        return this;
    }

    public StickyHeadersBuilder setDrawOrder(DrawOrder drawOrder) {
        this.drawOrder = drawOrder;

        return this;
    }
    public StickyHeadersBuilder setHeaderSize(int headerSize) {
        this.headerSize = headerSize;
        return this;
    }

    public StickyHeadersBuilder setFooterSize(int footerSize) {
        this.footerSize = footerSize;
        return this;
    }
    public StickyHeadersItemDecoration build() {

        HeaderStore store = new HeaderStore(recyclerView, headersAdapter, isSticky);
        store.setHeaderSize(headerSize);
        store.setFooterSize(footerSize);


        StickyHeadersItemDecoration decoration = new StickyHeadersItemDecoration(store, overlay, drawOrder);

        decoration.registerAdapterDataObserver(adapter);

        if (headerClickListener != null) {
            StickyHeadersTouchListener touchListener = new StickyHeadersTouchListener(recyclerView, store);

            touchListener.setListener(headerClickListener);

            recyclerView.addOnItemTouchListener(touchListener);
        }

        return decoration;
    }
}