package com.ashlikun.wheelview3d.adapter;

import java.util.List;

/**
 * 作者　　: 李坤
 * 创建时间:2017/3/31　11:24
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public class LoopStringAdapter extends BaseLoopAdapter<String> {
    List<String> listDatas;

    public LoopStringAdapter(List<String> listDatas) {
        this.listDatas = listDatas;
    }

    @Override
    public int getItemCount() {
        return listDatas == null ? 0 : listDatas.size();
    }

    @Override
    public String getItem(int position) {
        return listDatas == null ? "" : listDatas.get(position);
    }

    @Override
    public String getShowText(int position) {
        return getItem(position);
    }

}
