package com.ashlikun.wheelview3d.view;

import android.view.View;

import com.ashlikun.wheelview3d.LoopView;
import com.ashlikun.wheelview3d.R;
import com.ashlikun.wheelview3d.adapter.BaseLoopAdapter;
import com.ashlikun.wheelview3d.adapter.ILoopShowData;
import com.ashlikun.wheelview3d.adapter.LoopShowDataAdapter;
import com.ashlikun.wheelview3d.listener.LoopListener;
import com.ashlikun.wheelview3d.listener.OnItemSelectListener;

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

    private View pickeroptions;
    private LoopView options1;
    private LoopView options2;
    private LoopView options3;

    private List<ILoopShowData> options1Datas = new ArrayList<>();
    private List<ILoopShowData> options2Datas = null;
    private List<ILoopShowData> options3Datas = null;
    private BaseLoopAdapter options1Adapter = new LoopShowDataAdapter(options1Datas);
    private BaseLoopAdapter options2Adapter = null;
    private BaseLoopAdapter options3Adapter = null;

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
        setOneDatas();
        options1.setAdapter(options1Adapter);

        if (options2 != null) {
            options2.setInitPosition(0);
            setTwoDatas();
            if (options2Adapter != null) {
                options2.setAdapter(options2Adapter);
            }
        }
        if (options3 != null) {
            options3.setInitPosition(0);
            setThreeDatas();
            if (options3Adapter != null) {
                options3.setAdapter(options3Adapter);
            }
        }
        setListener();
        changVisibility();
    }

    private void setListener() {
        if (isLinkage) {
            options1.setListener(new LoopListener() {
                @Override
                public void onItemSelect(int item, Object data) {
                    setTwoDatas();
                    setThreeDatas();
                }
            });
            if (options2 != null) {
                options2.setListener(new LoopListener() {
                    @Override
                    public void onItemSelect(int item, Object data) {
                        setThreeDatas();
                    }
                });
            }
        } else {
            options1.setListener(null);
            if (options2 != null) {
                options2.setListener(null);
            }
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
                if (options1Datas.size() > onePosition) {
                    datas = onItemSelectListener.getTowData(onePosition, options1Datas.get(onePosition));
                }
            } else {
                datas = onItemSelectListener.getTowData(onePosition, options1Datas.size() > onePosition ? options1Datas.get(onePosition) : null);
            }
            if (datas != null) {
                if (options2Datas == null) {
                    options2Datas = new ArrayList<>();
                }
                if (options2Adapter == null) {
                    options2Adapter = new LoopShowDataAdapter(options2Datas);
                }
                options2Datas.clear();
                options2Datas.addAll(datas);
                options2Adapter.notifyDataSetChanged();
            }
        }
    }


    //设置第三列数据源
    private void setThreeDatas() {
        int onePosition = options1.getSelectedItem();
        int twoPosition = options2 != null ? options2.getSelectedItem() : 0;
        if (onItemSelectListener != null) {
            List<ILoopShowData> datas = null;
            if (options2Datas != null) {
                if (isLinkage) {
                    if (options2Datas.size() > twoPosition && options1Datas.size() > onePosition) {
                        datas = onItemSelectListener.getThreeData(onePosition, options1Datas.get(onePosition), options2Datas.get(twoPosition));
                    }
                } else {
                    datas = onItemSelectListener.getThreeData(onePosition, options1Datas.size() > onePosition ? options1Datas.get(onePosition) : null,
                            options2Datas.size() > twoPosition ? options2Datas.get(twoPosition) : null);
                }
            }

            if (datas != null) {
                if (options3Datas == null) {
                    options3Datas = new ArrayList<>();
                }
                if (options3Adapter == null) {
                    options3Adapter = new LoopShowDataAdapter(options3Datas);
                }
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
        if (options2 == null || options2Datas == null) {
            return null;
        }
        int twoPosition = options2.getSelectedItem();
        if (options2Datas.size() > twoPosition) {
            return (T) options2Datas.get(twoPosition);
        }
        return null;
    }

    public <T extends ILoopShowData> T getThreeData() {
        if (options3 == null || options3Datas == null) {
            return null;
        }
        int threePosition = options3.getSelectedItem();
        if (options3Datas.size() > threePosition) {
            return (T) options3Datas.get(threePosition);
        }
        return null;
    }

    public void changVisibility() {
        options1.setVisibility(View.VISIBLE);
        if (options2 != null) {
            if (options2Datas != null && options2Adapter != null) {
                options2.setVisibility(View.VISIBLE);
            } else {
                options2.setVisibility(View.GONE);
            }
        }
        if (options3 != null) {
            if (options3Datas != null && options3Adapter != null) {
                options3.setVisibility(View.VISIBLE);
            } else {
                options3.setVisibility(View.GONE);
            }
        }
    }


    public void setLinkage(boolean linkage) {
        isLinkage = linkage;
        setListener();
    }

    public void setLoop(boolean loop) {
        options1.setIsLoop(loop);
        if (options2 != null) {
            options2.setIsLoop(loop);
        }
        if (options3 != null) {
            options3.setIsLoop(loop);
        }
    }

    public void setTextSize(float size) {
        if (size > 0) {
            options1.setTextSize(size);
            if (options2 != null) {
                options2.setTextSize(size);
            }
            if (options3 != null) {
                options3.setTextSize(size);
            }

        }
    }

    public void setLineSpacingMultiplier(float size) {
        if (size > 0) {
            options1.setLineSpacingMultiplier(size);
            if (options2 != null) {
                options2.setLineSpacingMultiplier(size);
            }
            if (options3 != null) {
                options3.setLineSpacingMultiplier(size);
            }
        }
    }

    public void setDividerColor(int dividerColor) {
        if (dividerColor > 0) {
            options1.setDividerColor(dividerColor);
            if (options2 != null) {
                options2.setDividerColor(dividerColor);
            }
            if (options3 != null) {
                options3.setDividerColor(dividerColor);
            }
        }
    }

    public void setSelectTextColor(int selectTextColor) {
        if (selectTextColor > 0) {
            options1.setSelectTextColor(selectTextColor);
            if (options2 != null) {
                options2.setSelectTextColor(selectTextColor);
            }
            if (options3 != null) {
                options3.setSelectTextColor(selectTextColor);
            }
        }
    }

    public void setNoSelectTextColor(int noSelectTextColor) {
        if (noSelectTextColor > 0) {
            options1.setNoSelectTextColor(noSelectTextColor);
            if (options2 != null) {
                options2.setNoSelectTextColor(noSelectTextColor);
            }
            if (options3 != null) {
                options3.setNoSelectTextColor(noSelectTextColor);
            }
        }
    }

    public void setInitPosition(int initPosition) {
        if (initPosition > 0) {
            options1.setInitPosition(initPosition);
            if (options2 != null) {
                options2.setInitPosition(initPosition);
            }
            if (options3 != null) {
                options3.setInitPosition(initPosition);
            }
        }
    }


}
