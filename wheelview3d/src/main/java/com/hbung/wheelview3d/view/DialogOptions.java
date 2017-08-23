package com.hbung.wheelview3d.view;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.hbung.wheelview3d.R;
import com.hbung.wheelview3d.listener.OnItemSelectListener;
import com.hbung.wheelview3d.listener.OnPositiveClickListener;

/**
 * 作者　　: 李坤
 * 创建时间:2017/4/1　10:30
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：layoutId 自定义布局的时候一定要使用
 * id一致negativeButton，titleTv,positiveButton  和pickeroptions,options1，options2，options3
 */

public class DialogOptions extends Dialog implements View.OnClickListener {

    private ViewGroup rootView;
    private ViewGroup pickeroptions;
    private Button negativeButton;
    private Button positiveButton;
    private TextView titleTextView;
    private LoopOptions loopOptions;
    //动画属性ID
    private int animResId = 0;
    private int layoutId = 0;
    private int topbarLayoutId = 0;
    OnItemSelectListener onItemSelectListener;
    OnPositiveClickListener onPositiveClickListener;
    private String negativeText;
    private float negativeTextSize;
    private String positiveText;
    private float positiveTextSize;
    private float loopTextSize;//滚轮文字大小
    private float titleSize;//标题文字大小
    private String titleText;//标题文字

    private int noSelectTextColor; //分割线以外的文字颜色
    private int selectTextColor; //分割线之间的文字颜色
    private int dividerColor; //分割线的颜色
    // 条目间距倍数 默认2.5F
    private float lineSpacingMultiplier;
    //是否可以取消
    private boolean isCancelable;
    //是否联动
    private boolean isLinkage;
    private boolean isLoop;
    //对话框对齐方式
    private int gravity;

    private DialogOptions(Builder builder) {
        super(builder.context, builder.themeResId);
        this.animResId = builder.animResId;
        this.layoutId = builder.layoutId;
        this.topbarLayoutId = builder.topbarLayoutId;
        this.onItemSelectListener = builder.onItemSelectListener;
        this.onPositiveClickListener = builder.onPositiveClickListener;
        this.negativeText = builder.negativeText;
        this.negativeTextSize = builder.negativeTextSize;
        this.positiveText = builder.positiveText;
        this.positiveTextSize = builder.positiveTextSize;
        this.loopTextSize = builder.loopTextSize;
        this.titleSize = builder.titleSize;
        this.titleText = builder.titleText;
        this.noSelectTextColor = builder.noSelectTextColor;
        this.selectTextColor = builder.selectTextColor;
        this.dividerColor = builder.dividerColor;
        this.lineSpacingMultiplier = builder.lineSpacingMultiplier;
        this.isCancelable = builder.isCancelable;
        this.isLinkage = builder.isLinkage;
        this.isLoop = builder.isLoop;
        this.gravity = builder.gravity;
        init(builder);
    }


    //onCreate  之前的初始化
    private void init(Builder builder) {
        if (animResId != 0) {
            getWindow().setWindowAnimations(animResId);
        }
        if (layoutId == 0) {
            layoutId = R.layout.pickerview_options;
        }
        rootView = (ViewGroup) LayoutInflater.from(builder.context).inflate(layoutId, null);
        if (topbarLayoutId > 0) {
            View topBar = LayoutInflater.from(builder.context).inflate(topbarLayoutId, null);
            if (topBar != null) {
                rootView.removeViewAt(0);
                //加入rootView
                rootView.addView(topBar, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }
        loopOptions = new LoopOptions(rootView.findViewById(R.id.pickeroptions), onItemSelectListener);
        setContentView(rootView);
        setCancelable(isCancelable);
        loopOptions.setLoop(isLoop);
        loopOptions.setTextSize(loopTextSize);
        loopOptions.setLinkage(isLinkage);
        loopOptions.setLineSpacingMultiplier(lineSpacingMultiplier);
        loopOptions.setDividerColor(dividerColor);
        loopOptions.setNoSelectTextColor(dividerColor);
        loopOptions.setSelectTextColor(dividerColor);
        loopOptions.setMode(builder.mode);
        loopOptions.setInitPosition(builder.initPosition);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT; //设置宽度
        lp.height = (ActionBar.LayoutParams.WRAP_CONTENT); //设置宽度
        getWindow().setAttributes(lp);
        getWindow().getAttributes().gravity = Gravity.BOTTOM;
        initView();
    }

    //初始化view
    private void initView() {
        getWindow().setGravity(gravity);

        negativeButton = (Button) findViewById(R.id.negativeButton);
        positiveButton = (Button) findViewById(R.id.positiveButton);
        titleTextView = (TextView) findViewById(R.id.titleTv);

        if (negativeText != null && negativeText.length() > 0)
            negativeButton.setText(negativeText);
        if (negativeTextSize > 0)
            negativeButton.setTextSize(negativeTextSize);
        negativeButton.setBackgroundDrawable(createRipple(0x00000000, 0xff999999));
        if (positiveText != null && positiveText.length() > 0)
            positiveButton.setText(positiveText);
        if (positiveTextSize > 0)
            positiveButton.setTextSize(positiveTextSize);
        positiveButton.setBackgroundDrawable(createRipple(0x00000000, 0xff999999));

        if (titleText != null && titleText.length() > 0)
            titleTextView.setText(titleText);
        if (titleSize > 0)
            titleTextView.setTextSize(titleSize);

        negativeButton.setOnClickListener(this);
        positiveButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.negativeButton) {
            dismiss();
        } else if (v.getId() == R.id.positiveButton) {
            if (onPositiveClickListener != null) {
                onPositiveClickListener.onPositive(loopOptions.getOneData(), loopOptions.getTwoData(), loopOptions.getThreeData());
            }
            dismiss();
        }
    }

    //对话框数据设置
    public LoopOptions getLoopOptions() {
        return loopOptions;
    }

    public static class Builder {
        Context context;
        int themeResId = R.style.Dialog_Option;
        //动画属性ID
        private int animResId = 0;
        private int layoutId = 0;
        private int topbarLayoutId = 0;
        OnItemSelectListener onItemSelectListener;
        OnPositiveClickListener onPositiveClickListener;
        private String negativeText;
        private float negativeTextSize;
        private String positiveText;
        private float positiveTextSize;
        private float loopTextSize;//滚轮文字大小
        private float titleSize;//标题文字大小
        private String titleText;//标题文字

        private int noSelectTextColor; //分割线以外的文字颜色
        private int selectTextColor; //分割线之间的文字颜色
        private int dividerColor; //分割线的颜色
        // 条目间距倍数 默认2.5F
        private float lineSpacingMultiplier = 2.5F;
        //是否可以取消
        private boolean isCancelable = true;
        //是否联动
        private boolean isLinkage = true;
        private boolean isLoop = true;
        private int gravity = Gravity.BOTTOM;
        private LoopOptions.Mode mode = LoopOptions.Mode.THREE;
        private int initPosition = 0;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder themeResId(int themeResId) {
            this.themeResId = themeResId;
            return this;
        }

        public Builder animResId(int animResId) {
            this.animResId = animResId;
            return this;
        }

        public Builder layoutId(int layoutId) {
            this.layoutId = layoutId;
            return this;
        }

        public Builder topbarLayoutId(int topbarLayoutId) {
            this.topbarLayoutId = topbarLayoutId;
            return this;
        }

        public Builder onItemSelectListener(OnItemSelectListener onItemSelectListener) {
            this.onItemSelectListener = onItemSelectListener;
            return this;
        }

        public Builder onPositiveClickListener(OnPositiveClickListener onPositiveClickListener) {
            this.onPositiveClickListener = onPositiveClickListener;
            return this;
        }

        public Builder negativeText(String negativeText) {
            this.negativeText = negativeText;
            return this;
        }

        public Builder negativeTextSize(float negativeTextSize) {
            this.negativeTextSize = negativeTextSize;
            return this;
        }

        public Builder positiveText(String positiveText) {
            this.positiveText = positiveText;
            return this;
        }

        public Builder positiveTextSize(float positiveTextSize) {
            this.positiveTextSize = positiveTextSize;
            return this;
        }

        public Builder loopTextSize(float loopTextSize) {
            this.loopTextSize = loopTextSize;
            return this;
        }

        public Builder titleSize(float titleSize) {
            this.titleSize = titleSize;
            return this;
        }

        public Builder titleText(String titleText) {
            this.titleText = titleText;
            return this;
        }

        public Builder noSelectTextColor(int noSelectTextColor) {
            this.noSelectTextColor = noSelectTextColor;
            return this;
        }

        public Builder selectTextColor(int selectTextColor) {
            this.selectTextColor = selectTextColor;
            return this;
        }

        public Builder dividerColor(int dividerColor) {
            this.dividerColor = dividerColor;
            return this;
        }

        public Builder lineSpacingMultiplier(float lineSpacingMultiplier) {
            this.lineSpacingMultiplier = lineSpacingMultiplier;
            return this;
        }

        public Builder cancelable(boolean cancelable) {
            isCancelable = cancelable;
            return this;
        }

        public Builder linkage(boolean linkage) {
            isLinkage = linkage;
            return this;
        }

        public Builder loop(boolean loop) {
            this.isLoop = loop;
            return this;
        }

        public Builder gravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder mode(LoopOptions.Mode mode) {
            this.mode = mode;
            return this;
        }

        public Builder initPosition(int initPosition) {
            this.initPosition = initPosition;
            return this;
        }

        public DialogOptions builder() {
            if (themeResId == R.style.Dialog_Option) {
                animResId = R.style.pickerview_dialogAnim;
            }
            return new DialogOptions(this);
        }

    }


    /**
     * 作者　　: 李坤
     * 创建时间: 2016/12/12 9:52
     * <p>
     * 方法功能：ripple
     */
    public Drawable createRipple(int normal, int pressed) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ColorStateList colorList = new ColorStateList(new int[][]{{}}, new int[]{pressed});
            ColorDrawable content = new ColorDrawable(normal);
            RippleDrawable ripple = new RippleDrawable(colorList, content.getAlpha() == 0 ? null : content, content.getAlpha() == 0 ? new ColorDrawable(Color.WHITE) : null);
            return ripple;
        } else {
            return getStateListDrawable(normal, pressed, pressed);
        }
    }

    /**
     * 获取StateListDrawable实例
     *
     * @param idNormal    默认的资源id
     * @param idPressed   按下的资源id
     * @param isEnabledId 获取焦点的资源id
     */
    private StateListDrawable getStateListDrawable(int idNormal, int idPressed, int isEnabledId) {
        StateListDrawable bg = new StateListDrawable();
        ColorDrawable normal = idNormal == -1 ? null : new ColorDrawable(idNormal);
        ColorDrawable pressed = idPressed == -1 ? null : new ColorDrawable(idPressed);
        ColorDrawable enabled = isEnabledId == -1 ? null : new ColorDrawable(isEnabledId);
        // View.PRESSED_ENABLED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
        // View.ENABLED_FOCUSED_STATE_SET
        bg.addState(new int[]{-android.R.attr.state_enabled}, enabled);
        // View.ENABLED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_enabled}, normal);
        // View.FOCUSED_STATE_SET
        bg.addState(new int[]{android.R.attr.state_focused}, enabled);
        // View.EMPTY_STATE_SET
        bg.addState(new int[]{}, normal);
        return bg;
    }
}
