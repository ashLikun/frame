package com.hbung.supergridlayout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.google.android.flexbox.FlexboxLayout;
import com.hbung.adapter.ViewHolder;
import com.hbung.adapter.recyclerview.CommonAdapter;

import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间:2016/9/8　16:03
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：流布局封装成类似RecycleView使用方法
 */
public class SuperFlexboxLayout extends FlexboxLayout {

    CommonAdapter adapter;

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public SuperFlexboxLayout(Context context) {
        this(context, null);

    }

    public SuperFlexboxLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public SuperFlexboxLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
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
                SuperFlexboxLayout.this.onChanged();
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
            addView(view);
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
                addView(view);
            }
        }
        requestLayout();
    }

    private class MyOnClickListener<T> implements View.OnClickListener {
        int postion;
        T data;

        public MyOnClickListener(int postion, T data) {
            this.postion = postion;
            this.data = data;
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(SuperFlexboxLayout.this, v, postion, data);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        super.onLayout(changed, left, top, right, bottom);

    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int width = View.MeasureSpec.getSize(widthSpec);
        if (width != 0 && getChildCount() > 0) {
            for (int i = 0; i < getChildCount(); i++) {
                View view = getChildAt(i);
                if ((view.getTag(-2) == null || !(Boolean) view.getTag(-2)) && view.getTag(view.getId()) instanceof ViewHolder) {
                    view.setTag(-2, true);
                    adapter.convert((ViewHolder) view.getTag(view.getId()), adapter.getItemData(i));
                    if (onItemClickListener != null) {
                        view.setOnClickListener(new MyOnClickListener(i, adapter.getItemData(i)));
                    }
                }
            }
        }
        super.onMeasure(widthSpec, heightSpec);

    }


    public interface OnItemClickListener<T> {
        void onItemClick(SuperFlexboxLayout parent, View view, int position, T data);
    }
}
