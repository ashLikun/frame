package com.ashlikun.wheelview3d.listener;

import com.ashlikun.wheelview3d.adapter.ILoopShowData;

/**
 * 作者　　: 李坤
 * 创建时间:2017/4/7　14:05
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public interface OnPositiveClickListener<ONE extends ILoopShowData, TWO extends ILoopShowData, THREAD extends ILoopShowData> {
    void onPositive(ONE oneData, TWO twoData, THREAD threadData);
}
