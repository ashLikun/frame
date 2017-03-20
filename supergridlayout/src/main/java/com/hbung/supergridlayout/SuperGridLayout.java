package com.hbung.supergridlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridLayout;

import com.hbung.adapter.ViewHolder;
import com.hbung.adapter.recyclerview.CommonAdapter;

import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间:2016/9/8　16:03
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：表格布局封装成类似RecycleView使用方法
 */

public class SuperGridLayout extends GridLayout {

    CommonAdapter adapter;
    private int mItemSize;   // 水平每个item宽度
    private int hSpace = 0;   // 水平间距h
    private int vSpace = 0;   // 垂直间距h

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public SuperGridLayout(Context context) {
        this(context, null);

    }

    public SuperGridLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public SuperGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SuperGridLayout);
        this.hSpace = (int) typedArray.getDimension(R.styleable.SuperGridLayout_superGridLayoutHSpace, 0);
        this.vSpace = (int) typedArray.getDimension(R.styleable.SuperGridLayout_superGridLayoutVSpace, 0);
        typedArray.recycle();
    }

    /**
     * 是否已有的数据和指定的数据来自同一引用
     *
     * @param object
     * @return
     */
    public boolean isFormData(List object) {
        if (adapter == null || adapter.getDatas() != object) {
            return false;
        } else {
            return true;
        }
    }


    public void setAdapter(CommonAdapter adapter) {
        this.adapter = adapter;
        addAllView();

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                SuperGridLayout.this.onChanged();
            }
        });
    }

    public CommonAdapter getAdapter() {
        return adapter;
    }

    private void addAllView() {
        removeAllViews();
        for (int i = 0; i < adapter.getItemCount(); i++) {
            ViewHolder holder = ViewHolder.get(getContext(), null, this, adapter.getmLayoutId(), i);
            View view = holder.getConvertView();
            addViewInLayout(view, 0, generateDefaultLayoutParams(), true);
        }
    }

    private void onChanged() {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setTag(-2, false);
        }
        if (getChildCount() > adapter.getItemCount()) {
            removeViews(adapter.getItemCount(), getChildCount() - adapter.getItemCount());
        } else if (getChildCount() < adapter.getItemCount()) {
            for (int i = getChildCount(); i < adapter.getItemCount(); i++) {
                ViewHolder holder = ViewHolder.get(getContext(), null, this, adapter.getmLayoutId(), i);
                View view = holder.getConvertView();
                addViewInLayout(view, getChildCount(), generateDefaultLayoutParams(), true);
            }
        }
        requestLayout();
    }

    private class MyOnClickListener<T> implements OnClickListener {
        int postion;
        T data;

        public MyOnClickListener(int postion, T data) {
            this.postion = postion;
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(SuperGridLayout.this, v, postion, data);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int width = MeasureSpec.getSize(widthSpec);
        if (width != 0 && getChildCount() > 0) {
            int totalWidth = width - getPaddingLeft() - getPaddingRight();
            mItemSize = (int) ((totalWidth * 1.0 - hSpace * (getColumnCount() - 1)) / getColumnCount());
            for (int i = 0; i < getChildCount(); i++) {
                View view = getChildAt(i);
                view.setLayoutParams(generateDefaultLayoutParams((LayoutParams) view.getLayoutParams(), i));
                if ((view.getTag(-2) == null || !(Boolean) view.getTag(-2)) && view.getTag(view.getId()) instanceof ViewHolder) {
                    view.setTag(-2, true);
                    adapter.convert((ViewHolder) view.getTag(view.getId()), adapter.getItemData(i));
                    view.setOnClickListener(new MyOnClickListener(i, adapter.getItemData(i)));
                }
            }
        }
        super.onMeasure(widthSpec, heightSpec);

    }


    @Override
    public LayoutParams generateDefaultLayoutParams() {

        LayoutParams params = super.generateDefaultLayoutParams();
        params.width = mItemSize;

        //除了每行最后一个不加rightMargin
        if ((getChildCount() + 1) % getColumnCount() != 0) {
            if (hSpace > 0) {
                params.rightMargin = hSpace;
            }
        }
        //除了第一行不加rightMargin
        if (getChildCount() + 1 > getColumnCount()) {
            if (vSpace > 0) {
                params.topMargin = vSpace;
            }
        }

        return params;
    }

    protected LayoutParams generateDefaultLayoutParams(LayoutParams params, int position) {

        params.width = mItemSize;

        //除了每行最后一个不加rightMargin
        if ((position + 1) % getColumnCount() != 0) {
            if (hSpace > 0) {
                params.rightMargin = hSpace;
            }
        }
        //除了第一行不加rightMargin
        if (position + 1 > getColumnCount()) {
            if (vSpace > 0) {
                params.topMargin = vSpace;
            }
        }
        return params;
    }


    public interface OnItemClickListener<T> {
        void onItemClick(GridLayout parent, View view, int position, T data);
    }

    public int getmItemSize() {
        return mItemSize;
    }
}
