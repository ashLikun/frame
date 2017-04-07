package com.hbung.customdialog;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.hbung.utils.ui.ScreenInfoUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者　　: 李坤
 * 创建时间: 2016/9/19 16:28
 * <p>
 * 方法功能：从底部弹起的选择更多的兑换框
 */


public class DialogSelectMore extends Dialog {
    @BindView(R.id.dialog_select_item1)
    TextView tv_item1;
    @BindView(R.id.dialog_select_item2)
    TextView tv_item2;
    @BindView(R.id.dialog_select_cancel)
    TextView cancel;

    private String item1;
    private String item2;

    private OnClickCallback clickCallback;
    private Context context;


    public DialogSelectMore(Context context, String item1, String item2) {
        this(context, R.style.Dialog_select);
        this.item1 = item1;
        this.item2 = item2;
    }

    public DialogSelectMore(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        init();
    }

    private void init() {
        setContentView(R.layout.base_dialog_select_more);
        ButterKnife.bind(this, getWindow().getDecorView().findViewById(R.id.dialog_select_root));


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        ScreenInfoUtils screen = new ScreenInfoUtils();
        lp.width = (screen.getWidth()); //设置宽度
        lp.height = (ActionBar.LayoutParams.WRAP_CONTENT); //设置宽度
        getWindow().setAttributes(lp);
        getWindow().getAttributes().gravity = Gravity.BOTTOM;
        setAdapter();
    }


    private void setAdapter() {
        tv_item1.setText(item1);
        tv_item2.setText(item2);
    }


    @OnClick({R.id.dialog_select_item1, R.id.dialog_select_item2, R.id.dialog_select_cancel})
    public void onClick(View view) {

        if (clickCallback != null) {
            switch (view.getId()) {
                case R.id.dialog_select_item1:
                    clickCallback.onClick(1);
                    break;
                case R.id.dialog_select_item2:
                    clickCallback.onClick(2);
                    break;
                case R.id.dialog_select_cancel:
                    clickCallback.onClick(0);
                    break;
            }

        }
    }

    public void setClickCallback(OnClickCallback clickCallback) {

        this.clickCallback = clickCallback;
    }

    /**
     * type :1是第一个item,2是第二个item,0是取消
     */
    public interface OnClickCallback {

        void onClick(int type);
    }

}
