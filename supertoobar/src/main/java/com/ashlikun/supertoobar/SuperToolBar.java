package com.ashlikun.supertoobar;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * 作者　　: 李坤
 * 创建时间: 2018/6/4 0004　下午 3:58
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：标题栏view，新加的，没有的功能可以扩展
 */

public class SuperToolBar extends FrameLayout {

    public static final String ACTION_LAYOUT = "ACTION_LAYOUT";
    public static final int CLICK_COLOR = 0x88aaaaaa;
    /**
     * 是否设置了状态栏透明
     */
    protected boolean setTranslucentStatusBarPaddingTop = false;
    public static final int DEFAULT_BACKIMAGE = R.drawable.material_back;
    /**
     * 3个主要的Layout
     */
    protected FrameLayout leftLayout;
    protected FrameLayout centerLayout;
    protected FrameLayout rightLayout;
    protected int barHeight;
    protected int statusHeight;
    protected TextView titleView;
    protected ImageView backButton;
    protected int titleColor = 0xff333333;
    protected int backImage = DEFAULT_BACKIMAGE;
    protected float titleSize = 18;
    protected CharSequence title;
    protected Paint linePaint;
    /**
     * 底部的线
     *
     * @param context
     */
    protected int bottonLineColor = 0xffededed;
    protected int notificationBagColor = 0xffff0000;
    protected int notificationTextColor = 0xffffffff;
    protected int notificationStrokeColor = 0xffffffff;
    protected int actionTextColor = 0xffffffff;
    /**
     * 透明状态栏，小于安卓M的状态栏背景
     */
    protected int androidMTranslucentStatusBar = 0x66aaaaaa;
    protected int backImgColor = 0;
    protected boolean isSetBackImgColor = false;
    protected int bottonLineHeight = dip2px(0.5f);
    protected int actionPadding = dip2px(8f);
    /**
     * action的宽高,一般只用于图片
     */
    protected int actionWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
    protected int actionHeight = ViewGroup.LayoutParams.MATCH_PARENT;

    private Action.OnActionClick onActionClickListener;

    public SuperToolBar(@NonNull Context context) {
        this(context, null);
    }

    public SuperToolBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SuperToolBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            int barHeightT = MeasureSpec.getSize(heightMeasureSpec);
            if (barHeightT > barHeight) {
                barHeight = barHeightT;
            }
        }
        int height = barHeight + (setTranslucentStatusBarPaddingTop ? getStatusHeight() : 0);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final MarginLayoutParams leftLp = (MarginLayoutParams) leftLayout.getLayoutParams();
        final MarginLayoutParams rightLp = (MarginLayoutParams) rightLayout.getLayoutParams();
        final MarginLayoutParams centerLp = (MarginLayoutParams) centerLayout.getLayoutParams();
        int leftRightSize = leftLayout.getMeasuredWidth() + leftLp.leftMargin + leftLp.rightMargin
                + rightLayout.getMeasuredWidth() + rightLp.leftMargin + rightLp.rightMargin
                + getPaddingLeft() + getPaddingRight()
                + centerLp.leftMargin - centerLp.rightMargin;
        //如果中间layout放不下,就强制设置成最大值
        if (centerLayout.getMeasuredWidth() >= getMeasuredWidth() - leftRightSize) {
            centerLayout.measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth() - leftRightSize, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(centerLayout.getMeasuredHeight(), MeasureSpec.EXACTLY));
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        final MarginLayoutParams leftLp = (MarginLayoutParams) leftLayout.getLayoutParams();
        final MarginLayoutParams rightLp = (MarginLayoutParams) rightLayout.getLayoutParams();
        final MarginLayoutParams centerLp = (MarginLayoutParams) centerLayout.getLayoutParams();
        int leftSize = leftLayout.getMeasuredWidth() + leftLp.leftMargin + leftLp.rightMargin;
        int rightSize = rightLayout.getMeasuredWidth() + rightLp.leftMargin + rightLp.rightMargin;
        int leftRightSize = Math.max(leftSize, rightSize) * 2
                + getPaddingLeft() + getPaddingRight()
                + centerLp.leftMargin - centerLp.rightMargin;
        //如果中间layout放不下,就强制设置成最大值
        if (centerLayout.getMeasuredWidth() >= getMeasuredWidth() - leftRightSize) {
            int newLeft = 0;
            int newRight = 0;
            if (leftSize > rightSize) {
                //左边大
                newLeft = leftLayout.getRight() + centerLp.leftMargin;
                newRight = newLeft + centerLayout.getMeasuredWidth();
            } else {
                //右边大
                newRight = rightLayout.getLeft() - centerLp.rightMargin;
                newLeft = newRight - centerLayout.getMeasuredWidth();
            }
            centerLayout.layout(newLeft, centerLayout.getTop(), newRight, centerLayout.getBottom());
        }
    }

    /**
     * 获得状态栏的高度
     *
     * @return
     */
    public int getStatusHeight() {
        int result = 0;
        int resourceId = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getContext().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制底部的线
        if (bottonLineHeight > 0) {
            linePaint.setColor(bottonLineColor);
            canvas.drawRect(0, getHeight() - bottonLineHeight, getWidth(), getHeight(), linePaint);
        }
        //绘制半透明状态栏
        if (setTranslucentStatusBarPaddingTop && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            linePaint.setColor(androidMTranslucentStatusBar);
            canvas.drawRect(0, 0, getWidth(), statusHeight, linePaint);
        }
    }

    private int dip2px(float dipValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private void initView(Context context, AttributeSet attrs) {
        barHeight = dip2px(45);
        if (getBackground() == null) {
            //默认colorPrimary颜色
//            TypedValue typedValue = new TypedValue();
//            context.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
            setBackgroundColor(getResources().getColor(R.color.supertoolbar_backgroung_color));
        }
        TypedArray a = context.obtainStyledAttributes(
                attrs,
                R.styleable.SuperToolBar);
        titleColor = a.getColor(R.styleable.SuperToolBar_stb_titleColor, titleColor);
        titleSize = a.getDimension(R.styleable.SuperToolBar_stb_titleSize, dip2px(titleSize));
        title = a.getString(R.styleable.SuperToolBar_stb_title);
        if (a.hasValue(R.styleable.SuperToolBar_stb_backImgColor)) {
            isSetBackImgColor = true;
        }
        backImgColor = a.getColor(R.styleable.SuperToolBar_stb_backImgColor, backImgColor);
        bottonLineColor = a.getColor(R.styleable.SuperToolBar_stb_bottonLineColor, bottonLineColor);
        notificationBagColor = a.getColor(R.styleable.SuperToolBar_stb_notificationBagColor, notificationBagColor);
        notificationTextColor = a.getColor(R.styleable.SuperToolBar_stb_notificationTextColor, notificationTextColor);
        bottonLineHeight = (int) a.getDimension(R.styleable.SuperToolBar_stb_bottonLineHeight, bottonLineHeight);
        backImage = a.getResourceId(R.styleable.SuperToolBar_stb_backImg, backImage);
        actionTextColor = a.getColor(R.styleable.SuperToolBar_stb_actionTextColor, actionTextColor);
        actionPadding = (int) a.getDimension(R.styleable.SuperToolBar_stb_actionPadding, actionPadding);
        actionWidth = (int) a.getDimension(R.styleable.SuperToolBar_stb_actionWidth, actionWidth);
        actionHeight = (int) a.getDimension(R.styleable.SuperToolBar_stb_actionHeight, actionHeight);
        notificationStrokeColor = a.getColor(R.styleable.SuperToolBar_stb_notificationStrokeColor, notificationStrokeColor);
        androidMTranslucentStatusBar = a.getColor(R.styleable.SuperToolBar_stb_androidMTranslucentStatusBarColor, androidMTranslucentStatusBar);
        a.recycle();

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.FILL);

        setBottonLine(bottonLineHeight);
        //这3个布局都是Fragment
        addMainLayout();
        initLeftLayout();
        initCenterLayout();
        initRightLayout();
    }

    /**
     * 初始化左边layout
     * 按需添加,默认添加返回键
     */
    protected void initLeftLayout() {
        leftLayout.removeAllViews();
    }

    /**
     * 初始化中间layout
     * 按需添加,默认标题
     */
    protected void initCenterLayout() {
        centerLayout.removeAllViews();
        titleView = new TextView(getContext());
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
        titleView.setTextColor(titleColor);
        titleView.setMaxLines(1);
        titleView.setEllipsize(TextUtils.TruncateAt.END);
        titleView.setGravity(Gravity.CENTER);
        if (title != null) {
            titleView.setText(title);
        }
        LayoutParams titleParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        centerLayout.addView(titleView, titleParams);
    }

    /**
     * 初始化右边layout
     * 按需添加
     */
    protected void initRightLayout() {
        rightLayout.removeAllViews();
    }

    /**
     * 3个主要的Layout
     */
    private void addMainLayout() {
        leftLayout = new FrameLayout(getContext());
        centerLayout = new FrameLayout(getContext());
        rightLayout = new FrameLayout(getContext());
        LayoutParams leftParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        leftParams.gravity = Gravity.LEFT;
        addView(leftLayout, leftParams);
        LayoutParams centerParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        centerParams.gravity = Gravity.CENTER;
        addView(centerLayout, centerParams);
        LayoutParams rightParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        rightParams.gravity = Gravity.RIGHT;
        addView(rightLayout, rightParams);
    }

    public FrameLayout getLeftLayout() {
        return leftLayout;
    }

    public FrameLayout getCenterLayout() {
        return centerLayout;
    }

    public FrameLayout getRightLayout() {
        return rightLayout;
    }

    /**
     * 获取actionLayou
     *
     * @return
     */
    public LinearLayout getActionLayout() {
        LinearLayout actionLayout = rightLayout.findViewWithTag(ACTION_LAYOUT);
        if (actionLayout == null) {
            actionLayout = new LinearLayout(getContext());
            actionLayout.setOrientation(LinearLayout.HORIZONTAL);
            actionLayout.setTag(ACTION_LAYOUT);
            rightLayout.setPadding(0, 0, dip2px(5), 0);
            rightLayout.addView(actionLayout, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
        }
        return actionLayout;
    }

    /**
     * 设置底部的线
     *
     * @param height
     */
    public SuperToolBar setBottonLine(int height) {
        //高度大于0就可以绘制
        bottonLineHeight = height;
        if (bottonLineHeight > 0) {
            setWillNotDraw(false);
            setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), bottonLineHeight);
        }
        invalidate();
        return this;
    }

    /**
     * 设置底部的线的颜色
     *
     * @param color
     * @return
     */
    public SuperToolBar setBottonLineColor(int color) {
        bottonLineColor = color;
        if (linePaint != null) {
            linePaint.setColor(bottonLineColor);
        }
        invalidate();
        return this;
    }
    /********************************************************************************************
     *                                           设置左边的控件
     ********************************************************************************************/
    /**
     * 设置返回图片
     */
    public void setBackImage(@DrawableRes int backImage) {
        this.backImage = backImage;
        if (backButton != null) {
            //自动计算返回键颜色
            if (backImage == DEFAULT_BACKIMAGE && !isSetBackImgColor && getBackground() instanceof ColorDrawable) {
                int color = ((ColorDrawable) getBackground()).getColor();
                backImgColor = BarHelp.isColorDrak(color) ? 0xffffffff : 0xff000000;
            }
            if (isSetBackImgColor) {
                backButton.setImageDrawable(BarHelp.getTintDrawable(getResources().getDrawable(backImage), backImgColor));
            } else {
                backButton.setImageResource(backImage);
            }
        }
    }

    /**
     * 设置返回
     */
    public void setBack(final Activity activity) {
        if (backButton == null) {
            backButton = new ImageView(getContext());
            backButton.setPadding(dip2px(12), 0,
                    dip2px(12), 0);
            FrameLayout.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.CENTER_VERTICAL | Gravity.LEFT;
            leftLayout.addView(backButton, params);
        }
        setBackImage(backImage);
        if (activity != null) {
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.onBackPressed();
                }
            });
        }
    }

    /**
     * 获取返回view
     *
     * @return
     */
    public ImageView getBackButton() {
        return backButton;
    }

    /********************************************************************************************
     *                                           设置中间的控件
     ********************************************************************************************/

    public SuperToolBar setTitle(String title) {
        if (title == null) {
            return this;
        }
        this.title = title;
        titleView.setText(title);
        return this;
    }

    public TextView getTitleView() {
        return titleView;
    }

    public void setTitleColor(int titleColor) {
        this.titleColor = titleColor;
        if (titleView != null) {
            titleView.setTextColor(titleColor);
        }
    }

    public void setTitleSize(float titleSize) {
        this.titleSize = titleSize;
        if (titleView != null) {
            titleView.setTextSize(titleSize);
        }
    }
    /********************************************************************************************
     *                                           设置右边的控件(Action)
     ********************************************************************************************/
    /**
     * 移除全部action
     */
    public void removeAllActions() {
        getActionLayout().removeAllViews();
    }

    /**
     * 移除指定的action
     */
    public void removeActionAt(int index) {
        getActionLayout().removeViewAt(index);
    }

    /**
     * 移除指定的action
     */
    public void removeAction(Action action) {
        View view = getViewByAction(action);
        if (view != null) {
            getActionLayout().removeView(view);
        }
    }

    /**
     * 获取action的个数
     */
    public int getActionCount() {
        return getActionLayout().getChildCount();
    }

    /**
     * 添加一个Action view
     */
    public Action addAction(final Action action, final int index) {
        if (action.getActionView() == null) {
            action.set();
        }
        action.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onActionClickListener != null) {
                    onActionClickListener.onActionClick(index, action);
                }
            }
        });
        if (action.getActionView().getLayoutParams() == null) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            action.getActionView().setLayoutParams(params);
        }
        getActionLayout().addView(action.getActionView(), index);
        return action;
    }

    /**
     * 添加多个actionView
     */
    public void addActions(ArrayList<Action> actionList) {
        int actions = actionList.size();
        for (int i = 0; i < actions; i++) {
            addAction(actionList.get(i), i);
        }
    }

    public Action addAction(Action action) {
        final int index = getActionLayout().getChildCount();
        return addAction(action, index);
    }

    public View getViewByAction(Action action) {
        return getActionLayout().findViewWithTag(action);
    }

    /**
     * 获取某个Action
     *
     * @param index
     * @param <T>
     * @return
     */
    public <T extends Action> T getAction(int index) {
        if (index < getActionCount()) {
            View view = getActionLayout().getChildAt(index);
            return (T) view.getTag();
        }
        return null;
    }

    public void setNotificationMax(int max) {
        int maxCount = getActionCount();
        for (int index = 0; index < maxCount; index++) {
            Action action = getAction(index);
            if (action != null) {
                action.setNotificationMax(max);
            }
        }
    }

    public void setNotification(int index, int number) {
        Action action = getAction(index);
        if (action != null) {
            action.setNotification(number);
        }
    }


    /********************************************************************************************
     *                                           其他方法
     ********************************************************************************************/
    /**
     * 设置透明状态栏时候，顶部的padding
     *
     * @return
     */
    public SuperToolBar setTranslucentStatusBarPaddingTop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setTranslucentStatusBarPaddingTop = true;
            statusHeight = getStatusHeight();
            setPadding(getPaddingLeft(), statusHeight, getPaddingRight(), getPaddingBottom());
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                //6.0以下绘制半透明,因为不能设置状态栏字体颜色
                setWillNotDraw(false);
            }
            requestLayout();
        }
        return this;
    }

    /**
     * 设置返回键颜色
     *
     * @param backImgColor
     */
    public void setBackImgColor(int backImgColor) {
        this.backImgColor = backImgColor;
        setBackImage(backImage);
    }

    /********************************************************************************************
     *                                           事件
     ********************************************************************************************/

    public void setOnActionClickListener(Action.OnActionClick onActionClickListener) {
        this.onActionClickListener = onActionClickListener;
    }


    /********************************************************************************************
     *                                           接口
     ********************************************************************************************/

    public interface OnLeftClickListener {
        void onLeftClick(View view);
    }
}