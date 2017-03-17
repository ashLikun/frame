package com.hbung.loadingandretrymanager;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zhy on 15/8/27.
 */
public class LoadingAndRetryManager {
    public static final int NO_LAYOUT_ID = 0;
    public static int BASE_LOADING_LAYOUT_ID = R.layout.base_load_loading;
    public static int BASE_RETRY_LAYOUT_ID = R.layout.base_load_retry;
    public static int BASE_EMPTY_LAYOUT_ID = R.layout.base_load_empty;

    private LoadingAndRetryLayout mLoadingAndRetryLayout;
    // 如果成功了那么Start方法就不会显示加载中的布局
    private boolean loadingCanShow = true;
    //标记是否只显示加载中一次
    private boolean loadingShowOne = true;

    public boolean isLoadingCanShow() {
        return loadingCanShow;
    }

    public void setLoadingCanShow(boolean loadingCanShow) {
        this.loadingCanShow = loadingCanShow;
    }

    public void setLoadingShowOne(boolean loadingShowOne) {
        this.loadingShowOne = loadingShowOne;
    }

    public OnLoadingAndRetryListener DEFAULT_LISTENER = new OnLoadingAndRetryListener() {
        @Override
        public void setRetryEvent(View retryView, ContextData data) {

        }

        @Override
        public void setLoadingEvent(View loadingView, ContextData data) {

        }

        @Override
        public void setEmptyEvent(View emptyView, ContextData data) {

        }
    };

    public static LoadingAndRetryManager getLoadingAndRetryManager(Object activityOrFragmentOrView, OnLoadingAndRetryListener listener) {
        if (activityOrFragmentOrView != null) {
            return new LoadingAndRetryManager(activityOrFragmentOrView, listener);
        }
        return null;
    }

    public LoadingAndRetryManager(Object activityOrFragmentOrView, OnLoadingAndRetryListener listener) {
        if (listener == null) listener = DEFAULT_LISTENER;

        ViewGroup contentParent = null;
        Context context;
        if (activityOrFragmentOrView instanceof Activity) {
            Activity activity = (Activity) activityOrFragmentOrView;
            context = activity;
            contentParent = (ViewGroup) activity.findViewById(android.R.id.content);
        } else if (activityOrFragmentOrView instanceof Fragment) {
            Fragment fragment = (Fragment) activityOrFragmentOrView;
            context = fragment.getActivity();
            contentParent = (ViewGroup) (fragment.getView().getParent());
        } else if (activityOrFragmentOrView instanceof View) {
            View view = (View) activityOrFragmentOrView;
            contentParent = (ViewGroup) (view.getParent());
            context = view.getContext();
        } else {
            throw new IllegalArgumentException("the argument's type must be Fragment or Activity: init(context)");
        }
        int childCount = contentParent.getChildCount();
        //get contentParent
        int index = 0;
        View oldContent;
        if (activityOrFragmentOrView instanceof View) {
            oldContent = (View) activityOrFragmentOrView;
            for (int i = 0; i < childCount; i++) {
                if (contentParent.getChildAt(i) == oldContent) {
                    index = i;
                    break;
                }
            }
        } else {
            oldContent = contentParent.getChildAt(0);
        }
        contentParent.removeView(oldContent);
        //setup content layout
        LoadingAndRetryLayout loadingAndRetryLayout = new LoadingAndRetryLayout(context);
        loadingAndRetryLayout.setLoadingAndRetryListener(listener);
        ViewGroup.LayoutParams lp = oldContent.getLayoutParams();
        contentParent.addView(loadingAndRetryLayout, index, lp);
        loadingAndRetryLayout.setContentView(oldContent);
        // setup loading,retry,empty layout
        setupLoadingLayout(listener, loadingAndRetryLayout);
        setupRetryLayout(listener, loadingAndRetryLayout);
        setupEmptyLayout(listener, loadingAndRetryLayout);
        //callback
        mLoadingAndRetryLayout = loadingAndRetryLayout;
        showContent();
        loadingCanShow = true;
    }

    private void setupEmptyLayout(OnLoadingAndRetryListener listener, LoadingAndRetryLayout loadingAndRetryLayout) {
        if (listener.isSetEmptyLayout()) {
            int layoutId = listener.generateEmptyLayoutId();
            if (layoutId != NO_LAYOUT_ID) {
                loadingAndRetryLayout.setEmptyView(layoutId);
            } else {
                loadingAndRetryLayout.setEmptyView(listener.generateEmptyLayout());
            }
        } else {
            if (BASE_EMPTY_LAYOUT_ID != NO_LAYOUT_ID)
                loadingAndRetryLayout.setEmptyView(BASE_EMPTY_LAYOUT_ID);
        }
    }

    private void setupLoadingLayout(OnLoadingAndRetryListener listener, LoadingAndRetryLayout loadingAndRetryLayout) {
        if (listener.isSetLoadingLayout()) {
            int layoutId = listener.generateLoadingLayoutId();
            if (layoutId != NO_LAYOUT_ID) {
                loadingAndRetryLayout.setLoadingView(layoutId);
            } else {
                loadingAndRetryLayout.setLoadingView(listener.generateLoadingLayout());
            }
        } else {
            if (BASE_LOADING_LAYOUT_ID != NO_LAYOUT_ID)
                loadingAndRetryLayout.setLoadingView(BASE_LOADING_LAYOUT_ID);
        }
    }

    private void setupRetryLayout(OnLoadingAndRetryListener listener, LoadingAndRetryLayout loadingAndRetryLayout) {
        if (listener.isSetRetryLayout()) {
            int layoutId = listener.generateRetryLayoutId();
            if (layoutId != NO_LAYOUT_ID) {
                loadingAndRetryLayout.setLoadingView(layoutId);
            } else {
                loadingAndRetryLayout.setLoadingView(listener.generateRetryLayout());
            }
        } else {
            if (BASE_RETRY_LAYOUT_ID != NO_LAYOUT_ID)
                loadingAndRetryLayout.setRetryView(BASE_RETRY_LAYOUT_ID);
        }
    }

    public static LoadingAndRetryManager generate(Object activityOrFragment, OnLoadingAndRetryListener listener) {
        return new LoadingAndRetryManager(activityOrFragment, listener);
    }

    public void showLoading(ContextData data) {
        if (loadingCanShow)
            mLoadingAndRetryLayout.showLoading(data);
    }

    public void showRetry(ContextData data) {
        mLoadingAndRetryLayout.showRetry(data);
        loadingCanShow = true;
    }

    public void showContent() {
        mLoadingAndRetryLayout.showContent();
        if (loadingShowOne)
            loadingCanShow = false;
    }

    public void showEmpty(ContextData data) {
        mLoadingAndRetryLayout.showEmpty(data);
        loadingCanShow = true;
    }


    public LoadingAndRetryLayout getmLoadingAndRetryLayout() {
        return mLoadingAndRetryLayout;
    }
}
