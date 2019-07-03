package com.ashlikun.supertoobar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/7/26　11:34
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：toolbar的action
 */
public abstract class Action {

    public static final int IMAGE_ID = 1125;
    public static final int TEXT_ID = 1125;
    protected Context context;
    protected FrameLayout actionView;
    protected int notificationTextColor;
    protected int notificationBagColor;
    protected int notificationStrokeColor;
    protected int actionPadding;
    protected int actionTextColor;
    protected int notificationMax = 99;
    protected int notificationNumber = -9999;
    protected SparseArray<Object> mKeyedTags;
    /**
     * 图标宽高
     */
    protected int width;
    protected int height;
    private TextView notificationTextView;
    private int gravity = Gravity.CENTER;

    public Action(SuperToolBar toolBar) {
        context = toolBar.getContext();
        notificationBagColor = toolBar.notificationBagColor;
        notificationTextColor = toolBar.notificationTextColor;
        notificationStrokeColor = toolBar.notificationStrokeColor;
        actionTextColor = toolBar.actionTextColor;
        actionPadding = toolBar.actionPadding;
        width = toolBar.actionWidth;
        height = toolBar.actionHeight;

        actionView = new FrameLayout(getContext());
        actionView.setTag(this);
        BarHelp.setForeground(SuperToolBar.CLICK_COLOR, actionView);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        actionView.setLayoutParams(params);
    }

    public Action set() {
        View acV = createView();
        if (acV.getLayoutParams() != null) {
            ((FrameLayout.LayoutParams) acV.getLayoutParams()).gravity = gravity;
        } else {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,
                    height);
            params.gravity = gravity;
            acV.setLayoutParams(params);
        }
        actionView.addView(acV);
        convert(actionView);
        return this;
    }

    public Context getContext() {
        return context;
    }

    public Action setNotificationMax(int notificationMax) {
        this.notificationMax = notificationMax;
        return this;
    }


    protected void addNotification() {
        if (notificationTextView == null) {
            notificationTextView = createNotification();
            actionView.addView(notificationTextView, getRightNumberParams());
        }
    }

    /**
     * 右边消息的参数
     *
     * @return
     */
    protected FrameLayout.LayoutParams getRightNumberParams() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, dip2px(14));
        params.setMargins(0, dip2px(4),
                dip2px(4), 0);
        params.gravity = Gravity.RIGHT | Gravity.TOP;
        return params;
    }

    /**
     * 创建一个消息的文本，红点提示
     */
    protected TextView createNotification() {
        notificationTextView = new TextView(context);
        updataNotification();
        return notificationTextView;
    }


    protected int dip2px(float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    /**
     * 创建通知的背景
     *
     * @return
     */
    protected Drawable createNotificationBag() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(notificationBagColor);
        drawable.setCornerRadius(dip2px(7));
        drawable.setStroke(dip2px(0.9f), notificationStrokeColor);
        return drawable;
    }


    /********************************************************************************************
     *                                           可以调用修改的属性
     ********************************************************************************************/
    /**
     * 获取整个布局
     *
     * @return
     */
    public FrameLayout getActionView() {
        return actionView;
    }

    public Action setGravity(int gravity) {
        this.gravity = gravity;
        return this;
    }

    /**
     * 设置消息
     */
    public Action setNotification(int number) {
        notificationNumber = number;
        addNotification();
        updataNotification();
        return this;
    }

    public Action setActionPadding(int actionPadding) {
        this.actionPadding = actionPadding;
        addNotification();
        updata();
        return this;
    }

    public Action setActionTextColor(int actionTextColor) {
        this.actionTextColor = actionTextColor;
        updata();
        return this;
    }

    public Action setNotificationTextColor(int notificationTextColor) {
        this.notificationTextColor = notificationTextColor;
        addNotification();
        updataNotification();
        return this;
    }

    public Action setNotificationBagColor(int notificationBagColor) {
        this.notificationBagColor = notificationBagColor;
        addNotification();
        updataNotification();
        return this;
    }

    public Action setNotificationStrokeColor(int notificationStrokeColor) {
        this.notificationStrokeColor = notificationStrokeColor;
        addNotification();
        updataNotification();
        return this;
    }

    public Action setTag(int key, Object obj) {
        if (mKeyedTags == null) {
            mKeyedTags = new SparseArray<>();
        }
        mKeyedTags.put(key, obj);
        return this;
    }

    public Object getTag(int key) {
        if (mKeyedTags == null) {
            mKeyedTags = new SparseArray<>();
        }
        if (mKeyedTags != null) {
            return mKeyedTags.get(key);
        }
        return null;
    }

    /********************************************************************************************
     *                                           可以重写的属性
     ********************************************************************************************/

    /**
     * 自己设置view的属性
     *
     * @param view
     */
    protected void convert(FrameLayout view) {

    }

    /**
     * 刷新通知
     */
    protected void updataNotification() {
        if (notificationTextView != null) {
            notificationTextView.setTextSize(8);
            notificationTextView.setBackground(createNotificationBag());
            notificationTextView.setGravity(Gravity.CENTER);
            notificationTextView.setMinWidth(dip2px(14));
            notificationTextView.setPadding(dip2px(2), 0,
                    dip2px(2), 0);
            notificationTextView.setTextColor(notificationTextColor);
            BarHelp.setNotification(notificationTextView, notificationNumber, notificationMax);
        }
    }

    /**
     * 更新这个action
     */
    public abstract void updata();

    /**
     * 创建一个View
     *
     * @return
     */
    protected abstract View createView();

    public interface OnActionClick {
        /**
         * 当点击的时候
         *
         * @param action
         */
        void onActionClick(int index, Action action);
    }

}
