package com.hbung.supertoobar;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by Administrator on 2016/8/26.
 */

public class SupperToolBar extends Toolbar {
    private TextView titleView;
    private int mTitleTextAppearance;

    public SupperToolBar(Context context) {
        this(context, null);
    }

    public SupperToolBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SupperToolBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                android.support.v7.appcompat.R.styleable.Toolbar, defStyleAttr, 0);
        mTitleTextAppearance = a.getResourceId(android.support.v7.appcompat.R.styleable.Toolbar_titleTextAppearance, 0);
        a.recycle();
        initView();
    }

    private void initView() {
        if (mTitleTextAppearance != 0 && titleView != null) {
            titleView.setTextAppearance(getContext(), mTitleTextAppearance);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(null);
        setSubtitle(null);
        addTitleView();
        titleView.setText(title);

    }

    public void setSuperTitle(CharSequence title) {
        super.setTitle(title);
        if (titleView != null) {
            titleView.setText("");
        }


    }

    public void setTitleAlpha(@FloatRange(from = 0.0, to = 1.0) float res) {
        titleView.setAlpha(res);
    }

    public void setTitleColor(@ColorRes int res) {
        addTitleView();
        titleView.setTextColor(getResources().getColor(res));
    }

    public void setTitleSize(@DimenRes int res) {
        addTitleView();
        titleView.setTextColor(px2sp(getContext(), getResources().getDimension(res)));
    }


    private void addTitleView() {
        if (titleView == null) {
            titleView = new TextView(getContext());
            titleView.setGravity(Gravity.CENTER);
            titleView.setMaxLines(1);
            titleView.setEllipsize(TextUtils.TruncateAt.END);
            LayoutParams lp = new LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER;
            //lp.setMargins(0, 0, (int) (getResources().getDimension(R.dimen.dp_6)), 0);
            titleView.setLayoutParams(lp);
            addView(titleView);
        }
        if (mTitleTextAppearance != 0) {
            titleView.setTextAppearance(getContext(), mTitleTextAppearance);
        }
    }

    @Override
    public void setTitleTextColor(@ColorInt int color) {
        super.setTitleTextColor(color);
        if (titleView != null) {
            titleView.setTextColor(color);
        }
    }

    @Override
    public void setTitleTextAppearance(Context context, @StyleRes int resId) {
        super.setTitleTextAppearance(context, resId);
        mTitleTextAppearance = resId;
        if (titleView != null) {
            titleView.setTextAppearance(context, resId);
        }
    }

    public MenuItem addAction(int postion, String title) {
        return addAction(postion, title, 0);
    }

    /**
     * SHOW_AS_ACTION_ALWAYS //总是作为Action项显示
     * SHOW_AS_ACTION_IF_ROOM //空间足够时显示
     * SHOW_AS_ACTION_NEVER //永远不作为Action项显示
     * SHOW_AS_ACTION_WITH_TEXT //显示Action项的文字部分
     *
     * @param postion
     * @param title
     * @param icon
     * @return
     */
    public MenuItem addAction(int postion, String title, @DrawableRes int icon) {
        MenuItem menuItem = getMenu().add(0, postion, 0, title);
        if (icon > 0) {
            menuItem.setIcon(icon);
        }
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) titleView.getLayoutParams();
//
//        layoutParams.setMargins(0, 0, (int) (layoutParams.rightMargin + getResources().getDimension(R.dimen.dp_24)), 0);
        return menuItem;
    }


    public void setBack(final Activity context) {
        setNavigationIcon(R.drawable.material_back);
        setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.onBackPressed();
            }
        });
    }

    public static class LayoutParams extends Toolbar.LayoutParams {

        private int mViewType;

        public LayoutParams(@NonNull Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
            mViewType = 2;
        }

        public LayoutParams(int width, int height, int gravity) {
            super(width, height);
        }

        public LayoutParams(int gravity) {
            this(WRAP_CONTENT, MATCH_PARENT, gravity);
        }

        public LayoutParams(Toolbar.LayoutParams source) {
            super(source);

        }

        public LayoutParams(ActionBar.LayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
            // ActionBar.LayoutParams doesn't have a MarginLayoutParams constructor.
            // Fake it here and copy over the relevant data.
            copyMarginsFromCompat(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        void copyMarginsFromCompat(ViewGroup.MarginLayoutParams source) {
            this.leftMargin = source.leftMargin;
            this.topMargin = source.topMargin;
            this.rightMargin = source.rightMargin;
            this.bottomMargin = source.bottomMargin;
        }
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2016/9/29 15:48
     * <p>
     * 方法功能：软键盘与fitssystemwindows= “true”  的冲突，会把appbar拉升
     * 解决办法去掉fitssystemwindows   在同等级的view上添加一个view背景色就是activity的状态栏颜色
     */

    public void setStatusViewHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setPadding(getPaddingLeft(), getStatusHeight(), getPaddingRight(), getPaddingBottom());
        }
    }

    private int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
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
}
