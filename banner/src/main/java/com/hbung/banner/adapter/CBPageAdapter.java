package com.hbung.banner.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.hbung.banner.ConvenientBanner;
import com.hbung.banner.holder.CBViewHolderCreator;
import com.hbung.banner.holder.Holder;
import com.hbung.banner.view.CBLoopViewPager;

import java.util.List;

/**
 * Created by Sai on 15/7/29.
 */
public class CBPageAdapter<T> extends PagerAdapter {
    protected List<T> mDatas;
    protected CBViewHolderCreator holderCreator;
    //    private View.OnClickListener onItemClickListener;
    private boolean canLoop = true;
    private CBLoopViewPager viewPager;
    private final int MULTIPLE_COUNT = 300;
    private int POSITION_CHANG = POSITION_UNCHANGED;
    private ConvenientBanner convenientBanner;

    public int toRealPosition(int position) {
        int realCount = getRealCount();
        if (realCount == 0)
            return 0;
        int realPosition = position % realCount;
        return realPosition;
    }

    @Override
    public int getCount() {
        return canLoop ? getRealCount() * MULTIPLE_COUNT : getRealCount();
    }

    public int getRealCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int realPosition = toRealPosition(position);

        View view = getView(realPosition, null, container);
//        if(onItemClickListener != null) view.setOnClickListener(onItemClickListener);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        int position = viewPager.getCurrentItem();
        if (position == 0) {
            position = viewPager.getFristItem();
        } else if (position == getCount() - 1) {
            position = viewPager.getLastItem();
        }
        try {
            viewPager.setCurrentItem(position, false);
        } catch (IllegalStateException e) {
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void setCanLoop(boolean canLoop) {
        this.canLoop = canLoop;
    }

    public void setViewPager(CBLoopViewPager viewPager) {
        this.viewPager = viewPager;
    }

    public CBPageAdapter(ConvenientBanner convenientBanner, CBViewHolderCreator holderCreator, List<T> datas) {
        this.convenientBanner = convenientBanner;
        this.holderCreator = holderCreator;
        this.mDatas = datas;
    }

    public View getView(int position, View view, ViewGroup container) {
        Holder holder = null;
        if (view == null) {
            holder = (Holder) holderCreator.createHolder();
            view = holder.createView(convenientBanner, container.getContext());
            view.setTag(view.getId(), holder);
        } else {
            holder = (Holder<T>) view.getTag(view.getId());
        }
        if (mDatas != null && !mDatas.isEmpty())
            holder.UpdateUI(container.getContext(), position, mDatas.get(position));
        return view;
    }

    @Override
    public void notifyDataSetChanged() {
        POSITION_CHANG = POSITION_NONE;
        super.notifyDataSetChanged();
    }

    /**
     * 意思是如果item的位置如果没有发生变化，则返回POSITION_UNCHANGED。
     * 如果返回了POSITION_NONE，表示该位置的item已经不存在了。
     * 默认的实现是假设item的位置永远不会发生变化，而返回POSITION_UNCHANGED
     *
     * @param object
     * @return
     */
    @Override
    public int getItemPosition(Object object) {
        if (POSITION_CHANG == POSITION_NONE) {
            POSITION_CHANG = POSITION_UNCHANGED;
            return POSITION_NONE;
        } else {
            return POSITION_UNCHANGED;
        }
    }

//    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
//        this.onItemClickListener = onItemClickListener;
//    }
}
