package com.hbung.wheelview3d.adapter;

import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间:2017/3/31　11:43
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public abstract class SimpleLoopAdapter<T> implements BaseLoopAdapter<T> {
    List<T> listDatas;

    public SimpleLoopAdapter(List<T> listDatas) {
        this.listDatas = listDatas;
    }

    @Override
    public int getItemCount() {
        return listDatas == null ? 0 : listDatas.size();
    }

    @Override
    public T getItem(int position) {
        return listDatas == null ? null : listDatas.get(position);
    }

}
