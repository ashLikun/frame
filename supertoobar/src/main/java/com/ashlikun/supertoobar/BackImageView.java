package com.ashlikun.supertoobar;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * 作者　　: 李坤
 * 创建时间: 2019/3/4　17:27
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：返回键，正方形
 */
class BackImageView extends AppCompatImageView {
    //返回键是否是正方形
    protected boolean backImgSquare = false;

    public BackImageView(Context context) {
        super(context, null);
    }

    public BackImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public BackImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (backImgSquare) {
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            super.onMeasure(MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY));
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
