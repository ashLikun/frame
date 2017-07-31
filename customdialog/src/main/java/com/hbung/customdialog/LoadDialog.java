package com.hbung.customdialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbung.utils.ui.DrawableUtils;
import com.hbung.utils.ui.UiUtils;

/**
 * 作者　　: 李坤
 * 创建时间: 2017/7/7 10:20
 * 邮箱　　：496546144@qq.com
 * <p>
 * 方法功能：加载的对话框
 */
public class LoadDialog extends Dialog {
    TextView msgView;
    ImageView imageView;
    AnimationDrawable drawable;
    int loaddingSize = 12;

    public LoadDialog(Context context) {
        super(context, R.style.Dialog_Loadding);
        setContentView(R.layout.base_dialog_loadding);
        getWindow().getAttributes().gravity = Gravity.CENTER;
        getWindow().setBackgroundDrawable(new DrawableUtils(getContext()).getGradientDrawable(R.color.customdialog_loadding_back, 10));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawable = new AnimationDrawable();
        UiUtils.applyFont(getWindow().getDecorView().findViewById(
                android.R.id.content));
        msgView = ((TextView) findViewById(R.id.dialog_load_msg));
        imageView = ((ImageView) findViewById(R.id.dialog_load_image));
        for (int i = 1; i <= loaddingSize; i++) {
            int pngId = getContext().getResources().getIdentifier(String.format("loading_%d", i), "drawable", getContext().getPackageName());
            drawable.addFrame(getContext().getResources().getDrawable(pngId), 200);
        }
        imageView.setImageDrawable(drawable);
        drawable.setOneShot(false);
        drawable.start();
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/7/7 16:23
     * 邮箱　　：496546144@qq.com
     * <p>
     * 方法功能：设置底部的提示
     */
    public LoadDialog setContent(CharSequence content) {

        if (content != null && msgView != null) {
            msgView.setText(content);
        }
        if (content != null && msgView == null) {
            msgView.setVisibility(View.GONE);
        }
        return this;
    }
}
