// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.ashlikun.wheelview3d;

import java.util.TimerTask;
/**
 * 作者　　: 李坤
 * 创建时间: 15:35 Administrator
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：更具手势识别的onFling的velocityY来实现滚动
 */

final class LoopTimerTask extends TimerTask {

    float a;
    final float velocityY;
    final LoopView loopView;

    LoopTimerTask(LoopView loopview, float velocityY) {
        super();
        loopView = loopview;
        this.velocityY = velocityY;
        a = Integer.MAX_VALUE;
    }

    @Override
    public final void run() {
        //velocityY上限2000
        if (a == Integer.MAX_VALUE) {
            if (Math.abs(velocityY) > 2000F) {
                if (velocityY > 0.0F) {
                    a = 2000F;
                } else {
                    a = -2000F;
                }
            } else {
                a = velocityY;
            }
        }
        if (Math.abs(a) >= 0.0F && Math.abs(a) <= 20F) {
            loopView.cancelFuture();
            loopView.handler.sendEmptyMessage(MessageHandler.WHAT_SMOOTH_SCROLL);
            return;
        }
        int i = (int) ((a * 10F) / 1000F);
        loopView.setTotalScrollY(loopView.getTotalScrollY() - i);
        //边界处理
        if (!loopView.isLoop()) {
            float itemHeight = loopView.getItemHeight();
            float top = (-loopView.getInitPosition()) * itemHeight;
            float bottom = (loopView.getItemCount() - 1 - loopView.getInitPosition()) * itemHeight;
            if (loopView.getTotalScrollY() - itemHeight * 0.3 < top) {
                top = loopView.getTotalScrollY() + i;
            } else if (loopView.getTotalScrollY() + itemHeight * 0.3 > bottom) {
                bottom = loopView.getTotalScrollY() + i;
            }

            if (loopView.getTotalScrollY() <= top) {
                a = 40F;
                loopView.setTotalScrollY((int) top);
            } else if (loopView.getTotalScrollY() >= bottom) {
                loopView.setTotalScrollY((int) bottom);
                a = -40F;
            }
        }
        if (a < 0.0F) {
            a = a + 20F;
        } else {
            a = a - 20F;
        }
        loopView.handler.sendEmptyMessage(MessageHandler.WHAT_INVALIDATE_LOOP_VIEW);
    }
}
