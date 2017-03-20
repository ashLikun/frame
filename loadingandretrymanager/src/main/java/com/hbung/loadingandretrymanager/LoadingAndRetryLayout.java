package com.hbung.loadingandretrymanager;

import android.content.Context;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;


/**
 * Created by zhy on 15/8/26.
 */
public class LoadingAndRetryLayout extends FrameLayout {
    private View mLoadingView;
    private View mRetryView;
    private View mContentView;
    private View mEmptyView;
    private OnLoadingAndRetryListener loadingAndRetryListener;

    private static final String TAG = LoadingAndRetryLayout.class.getSimpleName();


    public LoadingAndRetryLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public LoadingAndRetryLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public LoadingAndRetryLayout(Context context) {
        this(context, null);
    }

    public void setLoadingAndRetryListener(OnLoadingAndRetryListener loadingAndRetryListener) {
        this.loadingAndRetryListener = loadingAndRetryListener;
    }

    private boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public void showLoading(final ContextData data) {
        if (isMainThread()) {
            showView(mLoadingView, data);
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    showView(mLoadingView, data);
                }
            });
        }
    }

    public void showRetry(final ContextData data) {
        if (isMainThread()) {
            showView(mRetryView, data);
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    showView(mRetryView, data);
                }
            });
        }

    }

    public void showContent() {
        if (isMainThread()) {
            showView(mContentView, null);
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    showView(mContentView, null);
                }
            });
        }
    }

    public void showEmpty(final ContextData data) {
        if (isMainThread()) {
            showView(mEmptyView, data);
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    showView(mEmptyView, data);
                }
            });
        }
    }


    private void showView(View view, ContextData data) {
        if (view == null) return;

        if (view == mLoadingView) {
            mLoadingView.setVisibility(View.VISIBLE);
            if (mRetryView != null)
                mRetryView.setVisibility(View.GONE);
            if (mContentView != null)
                mContentView.setVisibility(View.GONE);
            if (mEmptyView != null)
                mEmptyView.setVisibility(View.GONE);

            loadingAndRetryListener.setLoadingEvent(mLoadingView, data);
        } else if (view == mRetryView) {
            mRetryView.setVisibility(View.VISIBLE);
            if (mLoadingView != null)
                mLoadingView.setVisibility(View.GONE);
            if (mContentView != null)
                mContentView.setVisibility(View.GONE);
            if (mEmptyView != null)
                mEmptyView.setVisibility(View.GONE);
            loadingAndRetryListener.setRetryEvent(mRetryView, data);
        } else if (view == mContentView) {
            mContentView.setVisibility(View.VISIBLE);
            mContentView.invalidate();
            if (mLoadingView != null)
                mLoadingView.setVisibility(View.GONE);
            if (mRetryView != null)
                mRetryView.setVisibility(View.GONE);
            if (mEmptyView != null)
                mEmptyView.setVisibility(View.GONE);
        } else if (view == mEmptyView) {
            mEmptyView.setVisibility(View.VISIBLE);
            if (mLoadingView != null)
                mLoadingView.setVisibility(View.GONE);
            if (mRetryView != null)
                mRetryView.setVisibility(View.GONE);
            if (mContentView != null)
                mContentView.setVisibility(View.GONE);
            loadingAndRetryListener.setEmptyEvent(mEmptyView, data);
        }


    }

    public View setContentView(int layoutId) {

        return setContentView(LayoutInflater.from(getContext()).inflate(layoutId, this, false));
    }

    public View setLoadingView(int layoutId) {
        return setLoadingView(LayoutInflater.from(getContext()).inflate(layoutId, this, false));
    }

    public View setEmptyView(int layoutId) {
        return setEmptyView(LayoutInflater.from(getContext()).inflate(layoutId, this, false));
    }

    public View setRetryView(int layoutId) {
        return setRetryView(LayoutInflater.from(getContext()).inflate(layoutId, this, false));
    }

    public View setLoadingView(View view) {
        View loadingView = mLoadingView;
        if (loadingView != null) {
            Log.w(TAG, "you have already set a loading view and would be instead of this new one.");
        }
        removeView(loadingView);
        addView(view);
        mLoadingView = view;
        return mLoadingView;
    }

    public View setEmptyView(View view) {
        View emptyView = mEmptyView;
        if (emptyView != null) {
            Log.w(TAG, "you have already set a empty view and would be instead of this new one.");
        }
        removeView(emptyView);
        addView(view);
        mEmptyView = view;
        return mEmptyView;
    }

    public View setRetryView(View view) {
        View retryView = mRetryView;
        if (retryView != null) {
            Log.w(TAG, "you have already set a retry view and would be instead of this new one.");
        }
        removeView(retryView);
        addView(view);
        mRetryView = view;
        return mRetryView;

    }

    public View setContentView(View view) {
        View contentView = mContentView;
        if (contentView != null) {
            Log.w(TAG, "you have already set a retry view and would be instead of this new one.");
        }
        removeView(contentView);
        addView(view);
        mContentView = view;
        return mContentView;
    }

    public View getRetryView() {
        return mRetryView;
    }

    public View getLoadingView() {
        return mLoadingView;
    }

    public View getContentView() {
        return mContentView;
    }

    public View getEmptyView() {
        return mEmptyView;
    }
}
