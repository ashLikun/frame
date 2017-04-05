package com.hbung.wheelview3d.view;

import android.view.View;

import com.hbung.wheelview3d.LoopListener;
import com.hbung.wheelview3d.LoopView;
import com.hbung.wheelview3d.R;
import com.hbung.wheelview3d.adapter.BaseLoopAdapter;
import com.hbung.wheelview3d.adapter.ILoopShowData;
import com.hbung.wheelview3d.adapter.LoopShowDataAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间:2017/4/1　15:50
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public class LoopOptions {

    public enum Mode {
        ONE, TWO, THREE
    }

    //是否联动
    private boolean isLinkage = true;

    private Mode mode = Mode.THREE;
    private View pickeroptions;
    private LoopView options1;
    private LoopView options2;
    private LoopView options3;

    private List<ILoopShowData> options1Datas = new ArrayList<>();
    private List<ILoopShowData> options2Datas = new ArrayList<>();
    private List<ILoopShowData> options3Datas = new ArrayList<>();
    private BaseLoopAdapter options1Adapter = new LoopShowDataAdapter(options1Datas);
    private BaseLoopAdapter options2Adapter = new LoopShowDataAdapter(options2Datas);
    private BaseLoopAdapter options3Adapter = new LoopShowDataAdapter(options3Datas);

    private OnItemSelectListener onItemSelectListener;

    public LoopOptions(View pickeroptions, OnItemSelectListener onItemSelectListener) {
        this.pickeroptions = pickeroptions;
        this.onItemSelectListener = onItemSelectListener;
        initView();
    }

    private void initView() {
        options1 = (LoopView) pickeroptions.findViewById(R.id.options1);
        options2 = (LoopView) pickeroptions.findViewById(R.id.options2);
        options3 = (LoopView) pickeroptions.findViewById(R.id.options3);
        changVisibility();
        options1.setAdapter(options1Adapter);
        options2.setAdapter(options2Adapter);
        options3.setAdapter(options3Adapter);
        setOneDatas();
        setTwoDatas(options1.getSelectedItem());
        setThreeDatas(options2.getSelectedItem());
        setListener();
    }

    private void setListener() {
        if (isLinkage) {
            options1.setListener(new LoopListener() {
                @Override
                public void onItemSelect(int item, Object data) {
                    setTwoDatas(item);
                    if (!options2Datas.isEmpty()) {
                        setThreeDatas(options2.getSelectedItem());
                    }
                }
            });
            options2.setListener(new LoopListener() {
                @Override
                public void onItemSelect(int item, Object data) {
                    setThreeDatas(item);
                }
            });
        } else {
            options1.setListener(null);
            options2.setListener(null);
        }
    }


    //设置第一列数据源
    private void setOneDatas() {
        if (onItemSelectListener != null) {
            List<ILoopShowData> datas = onItemSelectListener.getOneData();
            if (datas != null) {
                options1Datas.addAll(datas);
            }
        }
    }

    //设置第二列数据源
    private void setTwoDatas(int onePosition) {
        if (onItemSelectListener != null && options1Datas.size() > onePosition) {
            List<ILoopShowData> datas = onItemSelectListener.getTowData(onePosition, options1Datas.get(onePosition));
            if (datas != null) {
                options2Datas.addAll(datas);
            }
        }
    }

    //设置第三列数据源
    private void setThreeDatas(int twoPosition) {
        if (onItemSelectListener != null && options2Datas.size() > twoPosition) {
            List<ILoopShowData> datas = onItemSelectListener.getThreeData(twoPosition, options2Datas.get(twoPosition));
            if (datas != null) {
                options3Datas.addAll(datas);
            }
        }
    }

    public void changVisibility() {
        if (mode == Mode.ONE) {
            options1.setVisibility(View.VISIBLE);
            options2.setVisibility(View.GONE);
            options3.setVisibility(View.GONE);
        } else if (mode == Mode.TWO) {
            options1.setVisibility(View.VISIBLE);
            options2.setVisibility(View.VISIBLE);
            options3.setVisibility(View.GONE);
        } else if (mode == Mode.THREE) {
            options1.setVisibility(View.VISIBLE);
            options2.setVisibility(View.VISIBLE);
            options3.setVisibility(View.VISIBLE);
        }
    }

    public void setLinkage(boolean linkage) {
        isLinkage = linkage;
        setListener();
    }

    public void setLoop(boolean loop) {
        options1.setIsLoop(loop);
        options2.setIsLoop(loop);
        options3.setIsLoop(loop);
    }

    public static abstract class OnItemSelectListener<T extends ILoopShowData> {
        public abstract List<T> getOneData();

        public List<T> getTowData(int onePosition, T oneItemData) {
            return null;
        }

        public List<T> getThreeData(int twoPosition, T towItemData) {
            return null;
        }
    }
}
