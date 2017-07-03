package com.hbung.utils.ui;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.BulletSpan;
import android.util.SparseArray;
import android.widget.EditText;


/**
 * 作者　　: 李坤
 * 创建时间: 2017/6/28 13:30
 * <p>
 * 方法功能：输入框检查工具
 * 1：最后再调用check正则判断
 * 2：可监听输入状态，然后回掉接口判断
 */


public class EditCheck {

    private Context context;
    private IEditCheck mIEditCheck;
    private SparseArray<EditText> mEdithelpdatas;

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 16:26
     * <p>
     * 方法功能：
     *
     * @param mIEditCheck 检测的接口，基本上是实体类实现的
     */

    public EditCheck(Context context, IEditCheck mIEditCheck) {
        this.context = context;
        this.mIEditCheck = mIEditCheck;
    }



    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 13:32
     * <p>
     * 方法功能：设置监听的输入框
     *
     * @param edits 多个被检测的EditView对象
     */
    public void setEditText(EditText... edits) {
        if (mEdithelpdatas == null) mEdithelpdatas = new SparseArray<>();
        mEdithelpdatas.clear();
        for (EditText e : edits) {
            if (e != null) {
                mEdithelpdatas.put(e.getId(), e);
                addTextChangedListener(e);
            }
        }
    }

    public void addEditText(EditText edits) {
        if (edits != null) {
            if (mEdithelpdatas == null) mEdithelpdatas = new SparseArray<>();
            mEdithelpdatas.put(edits.getId(), edits);
            addTextChangedListener(edits);
        }
    }

    /**
     * 作者　　: 李坤
     * 创建时间: 2017/6/28 13:33
     * <p>
     * 方法功能：清空
     */

    public void clear() {
        if (mEdithelpdatas != null) {
            mEdithelpdatas.clear();
        }
    }


    private void addTextChangedListener(EditText edits) {
        if (edits != null) {
            edits.addTextChangedListener(new EditCheck.MyTextWatcher(edits));
        }
    }


    public class MyTextWatcher implements TextWatcher {

        EditText editText;

        public MyTextWatcher(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            boolean isNoGoCheck = false;
            if (!isNoGoCheck) {//去检测
                if (mIEditCheck != null) {
                    mIEditCheck.check();
                }
            }
        }
    }

}
