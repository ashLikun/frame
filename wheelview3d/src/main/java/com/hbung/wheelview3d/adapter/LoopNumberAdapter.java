package com.hbung.wheelview3d.adapter;

import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间:2017/3/31　11:24
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public class LoopNumberAdapter implements BaseLoopAdapter<Integer> {
    List<Integer> listDatas;

    public LoopNumberAdapter(List<Integer> listDatas) {
        this.listDatas = listDatas;
    }

    @Override
    public int getItemCount() {
        return listDatas == null ? 0 : listDatas.size();
    }

    @Override
    public Integer getItem(int position) {
        return listDatas == null ? 0 : listDatas.get(position);
    }

    @Override
    public String getShowText(int position) {
        return String.format("%02d", getItem(position));
    }


}
