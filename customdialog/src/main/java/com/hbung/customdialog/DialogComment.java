package com.hbung.customdialog;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.hanbang.baseproject.code.base.view.activity.BaseActivity;
import com.hbung.flatbutton.FlatButton;
import com.hbung.utils.animator.AnimUtils;
import com.hbung.utils.other.StringUtils;
import com.hbung.utils.ui.ScreenInfoUtils;
import com.hbung.utils.ui.ToastUtils;
import com.hbung.utils.ui.UiUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


/**
 * 作者　　: 李坤
 * 创建时间: 2017/1/6 11:45
 * <p>
 * 方法功能：评论框
 */


public class DialogComment extends Dialog {
    @BindView(R.id.esotericName)
    EditText textInputLayout;

    @BindView(R.id.item_issuer_comment_send)
    FlatButton send;

    private OnSendCallback clickCallback;


    public DialogComment(BaseActivity context) {
        super(context, R.style.Dialog_Botton_Form_top);
        setContentView(R.layout.base_dialog_comment);
        ButterKnife.bind(this, this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams wlp = getWindow().getAttributes();
        ScreenInfoUtils screen = new ScreenInfoUtils();
        wlp.width = screen.getWidth();
        wlp.height = ActionBar.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(wlp);
        getWindow().getAttributes().gravity = Gravity.BOTTOM;

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickCallback != null) {
                    if (StringUtils.isEmpty(textInputLayout.getText().toString())) {
                        AnimUtils.shakeLeft(textInputLayout, 0.85f, 6);
                        ToastUtils.showShort(getContext(), "请输入发送的内容！");
                    } else {
                        clickCallback.onSend(textInputLayout.getText().toString());
                        dismiss();
                    }
                }
            }
        });
        setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Observable.timer(100, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                UiUtils.showInput(textInputLayout);
                            }
                        });

            }
        });

    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        UiUtils.exitInput(textInputLayout);
        super.dismiss();
    }


    public void setSendText(CharSequence sendText) {
        send.setText(sendText);
    }


    public void setSendCallback(OnSendCallback clickCallback) {

        this.clickCallback = clickCallback;
    }


    public interface OnSendCallback {

        void onSend(String content);
    }


}
