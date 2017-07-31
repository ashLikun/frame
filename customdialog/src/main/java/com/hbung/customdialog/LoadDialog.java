package com.hbung.customdialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.hbung.utils.ui.DrawableUtils;
import com.hbung.utils.ui.UiUtils;


/**
 * Created by Administrator on 2016/7/29.
 */

public class LoadDialog extends Dialog {
    public LoadDialog(Context context) {
        super(context, R.style.Dialog_Loadding);
        setContentView(R.layout.base_dialog_loadding);
        getWindow().getAttributes().gravity = Gravity.CENTER;
        getWindow().setBackgroundDrawable(new DrawableUtils(getContext()).getGradientDrawable(R.color.white_90, 0, 10, 0));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UiUtils.applyFont(getWindow().getDecorView().findViewById(
                android.R.id.content));

    }

    /*
   * 设置内容
   */
    public LoadDialog setContent(CharSequence content) {
        TextView c = ((TextView) findViewById(R.id.content));
        if (c != null && content != null) {
            c.setText(content);
        }
        if (c != null && content == null) {
            c.setVisibility(View.GONE);
        }
        return this;
    }
}
