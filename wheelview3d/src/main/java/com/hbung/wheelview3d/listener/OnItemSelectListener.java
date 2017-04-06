package com.hbung.wheelview3d.listener;

import com.hbung.wheelview3d.adapter.ILoopShowData;

import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间:2017/4/6　13:55
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：获取每列的数据
 */

public abstract class OnItemSelectListener<ONE extends ILoopShowData, TOW extends ILoopShowData, THREE extends ILoopShowData> {
    public abstract List<ONE> getOneData();

    public List<TOW> getTowData(int onePosition, ONE oneItemData) {
        return null;
    }

    public List<THREE> getThreeData(int twoPosition, ONE oneItemData, TOW towItemData) {
        return null;
    }
}
