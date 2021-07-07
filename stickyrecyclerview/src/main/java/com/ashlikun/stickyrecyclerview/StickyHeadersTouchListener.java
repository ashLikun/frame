package com.ashlikun.stickyrecyclerview;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by aurel on 08/11/14.
 */
public class StickyHeadersTouchListener implements RecyclerView.OnItemTouchListener {

    private final HeaderStore headerStore;
    private final GestureDetector gestureDetector;
    private OnHeaderClickListener listener;
    private boolean overlay;

    public void setOverlay(boolean overlay) {
        this.overlay = overlay;
    }

    public StickyHeadersTouchListener(RecyclerView parent, HeaderStore headerStore) {
        this.headerStore = headerStore;
        this.gestureDetector = new GestureDetector(parent.getContext(), new SingleTapDetector(parent));
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView parent, MotionEvent e) {
        return listener != null && gestureDetector.onTouchEvent(e);
    }

    @Override
    public void onTouchEvent(RecyclerView parent, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public void setListener(OnHeaderClickListener listener) {
        this.listener = listener;
    }

    private class SingleTapDetector extends GestureDetector.SimpleOnGestureListener {

        private final RecyclerView parent;

        public SingleTapDetector(RecyclerView parent) {
            this.parent = parent;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return findItemHolderUnder(e.getX(), e.getY()) != -1;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {

            int position = findItemHolderUnder(e.getX(), e.getY());
            if (position != -1) {
                StickyRootView headerView = headerStore.getHeaderViewByItem(position);
                listener.onHeaderClick(headerView.getView(), position);
                return true;
            }

            return false;
        }

        private int findItemHolderUnder(float x, float y) {

            for (int i = parent.getChildCount() - 1; i > 0; i--) {
                View item = parent.getChildAt(i);
                RecyclerView.ViewHolder holder = parent.getChildViewHolder(item);

                if (holder != null && headerStore.isHeader(holder)) {
                    if (!overlay && y < item.getTop() && item.getTop() - headerStore.getHeaderHeight(holder) < y) {
                        return headerStore.findHeaderViewHolderPosition(holder);
                    } else if (overlay && y >= item.getTop() && item.getTop() + headerStore.getHeaderHeight(holder) >= y) {
                        return headerStore.findHeaderViewHolderPosition(holder);
                    }
                }
            }

            View firstItem = parent.getChildAt(0);

            if (firstItem != null) {
                RecyclerView.ViewHolder holder = parent.getChildViewHolder(firstItem);
                if (holder != null) {
                    //找到粘性的position
                    int position = headerStore.findHeaderViewHolderPosition(holder);
                    if (position >= 0 && y < headerStore.getHeaderHeight(holder)) {
                        if (headerStore.getPosition(holder) == 0 || headerStore.isSticky()) {
                            return headerStore.findHeaderViewHolderPosition(holder);
                        }
                    }
                }
            }
            return -1;
        }
    }
}
