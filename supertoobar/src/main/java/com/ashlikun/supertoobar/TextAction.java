package com.ashlikun.supertoobar;

import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 作者　　: 李坤
 * 创建时间: 2018/7/26　11:36
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：图标action
 */
public class TextAction extends Action {
    public static final int DEFAULT_ACTION_TEXT_SIZE = 15;
    private CharSequence text = null;
    private TextView textView;

    public TextAction(SuperToolBar toolBar, CharSequence text) {
        super(toolBar);
        this.text = text;
    }

    public CharSequence getText() {
        return text;
    }

    /**
     * 创建一个TextView
     *
     * @return
     */
    @Override
    protected TextView createView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        textView = new TextView(getContext());
        updata();
        textView.setLayoutParams(params);
        return textView;
    }

    public void setText(CharSequence text) {
        this.text = text;
        updata();
    }

    /**
     * 获取TextView
     *
     * @return
     */
    public TextView getTextView() {
        return textView;
    }

    @Override
    protected void convert(FrameLayout view) {
    }

    @Override
    public void updata() {
        if (textView != null) {
            textView.setId(TEXT_ID);
            textView.setGravity(Gravity.CENTER);
            textView.setText(getText());
            textView.setTextSize(DEFAULT_ACTION_TEXT_SIZE);
            textView.setTextColor(actionTextColor);
            textView.setPadding(actionPadding, actionPadding, actionPadding, actionPadding);
        }
    }

}
