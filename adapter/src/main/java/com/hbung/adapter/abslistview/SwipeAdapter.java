package com.hbung.adapter.abslistview;

import android.content.Context;

import com.hbung.baseadapter.ISwipeAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/5/22.
 */
public abstract class SwipeAdapter<T> extends CommonAdapter<T> implements ISwipeAdapter {
    public SwipeAdapter(Context context, int layoutId, List<T> datas) {
        super(context, layoutId, datas);


    }

    public boolean getSwipEnableByPosition(int position) {
        return true;
    }

}
