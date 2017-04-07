package com.hbung.wheelview3d.view;

import android.view.View;

import com.hbung.wheelview3d.LoopView;
import com.hbung.wheelview3d.R;
import com.hbung.wheelview3d.adapter.BaseLoopAdapter;
import com.hbung.wheelview3d.adapter.ILoopShowData;
import com.hbung.wheelview3d.adapter.LoopShowDataAdapter;
import com.hbung.wheelview3d.listener.LoopListener;
import com.hbung.wheelview3d.listener.OnItemSelectListener;

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
        options1.setInitPosition(0);
        options2.setInitPosition(0);
        options3.setInitPosition(0);
        setOneDatas();
        options1.setAdapter(options1Adapter);
        setTwoDatas();
        options2.setAdapter(options2Adapter);
        setThreeDatas();
        options3.setAdapter(options3Adapter);

        setListener();
    }

    private void setListener() {
        if (isLinkage) {
            options1.setListener(new LoopListener() {
                @Override
                public void onItemSelect(int item, Object data) {
                    setTwoDatas();
                    if (!options2Datas.isEmpty()) {
                        setThreeDatas();
                    }
                }
            });
            options2.setListener(new LoopListener() {
                @Override
                public void onItemSelect(int item, Object data) {
                    setThreeDatas();
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
                options1Datas.clear();
                options1Datas.addAll(datas);
                options1Adapter.notifyDataSetChanged();
            }
        }
    }

    //设置第二列数据源
    private void setTwoDatas() {
        int onePosition = options1.getSelectedItem();
        if (onItemSelectListener != null) {
            List<ILoopShowData> datas = null;
            if (isLinkage) {
                if (options1Datas.size() > onePosition)
                    datas = onItemSelectListener.getTowData(onePosition, options1Datas.get(onePosition));
            } else {
                datas = onItemSelectListener.getTowData(onePosition, options1Datas.size() > onePosition ? options1Datas.get(onePosition) : null);
            }
            if (datas != null) {
                options2Datas.clear();
                options2Datas.addAll(datas);
                options2Adapter.notifyDataSetChanged();
            }
        }
    }


    //设置第三列数据源
    private void setThreeDatas() {
        int onePosition = options1.getSelectedItem();
        int twoPosition = options2.getSelectedItem();
        if (onItemSelectListener != null) {
            List<ILoopShowData> datas = null;
            if (isLinkage) {
                if (options2Datas.size() > twoPosition && options1Datas.size() > onePosition)
                    datas = onItemSelectListener.getThreeData(onePosition, options1Datas.get(onePosition), options2Datas.get(twoPosition));
            } else {
                datas = onItemSelectListener.getThreeData(onePosition, options1Datas.size() > onePosition ? options1Datas.get(onePosition) : null,
                        options2Datas.size() > twoPosition ? options2Datas.get(twoPosition) : null);
            }
            if (datas != null) {
                options3Datas.clear();
                options3Datas.addAll(datas);
                options3Adapter.notifyDataSetChanged();
            }
        }
    }

    public <T extends ILoopShowData> T getOneData() {
        int onePosition = options1.getSelectedItem();
        if (options1Datas.size() > onePosition) {
            return (T) options1Datas.get(onePosition);
        }
        return null;
    }

    public <T extends ILoopShowData> T getTwoData() {
        int twoPosition = options2.getSelectedItem();
        if (options2Datas.size() > twoPosition) {
            return (T) options2Datas.get(twoPosition);
        }
        return null;
    }

    public <T extends ILoopShowData> T getThreeData() {
        int threePosition = options3.getSelectedItem();
        if (options3Datas.size() > threePosition) {
            return (T) options3Datas.get(threePosition);
        }
        return null;
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

    public void setMode(Mode mode) {
        this.mode = mode;
        changVisibility();
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

    public void setTextSize(float size) {
        if (size > 0) {
            options1.setTextSize(size);
            options2.setTextSize(size);
            options3.setTextSize(size);
        }
    }

    public void setLineSpacingMultiplier(float size) {
        if (size > 0) {
            options1.setLineSpacingMultiplier(size);
            options2.setLineSpacingMultiplier(size);
            options3.setLineSpacingMultiplier(size);
        }
    }

    public void setDividerColor(int dividerColor) {
        if (dividerColor > 0) {
            options1.setDividerColor(dividerColor);
            options2.setDividerColor(dividerColor);
            options3.setDividerColor(dividerColor);
        }
    }

    public void setSelectTextColor(int selectTextColor) {
        if (selectTextColor > 0) {
            options1.setSelectTextColor(selectTextColor);
            options2.setSelectTextColor(selectTextColor);
            options3.setSelectTextColor(selectTextColor);
        }
    }

    public void setNoSelectTextColor(int noSelectTextColor) {
        if (noSelectTextColor > 0) {
            options1.setNoSelectTextColor(noSelectTextColor);
            options2.setNoSelectTextColor(noSelectTextColor);
            options3.setNoSelectTextColor(noSelectTextColor);
        }
    }

    public void setInitPosition(int initPosition) {
        if (initPosition > 0) {
            options1.setInitPosition(initPosition);
            options2.setInitPosition(initPosition);
            options3.setInitPosition(initPosition);
        }
    }


}
