package com.ashlikun.wheelview3d.listener;

import com.ashlikun.wheelview3d.adapter.ILoopShowData;

/**
 * 作者　　: 李坤
 * 创建时间:2017/4/7　14:13
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：当3种数据泛型一致是可以使用这个简单的接口
 */

public abstract class OnSimpleItemSelectListener<T extends ILoopShowData>
        extends OnItemSelectListener<T, T, T> {
}
