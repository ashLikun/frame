package com.ashlikun.supertoobar;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * 作者　　: 李坤
 * 创建时间: 2018/6/4 0004　下午 3:58
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：标题栏view，新加的，没有的功能可以扩展
 */

public class SupperToolBar extends FrameLayout {
    /**
     * 是否设置了状态栏透明
     */
    protected boolean setTranslucentStatusBarPaddingTop = false;
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
    protected int backImage = R.drawable.material_back;
    protected float titleSize = 18;
    protected CharSequence title;
    protected @DrawableRes
    int rightIconId;
    protected Paint linePaint;
    /**
     * 底部的线
     *
     * @param context
     */
    protected int bottonLineColor = 0xffededed;
    protected int notificationBagColor = 0xffff6600;
    protected int notificationTextColor = 0xffffffff;
    protected int backImgColor = -1;
    protected int bottonLineHeight = dip2px(0.5f);


    private OnRightClickListener rightClickListener;
    private OnLeftClickListener leftClickListener;

    public SupperToolBar(@NonNull Context context) {
        this(context, null);
    }

    public SupperToolBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SupperToolBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
        if (setTranslucentStatusBarPaddingTop) {
            linePaint.setColor(0x88aaaaaa);
            canvas.drawRect(0, 0, getWidth(), statusHeight, linePaint);
        }
    }

    private int dip2px(float dipValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        barHeight = dip2px(45);
        if (getBackground() == null) {
            //默认colorPrimary颜色
            setBackgroundColor(typedValue.data);
        }
        TypedArray a = context.obtainStyledAttributes(
                attrs,
                R.styleable.TitleBarView);
        titleColor = a.getColor(R.styleable.TitleBarView_stb_titleColor, titleColor);
        titleSize = a.getDimension(R.styleable.TitleBarView_stb_titleSize, dip2px(titleSize));
        title = a.getString(R.styleable.TitleBarView_stb_title);
        rightIconId = a.getResourceId(R.styleable.TitleBarView_stb_rightIcon, 0);
        backImgColor = a.getColor(R.styleable.TitleBarView_stb_backImgColor, backImgColor);
        bottonLineColor = a.getColor(R.styleable.TitleBarView_stb_bottonLineColor, bottonLineColor);
        notificationBagColor = a.getColor(R.styleable.TitleBarView_stb_notification_bagColor, notificationBagColor);
        notificationTextColor = a.getColor(R.styleable.TitleBarView_stb_notification_textColor, notificationTextColor);
        bottonLineHeight = (int) a.getDimension(R.styleable.TitleBarView_stb_bottonLineHeight, bottonLineHeight);
        backImage = a.getResourceId(R.styleable.TitleBarView_stb_backImg, backImage);
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
        //只有icon存在才去添加
        if (rightIconId > 0) {
            setRightImageViewIcon(rightIconId);
        }
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
        BarHelp.setForeground(0xffaaaaaa, rightLayout);
        BarHelp.setForeground(0xffaaaaaa, centerLayout);
        BarHelp.setForeground(0xffaaaaaa, leftLayout);
        addView(rightLayout, rightParams);

        rightLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightClickListener != null) {
                    rightClickListener.onRightClick(v);
                }
            }
        });
        leftLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (leftClickListener != null) {
                    leftClickListener.onLeftClick(v);
                }
            }
        });
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
     * 设置底部的线
     *
     * @param height
     */
    public SupperToolBar setBottonLine(int height) {
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
    public SupperToolBar setBottonLineColor(int color) {
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
     * 设置返回
     */
    public void setBack(final Activity activity) {
        if (backButton == null) {
            backButton = new ImageView(getContext());
            if (backImgColor != -1) {
                backButton.setImageDrawable(BarHelp.getTintDrawable(getResources().getDrawable(backImage), backImgColor));
            } else {
                backButton.setImageResource(backImage);
            }
            backButton.setPadding(dip2px(12), 0,
                    dip2px(12), 0);
            FrameLayout.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.CENTER_VERTICAL | Gravity.LEFT;
            leftLayout.addView(backButton, params);
        }
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onBackPressed();
            }
        });
    }

    /**
     * 设置左边icon
     *
     * @param iconId
     */
    public void setLeftIcon(@DrawableRes int iconId) {
        if (backButton == null) {
            backButton = new ImageView(getContext());
            backButton.setImageResource(iconId);
            backButton.setPadding(dip2px(12), 0,
                    dip2px(12), 0);
            FrameLayout.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.CENTER_VERTICAL | Gravity.LEFT;
            leftLayout.addView(backButton, params);
        }
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (leftClickListener != null) {
                    leftClickListener.onLeftClick(backButton);
                }
            }
        });
    }

    /********************************************************************************************
     *                                           设置中间的控件
     ********************************************************************************************/

    public SupperToolBar setTitle(String title) {
        if (title == null) {
            return this;
        }
        this.title = title;
        titleView.setText(title);
        return this;
    }

    /********************************************************************************************
     *                                           设置右边的控件
     ********************************************************************************************/
    /**
     * 设置右边文字
     */
    public SupperToolBar setRightTextView(CharSequence sequence) {
        return setRightTextView(sequence, 0xff333333);
    }

    /**
     * 设置右边文字
     */
    public SupperToolBar setRightTextView(CharSequence sequence, int color) {
        TextView rightTextView = rightLayout.findViewWithTag("rightTextView");
        if (rightTextView == null) {
            rightTextView = new TextView(getContext());
            rightTextView.setTextSize(15);
            rightTextView.setGravity(Gravity.CENTER);
            rightTextView.setCompoundDrawablePadding(dip2px(6));
        }
        rightTextView.setTextColor(color);
        rightTextView.setText(sequence);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.setMargins(0, 0, dip2px(4), 0);
        rightLayout.addView(rightTextView, params);
        return this;
    }


    /**
     * @author　　: 李坤
     * 创建时间: 2018/7/6 10:56
     * 邮箱　　：496546144@qq.com
     * <p>
     * 方法功能：设置右边icon按钮
     */

    public SupperToolBar setRightImageViewIcon(@DrawableRes int iconId) {
        //不占用主类的成员变量
        ImageView rightIcon = rightLayout.findViewWithTag("rightIcon");
        if (rightIcon == null) {
            rightIcon = new ImageView(getContext());
            rightIcon.setImageResource(iconId);
            int dp8 = dip2px(8);
            rightIcon.setPadding(dp8, dp8, dp8, dp8);
            LayoutParams titleParams = new LayoutParams(dip2px(38), LayoutParams.MATCH_PARENT);
            titleParams.gravity = Gravity.CENTER;
            rightLayout.addView(rightIcon, titleParams);
        }
        rightIcon.setImageResource(iconId);
        return this;
    }

    /**
     * 获取右边文本
     *
     * @return
     */
    public TextView getRightTextView() {
        TextView rightTextView = rightLayout.findViewWithTag("rightTextView");
        if (rightTextView == null) {
            setRightTextView("");
        }
        return rightTextView;
    }

    /**
     * 右边消息的参数
     *
     * @return
     */
    private LayoutParams getRightNumberParams() {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, dip2px(18));
        params.setMargins(0, dip2px(4),
                dip2px(4), 0);
        params.gravity = Gravity.RIGHT | Gravity.TOP;
        return params;
    }

    /**
     * 设置右边的消息数目
     */
    public SupperToolBar setRightNumber(int number) {
        return setRightNumber(number, 99);
    }

    public SupperToolBar setRightNumber(int number, int max) {
        TextView rightNumber = rightLayout.findViewWithTag("rightNumber");
        if (rightNumber == null) {
            rightNumber = createNotification(notificationTextColor, notificationBagColor);
            rightLayout.addView(rightNumber, getRightNumberParams());
        }
        BarHelp.setNotification(rightNumber, number, max);
        return this;
    }


    /********************************************************************************************
     *                                           其他方法
     ********************************************************************************************/
    /**
     * 设置透明状态栏时候，顶部的padding
     *
     * @return
     */
    public SupperToolBar setTranslucentStatusBarPaddingTop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatusBarPaddingTop = true;
            statusHeight = getStatusHeight();
            setPadding(getPaddingLeft(), statusHeight, getPaddingRight(), getPaddingBottom());
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                //5.0以下绘制半透明
                setWillNotDraw(false);
            }
            requestLayout();
        }
        return this;
    }

    /**
     * 创建一个消息的文本，红点提示
     */
    public TextView createNotification(int notificationTextColor, int notificationBagColor) {
        TextView view = new TextView(getContext());
        view.setTextSize(8);
        view.setBackground(BarHelp.getTintDrawable(getResources().getDrawable(R.drawable.supper_toolbar_notification),
                notificationBagColor));
        view.setGravity(Gravity.CENTER);
        view.setMinWidth(dip2px(17));
        view.setPadding(dip2px(2), 0,
                dip2px(2), 0);
        view.setTextColor(notificationTextColor);
        return view;
    }
    /********************************************************************************************
     *                                           事件
     ********************************************************************************************/
    /**
     * 设置右边点击事件监听
     */
    public void setRightClickListener(OnRightClickListener rightClickListener) {
        this.rightClickListener = rightClickListener;
    }

    /**
     * 设置左边点击事件监听
     */
    public void setLeftClickListener(OnLeftClickListener leftClickListener) {
        this.leftClickListener = leftClickListener;
    }

    /********************************************************************************************
     *                                           接口
     ********************************************************************************************/

    public interface OnRightClickListener {
        void onRightClick(View view);
    }

    public interface OnLeftClickListener {
        void onLeftClick(View view);
    }
}