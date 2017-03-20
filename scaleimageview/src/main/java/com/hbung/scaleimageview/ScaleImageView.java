package com.hbung.scaleimageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;


public class ScaleImageView extends ImageView {
    private int widthScale = 1;
    private int heightScale = 1;
    private int primary = 1;

    public ScaleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.ScaleImageView);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.ScaleImageView_widthScale) {
                widthScale = a.getInt(attr, 1);
            } else if (attr == R.styleable.ScaleImageView_heightScale) {
                heightScale = a.getInt(attr, 1);
            } else if (attr == R.styleable.ScaleImageView_primary) {
                primary = a.getInt(R.styleable.ScaleImageView_primary, 1);
            }
        }


        a.recycle();
    }

    public ScaleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleImageView(Context context) {
        this(context, null, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = widthMeasureSpec;
        int height = heightMeasureSpec;
        if (primary == 1) {
            width = MeasureSpec.getSize(widthMeasureSpec);
            height = (int) Math.ceil((float) width * heightScale
                    / (float) widthScale);
        } else if (primary == 2) {
            height = MeasureSpec.getSize(heightMeasureSpec);
            width = (int) Math.ceil((float) height * widthScale
                    / (float) heightScale);
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    public int getWidthScale() {
        return widthScale;
    }

    public void setWidthScale(int widthScale) {
        this.widthScale = widthScale;
        invalidate();
    }

    public int getHeightScale() {
        return heightScale;
    }

    public void setHeightScale(int heightScale) {
        this.heightScale = heightScale;
        invalidate();
    }

    public int getPrimary() {
        return primary;
    }

    public void setPrimary(int primary) {
        this.primary = primary;
        invalidate();
    }

}
