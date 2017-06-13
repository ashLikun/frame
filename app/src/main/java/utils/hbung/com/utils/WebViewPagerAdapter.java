package utils.hbung.com.utils;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间:2017/6/12　9:39
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public class WebViewPagerAdapter extends PagerAdapter {
    private List<View> viewLists;

    public WebViewPagerAdapter(List<View> viewLists) {
        this.viewLists = viewLists;
    }

    @Override
    public int getCount() {
        return viewLists == null ? 0 : viewLists.size();
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = viewLists.get(position);
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewLists.get(position));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
