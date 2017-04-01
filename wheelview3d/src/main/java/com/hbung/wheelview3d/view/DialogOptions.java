package com.hbung.wheelview3d.view;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hbung.wheelview3d.R;

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


    private String negativeText;
    private int negativeTextSize;

    public DialogOptions(Context context) {
        this(context, R.style.Dialog_Option);
    }

    public DialogOptions(Context context, int themeResId) {
        super(context, themeResId);
        if (themeResId == R.style.Dialog_Option) {
            animResId = R.style.pickerview_dialogAnim;
        }
        init(context);
    }

    private void init(Context context) {
        if (animResId != 0) {
            getWindow().setWindowAnimations(animResId);
        }
        if (layoutId == 0) {
            layoutId = R.layout.pickerview_options;
        }
        rootView = (ViewGroup) LayoutInflater.from(context).inflate(layoutId, null);
        if (topbarLayoutId > 0) {
            View topBar = LayoutInflater.from(context).inflate(topbarLayoutId, null);
            if (topBar != null) {
                rootView.removeViewAt(0);
                //加入rootView
                rootView.addView(topBar, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }
        loopOptions = new LoopOptions(rootView.findViewById(R.id.pickeroptions));
        setContentView(rootView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        negativeButton = (Button) findViewById(R.id.negativeButton);
        positiveButton = (Button) findViewById(R.id.positiveButton);
        titleTextView = (TextView) findViewById(R.id.titleTv);

        negativeButton.setOnClickListener(this);
        positiveButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.negativeButton) {

        } else if (v.getId() == R.id.positiveButton) {

        }
    }

    public static class Builder {

    }


    /**
     * 作者　　: 李坤
     * 创建时间: 2016/12/12 9:52
     * <p>
     * 方法功能：ripple
     */
    public Drawable createRipple(Context c, int normal, int pressed) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ColorStateList colorList = new ColorStateList(new int[][]{{}}, new int[]{c.getResources().getColor(pressed)});
            ColorDrawable content = new ColorDrawable(c.getResources().getColor(normal));
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
