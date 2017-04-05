package com.hbung.wheelview3d.adapter;

import android.database.Observable;

/**
 * 作者　　: 李坤
 * 创建时间:2017/4/5　13:33
 * 邮箱　　：496546144@qq.com
 * <p>
 * 功能介绍：
 */

public class LoopDataObservable extends Observable<LoopDataObserver> {
    public void notifyChanged() {
        synchronized (mObservers) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).onChanged();
            }
        }
    }
}
