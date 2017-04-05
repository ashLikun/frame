package com.hbung.wheelview3d.adapter;

import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间:2017/4/5　15:00
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public class LoopShowDataAdapter extends SimpleLoopAdapter<ILoopShowData> {

    public LoopShowDataAdapter(List<ILoopShowData> listDatas) {
        super(listDatas);
    }

    @Override
    public String getShowText(int position) {
        return getItem(position).getShowText();
    }
}
