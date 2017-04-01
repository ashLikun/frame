package com.hbung.wheelview3d.view;

import android.view.View;

import com.hbung.wheelview3d.LoopView;
import com.hbung.wheelview3d.R;
import com.hbung.wheelview3d.adapter.BaseLoopAdapter;

/**
 * 作者　　: 李坤
 * 创建时间:2017/4/1　15:50
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public class LoopOptions<T> {

    private View pickeroptions;

    private LoopView options1;
    private LoopView options2;
    private LoopView options3;
    private BaseLoopAdapter<T> options1Adapter;
    private BaseLoopAdapter<T> options2Adapter;
    private BaseLoopAdapter<T> options3Adapter;

    public LoopOptions(View pickeroptions) {
        this.pickeroptions = pickeroptions;
        initView();
    }

    private void initView() {
        options1 = (LoopView) pickeroptions.findViewById(R.id.options1);
        options2 = (LoopView) pickeroptions.findViewById(R.id.options2);
        options3 = (LoopView) pickeroptions.findViewById(R.id.options3);

    }

    public void setLoopAdapter(BaseLoopAdapter<T> options1Adapter, BaseLoopAdapter<T> options2Adapter, BaseLoopAdapter<T> options3Adapter) {

    }
}
