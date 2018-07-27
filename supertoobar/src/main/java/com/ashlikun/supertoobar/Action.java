package com.ashlikun.supertoobar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
    private static final int DEFAULT_ACTION_TEXT_SIZE = 15;
    protected Context context;
    protected FrameLayout actionView;
    protected TextView rightNumber;
    protected int notificationTextColor;
    protected int notificationBagColor;
    protected int notificationStrokeColor;
    protected int actionPadding;
    protected int actionTextColor;
    protected int notificationMax = 99;
    protected int notificationNumber = -9999;

    public Action(SupperToolBar toolBar) {
        context = toolBar.getContext();
        setNotificationBagColor(toolBar.notificationBagColor);
        setNotificationTextColor(toolBar.notificationTextColor);
        setNotificationStrokeColor(toolBar.notificationStrokeColor);
        setActionTextColor(toolBar.actionTextColor);
        setActionPadding(toolBar.actionPadding);
    }

    public Action set() {
        actionView = inflateAction();
        if (notificationNumber != -9999) {
            addNotification(actionView);
            BarHelp.setNotification(rightNumber, notificationNumber, notificationMax);
        }
        return this;
    }

    public Context getContext() {
        return context;
    }

    public Action setNotificationMax(int notificationMax) {
        this.notificationMax = notificationMax;
        return this;
    }

    /**
     * 设置消息
     */
    public Action setNotification(int number) {
        notificationNumber = number;
        return this;
    }

    protected void addNotification(FrameLayout view) {
        if (rightNumber == null) {
            rightNumber = createNotification(notificationTextColor, notificationBagColor);
            view.addView(rightNumber, getRightNumberParams());
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
    protected TextView createNotification(int notificationTextColor, int notificationBagColor) {
        TextView view = new TextView(context);
        view.setTextSize(8);
        view.setBackground(createNotificationBag());
        view.setGravity(Gravity.CENTER);
        view.setMinWidth(dip2px(14));
        view.setPadding(dip2px(2), 0,
                dip2px(2), 0);
        view.setTextColor(notificationTextColor);
        return view;
    }


    protected int dip2px(float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 添加一个action到右边的view
     */
    protected FrameLayout inflateAction() {
        FrameLayout view = new FrameLayout(getContext());
        if (TextUtils.isEmpty(getText())) {
            view.addView(createImage());
        } else {
            view.addView(convertText());
        }
        view.setTag(this);
        BarHelp.setForeground(SupperToolBar.CLICK_COLOR, view);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        convert(view);
        return view;
    }

    protected ImageView createImage() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        ImageView img = new ImageView(getContext());
        img.setImageDrawable(getDrawable());
        img.setPadding(actionPadding, actionPadding, actionPadding, actionPadding);
        img.setLayoutParams(params);
        return img;
    }

    protected TextView convertText() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        TextView text = new TextView(getContext());
        text.setGravity(Gravity.CENTER);
        text.setText(getText());
        text.setTextSize(DEFAULT_ACTION_TEXT_SIZE);
        text.setTextColor(actionTextColor);
        text.setPadding(actionPadding, actionPadding, actionPadding, actionPadding);
        text.setLayoutParams(params);
        return text;
    }

    public Action setNotificationTextColor(int notificationTextColor) {
        this.notificationTextColor = notificationTextColor;
        return this;
    }

    public Action setNotificationBagColor(int notificationBagColor) {
        this.notificationBagColor = notificationBagColor;
        return this;
    }

    public Action setNotificationStrokeColor(int notificationStrokeColor) {
        this.notificationStrokeColor = notificationStrokeColor;
        return this;
    }

    public Action setActionPadding(int actionPadding) {
        this.actionPadding = actionPadding;
        return this;
    }

    public Action setActionTextColor(int actionTextColor) {
        this.actionTextColor = actionTextColor;
        return this;
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

    public FrameLayout getActionView() {
        return actionView;
    }

    /**
     * 自己设置view的属性
     *
     * @param view
     */
    protected void convert(FrameLayout view) {

    }

    /**
     * 获取文本
     *
     * @return
     */
    protected abstract CharSequence getText();

    /**
     * 获取图标
     *
     * @return
     */
    protected abstract Drawable getDrawable();


    public interface OnActionClick {
        /**
         * 当点击的时候
         *
         * @param action
         */
        void onActionClick(int index, Action action);
    }

}
